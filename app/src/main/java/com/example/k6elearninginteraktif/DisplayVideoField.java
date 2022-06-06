package com.example.k6elearninginteraktif;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayVideoField#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayVideoField extends Fragment {

    FirebaseStorage storage;
    DocumentSnapshot data;
    String id_subjek;
    Context context;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public DisplayVideoField(Context context, DocumentSnapshot data, String id_subjek) {
        // Required empty public constructor
        this.context = context;
        this.data = data;
        this.id_subjek = id_subjek;
    }

    public DisplayVideoField() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayVideoField.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayVideoField newInstance(String param1, String param2) {
        DisplayVideoField fragment = new DisplayVideoField();
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

    StyledPlayerView vp_videoHolder;
    @Override
    public void onStart() {
        super.onStart();
        vp_videoHolder =  requireView().findViewById(R.id.pv_video);
        vp_videoHolder.requestFocus();
        ProgressDialog loading = new ProgressDialog(requireContext());
        loading.setTitle("Loading..");
        loading.setMessage("mengunduh konten..");
        loading.show();
        storage.getReference("Konten Pembelajaran").child(id_subjek).child(data.getId()).getDownloadUrl().addOnCompleteListener (new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                MediaItem item = MediaItem.fromUri(task.getResult());
                ExoPlayer player = new ExoPlayer.Builder(context).build();
                player.addMediaItem(item);
                vp_videoHolder.setPlayer(player);
                loading.dismiss();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_video_field, container, false);
    }
}