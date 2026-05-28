package bookstore;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ReceiptServlet")
public class ReceiptServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/books";
    private static final String DATABASE_USER = "guest";
    private static final String DATABASE_PASSWORD = "guest";
    
    public ReceiptServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        ShoppingCart cart = (ShoppingCart) session.getAttribute("bookstore.cart");
        
        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        String customerName = request.getParameter("customerName");
        
        double totalAmount = 0.0;
        if (cart != null) {
            totalAmount = cart.getTotalPrice();
        }
        
        Connection connection = null;
        PreparedStatement orderStatement = null;
        PreparedStatement itemStatement = null;
        ResultSet generatedKeys = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            
            connection.setAutoCommit(false);
            
            String orderSql = "INSERT INTO orders (user_id, total_amount) VALUES (?, ?)";
            orderStatement = connection.prepareStatement(orderSql, PreparedStatement.RETURN_GENERATED_KEYS);
            orderStatement.setInt(1, user.getUserId());
            orderStatement.setDouble(2, totalAmount);
            orderStatement.executeUpdate();
            
            generatedKeys = orderStatement.getGeneratedKeys();
            int orderId = -1;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }
            
            if (cart != null && !cart.isEmpty()) {
                String itemSql = "INSERT INTO order_items (order_id, isbn, quantity, price) VALUES (?, ?, 1, ?)";
                itemStatement = connection.prepareStatement(itemSql);
                
                for (Book book : cart) {
                    itemStatement.setInt(1, orderId);
                    itemStatement.setString(2, book.getIsbn());
                    itemStatement.setDouble(3, book.getPrice());
                    itemStatement.addBatch();
                }
                
                itemStatement.executeBatch();
            }
            
            connection.commit();
            
            if (cart != null) {
                cart.clear();
                session.setAttribute("bookstore.cart", cart);
            }
            
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            
            String outStr = "";
            outStr += "<html><head><title>Order Confirmation</title></head><body>";
            outStr += "<div style='margin: 20px;'>";
            outStr += "<h1>Order Confirmation</h1>";
            outStr += "<p><strong>Student Name:</strong> Li Ming Chun Simon</p>";
            outStr += "<p><strong>Student ID:</strong> 25017659D</p>";
            outStr += "<p>Dear " + customerName + " (Order #" + orderId + "), thanks for purchasing books from BookStore!</p>";
            outStr += "<p>Total Amount: $" + totalAmount + "</p>";
            outStr += "<p>This page will automatically redirect to the dashboard in 5 seconds.</p>";
            outStr += "<meta http-equiv=\"Refresh\" content=\"5; url=student-dashboard.jsp\">";
            outStr += "<p><a href='student-dashboard.jsp'>Return to Dashboard Now</a></p>";
            outStr += "</div></body></html>";
            out.print(outStr);
            out.close();
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>Order Failed</h1>");
            out.println("<p>Sorry, there was an error processing your order. Please try again.</p>");
            out.println("<p><a href='show-order.jsp'>Return to Cart</a></p>");
            out.println("</body></html>");
            
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (itemStatement != null) itemStatement.close();
                if (orderStatement != null) orderStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}