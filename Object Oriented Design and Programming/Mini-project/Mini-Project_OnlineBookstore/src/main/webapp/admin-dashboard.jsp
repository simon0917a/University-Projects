<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="bookstore.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"admin".equals(user.getRole())) {
        response.sendRedirect("login.html");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .header { background: #f4f4f4; padding: 10px; margin-bottom: 20px; }
        .nav { margin: 20px 0; }
        .nav a { 
            display: inline-block;
            margin-right: 15px; 
            text-decoration: none; 
            color: #333;
            padding: 10px 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
            background: #f9f9f9;
        }
        .nav a:hover { 
            background: #e9e9e9; 
            text-decoration: none;
        }
        .dashboard-cards {
            display: flex;
            gap: 20px;
            margin-top: 30px;
        }
        .card {
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 20px;
            flex: 1;
            background: #f9f9f9;
        }
        .card h3 {
            margin-top: 0;
            color: #333;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>Admin Dashboard</h1>
        <p>Welcome, <%= user.getUsername() %>! (Role: <%= user.getRole() %>)</p>
        <p><strong>Student Name:</strong> Li Ming Chun Simon</p>
        <p><strong>Student ID:</strong> 25017659D</p>
        <a href="LogoutServlet">Logout</a>
    </div>
    
    <div class="nav">
        <a href="AdminBookServlet">📚 Manage Books</a>
   		<a href="AdminReviewServlet">⭐ Manage Reviews</a>
    </div>
    
    <div class="dashboard-cards">
        <div class="card">
            <h3>Book Management</h3>
            <p>Add, edit, or remove books from the bookstore inventory.</p>
            <ul>
                <li>View all books</li>
                <li>Add new books</li>
                <li>Update book information</li>
                <li>Remove books</li>
            </ul>
            <a href="AdminBookServlet">Go to Books Management →</a>
        </div>
        
        <div class="card">
            <h3>User Management</h3>
            <p>Manage user accounts and permissions.</p>
            <ul>
                <li>View all users</li>
                <li>Add new users</li>
                <li>Reset passwords</li>
                <li>Manage roles</li>
            </ul>
            <p><em>Coming soon...</em></p>
        </div>
        
        <div class="card">
            <h3>Reports</h3>
            <p>View sales reports and analytics.</p>
            <ul>
                <li>Sales overview</li>
                <li>Popular books</li>
                <li>Revenue reports</li>
            </ul>
            <p><em>Coming soon...</em></p>
        </div>
    </div>
</body>
</html>