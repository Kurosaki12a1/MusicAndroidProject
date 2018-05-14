package com.bku.musicandroid.Model;

/**
 * Created by Welcome on 5/14/2018.
 */

public class PlayListOnlineInfo {

    private String playListName;
    private String userName;
    private String playListId;
    private String liked;
    private String view;
    private String userId;

    public PlayListOnlineInfo(){

    }

    public PlayListOnlineInfo(String playListId,String playListName,String userId,String userName,String liked,String view ){
        this.playListName=playListName;
        this.userName=userName;
        this.userId=userId;
        this.playListId=playListId;
        this.liked=liked;
        this.view=view;

    }

    public String getPlayListName(){return playListName;}
    public String getUserName(){return userName;}
    public String getPlayListId(){return playListId;}
    public String getLiked(){return liked;}
    public String getView(){return view;}
    public String getUserId(){return userId;}
    public void setLiked(String liked){this.liked=liked;}
    public void setView(String view){this.view=view;}
}
