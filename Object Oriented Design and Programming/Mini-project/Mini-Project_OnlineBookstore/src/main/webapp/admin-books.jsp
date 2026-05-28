<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="bookstore.User" %>
<%@ page import="bookstore.Book" %>
<%@ page import="java.util.ArrayList" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"admin".equals(user.getRole())) {
        response.sendRedirect("login.html");
        return;
    }
    
    if (request.getAttribute("books") == null) {
        response.sendRedirect("AdminBookServlet");
        return;
    }
    
    ArrayList<Book> books = (ArrayList<Book>) request.getAttribute("books");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Books</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .header { background: #f4f4f4; padding: 10px; margin-bottom: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .actions a { margin-right: 10px; text-decoration: none; }
        .btn { padding: 5px 10px; background: #4CAF50; color: white; border: none; cursor: pointer; }
        .btn:hover { background: #45a049; }
        .success { color: green; margin: 10px 0; }
        .error { color: red; margin: 10px 0; }
    </style>
</head>
<body>
    <div class="header">
        <h1>Manage Books</h1>
        <p>Welcome, <%= user.getUsername() %>! (Role: <%= user.getRole() %>)</p>
        <p><strong>Student Name:</strong> Li Ming Chun Simon</p>
        <p><strong>Student ID:</strong> 25017659D</p>
        <a href="admin-dashboard.jsp">Back to Dashboard</a> | 
        <a href="LogoutServlet">Logout</a>
    </div>

    <%
        String success = request.getParameter("success");
        String error = request.getParameter("error");
        if (success != null) {
    %>
        <div class="success"><%= success %></div>
    <%
        }
        if (error != null) {
    %>
        <div class="error"><%= error %></div>
    <%
        }
    %>

    <div>
        <a href="add-book.jsp" class="btn">Add New Book</a>
    </div>

    <table>
        <thead>
            <tr>
                <th>ISBN</th>
                <th>Title</th>
                <th>Author</th>
                <th>Edition</th>
                <th>Publisher</th>
                <th>Copyright</th>
                <th>Price</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <%
            if (books != null && !books.isEmpty()) {
                for (Book book : books) {
            %>
            <tr>
                <td><%= book.getIsbn() %></td>
                <td><%= book.getTitle() %></td>
                <td><%= book.getAuthor() %></td>
                <td><%= book.getEdition() %></td>
                <td><%= book.getPublisher() %></td>
                <td><%= book.getCopyright() %></td>
                <td>$<%= book.getPrice() %></td>
                <td class="actions">
                    <a href="AdminBookServlet?action=edit&isbn=<%= book.getIsbn() %>">Edit</a>
                    <a href="AdminBookServlet?action=delete&isbn=<%= book.getIsbn() %>" 
                       onclick="return confirm('Are you sure you want to delete this book?')">Delete</a>
                </td>
            </tr>
            <%
                }
            } else {
            %>
            <tr>
                <td colspan="8">No books found in the database.</td>
            </tr>
            <%
            }
            %>
        </tbody>
    </table>
</body>
</html>