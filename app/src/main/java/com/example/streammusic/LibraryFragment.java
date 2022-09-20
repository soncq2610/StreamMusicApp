package com.example.streammusic;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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
 * Use the {@link LibraryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LibraryFragment() {
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
    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView rcvAlbum2;
    private AlbumAdapter albumAdapter;
    private RecyclerView rcSong2;
    private SongAdapter songAdapter;
    private List<Album> albumList2;
    private List<Album> searchList;
    private List<Song> songList2;
    private ImageView searchbtn;
    private EditText searchString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        rcvAlbum2 = view.findViewById(R.id.rcv_album2);
        rcvAlbum2.setLayoutManager(new GridLayoutManager(getContext(),3));
        albumList2 = new ArrayList<>();
        searchList = new ArrayList<>();
        getListAlbum();
        Log.e("aaa",albumList2.size()+"");

        searchString = view.findViewById(R.id.search_album);
        searchbtn = view.findViewById(R.id.search_icon_album);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTxt = searchString.getText().toString();
                Log.e("ccc",searchTxt+"");
                SearchAlbum(searchTxt);
            }
        });
        searchString.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchTxt = searchString.getText().toString();
                SearchAlbum(searchTxt);
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                    Log.e("abc",dataSnapshot.getValue().toString());
                    Album album = new Album(dataSnapshot.child("albumId").getValue().toString(),dataSnapshot.child("artist").getValue().toString(),dataSnapshot.child("img").getValue().toString(),dataSnapshot.child("name").getValue().toString());
                    albumList2.add(album);
                }
                rcvAlbum2.setAdapter(new AlbumAdapter( albumList2) );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void  SearchAlbum(String searchTxt){
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference rfrAlbum = database.getReference("Album");
        searchList.clear();
        rfrAlbum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Log.e("abc",dataSnapshot.getValue().toString());
                    if(dataSnapshot.child("name").getValue().toString().toLowerCase(Locale.ROOT).startsWith(searchTxt+"")){
                        Album album = new Album(dataSnapshot.child("albumId").getValue().toString(),dataSnapshot.child("artist").getValue().toString(),dataSnapshot.child("img").getValue().toString(),dataSnapshot.child("name").getValue().toString());
                        searchList.add(album);
                    }

                }
                rcvAlbum2.setAdapter(new AlbumAdapter( searchList) );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}