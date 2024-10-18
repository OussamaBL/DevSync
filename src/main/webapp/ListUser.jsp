<%@ page import="Model.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>User List</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>

<body>

<jsp:include page="Layouts/Nav.jsp" />
<div class="container">
    <h2>User List</h2>

    <%
        String status = request.getParameter("status");
        if (status != null) {
    %>
    <div class="alert alert-success" role="alert">
        <% if ("success".equals(status)) { %>
        User has been successfully created/updated!
        <% } else if ("failure".equals(status)) { %>
        There was an error creating/updating the user.
        <% } %>
    </div>
    <% } %>

    <table class="table table-bordered">
        <thead>
        <tr>
            <th>ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>UserName</th>
            <th>Email</th>
            <th>Password</th>
            <th>Manager</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<User> userList = (List<User>) request.getAttribute("userList");
            if (userList != null) {
                for (User user : userList) {
        %>
        <tr>
            <td><%= user.getId() %></td>
            <td><%= user.getFirst_name() %></td>
            <td><%= user.getLast_name() %></td>
            <td><%= user.getUsername() %></td>
            <td><%= user.getEmail() %></td>
            <td><%= user.getPassword() %></td>
            <td><%= user.getRole_user() %></td>
            <td>
                <a href="users?action=edit&id=<%= user.getId() %>" class="btn btn-warning btn-sm">Edit</a>
                <form action="users" method="post" style="display:inline;">
                    <input type="hidden" name="id" value="<%= user.getId() %>"/>
                    <input type="hidden" name="_method" value="delete"/>
                    <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                </form>
            </td>
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>

    <a href="FormUser.jsp" class="btn btn-primary">Add New User</a>
    <a href="home" class="btn btn-info">Back to Home</a>
</div>
</body>

</html>
