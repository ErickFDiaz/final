<%@ page import="java.util.List" %>
<%@ page import="example_jpa.model.User" %>
<%@ page import="example_jpa.enums.Role" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <title>Lista de Usuarios</title>
    <!-- Bootstrap CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Lista de Usuarios</h1>
        
        <!-- Formulario de Registro/Edición de Usuarios -->
        <form action="UserServlet" method="post" class="mb-4">
            <input type="hidden" name="type" value="registrar">
            <input type="hidden" name="id" value="<%= request.getAttribute("editUser") != null ? ((User) request.getAttribute("editUser")).getUserId() : "" %>">

            <!-- Username input -->
            <div class="form-group">
                <label for="username">Nombre de Usuario</label>
                <input type="text" class="form-control" id="username" name="username" placeholder="Ingrese el nombre de usuario" value="<%= request.getAttribute("editUser") != null ? ((User) request.getAttribute("editUser")).getUsername() : "" %>" required>
            </div>

            <!-- Email input -->
            <div class="form-group">
                <label for="email">Correo Electrónico</label>
                <input type="email" class="form-control" id="email" name="email" placeholder="nombre@ejemplo.com" value="<%= request.getAttribute("editUser") != null ? ((User) request.getAttribute("editUser")).getEmail() : "" %>" required>
            </div>

            <!-- Password input -->
            <div class="form-group">
                <label for="password">Contraseña</label>
                <input type="password" class="form-control" id="password" name="password" placeholder="Ingrese la contraseña" required>
            </div>

            <!-- Role input -->
            <div class="form-group">
                <label for="role">Rol</label>
                <select class="form-control" id="role" name="role" required>
                    <option value="ADMIN" <%= request.getAttribute("editUser") != null && ((User) request.getAttribute("editUser")).getRole() == Role.ADMIN ? "selected" : "" %>>Administrador</option>
                    <option value="CLIENT" <%= request.getAttribute("editUser") != null && ((User) request.getAttribute("editUser")).getRole() == Role.CLIENT ? "selected" : "" %>>Cliente</option>
                </select>
            </div>

            <button type="submit" class="btn btn-primary">Guardar</button>
        </form>

        <!-- Lista de Usuarios -->
        <div class="table-responsive">
            <table class="table table-bordered table-hover">
                <thead class="thead-dark">
                    <tr>
                        <th>ID Usuario</th>
                        <th>Nombre de Usuario</th>
                        <th>Correo Electrónico</th>
                        <th>Rol</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                    List<User> listUsers = (List<User>) request.getAttribute("users");
                    if (listUsers != null) {
                        for (User user : listUsers) {
                    %>
                    <tr>
                        <td><%= user.getUserId() %></td>
                        <td><%= user.getUsername() %></td>
                        <td><%= user.getEmail() %></td>
                        <td><%= user.getRole() %></td>
                        <td>
                            <form action="UserServlet" method="post" style="display:inline;">
                                <input type="hidden" name="type" value="eliminar">
                                <input type="hidden" name="id" value="<%= user.getUserId() %>">
                                <button type="submit" class="btn btn-danger btn-sm">Eliminar</button>
                            </form>
                            <form action="UserServlet" method="get" style="display:inline;">
                                <input type="hidden" name="type" value="obtener">
                                <input type="hidden" name="id" value="<%= user.getUserId() %>">
                                <button type="submit" class="btn btn-warning btn-sm">Editar</button>
                            </form>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="5" class="text-center">No hay usuarios disponibles</td>
                    </tr>
                    <%
                    }
                    %>
                </tbody>
            </table>
        </div>
    </div>
    
    <!-- Bootstrap JS, Popper.js, and jQuery -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>
