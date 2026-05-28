<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="bookstore.User" %>
<%@ page import="bookstore.Book" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"admin".equals(user.getRole())) {
        response.sendRedirect("login.html");
        return;
    }
    
    Book book = (Book) request.getAttribute("book");
    if (book == null) {
        response.sendRedirect("admin-books.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Book</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .header { background: #f4f4f4; padding: 10px; margin-bottom: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; }
        input[type="text"], input[type="number"] { 
            width: 300px; padding: 8px; border: 1px solid #ddd; 
        }
        .btn { padding: 10px 15px; background: #4CAF50; color: white; 
               border: none; cursor: pointer; margin-right: 10px; }
        .btn:hover { background: #45a049; }
    </style>
</head>
<body>
    <div class="header">
        <h1>Edit Book</h1>
        <p>Welcome, <%= user.getUsername() %>! (Role: <%= user.getRole() %>)</p>
        <p><strong>Student Name:</strong> Li Ming Chun Simon</p>
        <p><strong>Student ID:</strong> 25017659D</p>
        <a href="admin-books.jsp">Back to Books Management</a> | 
        <a href="LogoutServlet">Logout</a>
    </div>

    <form action="AdminBookServlet" method="post">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="isbn" value="<%= book.getIsbn() %>">
        
        <div class="form-group">
            <label for="isbn">ISBN:</label>
            <input type="text" id="isbn" value="<%= book.getIsbn() %>" disabled>
            <span style="color: #666; font-size: 12px;">(ISBN cannot be changed)</span>
        </div>
        
        <div class="form-group">
            <label for="title">Title:</label>
            <input type="text" id="title" name="title" value="<%= book.getTitle() %>" required>
        </div>
        
        <div class="form-group">
            <label for="author">Author:</label>
            <input type="text" id="author" name="author" value="<%= book.getAuthor() %>" required>
        </div>
        
        <div class="form-group">
            <label for="edition">Edition:</label>
            <input type="number" id="edition" name="edition" value="<%= book.getEdition() %>" required>
        </div>
        
        <div class="form-group">
            <label for="publisher">Publisher:</label>
            <input type="text" id="publisher" name="publisher" value="<%= book.getPublisher() %>">
        </div>
        
        <div class="form-group">
            <label for="copyright">Copyright:</label>
            <input type="text" id="copyright" name="copyright" value="<%= book.getCopyright() %>">
        </div>
        
        <div class="form-group">
            <label for="price">Price:</label>
            <input type="number" step="0.01" id="price" name="price" value="<%= book.getPrice() %>" required>
        </div>

        <div>
            <button type="submit" class="btn">Update Book</button>
            <a href="admin-books.jsp" class="btn" style="background: #666;">Cancel</a>
        </div>
    </form>
</body>
</html>