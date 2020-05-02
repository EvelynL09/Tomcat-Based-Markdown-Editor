package project2package;

import java.io.IOException;
import java.sql.* ;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * Servlet implementation class for Servlet: ConfigurationTest
 *
 */
public class Editor extends HttpServlet {
    /**
     * The Servlet constructor
     *
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public Editor() {}

    public void init() throws ServletException
    {
        /*  write any servlet initialization code here or remove this function */
    }

    public void destroy()
    {
        /*  write any servlet cleanup code here or remove this function */
    }

    /**
     * Handles HTTP GET requests
     *
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
	// implement your GET method handling code here
        String value = request.getParameter("action");
        if(value == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("<!DOCTYPE html><html><body><p>");
            response.getWriter().println(response.getStatus());
            response.getWriter().println(" (Bad Request): Missing Action when using method GET!!!</p></body></html>");
        }
        switch(value){
            case "open":
                actionOpen(request, response);
                break;
            case "list":
                actionList(request, response);
                break;
            case "preview":
                //goes to preview page -> w/o saving the current content
                actionPreview(request, response);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("<!DOCTYPE html><html><body><p>");
                response.getWriter().println(response.getStatus());
                response.getWriter().println(" (Bad Request): Method GET get unknown action \"" + value +"\"!!!</p></body></html>");
        }
    }

    /**
     * Handles HTTP POST requests
     *
     * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request,
     *      HttpServletResponse response)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
	// implement your POST method handling code here
        String value = request.getParameter("action");
        if(value == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("<!DOCTYPE html><html><body><p>");
            response.getWriter().println(response.getStatus());
            response.getWriter().println(" (Bad Request): Missing Action when using method POST!!!</p></body></html>");
        }

        switch(value){
            case "open":
                actionOpen(request, response);
                break;
            case "save":
                actionSave(request, response);
                break;
            case "list":
                actionList(request, response);
                break;
            case "preview":
                //goes to preview page -> w/o saving the current content
                actionPreview(request, response);
                break;
            case "delete":
                //deletes the post from the db and goes to the "list page"
                actionDelete(request, response);
                break;
            default://shouldn't go here
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("<!DOCTYPE html><html><body><p>");
                response.getWriter().println(response.getStatus());
                response.getWriter().println(" (Bad Request): Method POST get unknown action \"" + value +"\"!!!</p></body></html>");
        }
    }

    private void actionOpen(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        //required parameters: username and postid
        String username = request.getParameter("username");
        String postidStr = request.getParameter("postid");
        //make sure request has required parameters
        if(username == null || postidStr == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("<!DOCTYPE html><html><body><p>");
            response.getWriter().println(response.getStatus());
            response.getWriter().println(" (Bad Request): Missing username or postid!!</p></body></html>");
            // return;
        }
        else{
            int postid = stringToint(postidStr);
            if(postid == -1){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("<!DOCTYPE html><html><body><p>");
                response.getWriter().println(response.getStatus());
                response.getWriter().println(" (Bad Request): Postid is not valid. It must be an integer</p></body></html>");
            }
            else{
                //optional title body parameters
                String title = request.getParameter("title");
                String body = request.getParameter("body");

                boolean bodyAndTitlePassed;
                if(body == null || title == null)
                    bodyAndTitlePassed = false;
                else
                    bodyAndTitlePassed = true;

                if(postid <= 0){
                    if(bodyAndTitlePassed){
                        //use the passed parameter values as the initial title and body values
                        request.setAttribute("title", title);
                        request.setAttribute("body", body);
                        //return with status code 200
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                    else{//otherwise, set missing title and/or body to empty string and return with status code 200 (OK)
                        request.setAttribute("title", "");
                        request.setAttribute("body", "");
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                    //return the "edit page" for the post with the given postid by the user
                    request.getRequestDispatcher("/edit.jsp").forward(request, response);
                }
                else{//postid > 0
                    if(bodyAndTitlePassed){//click close window when using preview page
                        //use the passed parameter values as the initial title and body values
                        request.setAttribute("title", title);
                        request.setAttribute("body", body);
                        //return with status code 200
                        response.setStatus(HttpServletResponse.SC_OK);
                        //return the "edit page" for the post with the given postid by the user
                        request.getRequestDispatcher("/edit.jsp").forward(request, response);
                    }
                    else{
                        AccessDB mydb = new AccessDB();
                        boolean nameAndIDExist = mydb.checkExistence(username, postid);
                        if(nameAndIDExist){//if (username, postid) row exists in the database, retrieve the title and body from the database and return with status code 200(OK)
                            Record myRecord = mydb.fetchTitleAndBody(username, postid);
                            request.setAttribute("title", myRecord.title);
                            request.setAttribute("body", myRecord.body);
                            response.setStatus(HttpServletResponse.SC_OK);
                            //return the "edit page" for the post with the given postid by the user
                            request.getRequestDispatcher("/edit.jsp").forward(request, response);
                        }
                        else{ //otherwise, return with status code 404 (Not found) //SC_NOT_FOUND
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            response.getWriter().println("<!DOCTYPE html><html><body><p>");
                            response.getWriter().println(response.getStatus() + " (Not found): Cannot find the post with id " + postid + " for user " + username);
                            response.getWriter().println("</p></body></html>");
                        }
                    }
                }
            }
        }
    }

    private void actionSave(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

            AccessDB db = new AccessDB();
            //required parameters
            String username = request.getParameter("username");
            String postidStr = request.getParameter("postid");
            String title = request.getParameter("title");
            String body = request.getParameter("body");
            //parameter validation
            if(username == null || postidStr == null || title == null || body == null){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("<!DOCTYPE html><html><body><p>");
                response.getWriter().println(response.getStatus());
                response.getWriter().println(" (Bad Request): Missing username, postid, title or body!!</p></body></html>");
            }
            int postid = stringToint(postidStr);
            if(postid == -1){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("<!DOCTYPE html><html><body><p>");
                response.getWriter().println(response.getStatus());
                response.getWriter().println(" (Bad Request): Postid is not valid. It must be an integer</p></body></html>");
            }
            else if (postid<=0) {
                //assign a new postid, and save the content as a "new post"
                int newPostid = db.getMaxPostid(username) + 1;
                db.createPost(username, newPostid, title, body);

            }
            else {
                //if (username, postid) row exists in the database, update the row with new title, body, and modification date.
                if (db.checkExistence(username, postid)==true) {
                    db.updatePost(username, postid, title, body);
                }
                else{
                    //if (username, postid) row does not exist, do not make any change to the database
                }
            }
            actionList(request, response);

    }
    private void actionList(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            AccessDB db = new AccessDB();
            String username = request.getParameter("username");
            if(username == null){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("<!DOCTYPE html><html><body><p>");
                response.getWriter().println(response.getStatus());
                response.getWriter().println(" (Bad Request): Missing username!!</p></body></html>");
            }
            ArrayList<UserPosts> list = db.getList(username);
            request.setAttribute("list", list);
            request.getRequestDispatcher("/list.jsp").forward(request, response);

    }

    private void actionPreview(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        //required parameters: username, postid, title, and body
        String username = request.getParameter("username");
        String postid = request.getParameter("postid");
        //make sure request has required parameters
        if(username == null || postid == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("<!DOCTYPE html><html><body><p>");
            response.getWriter().println(response.getStatus());
            response.getWriter().println(" (Bad Request): Missing username or postid!!</p></body></html>");
        }
        String title = request.getParameter("title");
        String body = request.getParameter("body");
        //goes to preview page with the html rendering of the given title and body
        request.getRequestDispatcher("/preview.jsp").forward(request, response);
    }

    private void actionDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        //required parameters: username and postid
        String username = request.getParameter("username");
        String postidStr = request.getParameter("postid");
        //make sure request has required parameters
        if(username == null || postidStr == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("<!DOCTYPE html><html><body><p>");
            response.getWriter().println(response.getStatus());
            response.getWriter().println(" (Bad Request): Missing username or postid!!</p></body></html>");
        }
        int postid = stringToint(postidStr);
        if(postid == -1){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("<!DOCTYPE html><html><body><p>");
            response.getWriter().println(response.getStatus());
            response.getWriter().println(" (Bad Request): Postid is not valid. It must be an integer</p></body></html>");
        }
        else{
            //delete the corresponding post from the database
            AccessDB mydb = new AccessDB();
            mydb.delete(username, postid);
            //goes to updated list page
            actionList(request, response);
        }
    }

    //helper function -- convert
    private int stringToint(String mystr){
        Integer myint = -1;
        try {
            myint = Integer.parseInt(mystr);
        } catch(NumberFormatException e) {
            //the string postidStr does not contain a parsable integer
            System.out.println(e);
        }
        return myint;
    }
}


