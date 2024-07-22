package example_jpa.servlets;

import java.io.IOException;
import java.util.List;

import example_jpa.enums.Role;
import example_jpa.model.Product;
import example_jpa.model.User;
import example_jpa.service.ProductService;
import example_jpa.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ProductServlet
 */
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private ProductService productService = new ProductService();
    UserService userService = new UserService();
    
    public ProductServlet() {
        super();
    }

	
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	// Obtener el usuario actual desde la sesión
        Long userId = (Long) request.getSession().getAttribute("userId");
        User user = userService.find(userId);
        
        // Redirigir a la tienda si el usuario no está autenticado
        if (user == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String type = request.getParameter("type");

        // Permitir solo a los administradores acceder a las funciones de mantenimiento
        if (type.equals("registrar") || type.equals("listar") || type.equals("obtener") || type.equals("eliminar")) {
            if (user.getRole() != Role.ADMIN) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso no autorizado");
                return;
            }
        }

        switch (type) {
            case "registrar":
                String id = request.getParameter("id");
                if (id != null && !id.isEmpty()) {
                    editarProducto(request, response);
                } else {
                    crearProducto(request, response);
                }
                break;
            case "listar":
                listarProductos(request, response);
                break;
            case "tienda":
            	mostrarListaProductos(request, response);
                break;
            case "obtener":
                obtenerProducto(request, response);
                break;
            case "eliminar":
                eliminarProducto(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida");
                break;
        }
    }

    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        productService.delete(Long.parseLong(id));
        request.setAttribute("message", "Producto eliminado correctamente");
        listarProductos(request, response);
    }

    private void obtenerProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");

        Product product = productService.find(Long.parseLong(id));

        if (product != null) {
            request.setAttribute("product", product);
            listarProductos(request, response);
        } else {
            request.getRequestDispatcher("Error.jsp").forward(request, response);
        }
    }

    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products = productService.list();
        request.setAttribute("products", products);
        request.getRequestDispatcher("Products.jsp").forward(request, response);
    }
    
    private void mostrarListaProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products = productService.list();
        request.setAttribute("products", products);
        request.getRequestDispatcher("ProductList.jsp").forward(request, response);
    }

    private void crearProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Product product = extractProductFromRequest(request);
        Long productId = productService.add(product);
        if (productId != null) {
            System.out.println("Producto creado exitosamente con ID: " + productId);
            request.setAttribute("message", "Producto creado exitosamente con ID: " + productId);
        } else {
            request.setAttribute("message", "Error al crear el producto.");
        }
        listarProductos(request, response);
    }

    private void editarProducto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Product product = extractProductFromRequest(request);
        Long id = Long.parseLong(request.getParameter("id"));
        product.setProductId(id);
        Product updated = productService.update(product);
        if (updated != null) {
            request.setAttribute("message", "Producto actualizado exitosamente con ID: " + updated.getProductId());
        } else {
            request.setAttribute("message", "Error al actualizar el producto.");
        }
        listarProductos(request, response);
    }

    private Product extractProductFromRequest(HttpServletRequest request) {
        Product product = new Product();
        product.setName(request.getParameter("name"));
        product.setDescription(request.getParameter("description"));
        product.setPrice(Double.parseDouble(request.getParameter("price")));
        product.setStock(Integer.parseInt(request.getParameter("stock")));
        return product;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }


}
