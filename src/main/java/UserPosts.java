package project2package;

import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserPosts {
	public String       username;
    public Integer      postid;
    public String       title;
    public String       body;
    public Timestamp    modified;
    public Timestamp    created;

    public UserPosts() {
        this.username = null;
        this.postid =   null;
        this.title =    null;
        this.body =     null;
        this.modified = null;
        this.created =  null;
    }

    public String getUsername() {
    	return this.username;
    }

    public Integer getPostid() {
    	return this.postid;
    }

    public String getTitle() {
    	return this.title;
    }

    public String getBody() {
    	return this.body;
    }

    public String getModified() {
		SimpleDateFormat f = new SimpleDateFormat("MM/dd/YYYY HH:mm");
		return f.format(this.modified);
    }

    public String getCreated() {
    	SimpleDateFormat f = new SimpleDateFormat("MM/dd/YYYY HH:mm");
		return f.format(this.created);
    }

}