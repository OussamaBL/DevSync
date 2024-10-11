<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.User" %>
<%
    User user = (User) request.getAttribute("user");
%>
<html>
<head>
    <title><%= user != null ? "Update User" : "Create User" %></title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h1 class="mb-4"><%= user != null ? "Update User" : "Create User" %></h1>
    <form action="users" method="post">
        <input type="hidden" name="id" value="<%= user != null ? user.getId() : "" %>"/>

        <div class="form-group">
            <label for="userName">UserName:</label>
            <input type="text" class="form-control" id="userName" name="userName" value="<%= user != null ? user.getUsername() : "" %>" required/>
        </div>
        <div class="form-group">
            <label for="name">Name:</label>
            <input type="text" class="form-control" id="name" name="first_name" value="<%= user != null ? user.getFirst_name() : "" %>" required/>
        </div>
        <div class="form-group">
            <label for="lastName">lastName:</label>
            <input type="text" class="form-control" id="lastName" name="last_name" value="<%= user != null ? user.getLast_name() : "" %>" required/>
        </div>
       <%-- <div class="form-group">
            <label for="tokens">Tokens:</label>
            <input type="number" class="form-control" id="tokens" name="tokens" value="<%= user != null ? user.getTokens() : "" %>" required/>
        </div>--%>

        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" class="form-control" id="email" name="email" value="<%= user != null ? user.getEmail() : "" %>" required/>
        </div>

        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" class="form-control" id="password" name="password" value="<%= user != null ? user.getPassword() : "" %>" required/>
        </div>

        <button type="submit" class="btn btn-primary">
            <%= user != null ? "Update" : "Create" %>
        </button>

        <a href="users" class="btn btn-secondary">Cancel</a>
        <a href="home" class="btn btn-info">Back to Home</a>
    </form>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>