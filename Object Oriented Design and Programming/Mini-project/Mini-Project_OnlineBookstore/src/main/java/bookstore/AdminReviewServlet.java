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

@WebServlet("/AdminReviewServlet")
public class AdminReviewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/books";
    private static final String DATABASE_USER = "guest";
    private static final String DATABASE_PASSWORD = "guest";
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        
        if (role == null || !"admin".equals(role)) {
            response.sendRedirect("login.html");
            return;
        }
        
        List<Review> pendingReviews = getPendingReviews();
        request.setAttribute("pendingReviews", pendingReviews);
        request.getRequestDispatcher("admin-reviews.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        
        if (role == null || !"admin".equals(role)) {
            response.sendRedirect("login.html");
            return;
        }
        
        String action = request.getParameter("action");
        String reviewId = request.getParameter("reviewId");
        
        if (reviewId != null && action != null) {
            if ("approve".equals(action)) {
                updateReviewStatus(Integer.parseInt(reviewId), "approved");
                request.setAttribute("message", "Review approved successfully.");
            } else if ("reject".equals(action)) {
                updateReviewStatus(Integer.parseInt(reviewId), "rejected");
                request.setAttribute("message", "Review rejected successfully.");
            }
        }
        
        List<Review> pendingReviews = getPendingReviews();
        request.setAttribute("pendingReviews", pendingReviews);
        request.getRequestDispatcher("admin-reviews.jsp").forward(request, response);
    }
    
    private List<Review> getPendingReviews() {
        List<Review> reviews = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            
            String sql = "SELECT r.*, u.username, b.title as book_title " +
                        "FROM book_reviews r " +
                        "JOIN users u ON r.user_id = u.user_id " +
                        "JOIN bookinfo b ON r.isbn = b.isbn " +
                        "WHERE r.status = 'pending' " +
                        "ORDER BY r.review_date DESC";
            statement = connection.prepareStatement(sql);
            
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                Review review = new Review();
                review.setReviewId(resultSet.getInt("review_id"));
                review.setIsbn(resultSet.getString("isbn"));
                review.setUserId(resultSet.getInt("user_id"));
                review.setUsername(resultSet.getString("username"));
                review.setRating(resultSet.getInt("rating"));
                review.setReviewText(resultSet.getString("review_text"));
                review.setReviewDate(resultSet.getTimestamp("review_date"));
                review.setStatus(resultSet.getString("status"));
                review.setBookTitle(resultSet.getString("book_title"));
                
                reviews.add(review);
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
        
        return reviews;
    }
    
    private void updateReviewStatus(int reviewId, String status) {
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            
            String sql = "UPDATE book_reviews SET status = ? WHERE review_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, status);
            statement.setInt(2, reviewId);
            
            statement.executeUpdate();
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}