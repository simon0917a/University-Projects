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
    <title>Student Dashboard</title>
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
        <h1>Student Dashboard</h1>
        <p>Welcome, <%= user.getUsername() %>! (Role: <%= user.getRole() %>)</p>
        <p><strong>Student Name:</strong> Li Ming Chun Simon</p>
        <p><strong>Student ID:</strong> 25017659D</p>
        <a href="LogoutServlet">Logout</a>
    </div>
    
    <div class="nav">
        <a href="SearchBook.jsp">🔍 Search Books</a>
        <a href="PurchaseHistoryServlet">📋 Purchase History</a>
    </div>
    
    <div class="dashboard-cards">
        <div class="card">
            <h3>Book Search & Purchase</h3>
            <p>Browse and search for books in our bookstore.</p>
            <ul>
                <li>Search by ISBN or author</li>
                <li>Add books to shopping cart</li>
                <li>Secure checkout process</li>
            </ul>
            <a href="SearchBook.jsp">Start Shopping →</a>
        </div>
        
        <div class="card">
            <h3>Purchase History</h3>
            <p>View your previous book purchases.</p>
            <ul>
                <li>See order details</li>
                <li>View purchased books</li>
                <li>Track order dates and totals</li>
            </ul>
            <a href="PurchaseHistoryServlet">View History →</a>
        </div>
        
        <div class="card">
            <h3>Account Management</h3>
            <p>Manage your student account.</p>
            <ul>
                <li>View profile information</li>
                <li>Update preferences</li>
            </ul>
            <p><em>More features coming soon...</em></p>
        </div>
        
        <div class="nav">
   		 	<a href="SearchBook.jsp">🔍 Basic Search</a>
    		<a href="advanced-search.jsp">🔎 Advanced Search</a>
    		<a href="PurchaseHistoryServlet">📋 Purchase History</a>
		</div>
    </div>
</body>
</html>