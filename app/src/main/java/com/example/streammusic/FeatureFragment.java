package com.example.streammusic;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Album.Album;
import Album.AlbumAdapter;
import Song.Song;
import Song.SongAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeatureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeatureFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FeatureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeatureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeatureFragment newInstance(String param1, String param2) {
        FeatureFragment fragment = new FeatureFragment();
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

    private RecyclerView rcvAlbum;
    private AlbumAdapter albumAdapter;
    private RecyclerView rcSong;
    private SongAdapter songAdapter;
    private List<Album> albumList;
    private List<Song> songList;
    private ImageView btnLogOut;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feature, container, false);
        btnLogOut = view.findViewById(R.id.btn_log_uot);
        rcvAlbum = view.findViewById(R.id.rcv_album);
        rcvAlbum.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));

        albumList = new ArrayList<>();
        getListAlbum();
        Log.e("aaa",albumList.size()+"");


        rcSong = view.findViewById(R.id.rcv_Song);
        rcSong.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        songList = new ArrayList<>();
        getListSong();

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogOut();
            }
        });

        return view;
    }



    private void  getListAlbum(){
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference rfrAlbum = database.getReference("Album");

        rfrAlbum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                  //  Log.e("aaa",dataSnapshot.getValue().toString());
                    Album album = new Album(dataSnapshot.child("albumId").getValue().toString(),dataSnapshot.child("artist").getValue().toString(),dataSnapshot.child("img").getValue().toString(),dataSnapshot.child("name").getValue().toString());
                    albumList.add(album);
                }
                rcvAlbum.setAdapter(new AlbumAdapter( albumList) );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getListSong(){
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference rfrSong = database.getReference("Song");
        rfrSong.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Log.e("aaa",dataSnapshot.getValue().toString());
                    Song song = new Song(dataSnapshot.child("albumId").getValue().toString(),dataSnapshot.child("artist").getValue().toString(),dataSnapshot.child("image").getValue().toString(),dataSnapshot.child("link").getValue().toString(),dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("songId").getValue().toString());

                    songList.add(song);
                }
                rcSong.setAdapter(new SongAdapter( getContext(),songList) );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void onClickLogOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}