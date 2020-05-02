<%@ page language="java" import="project2package.UserPosts" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>

<html>
<head>
	<meta charset="UTF-8">
    <title>Post List</title>
    <link rel="stylesheet" href="styles.css" />
</head>
<body>
	<div>
		<form action="post" method="POST">
			<input type="hidden" name="username" value="<%= request.getParameter("username") %>">
            <input type="hidden" name="postid" value="0">
            <button type="submit" name="action" value="open">New Post</button>
        </form>
    </div>
    <table>
        <tr><th>Title</th><th>Created</th><th>Modified</th><th>&nbsp;</th></tr>
        <% ArrayList<UserPosts> list = (ArrayList<UserPosts>)request.getAttribute("list"); %>
        <c:forEach var="cur" items="${list}">
            <tr>
            	<form action="post" method="POST">
            		<td><c:out value="${cur.getTitle()}"/></td>
            		<td><c:out value="${cur.getCreated()}"/></td>
            		<td><c:out value="${cur.getModified()}"/></td>
            		<input type="hidden" name="username" value="<c:out value="${cur.getUsername()}"/>" />
                    <input type="hidden" name="postid"   value="<c:out value="${cur.getPostid()}"/>" />
                    <input type="hidden" name="newpost" value="0">
                    <td>
                    	<button class="option" type="submit" name="action" value="open">Open</button>
                        <button class="option" type="submit" name="action" value="delete">Delete</button>
                    </td>
            	</form>
            </tr>
        </c:forEach>
    </table>
</body>
</html>