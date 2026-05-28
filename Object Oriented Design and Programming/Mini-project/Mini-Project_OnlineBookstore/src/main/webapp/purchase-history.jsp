<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="bookstore.User" %>
<%@ page import="bookstore.Order" %>
<%@ page import="bookstore.OrderItem" %>
<%@ page import="java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.html");
        return;
    }
    
    List<Order> orders = (List<Order>) request.getAttribute("orders");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Purchase History</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .header { background: #f4f4f4; padding: 10px; margin-bottom: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .order-header { background: #e9e9e9; padding: 10px; margin: 20px 0 10px 0; }
    </style>
</head>
<body>
    <div class="header">
        <h1>Purchase History</h1>
        <p>Welcome, <%= user.getUsername() %>! (Role: <%= user.getRole() %>)</p>
        <p><strong>Student Name:</strong> Li Ming Chun Simon</p>
        <p><strong>Student ID:</strong> 25017659D</p>
        <a href="student-dashboard.jsp">Back to Dashboard</a> | 
        <a href="LogoutServlet">Logout</a>
    </div>

    <%
    if (orders != null && !orders.isEmpty()) {
        for (Order order : orders) {
    %>
    <div class="order-header">
        <strong>Order #<%= order.getOrderId() %></strong> | 
        Date: <%= order.getOrderDate() %> | 
        Total: $<%= order.getTotalAmount() %>
    </div>
    
    <table>
        <thead>
            <tr>
                <th>ISBN</th>
                <th>Title</th>
                <th>Author</th>
                <th>Price</th>
            </tr>
        </thead>
        <tbody>
            <%
            for (OrderItem item : order.getItems()) {
            %>
            <tr>
                <td><%= item.getIsbn() %></td>
                <td><%= item.getTitle() %></td>
                <td><%= item.getAuthor() %></td>
                <td>$<%= item.getPrice() %></td>
            </tr>
            <%
            }
            %>
        </tbody>
    </table>
    <%
        }
    } else {
    %>
    <p>No purchase history found.</p>
    <p><a href="SearchBook.jsp">Start shopping now!</a></p>
    <%
    }
    %>
</body>
</html>