package com.example.k6elearninginteraktif;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InputAudioField#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputAudioField extends Fragment {
    public static final String[] FRAGMENT_TYPE_LIST = {"Title Text Field", "Text Field", "Upload PDF Field", "Upload Image Field","Update Video Field", "Update Audio Field"};
    public static final  int FRAGMENT_TYPE_NO = 5;
    public static final String FRAGMENT_TYPE = FRAGMENT_TYPE_LIST[FRAGMENT_TYPE_NO];

    KelolaSubjekActivity thisActivity;

    Map<String,Object> result = new HashMap<String,Object>();
    public int numberOnSubject = 0;

    FirebaseFirestore db;
    FirebaseStorage storage;
    CollectionReference subjek;
    String id_pembelajaran, id_subjek, id_dokumen;

    ProgressDialog loading;

    ActivityResultLauncher<Intent> startActivityForResult;
    Uri audioFile;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InputAudioField() {
        // Required empty public constructor
    }

    //    Konstruktor untuk load data yang sudah ada
    String fileName;
    boolean isLoaded;
    public InputAudioField(DocumentSnapshot document, String id_pembelajaran, String id_subjek){
        isLoaded = true;
        id_dokumen = document.get("id_dokumen").toString();
         fileName = document.get("nama file").toString();
        numberOnSubject = Integer.parseInt(document.get("no urut").toString());

        this.id_pembelajaran = id_pembelajaran;
        this.id_subjek = id_subjek;
    }

    public InputAudioField(int numberOnSubject, String id_pembelajaran, String id_subjek) {
        this.numberOnSubject = numberOnSubject;
        this.id_pembelajaran = id_pembelajaran;
        this.id_subjek = id_subjek;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InputAudioField.
     */
    // TODO: Rename and change types and number of parameters
    public static InputAudioField newInstance(String param1, String param2) {
        InputAudioField fragment = new InputAudioField();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        thisActivity = ((KelolaSubjekActivity)getActivity());
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        subjek = db.collection("Pembelajaran").document(id_pembelajaran).collection(id_subjek);

        if(isLoaded){
            if(isLoaded){
                storage.getReference("Konten Pembelajaran").child(id_subjek).child(id_dokumen).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        audioFile = task.getResult();
                    }
                }) ;
            }
        }

        startActivityForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        audioFile = result.getData().getData();
                        fileName = audioFile.getLastPathSegment();
                        tv_urlTerpilih.setText("Terplih :" + fileName );

                        id_dokumen = UUID.randomUUID().toString();
                        InputAudioField.this.result.put("id_dokumen", id_dokumen);
                        InputAudioField.this.result.put("no urut", numberOnSubject);
                        InputAudioField.this.result.put("tipe", FRAGMENT_TYPE);
                        InputAudioField.this.result.put("nama file", fileName);

                        loading = new ProgressDialog(thisActivity);
                        loading.setTitle("Tunggu sebentar!!");
                        loading.show();
                        storage.getReference("Konten Pembelajaran").child(id_subjek).child(id_dokumen).putFile(audioFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                //nanti
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Upload berhasil!", Toast.LENGTH_SHORT).show();
                                    loading.setMessage("menyimpan informasi..");
                                    subjek.document(id_dokumen).set(InputAudioField.this.result).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(thisActivity, "sukses!!", Toast.LENGTH_SHORT).show();
                                                loading.dismiss();
                                            } else {
                                                Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                }

                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                loading.setMessage("mengunggah : "+(int)progress+"%");
                            }
                        });
                    }
                }
        );
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_audio_field, container, false);
    }

    Button bt_unggahAudio, bt_hapusField;
    TextView tv_urlTerpilih;

    @Override
    public void onStart() {
        super.onStart();
        bt_unggahAudio = this.requireView().findViewById(R.id.bt_unggahAudio);
        bt_hapusField = requireView().findViewById(R.id.bt_hapusField);
        tv_urlTerpilih = requireView().findViewById(R.id.tv_urlTerpilih);
        bt_unggahAudio.requestFocus();
        if (isLoaded){
            tv_urlTerpilih.setText(fileName);
        }
        bt_unggahAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult.launch(intent);
            }
        });
        bt_hapusField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputAudioField.this.getParentFragmentManager().beginTransaction().remove(InputAudioField.this).commit();
                if(audioFile != null){
                    subjek.document(id_dokumen).delete();
                }
                thisActivity.sebuahFieldDihapus();
            }
        });
    }
}