package com.example.streammusic;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import Song.Song;
import Song.SongAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongOfAlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongOfAlbumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SongOfAlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongOfAlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongOfAlbumFragment newInstance(String param1, String param2) {
        SongOfAlbumFragment fragment = new SongOfAlbumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView rcSong4;
    private TextView album_name;
    private List<Song> songList4;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private BottomNavigationView bottomNavigationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song_of_album, container, false);
        album_name = view.findViewById(R.id.albumSong_text);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);
        Bundle bundle = this.getArguments();
        String albumID = bundle.getString("albumID");
        String albumName = bundle.getString("albumName");
        album_name.setText(albumName);
        rcSong4 = view.findViewById(R.id.rcv_Song4);
        rcSong4.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        songList4 = new ArrayList<>();
        getListSong(albumID);
        return view;
    }
    private void getListSong(String albumId){
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference rfrSong = database.getReference("Song");

        rfrSong.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("albumId").getValue().toString().endsWith(albumId)){
                        Log.e("aaa",dataSnapshot.child("albumId").toString());
                        Song song = new Song(dataSnapshot.child("albumId").getValue().toString(),dataSnapshot.child("artist").getValue().toString(),dataSnapshot.child("image").getValue().toString(),dataSnapshot.child("link").getValue().toString(),dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("songId").getValue().toString());
                       songList4.add(song);
                    }

                }
                rcSong4.setAdapter(new SongAdapter( getContext(),songList4) );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}