<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="bookstore.User" %>
<%@ page import="bookstore.Review" %>
<%@ page import="java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"admin".equals(user.getRole())) {
        response.sendRedirect("login.html");
        return;
    }
    
    List<Review> pendingReviews = (List<Review>) request.getAttribute("pendingReviews");
    String message = (String) request.getAttribute("message");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Reviews</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .header { background: #f4f4f4; padding: 10px; margin-bottom: 20px; }
        .review { border: 1px solid #ddd; padding: 15px; margin: 10px 0; }
        .rating { color: #ffc107; font-weight: bold; }
        .book-info { background: #f0f0f0; padding: 10px; margin-bottom: 10px; }
        .actions { margin-top: 10px; }
        .btn { padding: 5px 10px; margin-right: 5px; text-decoration: none; border: none; cursor: pointer; }
        .btn-approve { background: #4CAF50; color: white; }
        .btn-reject { background: #f44336; color: white; }
    </style>
</head>
<body>
    <div class="header">
        <h1>Manage Book Reviews</h1>
        <p>Welcome, <%= user.getUsername() %>! (Role: <%= user.getRole() %>)</p>
        <p><strong>Student Name:</strong> Li Ming Chun Simon</p>
        <p><strong>Student ID:</strong> 25017659D</p>
        <a href="admin-dashboard.jsp">Back to Dashboard</a> | 
        <a href="LogoutServlet">Logout</a>
    </div>

    <% if (message != null) { %>
        <div style="color: green; margin: 10px 0; padding: 10px; background: #d4edda; border: 1px solid #c3e6cb;">
            <%= message %>
        </div>
    <% } %>

    <h2>Pending Reviews (<%= pendingReviews != null ? pendingReviews.size() : 0 %>)</h2>
    
    <% if (pendingReviews != null && !pendingReviews.isEmpty()) { 
        for (Review review : pendingReviews) { 
    %>
    <div class="review">
        <div class="book-info">
            <strong>Book:</strong> <%= review.getBookTitle() != null ? review.getBookTitle() : review.getIsbn() %> 
            (ISBN: <%= review.getIsbn() %>)
        </div>
        
        <div class="rating">
            Rating: 
            <% for (int i = 0; i < review.getRating(); i++) { %>★<% } %>
            <% for (int i = review.getRating(); i < 5; i++) { %>☆<% } %>
            (<%= review.getRating() %>/5)
        </div>
        
        <div><strong>User:</strong> <%= review.getUsername() %></div>
        <div><strong>Date:</strong> <%= review.getReviewDate() %></div>
        
        <% if (review.getReviewText() != null && !review.getReviewText().trim().isEmpty()) { %>
        <div><strong>Review:</strong> <%= review.getReviewText() %></div>
        <% } else { %>
        <div><strong>Review:</strong> <em>No text provided</em></div>
        <% } %>
        
        <div class="actions">
            <form action="AdminReviewServlet" method="post" style="display: inline;">
                <input type="hidden" name="reviewId" value="<%= review.getReviewId() %>">
                <input type="hidden" name="action" value="approve">
                <button type="submit" class="btn btn-approve">Approve</button>
            </form>
            
            <form action="AdminReviewServlet" method="post" style="display: inline;">
                <input type="hidden" name="reviewId" value="<%= review.getReviewId() %>">
                <input type="hidden" name="action" value="reject">
                <button type="submit" class="btn btn-reject">Reject</button>
            </form>
        </div>
    </div>
    <% } 
    } else { %>
    <p>No pending reviews at this time.</p>
    <% } %>
</body>
</html>