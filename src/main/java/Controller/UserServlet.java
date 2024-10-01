/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.User;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "UserServlet", urlPatterns = {"/UserServlet"})
public class UserServlet extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws
            ServletException, IOException {
       super.service(req, resp); 
    }
    @Override
    public void init() throws ServletException {
       super.init();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
        switch (action)
        {
        case "New":
            showNewForm(request, response);
        break;
        case "insert":
            insertUser(request, response);
        break;
        case "delete":
            deleteUser(request, response);
        break;
        case "edit":
            showEditForm(request, response);
        break;
        case "update":
            updateUser(request, response);
        break;
        case "list":
            listUser(request, response);
        break;
        }
        }
        catch (Exception ex)
        {
        throw new ServletException(ex);
        }
    }
    
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher =request.getRequestDispatcher("User.jsp");
        dispatcher.forward(request, response);
    }
    
    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException,ServletException, ClassNotFoundException {
       /* int code = Integer.parseInt(request.getParameter("code"));
        String designation = request.getParameter("designation");
        float prix = Float.parseFloat(request.getParameter("prix"));
        User User = new User(code, designation, prix);
        DaoUser.create(User);
        response.sendRedirect("UserServlet?action=list");*/
    }
    
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ClassNotFoundException {
        /*int id = Integer.parseInt(request.getParameter("code"));
        User art=DaoUser.getUser(id);
        DaoUser.delete(art);
        response.sendRedirect("UserServlet?action=list");*/
    }
    
     private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException, ClassNotFoundException {
       /* int id = Integer.parseInt(request.getParameter("code"));
        User art = DaoUser.getUser(id);
        System.out.println(" SERVLET : "+art.getCode()+ " "+art.getDesignation()+ " "+art.getPrix());
        request.setAttribute("User", art);
        RequestDispatcher dispatcher = request.getRequestDispatcher("Form_User.jsp");
        dispatcher.forward(request, response);*/
    }
     
    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ClassNotFoundException {
        /*int code = Integer.parseInt(request.getParameter("code"));
        String designation = request.getParameter("designation");
        float prix = Float.parseFloat(request.getParameter("prix"));
        User User = new User(code, designation, prix);
        DaoUser.update(User);
        response.sendRedirect("UserServlet?action=list");*/
    }
    
    private void listUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException, ClassNotFoundException {
       /* List<User> listArt = DaoUser.getAll();
        request.setAttribute("MaListe", listArt);*/
        RequestDispatcher dispatcher = request.getRequestDispatcher("about.jsp");
        dispatcher.forward(request, response);
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
