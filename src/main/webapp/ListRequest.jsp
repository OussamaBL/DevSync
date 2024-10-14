<%@ page import="Model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Task" %>
<%@ page import="Model.User" %>
<%@ page import="Model.TaskRequest" %>
<%@ page import="Model.Enums.TaskStatus" %>
<%@ page import="Model.Enums.UserType" %>
<%@ page import="Model.Enums.TypeRequest" %>
<%@ page import="Model.Enums.StatusRequest" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html lang="en">

<head>
    <title>Request List</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>

<body>
<jsp:include page="Layouts/Nav.jsp" />
<div class="container">
    <h2>Request List</h2>

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
            <th>Date Request</th>
            <th>Date fin</th>
            <th>Title</th>
            <th>Status task</th>
            <th>User Assigned</th>
            <th>Status Request</th>
            <th>Type Request</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<TaskRequest> listTasksRequest = (List<TaskRequest>) request.getAttribute("listRequest");
            if (listTasksRequest != null) {
                for (TaskRequest taskRequest : listTasksRequest) {
        %>
        <tr>
            <td><%= taskRequest.getDate_request() %></td>
            <td><%= taskRequest.getTask().getDate_fin() %></td>
            <td><%= taskRequest.getTask().getTitle() %></td>
            <td><%= taskRequest.getTask().getStatus().name() %></td>
            <td><%= taskRequest.getUser().getFirst_name()+" "+taskRequest.getUser().getLast_name() %></td>
            <td><%= taskRequest.getStatus().name() %></td>
            <td><%= taskRequest.getType().name() %></td>
            <td>

                <% User us=(User) request.getSession().getAttribute("user");
                    if (us.getRole_user()== UserType.MANAGER){
                        if(taskRequest.getType()== TypeRequest.REJECT && taskRequest.getStatus()== StatusRequest.PENDING){ %>
                            <form action="taskRequest_manager" method="get" style="display:inline;">
                                <input type="hidden" name="action" value="ACCEPT"/>
                                <input type="hidden" name="requestId" value="<%=taskRequest.getId()%>"/>
                                <button type="submit" class="btn btn-danger btn-sm">Change the user</button>
                            </form>
                         <% }
                        if(taskRequest.getType()== TypeRequest.DELETE && taskRequest.getStatus()== StatusRequest.PENDING) { %>
                        <form action="taskRequest_manager" method="get" style="display:inline;">
                            <input type="hidden" name="action" value="DELETE"/>
                            <input type="hidden" name="requestId" value="<%=taskRequest.getId()%>"/>
                            <button type="submit" class="btn btn-danger btn-sm">Accepte the delete of task</button>
                        </form>
                    <% } } %>

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
