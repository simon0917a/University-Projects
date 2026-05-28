<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="bookstore.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.html");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Advanced Book Search</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .header { background: #f4f4f4; padding: 10px; margin-bottom: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; }
        input[type="text"], input[type="number"], select { 
            width: 300px; padding: 8px; border: 1px solid #ddd; 
        }
        .btn { padding: 10px 15px; background: #4CAF50; color: white; 
               border: none; cursor: pointer; margin-right: 10px; }
        .btn:hover { background: #45a049; }
        .filter-section { border: 1px solid #ddd; padding: 15px; margin: 10px 0; }
    </style>
</head>
<body>
    <div class="header">
        <h1>Advanced Book Search</h1>
        <p>Welcome, <%= user.getUsername() %>! (Role: <%= user.getRole() %>)</p>
        <p><strong>Student Name:</strong> Li Ming Chun Simon</p>
        <p><strong>Student ID:</strong> 25017659D</p>
        <a href="student-dashboard.jsp">Back to Dashboard</a> | 
        <a href="LogoutServlet">Logout</a>
    </div>

    <form action="AdvancedSearchServlet" method="post">
        <div class="filter-section">
            <h3>Basic Search</h3>
            <div class="form-group">
                <label for="isbn">ISBN:</label>
                <input type="text" id="isbn" name="isbn">
            </div>
            
            <div class="form-group">
                <label for="title">Title:</label>
                <input type="text" id="title" name="title">
            </div>
            
            <div class="form-group">
                <label for="author">Author:</label>
                <input type="text" id="author" name="author">
            </div>
        </div>

        <div class="filter-section">
            <h3>Advanced Filters</h3>
            <div class="form-group">
                <label for="minPrice">Minimum Price:</label>
                <input type="number" step="0.01" id="minPrice" name="minPrice">
            </div>
            
            <div class="form-group">
                <label for="maxPrice">Maximum Price:</label>
                <input type="number" step="0.01" id="maxPrice" name="maxPrice">
            </div>
            
            <div class="form-group">
                <label for="publisher">Publisher:</label>
                <input type="text" id="publisher" name="publisher">
            </div>
            
            <div class="form-group">
                <label for="copyright">Copyright Year:</label>
                <input type="text" id="copyright" name="copyright">
            </div>
        </div>

        <div class="filter-section">
            <h3>Sorting Options</h3>
            <div class="form-group">
                <label for="sortBy">Sort By:</label>
                <select id="sortBy" name="sortBy">
                    <option value="title">Title</option>
                    <option value="author">Author</option>
                    <option value="price">Price</option>
                    <option value="editionNumber">Edition</option>
                    <option value="copyright">Copyright Year</option>
                </select>
            </div>
            
            <div class="form-group">
                <label for="sortOrder">Sort Order:</label>
                <select id="sortOrder" name="sortOrder">
                    <option value="ASC">Ascending</option>
                    <option value="DESC">Descending</option>
                </select>
            </div>
        </div>

        <div>
            <button type="submit" class="btn">Search</button>
            <button type="reset" class="btn" style="background: #666;">Reset</button>
            <a href="SearchBook.jsp" class="btn" style="background: #999;">Basic Search</a>
        </div>
    </form>
</body>
</html>