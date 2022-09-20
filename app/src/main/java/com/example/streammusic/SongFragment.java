package com.example.streammusic;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Album.Album;
import Album.AlbumAdapter;
import Song.Song;
import Song.SongAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongFragment newInstance(String param1, String param2) {
        SongFragment fragment = new SongFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView rcSong3;

    private List<Song> songList3;
    private List<Song> searchList;
    private List<Song> listSort;
    private ImageView searchbtn;
    private EditText searchString;
    private ImageView sortBtn;
    private boolean state = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song, container, false);


        rcSong3 = view.findViewById(R.id.rcv_Song3);
        rcSong3.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        songList3 = new ArrayList<>();
        getListSong();

        searchList = new ArrayList<>();
        searchbtn = view.findViewById(R.id.search_icon_song);
        searchString = view.findViewById(R.id.search_song);

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTxt = searchString.getText().toString();
                ListSearchSong(searchTxt);
            }
        });

        searchString.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchTxt = searchString.getText().toString();
                ListSearchSong(searchTxt);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        listSort = new ArrayList<>();
        sortBtn = view.findViewById(R.id.sort_Az);
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state == false ){
                    state = true;
                    sortBtn.setImageResource(R.drawable.ic_msort);
                    getListSort();
                }else{
                    state = false;
                    sortBtn.setImageResource(R.drawable.ic_msort_dark);
                    getListSong();
                }
            }
        });

        return view;
    }
    private void getListSort(){
        listSort.clear();
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference rfrSong = database.getReference("Song");
        Query query = rfrSong.orderByChild("name");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Song song = new Song(dataSnapshot.child("albumId").getValue().toString(),dataSnapshot.child("artist").getValue().toString(),dataSnapshot.child("image").getValue().toString(),dataSnapshot.child("link").getValue().toString(),dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("songId").getValue().toString());

                    listSort.add(song);
                }
                rcSong3.setAdapter(new SongAdapter( getContext(),listSort) );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getListSong(){
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference rfrSong = database.getReference("Song");
        songList3.clear();
        rfrSong.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Song song = new Song(dataSnapshot.child("albumId").getValue().toString(),dataSnapshot.child("artist").getValue().toString(),dataSnapshot.child("image").getValue().toString(),dataSnapshot.child("link").getValue().toString(),dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("songId").getValue().toString());

                    songList3.add(song);
                }
                rcSong3.setAdapter(new SongAdapter( getContext(),songList3) );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void ListSearchSong(String searchTxt){
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference rfrSong = database.getReference("Song");
        searchList.clear();
        rfrSong.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("name").getValue().toString().toLowerCase(Locale.ROOT).startsWith(searchTxt+"")){
                        Song song = new Song(dataSnapshot.child("albumId").getValue().toString(),dataSnapshot.child("artist").getValue().toString(),dataSnapshot.child("image").getValue().toString(),dataSnapshot.child("link").getValue().toString(),dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("songId").getValue().toString());

                        searchList.add(song);
                    }

                }
                rcSong3.setAdapter(new SongAdapter( getContext(),searchList) );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}