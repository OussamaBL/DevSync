<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.User" %>
<%@ page import="java.util.List" %>
<%
    User us = (User) request.getAttribute("user");
%>
<html>
<head>
    <title>Task assigne</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h1 class="mb-4">Task assigne</h1>
    <br>
    <br>
    <br>
    <br>
    <div>
        <p>Task title : <strong><%= request.getAttribute("taskTitle")%></strong></p>
        <p>Task description : <strong><%= request.getAttribute("taskDescription")%></strong></p>
        <p>Task id : <strong><%= request.getAttribute("taskId")%></strong></p>
        <p>Task request user id : <strong><%= request.getAttribute("requestUserId")%></strong></p>
    </div>
    <form action="taskRequest_manager" method="post">
        <input type="hidden" name="requestId" value="<%= request.getAttribute("requestId")  %>"/>
        <input type="hidden" name="action" value="ACCEPT" />
        <input type="hidden" name="taskId" value="<%=  request.getAttribute("taskId") %>" />



        <label for="users">Assigned:</label>
        <select id="users" class="form-control" name="newAssignedUser" >
            <%
                List<User> userList = (List<User>) request.getAttribute("users");
                if (userList != null) {
                    for (User user : userList) {
            %>
            <option value="<%= user.getId() %>" >
                <%= user.getFirst_name()+" "+user.getLast_name() %>
            </option>
            <% } } %>
        </select>


        <button type="submit" class="btn btn-primary">
            Assigne
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