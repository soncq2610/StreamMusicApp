package Album;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.streammusic.R;
import com.example.streammusic.SongOfAlbumFragment;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    private Context context;
    private List<Album> mAlbums;
    private String mAlbumId;
    public AlbumAdapter(){}
    public AlbumAdapter(List<Album> mAlbums){
        this.mAlbums = mAlbums;
    }
    public AlbumAdapter(List<Album> mAlbums,String mAlbumId){
        this.mAlbums = mAlbums;
        this.mAlbumId =mAlbumId;
    }
    public void setAlbumData(List<Album> list){
        this.mAlbums = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist,parent,false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = mAlbums.get(position);
        if(album == null){
            return;
        }
        Glide.with(holder.imageAlbum).load(mAlbums.get(position).getImage()).into(holder.imageAlbum);
        holder.textView.setText(album.getName());

        holder.albumitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("albumID",album.getId());
                bundle.putString("albumName",album.getName());
                SongOfAlbumFragment songOfAlbumFragment = new SongOfAlbumFragment();
                songOfAlbumFragment.setArguments(bundle);
                AppCompatActivity appCompatActivity = (AppCompatActivity)view.getContext() ;
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity_Id,songOfAlbumFragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        if(mAlbums != null){
            return mAlbums.size();
        }
        return 0;
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder{
        private CardView albumitem;
        private ImageView imageAlbum,searchBtn;
        private TextView textView;
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            albumitem = itemView.findViewById(R.id.album_item);
            imageAlbum = itemView.findViewById(R.id.img_album);
            textView = itemView.findViewById(R.id.album_title);
            searchBtn = itemView.findViewById(R.id.search_album);
        }
    }
}
