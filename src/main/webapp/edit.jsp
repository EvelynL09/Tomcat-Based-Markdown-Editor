<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Post</title>
    <link rel="stylesheet" href="styles.css" />
</head>
<body>
    <div><h1>Edit Post</h1></div>
    <form action="post" method="POST">
        <div>
            <button type="submit" name="action" value="save">Save</button>
            <button type="submit" name="action" value="list">Close</button>
            <button type="submit" name="action" value="preview">Preview</button>
            <button type="submit" name="action" value="delete">Delete</button>
        </div>
            <input type="hidden" name="username" value="<%= request.getParameter("username") %>">
            <input type="hidden" name="postid" value="<%= request.getParameter("postid") %>">
        <div>
            <label class="text" for="title">Title</label>
        </div>
        <div>
            <input type="text" name="title" id="title" value="<%= request.getAttribute("title") %>">
        </div>
        <div>
            <label class="text" for="body">Body</label>
        </div>
        <div>
            <textarea name="body" style="height: 20rem;" id="body"><%= request.getAttribute("body") %></textarea>
        </div>
    </form>
</body>
</html>
