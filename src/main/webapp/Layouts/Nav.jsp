
<%@ page import="Model.User" %>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="#">DevSync</a>
    <div class="collapse navbar-collapse">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="tasks">Tasks</a>
            </li>
            <% User us=(User) request.getSession().getAttribute("user");
            if (us.getRole_user().name().equals("MANAGER")){ %>
                <li class="nav-item">
                    <a class="nav-link" href="users">Users </a>
                </li>
            <% } %>
            <li class="nav-item">
                <form action="login" method="post" style="display:inline;">
                    <input type="hidden" name="_method" value="DESTROY">
                    <input type="submit" class="btn btn-primary" value="Log out">
                </form>

            </li>
            <li style="margin-left: 20px"><%= us.getFirst_name()+" "+us.getLast_name()+" : "+us.getRole_user().name() %></li>
        </ul>
    </div>
</nav>
