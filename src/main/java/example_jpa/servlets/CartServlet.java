package example_jpa.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import example_jpa.model.Cart;
import example_jpa.model.CartItem;
import example_jpa.model.Order;
import example_jpa.model.OrderItem;
import example_jpa.model.Product;
import example_jpa.model.User;
import example_jpa.service.CartItemService;
import example_jpa.service.CartService;
import example_jpa.service.OrderItemService;
import example_jpa.service.OrderService;
import example_jpa.service.ProductService;
import example_jpa.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CartService cartService = new CartService();
	private CartItemService cartItemService = new CartItemService();
	private ProductService productService = new ProductService();
	private UserService userService = new UserService();
	private OrderService orderService = new OrderService();
	private OrderItemService orderItemService = new OrderItemService();

	public CartServlet() {
		super();
	}


	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String type = request.getParameter("type");

		switch (type) {
		case "add":
			addToCart(request, response);
			break;
		case "view":
			viewCart(request, response);
			break;
		case "remove":
			removeFromCart(request, response);
			break;
		case "checkout":
			checkoutCart(request, response);
			break;
		default:
			break;
		}
	}

	private void addToCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long userId = Long.parseLong(request.getParameter("userId"));
		Long productId = Long.parseLong(request.getParameter("productId"));
		Integer quantity = Integer.parseInt(request.getParameter("quantity"));
		System.out.println(userId);
		System.out.println(productId);
		System.out.println(quantity);
		User user = userService.find(userId);
		Product product = productService.find(productId);

		if (user == null || product == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user or product ID");
			return;
		}

		Cart cart = cartService.findByUser(user);
		if (cart == null) {
			cart = new Cart();
			cart.setUser(user);
			cartService.add(cart);
		}

		CartItem cartItem = new CartItem();
		cartItem.setCart(cart);
		cartItem.setProduct(product);
		cartItem.setQuantity(quantity);
		cartItemService.add(cartItem);

		viewCart(request, response);
	}

	private void viewCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long userId = Long.parseLong(request.getParameter("userId"));
		User user = userService.find(userId);

		if (user == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
			return;
		}

		Cart cart = cartService.findByUser(user);
		List<CartItem> cartItems = cartItemService.findByCart(cart);
		cart.setCartItems(cartItems);
		for (CartItem item : cartItems) {
            System.out.println(item.getProduct().getName());
        }
		request.setAttribute("cart", cart);
		request.setAttribute("cartItems", cartItems);
		request.getRequestDispatcher("Cart.jsp").forward(request, response);
	}

	private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		Long cartItemId = Long.parseLong(request.getParameter("cartItemId"));
//		cartItemService.delete(cartItemId);
		
		int productId = Integer.parseInt(request.getParameter("productId"));
		Long cartId = Long.parseLong(request.getParameter("cartId"));

        Cart cart = cartService.find(cartId);
        if (cart != null) {
            List<CartItem> cartItems = cart.getCartItems();
            CartItem itemToRemove = null;
            for (CartItem item : cartItems) {
                if (item.getProduct().getProductId() == productId) {
                    itemToRemove = item;
                    break;
                }
            }
            if (itemToRemove != null) {
                cartItems.remove(itemToRemove);
                cartItemService.delete(itemToRemove.getCartItemId());
            }
        }

		viewCart(request, response);
	}

	private void checkoutCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long userId = 1L;
		User user = userService.find(userId);

		if (user == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
			return;
		}

		Cart cart = cartService.findByUser(user);
		List<CartItem> cartItems = cartItemService.findByCart(cart);

		if (cartItems.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cart is empty");
			return;
		}

		// Crear una nueva orden
		Order order = new Order();
		order.setUser(user);
		order.setOrderDate(new Date());
		Long orderId = orderService.add(order);

		// Procesar cada ítem en el carrito
		for (CartItem cartItem : cartItems) {
			Product product = cartItem.getProduct();
			if (product.getStock() < cartItem.getQuantity()) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Insufficient stock for product: " + product.getName());
				return;
			}

			// Actualizar el stock del producto
			product.setStock(product.getStock() - cartItem.getQuantity());
			productService.update(product);

			// Crear un nuevo ítem de orden
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(product);
			orderItem.setQuantity(cartItem.getQuantity());
			orderItemService.add(orderItem);

			// Eliminar el ítem del carrito
			cartItemService.delete(cartItem.getCartItemId());
		}
		
		List<OrderItem> listOrderItem = orderItemService.listByOrder(orderId);

		// Redirigir a una página de confirmación de pedido o similar
		request.setAttribute("order", order);
		request.setAttribute("listOrderItem", listOrderItem);

		request.getRequestDispatcher("OrderConfirmation.jsp").forward(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
