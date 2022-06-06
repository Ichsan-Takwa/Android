package com.example.k6elearninginteraktif;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InputTextTitleField#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputTextTitleField extends Fragment {
    public static final String[] FRAGMENT_TYPE_LIST = {"Title Text Field", "Text Field", "Upload PDF Field", "Upload Image Field","Update Video Field", "Update Audio Field"};
    public static final  int FRAGMENT_TYPE_NO = 0;
    public static final String FRAGMENT_TYPE = FRAGMENT_TYPE_LIST[FRAGMENT_TYPE_NO];

    FirebaseFirestore db;
    public int numberOnSubject = 0;
    String id_pembelajaran, id_subjek, id_dokumen;
    CollectionReference subjek;
    Map<String,Object> result = new HashMap<String,Object>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InputTextTitleField() {
        // Required empty public constructor
    }

    KelolaSubjekActivity thisActivity;
    public boolean isSaved;
    DocumentSnapshot data;
    boolean isLoaded;
    public InputTextTitleField(DocumentSnapshot data, String id_pembelajaran, String id_subjek) {
        this.data = data;
        this.id_pembelajaran = id_pembelajaran;
        this.id_subjek = id_subjek;
        numberOnSubject = Integer.parseInt(data.get("no urut").toString());
        isLoaded = true;
        isSaved = true;
    }

    public InputTextTitleField(int numberOnSubject, String id_pembelajaran, String id_subjek) {
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
     * @return A new instance of fragment InputTextTitleField.
     */
    // TODO: Rename and change types and number of parameters
    public static InputTextTitleField newInstance(String param1, String param2) {
        InputTextTitleField fragment = new InputTextTitleField();
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
        db = FirebaseFirestore.getInstance();
        subjek = db.collection("Pembelajaran").document(id_pembelajaran).collection(id_subjek);

        thisActivity = ((KelolaSubjekActivity)getActivity());

        id_dokumen = UUID.randomUUID().toString();
        if(isLoaded){
            id_dokumen = data.get("id_dokumen").toString();
        }
        InputTextTitleField.this.result.put("id_dokumen", id_dokumen);
        InputTextTitleField.this.result.put("no urut", numberOnSubject);
        InputTextTitleField.this.result.put("tipe", FRAGMENT_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input_text_title_field, container, false);
    }


    Button bt_hapusField;
    TextInputEditText ti_inputField;
    @Override
    public void onStart() {
        super.onStart();
        bt_hapusField = requireView().findViewById(R.id.bt_hapusField);
        ti_inputField =  requireView().findViewById(R.id.ti_inputField);
        ti_inputField.requestFocus();
        if(isLoaded){
            ti_inputField.setText(data.get("content").toString());
        }
        ti_inputField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    InputTextTitleField.this.result.put("content", ti_inputField.getText().toString()+" ");
                    subjek.document(id_dokumen).set(InputTextTitleField.this.result).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(requireContext(), "Teks disimpan", Toast.LENGTH_SHORT).show();
                            isSaved = true;
                        }
                    });
                }
            }
        });
        bt_hapusField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputTextTitleField.this.getParentFragmentManager().beginTransaction().remove(InputTextTitleField.this).commit();
                if(isSaved){
                     subjek.document(id_dokumen).delete();
                }
                thisActivity.sebuahFieldDihapus();
            }
        });
    }
}