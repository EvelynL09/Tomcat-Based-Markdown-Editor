<%@ page language="java" import="project2package.RenderMD" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Preview Post</title>
    <link rel="stylesheet" href="styles.css" />
</head>
<body>
    <div>
        <form action="post" method="POST">
            <%
                String username = request.getParameter("username");
                String postid = request.getParameter("postid");
                String title = request.getParameter("title");
                String body = request.getParameter("body");
                RenderMD myRender = new RenderMD();
                String titleHtml = "";//display empty if there's no title
                String bodyHtml = "";//display empty if there's no body
                if(title != null)
                    titleHtml = myRender.myrender(title);
                if(body != null)
                    bodyHtml = myRender.myrender(body);
            %>
            <input type="hidden" name="username" value="<%= username %>">
            <input type="hidden" name="postid" value="<%= postid %>">
            <input type="hidden" name="title" value="<%= title %>">
            <input type="hidden" name="body" value="<%= body %>">
            <button type="submit" name="action" value="open">Close Preivew</button>
        </form>
    </div>
    <div>
        <h1 id="title">
            <%= titleHtml %>
        </h1>
        <div id="body">
            <%= bodyHtml %>
        </div>
    </div>
</body>
</html>