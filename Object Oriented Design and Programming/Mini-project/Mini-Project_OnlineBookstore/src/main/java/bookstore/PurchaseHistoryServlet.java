package bookstore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/PurchaseHistoryServlet")
public class PurchaseHistoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/books";
    private static final String DATABASE_USER = "guest";
    private static final String DATABASE_PASSWORD = "guest";
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        List<Order> orders = getPurchaseHistory(user.getUserId());
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("purchase-history.jsp").forward(request, response);
    }
    
    private List<Order> getPurchaseHistory(int userId) {
        List<Order> orders = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            
            String sql = "SELECT o.order_id, o.order_date, o.total_amount, " +
                        "oi.isbn, b.title, b.author, oi.price " +
                        "FROM orders o " +
                        "JOIN order_items oi ON o.order_id = oi.order_id " +
                        "JOIN bookinfo b ON oi.isbn = b.isbn " +
                        "WHERE o.user_id = ? " +
                        "ORDER BY o.order_date DESC";
            
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();
            
            Order currentOrder = null;
            int currentOrderId = -1;
            
            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                
                if (orderId != currentOrderId) {
                    if (currentOrder != null) {
                        orders.add(currentOrder);
                    }
                    currentOrder = new Order();
                    currentOrder.setOrderId(orderId);
                    currentOrder.setOrderDate(resultSet.getTimestamp("order_date"));
                    currentOrder.setTotalAmount(resultSet.getDouble("total_amount"));
                    currentOrder.setItems(new ArrayList<>());
                    currentOrderId = orderId;
                }
                
                OrderItem item = new OrderItem();
                item.setIsbn(resultSet.getString("isbn"));
                item.setTitle(resultSet.getString("title"));
                item.setAuthor(resultSet.getString("author"));
                item.setPrice(resultSet.getDouble("price"));
                
                currentOrder.getItems().add(item);
            }
            
            if (currentOrder != null) {
                orders.add(currentOrder);
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return orders;
    }
}