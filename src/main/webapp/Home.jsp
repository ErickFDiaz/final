<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="example_jpa.model.User" %>
<%@ page import="example_jpa.enums.Role" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Inicio</title>
    <!-- Bootstrap CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    
    <div class="container mt-5">
        <h1>Bienvenido, <%= user.getUsername() %>!</h1>
        <p>Tu nivel de acceso es: <%= user.getRole() %></p>
        
        <% if (user.getRole() == Role.ADMIN) { %>
            <h2>Links de mantenimientos</h2>
            <ul>
                <li><a href="UserServlet?type=listar">Mantenimiento de usuarios</a></li>
                <li><a href="ProductServlet?type=listar">Mantenimiento de productos</a></li>
            </ul>
        <% } %>
        
        <% if (user.getRole() == Role.CLIENT) { %>
            <h2>Menú de usuario</h2>
            <ul>
                <li><a href="ProductServlet?type=tienda">Tienda</a></li>
                <li><a href="CartServlet?type=view">Ver Carrito</a></li>
            </ul>
        <% } %>
    </div>

    <!-- Bootstrap JS, Popper.js, and jQuery -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>