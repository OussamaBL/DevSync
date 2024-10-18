<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.Task" %>
<%@ page import="Model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Enums.UserType" %>
<html>
<head>
    <title>Create Task</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="Layouts/Nav.jsp" />
<div class="container mt-5">
    <h1 class="mb-4">Create Task</h1>
    <form action="tasks" method="post" >

        <div class="form-group">
            <label for="title">Title:</label>
            <input type="text" class="form-control" id="title" name="title" required><br>
        </div>

        <div class="form-group">
            <label for="description">Description:</label>
            <textarea id="description" class="form-control" name="description" required></textarea><br>
        </div>

        <div class="form-group">
            <label for="date_start">Start Date:</label>
            <input type="date" class="form-control" id="date_start" name="date_start" required><br>
        </div>

        <div class="form-group">
            <label for="date_fin">Due Date:</label>
            <input type="date" class="form-control" id="date_fin" name="date_fin"><br>
        </div>

        <%--<label for="status">Status:</label>
        <select id="status" class="form-control" name="status">
            <option value="NOT_STARTED">NOT_STARTED</option>
            <option value="IN_PROGRESS">In Progress</option>
            <option value="COMPLETED">Completed</option>
            <option value="OVERDUE">OVERDUE</option>
        </select><br>
--%>
        <div class="form-group">
            <label for="tags">Tags (comma separated):</label>
            <input type="text" class="form-control" id="tags" name="tags" placeholder="tag1, tag2, tag3" required><br>
        </div>

        <% User us=(User) request.getSession().getAttribute("user");
            if (us.getRole_user()== UserType.MANAGER){ %>
        <label for="user_assigne_id">Assigned:</label>
        <select id="user_assigne_id" class="form-control" name="user_assigne_id">
            <%
                List<User> userList = (List<User>) request.getAttribute("userList");
                if (userList != null) {
                    for (User user : userList) {
            %>
            <option value="<%= user.getId() %>" <% if(us.getId()==user.getId()){ %> selected <% } %> >
                <%= user.getFirst_name()+" "+user.getLast_name() %>
            </option>
            <% } } %>
        </select>
        <% } %>
        <br>
        <input type="hidden" name="_method" value="addTask"/>
        <input type="submit" class="btn btn-primary" value="Create Task">

        <a href="users" class="btn btn-secondary">Cancel</a>
        <a href="home" class="btn btn-info">Back to Home</a>
    </form>
</div>


    <script>
       /* function validateDates() {
        const startDateInput = document.getElementById('date_start').value;
        const dueDateInput = document.getElementById('date_fin').value;

        const startDate = new Date(startDateInput);
        const dueDate = new Date(dueDateInput);
        const currentDate = new Date();

        currentDate.setDate(currentDate.getDate() + 3);

        if (startDate < currentDate) {
        alert("The start date must be at least 3 days after the current date.");
        return false;
    }

        if (dueDate <= startDate) {
        alert("The due date must be after the start date.");
        return false;
    }

        return true;
    }*/
</script>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>