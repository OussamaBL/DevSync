<%@ page import="Model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Task" %>
<%@ page import="Model.User" %>
<%@ page import="Model.Tag" %>
<%@ page import="Model.Enums.TaskStatus" %>
<%@ page import="Model.Enums.UserType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html lang="en">

<head>
    <title>Task List</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>

<body>
<jsp:include page="Layouts/Nav.jsp" />
<div class="container">
    <h2>Task List</h2>

    <%
        String status = request.getParameter("status");
        if (status != null) {
    %>
    <div class="alert alert-success" role="alert">
        <% if ("success".equals(status)) { %>
        Task has been successfully created/updated/deleted!
        <% } else{ %>
        <%= status %>
        <% } %>
    </div>
    <% } %>

    <table class="table table-bordered">
        <thead>
        <tr>
            <th>Date creation</th>
            <th>Date fin</th>
            <th>Title</th>
            <th>Description</th>
            <th>Status</th>
            <th>User Assigned</th>
            <th>User Created</th>
            <th>Tags</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Task> listTasks = (List<Task>) request.getAttribute("listTasks");
            if (listTasks != null) {
                for (Task task : listTasks) {
        %>
        <tr>
            <td><%= task.getDate_create() %></td>
            <td><%= task.getDate_fin() %></td>
            <td><%= task.getTitle() %></td>
            <td><%= task.getDescription() %></td>
            <td><%= task.getStatus() %></td>
            <td><%= task.getUser_assigne() != null ? task.getUser_assigne().getFirst_name() + " " + task.getUser_assigne().getLast_name() : "Unassigned" %></td>
            <td><%= task.getUser_create() != null ? task.getUser_create().getFirst_name() + " " + task.getUser_create().getLast_name() : "Unassigned" %></td>
            <td>
                <% for (Tag t: task.getListTags()) { %>
                    <%=t.getName() %><br>
                <%  } %>
            </td>
            <td>
                <%= task.getStatus().name() %>
            </td>
            <td>
                <a href="tasks?action=edit&id=<%= task.getId() %>" class="btn btn-warning btn-sm">Edit</a>
                <% User us=(User) request.getSession().getAttribute("user");
                    if (us.getId()== task.getUser_create().getId()){ %>
                    <form action="tasks" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="<%= task.getId() %>"/>
                        <input type="hidden" name="_method" value="deleteTask"/>
                        <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                    </form>
                <% } else {%>
                <form action="requestTask" method="post" style="display:inline;">
                    <input type="hidden" name="taskId" value="<%= task.getId() %>"/>
                    <input type="hidden" name="user_id" value="<%= us.getId() %>"/>
                    <input type="hidden" name="requestType" value="DELETE"/>
                    <button type="submit" class="btn btn-danger btn-sm">Delete with jeton</button>
                </form>

                <form action="requestTask" method="post" style="display:inline;">
                    <input type="hidden" name="taskId" value="<%= task.getId() %>"/>
                    <input type="hidden" name="user_id" value="<%= us.getId() %>"/>
                    <input type="hidden" name="requestType" value="REJECTED"/>
                    <button type="submit" class="btn btn-danger btn-sm">Reject</button>
                </form>

                <% } %>
                <form action="tasks" method="post" style="display: inline" id="formChangeStatus<%=task.getId()%>">
                    <input type="hidden" name="id_task" value="<%= task.getId()%>">
                    <input type="hidden" name="date_fin" value="<%= task.getDate_fin()%>">
                    <input type="hidden" name="_method" value="changeStatus"/>
                    <select name="status" onchange="document.getElementById('formChangeStatus<%=task.getId()%>').submit()">
                        <option value="NOT_STARTED" <%= task.getStatus()==TaskStatus.NOT_STARTED ? "selected" : "" %>>NOT_STARTED</option>
                        <option value="IN_PROGRESS" <%= task.getStatus()==TaskStatus.IN_PROGRESS ? "selected" : "" %>>IN_PROGRESS</option>
                        <option value="COMPLETED" <%= task.getStatus()==TaskStatus.COMPLETED ? "selected" : "" %>>COMPLETED</option>
                    </select>
                </form>
            </td>
        </tr>

        <%
                }
            }
        %>
        </tbody>
    </table>

    <a href="tasks?action=newTask" class="btn btn-primary">Add New Task</a>
    <a href="tasks" class="btn btn-info">Back to Home</a>
</div>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</body>

</html>
