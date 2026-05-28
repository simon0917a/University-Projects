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

@WebServlet("/ReviewServlet")
public class ReviewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/books";
    private static final String DATABASE_USER = "guest";
    private static final String DATABASE_PASSWORD = "guest";
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String isbn = request.getParameter("isbn");
        if (isbn != null) {
            List<Review> reviews = getApprovedReviews(isbn);
            request.setAttribute("reviews", reviews);
            request.setAttribute("bookIsbn", isbn);
            request.getRequestDispatcher("book-reviews.jsp").forward(request, response);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        String isbn = request.getParameter("isbn");
        String ratingStr = request.getParameter("rating");
        String reviewText = request.getParameter("reviewText");
        
        if (isbn != null && ratingStr != null) {
            int rating = Integer.parseInt(ratingStr);
            
            if (hasUserPurchasedBook(user.getUserId(), isbn)) {
                submitReview(user.getUserId(), isbn, rating, reviewText);
                request.setAttribute("message", "Thank you for your review! It will be visible after approval.");
            } else {
                request.setAttribute("error", "You can only review books you have purchased.");
            }
            
            List<Review> reviews = getApprovedReviews(isbn);
            request.setAttribute("reviews", reviews);
            request.setAttribute("bookIsbn", isbn);
            request.getRequestDispatcher("book-reviews.jsp").forward(request, response);
        }
    }
    
    private boolean hasUserPurchasedBook(int userId, String isbn) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            
            String sql = "SELECT COUNT(*) FROM order_items oi " +
                        "JOIN orders o ON oi.order_id = o.order_id " +
                        "WHERE o.user_id = ? AND oi.isbn = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setString(2, isbn);
            
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
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
        
        return false;
    }
    
    private void submitReview(int userId, String isbn, int rating, String reviewText) {
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            
            String sql = "INSERT INTO book_reviews (isbn, user_id, rating, review_text, status) VALUES (?, ?, ?, ?, 'pending')";
            statement = connection.prepareStatement(sql);
            statement.setString(1, isbn);
            statement.setInt(2, userId);
            statement.setInt(3, rating);
            statement.setString(4, reviewText);
            
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
    
    private List<Review> getApprovedReviews(String isbn) {
        List<Review> reviews = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            
            String sql = "SELECT r.*, u.username FROM book_reviews r " +
                        "JOIN users u ON r.user_id = u.user_id " +
                        "WHERE r.isbn = ? AND r.status = 'approved' " +
                        "ORDER BY r.review_date DESC";
            statement = connection.prepareStatement(sql);
            statement.setString(1, isbn);
            
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
}