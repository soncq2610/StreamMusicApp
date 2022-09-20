package Song;

import java.io.Serializable;

public class Song implements Serializable {
    private String album,artist,img,link,title,songId;

    public Song(String album, String artist, String img, String link, String title, String songId) {
        this.album = album;
        this.artist = artist;
        this.img = img;
        this.link = link;
        this.title = title;
        this.songId = songId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}
