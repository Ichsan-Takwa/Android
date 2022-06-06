package com.example.k6elearninginteraktif;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayTextTitleField#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayTextTitleField extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DisplayTextTitleField() {
        // Required empty public constructor
    }

    DocumentSnapshot data;
    String id_subjek;
    public DisplayTextTitleField(DocumentSnapshot data, String id_subjek) {
        this.data = data;
        this.id_subjek = id_subjek;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayTextTitleField.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayTextTitleField newInstance(String param1, String param2) {
        DisplayTextTitleField fragment = new DisplayTextTitleField();
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
    }

    TextView tv_contentHolder;
    @Override
    public void onStart() {
        super.onStart();
        tv_contentHolder = requireView().findViewById(R.id.tv_contentHolder);
        tv_contentHolder.requestFocus();
        tv_contentHolder.setText(data.get("content").toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_text_title_field, container, false);
    }
}