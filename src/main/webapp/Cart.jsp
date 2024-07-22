<%@ page import="java.util.List" %>
<%@page import="example_jpa.model.Cart"%>
<%@page import="example_jpa.model.CartItem"%>
<%@page import="example_jpa.model.Product"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
    <title>Shopping Cart</title>
    <!-- Bootstrap CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h1 class="mb-4">Shopping Cart</h1>
        <div class="table-responsive">
            <table class="table table-bordered table-hover">
                <thead class="thead-dark">
                    <tr>
                        <th>Product ID</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Total</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                    Cart cart = (Cart) request.getAttribute("cart");
                    
                    List<CartItem> cartItems = cart.getCartItems();
                    //List<CartItem> cartItems =(List) request.getAttribute("cartItems");
                    double totalCartPrice = 0;
                    if (cartItems != null) {
                        for (CartItem item : cartItems) {
                            totalCartPrice += item.getQuantity() * item.getProduct().getPrice();
                    %>
                    <tr>
                        <td><%= item.getProduct().getProductId() %></td>
                        <td><%= item.getProduct().getName() %></td>
                        <td><%= item.getProduct().getDescription() %></td>
                        <td><%= item.getProduct().getPrice() %></td>
                        <td><%= item.getQuantity() %></td>
                        <td><%= item.getQuantity() * item.getProduct().getPrice() %></td>
                        <td>
                            <form action="CartServlet" method="post" class="form-inline">
                                <input type="hidden" name="type" value="remove">
                                <input type="hidden" name="userId" value="1">
                                <input type="hidden" name="cartItemId" value="<%= item.getCartItemId() %>">
                                <input type="hidden" name="cartId" value="<%= item.getCart().getCartId() %>">
                                <input type="hidden" name="productId" value="<%= item.getProduct().getProductId() %>">
                                <button type="submit" class="btn btn-danger mb-2">Remove</button>
                            </form>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="7" class="text-center">No items in the cart</td>
                    </tr>
                    <%
                    }
                    %>
                    <tr>
                        <td colspan="5" class="text-right"><strong>Total</strong></td>
                        <td><strong><%= totalCartPrice %></strong></td>
                        <td></td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="mt-4">
            <a href="ProductServlet?type=tienda" class="btn btn-primary">Continue Shopping</a>
            <a href="CartServlet?type=checkout" class="btn btn-success">Checkout</a>
        </div>
    </div>
    
    <!-- Bootstrap JS, Popper.js, and jQuery -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>
