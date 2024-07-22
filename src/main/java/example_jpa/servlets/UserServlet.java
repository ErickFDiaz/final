package example_jpa.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import example_jpa.enums.Role;
import example_jpa.model.User;
import example_jpa.service.UserService;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    UserService userService = new UserService();
    
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
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

        // Permitir solo a los administradores acceder a las funciones de mantenimiento
        if (user.getRole() != Role.ADMIN) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso no autorizado");
            return;
        }
        
        String type = request.getParameter("type");

        switch (type) {
            case "registrar":
                String id = request.getParameter("id");
                if (id != null && !id.isEmpty()) {
                    editarUsuario(request, response);
                } else {
                    crearUsuario(request, response);
                }
                break;
            case "listar":
                listarUsuario(request, response);
                break;
            case "obtener":
                obtenerUsuario(request, response);
                break;
            case "eliminar":
                eliminarUsuario(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida");
                break;
        }
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        userService.delete(Long.parseLong(id));
        request.setAttribute("message", "Usuario eliminado correctamente");
        listarUsuario(request, response);
    }

    private void obtenerUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");

        User user = userService.find(Long.parseLong(id));

        if (user != null) {
            request.setAttribute("editUser", user);
            listarUsuario(request, response);
        } else {
            request.getRequestDispatcher("Error.jsp").forward(request, response);
        }
    }

    private void listarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> users = userService.list();
        request.setAttribute("users", users);
        request.getRequestDispatcher("Users.jsp").forward(request, response);
    }

    private void crearUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = extractUserFromRequest(request);
        Long userId = userService.add(user);
        if (userId != null) {
        	System.out.println("Usuario creado exitosamente con ID: " + userId);
            request.setAttribute("message", "Usuario creado exitosamente con ID: " + userId);
        } else {
            request.setAttribute("message", "Error al crear el usuario.");
        }
        listarUsuario(request, response);
    }

    private void editarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = extractUserFromRequest(request);
        Long id = Long.parseLong(request.getParameter("id"));
        user.setUserId(id);
        User updated = userService.update(user);
        if (updated != null) {
            request.setAttribute("message", "Usuario actualizado exitosamente con ID: " + updated.getUserId());
        } else {
            request.setAttribute("message", "Error al actualizar el usuario.");
        }
        listarUsuario(request, response);
    }

    private User extractUserFromRequest(HttpServletRequest request) {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        user.setEmail(request.getParameter("email"));
        user.setRole(Role.valueOf(request.getParameter("role")));
        return user;
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
