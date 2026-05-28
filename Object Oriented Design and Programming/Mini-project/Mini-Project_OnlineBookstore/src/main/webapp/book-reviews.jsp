<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="bookstore.User" %>
<%@ page import="bookstore.Review" %>
<%@ page import="java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.html");
        return;
    }
    
    List<Review> reviews = (List<Review>) request.getAttribute("reviews");
    String bookIsbn = (String) request.getAttribute("bookIsbn");
    String message = (String) request.getAttribute("message");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Book Reviews</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .header { background: #f4f4f4; padding: 10px; margin-bottom: 20px; }
        .review { border: 1px solid #ddd; padding: 15px; margin: 10px 0; border-radius: 5px; }
        .rating { color: #ffc107; font-weight: bold; margin-bottom: 5px; }
        .review-form { background: #f9f9f9; padding: 20px; margin: 20px 0; border-radius: 5px; }
        .star-rating { 
            display: flex;
            flex-direction: row-reverse;
            justify-content: flex-end;
            gap: 5px;
            margin-bottom: 10px;
        }
        .star-rating input { display: none; }
        .star-rating label { 
            font-size: 24px; 
            color: #ddd; 
            cursor: pointer;
            transition: color 0.2s;
        }
        .star-rating label:hover,
        .star-rating label:hover ~ label {
            color: #ffc107;
        }
        .star-rating input:checked ~ label {
            color: #ffc107;
        }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        textarea { 
            width: 100%; 
            padding: 8px; 
            border: 1px solid #ddd; 
            border-radius: 4px;
            box-sizing: border-box;
        }
        .btn { 
            padding: 10px 15px; 
            background: #4CAF50; 
            color: white; 
            border: none; 
            border-radius: 4px;
            cursor: pointer;
        }
        .btn:hover { background: #45a049; }
        .message { 
            padding: 10px; 
            margin: 10px 0; 
            border-radius: 4px; 
        }
        .success { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .error { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
    </style>
</head>
<body>
    <div class="header">
        <h1>Book Reviews</h1>
        <p>Welcome, <%= user.getUsername() %>! (Role: <%= user.getRole() %>)</p>
        <p><strong>Student Name:</strong> Li Ming Chun Simon</p>
        <p><strong>Student ID:</strong> 25017659D</p>
        <a href="BookInfo.jsp">Back to Book List</a> | 
        <a href="student-dashboard.jsp">Dashboard</a> | 
        <a href="LogoutServlet">Logout</a>
    </div>

    <% if (message != null) { %>
        <div class="message success"><%= message %></div>
    <% } %>
    
    <% if (error != null) { %>
        <div class="message error"><%= error %></div>
    <% } %>

    <div class="review-form">
        <h3>Write a Review</h3>
        <form action="ReviewServlet" method="post">
            <input type="hidden" name="isbn" value="<%= bookIsbn %>">
            
            <div class="form-group">
                <label>Rating:</label>
                <div class="star-rating">
                    <input type="radio" id="star5" name="rating" value="5">
                    <label for="star5" title="5 stars">★</label>
                    <input type="radio" id="star4" name="rating" value="4">
                    <label for="star4" title="4 stars">★</label>
                    <input type="radio" id="star3" name="rating" value="3">
                    <label for="star3" title="3 stars">★</label>
                    <input type="radio" id="star2" name="rating" value="2">
                    <label for="star2" title="2 stars">★</label>
                    <input type="radio" id="star1" name="rating" value="1">
                    <label for="star1" title="1 star">★</label>
                </div>
            </div>
            
            <div class="form-group">
                <label for="reviewText">Review:</label>
                <textarea id="reviewText" name="reviewText" rows="4" placeholder="Share your thoughts about this book..."></textarea>
            </div>
            
            <button type="submit" class="btn">Submit Review</button>
        </form>
    </div>

    <h3>Customer Reviews</h3>
    <% if (reviews != null && !reviews.isEmpty()) { 
        for (Review review : reviews) { 
    %>
    <div class="review">
        <div class="rating">
            <% for (int i = 0; i < review.getRating(); i++) { %>★<% } %>
            <% for (int i = review.getRating(); i < 5; i++) { %>☆<% } %>
            <span style="color: #333; margin-left: 10px;"><%= review.getRating() %>/5</span>
        </div>
        <div><strong><%= review.getUsername() %></strong> - <%= review.getReviewDate() %></div>
        <div style="margin-top: 10px;"><%= review.getReviewText() != null ? review.getReviewText() : "No comment provided." %></div>
    </div>
    <% } 
    } else { %>
    <div class="review">
        <p>No reviews yet. Be the first to review this book!</p>
    </div>
    <% } %>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const stars = document.querySelectorAll('.star-rating input');
            const labels = document.querySelectorAll('.star-rating label');
            
            labels.forEach(label => {
                label.addEventListener('click', function() {
                    const starId = this.htmlFor;
                    const starInput = document.getElementById(starId);
                    if (starInput) {
                        starInput.checked = true;
                    }
                });
            });
            
            labels.forEach(label => {
                label.addEventListener('mouseover', function() {
                    const starId = this.htmlFor;
                    const starValue = document.getElementById(starId).value;
                    highlightStars(starValue);
                });
            });
            
            document.querySelector('.star-rating').addEventListener('mouseleave', function() {
                const checkedStar = document.querySelector('.star-rating input:checked');
                if (checkedStar) {
                    highlightStars(checkedStar.value);
                } else {
                    resetStars();
                }
            });
            
            function highlightStars(value) {
                labels.forEach(label => {
                    const starId = label.htmlFor;
                    const starInput = document.getElementById(starId);
                    if (starInput.value <= value) {
                        label.style.color = '#ffc107';
                    } else {
                        label.style.color = '#ddd';
                    }
                });
            }
            
            function resetStars() {
                labels.forEach(label => {
                    label.style.color = '#ddd';
                });
            }
        });
    </script>
</body>
</html>