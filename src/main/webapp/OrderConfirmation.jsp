<%@ page import="java.util.List" %>
<%@page import="example_jpa.model.Order"%>
<%@page import="example_jpa.model.OrderItem"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <title>Order Confirmation</title>
    <!-- Bootstrap CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Order Confirmation</h1>
        <div class="table-responsive">
            <table class="table table-bordered table-hover">
                <thead class="thead-dark">
                    <tr>
                        <th>Product ID</th>
                        <th>Name</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                    Order order = (Order) request.getAttribute("order");
                    List<OrderItem> orderItems = (List<OrderItem>) request.getAttribute("listOrderItem");
                    double totalOrderPrice = 0; 
                    if (orderItems != null) {
                        for (OrderItem item : orderItems) {
                            totalOrderPrice += item.getQuantity() * item.getProduct().getPrice();
                    %>
                    <tr>
                        <td><%= item.getProduct().getProductId() %></td>
                        <td><%= item.getProduct().getName() %></td>
                        <td><%= item.getQuantity() %></td>
                        <td><%= item.getProduct().getPrice() %></td>
                        <td><%= item.getQuantity() * item.getProduct().getPrice() %></td>
                    </tr>
                    <%
                        }
                    }
                    %>
                    <tr>
                        <td colspan="4" class="text-right"><strong>Total</strong></td>
                        <td><strong><%= totalOrderPrice %></strong></td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="mt-4">
            <a href="ProductServlet?type=tienda" class="btn btn-primary">Continue Shopping</a>
        </div>
    </div>
    
    <!-- Bootstrap JS, Popper.js, and jQuery -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>
