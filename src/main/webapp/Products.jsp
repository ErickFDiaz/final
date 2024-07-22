<%@ page import="java.util.List" %>
<%@ page import="example_jpa.model.Product" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <title>Mantenimiento de Productos</title>
    <!-- Bootstrap CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Mantenimiento de Productos</h1>
        <form action="ProductServlet" method="post" class="mb-4">
            <input type="hidden" name="type" value="registrar">
            <input type="hidden" name="id" value="${product.productId}">

            <!-- Name input -->
            <div class="form-group">
                <label for="name">Nombre del Producto</label>
                <input type="text" class="form-control" id="name" name="name" placeholder="Ingrese el nombre del producto" value="${product.name}" required>
            </div>

            <!-- Description input -->
            <div class="form-group">
                <label for="description">Descripción</label>
                <textarea class="form-control" id="description" name="description" placeholder="Ingrese la descripción del producto" required>${product.description}</textarea>
            </div>

            <!-- Price input -->
            <div class="form-group">
                <label for="price">Precio</label>
                <input type="number" step="0.01" class="form-control" id="price" name="price" placeholder="Ingrese el precio del producto" value="${product.price}" required>
            </div>

            <!-- Stock input -->
            <div class="form-group">
                <label for="stock">Stock</label>
                <input type="number" class="form-control" id="stock" name="stock" placeholder="Ingrese la cantidad de stock" value="${product.stock}" required>
            </div>

            <button type="submit" class="btn btn-primary">Guardar</button>
        </form>

        <div class="table-responsive">
            <table class="table table-bordered table-hover">
                <thead class="thead-dark">
                    <tr>
                        <th>ID Producto</th>
                        <th>Nombre</th>
                        <th>Descripción</th>
                        <th>Precio</th>
                        <th>Stock</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                    List<Product> listProducts = (List<Product>) request.getAttribute("products");
                    if (listProducts != null) {
                        for (Product product : listProducts) {
                    %>
                    <tr>
                        <td><%= product.getProductId() %></td>
                        <td><%= product.getName() %></td>
                        <td><%= product.getDescription() %></td>
                        <td><%= product.getPrice() %></td>
                        <td><%= product.getStock() %></td>
                        <td>
                            <form action="ProductServlet" method="post" style="display:inline;">
                                <input type="hidden" name="type" value="eliminar">
                                <input type="hidden" name="id" value="<%= product.getProductId() %>">
                                <button type="submit" class="btn btn-danger btn-sm">Eliminar</button>
                            </form>
                            <form action="ProductServlet" method="get" style="display:inline;">
                                <input type="hidden" name="type" value="obtener">
                                <input type="hidden" name="id" value="<%= product.getProductId() %>">
                                <button type="submit" class="btn btn-warning btn-sm">Editar</button>
                            </form>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="6" class="text-center">No hay productos disponibles</td>
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