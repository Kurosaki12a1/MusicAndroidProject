package  com.bku.musicandroid;

import java.util.Date;

/**
 * Created by Administrator on 3/21/2018.
 */

public class Users {

    public String DateOfBirth;
    public String FullName;
    public String UserName;
    public String Email;
    public String AvatarURL;

    public Users(String UserName, String Email, String FullName,String AvatarURL,String DateOfBirth) {
        this.UserName=UserName;
        this.Email=Email;
        this.FullName=FullName;
        this.AvatarURL=AvatarURL;
        this.DateOfBirth=DateOfBirth;
    }

    public Users(){

    }

    public String getUserName(){return UserName;}

    public String getEmail(){return Email;}

    public String getFullName(){return FullName;}

    public String getAvatarURL(){return AvatarURL;}

    public String DateOfBirth(){return DateOfBirth;}




}

