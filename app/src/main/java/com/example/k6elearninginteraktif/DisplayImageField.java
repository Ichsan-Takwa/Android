package com.example.k6elearninginteraktif;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayImageField#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayImageField extends Fragment {

    DocumentSnapshot data;
    FirebaseStorage storage;

    String id_subjek;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public DisplayImageField() {
        // Required empty public constructor
    }

    public DisplayImageField(DocumentSnapshot data, String id_subjek) {
        this.data = data;
        this.id_subjek = id_subjek;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayImageField.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayImageField newInstance(String param1, String param2) {
        DisplayImageField fragment = new DisplayImageField();
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

        storage = FirebaseStorage.getInstance();
    }

    ImageView iv_holderGambar;
    @Override
    public void onStart() {
        super.onStart();

        ProgressDialog loading = new ProgressDialog(requireContext());
        loading.setTitle("Loading..");
        loading.setMessage("mengunduh konten..");
        loading.show();
        iv_holderGambar = requireView().findViewById(R.id.iv_holderGambar);
        iv_holderGambar.requestFocus();
        storage.getReference("Konten Pembelajaran").child(id_subjek).child(data.getId()).getBytes(10*1024*1024).addOnCompleteListener(new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                Bitmap bmp = BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length);
                iv_holderGambar.setImageBitmap(bmp);
                loading.dismiss();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_image_field, container, false);
    }
}