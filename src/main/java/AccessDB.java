package project2package;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

class Record {
    String username = null;
    int postid = 0;
    String title = null;
    String body = null;
    public Record(String title, String body){
        this.title = title;
        this.body = body;
    }
}

public class AccessDB {
    //private variables
    private Connection con;
    private Statement  s;
    private ResultSet rs;
    //constructor
    public AccessDB() {
        this.con = null;
        this.s = null;
        this.rs = null;

        /* load the driver */
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
            return;
        }

        try{
            this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/CS144", "cs144", "");
        } catch (Exception e){
            System.out.println("Driver Manager Connection Failed!\n");
            return;
        }

    }
    //desturctor
    protected void finalize()
    {
        //TODO do we need to write anything to handle ignore
        try {
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        try {
            s.close();
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        try {
            con.close();
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
    }

    public int getMaxPostid(String username){
        int maxPostid = 0;
        try {
            PreparedStatement stmt = con.prepareStatement(
                "SELECT MAX(postid) AS maxPostid FROM Posts WHERE username = ?"
            );
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                maxPostid = rs.getInt("maxPostid");
            }

        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        return maxPostid;
    }

    public boolean checkExistence(String username, int postid){
        try{
            PreparedStatement selectStmt = con.prepareStatement(
                "SELECT username,postid FROM Posts WHERE postid = ? AND username = ?"
            );
            selectStmt.setInt(1, postid);
            selectStmt.setString(2, username);
            ResultSet rs = selectStmt.executeQuery();
            if(rs.next() == true){
                return true;
            }
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        return false;
    }

    public void createPost(String username, int postid, String title, String body){
        try {
            PreparedStatement newStmt = con.prepareStatement(
                "INSERT INTO Posts Values(?, ?, ?, ?, ?, ?)"
            );
            newStmt.setString(1, username);
            newStmt.setInt(2, postid);
            newStmt.setString(3, title);
            newStmt.setString(4, body);
            newStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis() - 25200000));
            newStmt.setTimestamp(6, new Timestamp(System.currentTimeMillis() - 25200000));
            int n = newStmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
    }

    public void updatePost(String username, int postid, String title, String body){
        try {
            PreparedStatement updateStmt = con.prepareStatement(
                "UPDATE Posts SET title=?, body=?, modified=? WHERE postid = ? AND username = ?"
            );
            updateStmt.setString(1, title);
            updateStmt.setString(2, body);
            updateStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis() - 25200000));
            updateStmt.setInt(4, postid);
            updateStmt.setString(5, username);
            int n = updateStmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
    }

    public ArrayList<UserPosts> getList(String username){
        ArrayList<UserPosts> resList = new ArrayList<>();
        try {
            PreparedStatement allStmt = con.prepareStatement(
                "SELECT * FROM Posts WHERE username=?"
            );
            allStmt.setString(1, username);
            ResultSet rs = allStmt.executeQuery();
            while (rs.next()){
                UserPosts temp = new UserPosts();
                temp.username = rs.getString("username");
                temp.postid = rs.getInt("postid");
                temp.title = rs.getString("title");
                temp.body = rs.getString("body");
                temp.modified = rs.getTimestamp("modified");
                temp.created = rs.getTimestamp("created");
                resList.add(temp);
            }
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        return resList;
    }

    // partB
    public void delete(String username, int postid){
        try{
            PreparedStatement deleteStmt = con.prepareStatement(
                "DELETE FROM Posts WHERE postid = ? AND username = ?"
            );
            deleteStmt.setInt(1, postid);
            deleteStmt.setString(2, username);
            int n = deleteStmt.executeUpdate();
        } catch (SQLException ex) {
                System.err.println("SQLException: " + ex.getMessage());
        }
    }

    public Record fetchTitleAndBody (String username, int postid){
        try{
            PreparedStatement selectStmt = con.prepareStatement(
                "SELECT title, body FROM Posts WHERE postid = ? AND username = ?"
            );
            selectStmt.setInt(1, postid);
            selectStmt.setString(2, username);
            ResultSet rs = selectStmt.executeQuery();
            if(rs.next()){
                String title, body;
                title = rs.getString("title");
                body = rs.getString("body");
                Record myRecord = new Record(title, body);
                return myRecord;
            }
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        return null;
    }


}
