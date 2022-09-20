package Song;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.streammusic.PlayActivity;
import com.example.streammusic.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private List<Song> mListSong;
    private Context mContext;
    private PlayActivity playActivity;


    public SongAdapter(Context context, List<Song> mListSong){
        this.mContext = context;

        this.mListSong = mListSong;
    }


    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,parent,false);
        return new SongViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
       final Song song = mListSong.get(position);
        if(song == null){
            return;
        }
        int index = position;
        Glide.with(holder.imageSong).load(mListSong.get(position).getImg()).into(holder.imageSong);
        holder.textView.setText(song.getTitle());
        holder.artistView.setText(song.getArtist());
        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onCLickGoToPlay(song);
            }
        });
    }

    private void onCLickGoToPlay(Song song){

        Intent intent = new Intent(mContext, PlayActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("object_song",song);

      //  intent.putExtra("position",position);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(mListSong != null){
            return mListSong.size();
        }
        return 0;
    }

    public class SongViewHolder extends RecyclerView.ViewHolder{
        private CardView layout_item;
        private ImageView imageSong;
        private TextView textView;
        private TextView artistView;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_item = itemView.findViewById(R.id.song_item);
            imageSong = itemView.findViewById(R.id.img_song);
            textView = itemView.findViewById(R.id.name_song);
            artistView = itemView.findViewById(R.id.artist_name);
        }
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

                    mListSong.add(song);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
