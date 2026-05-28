<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page import="bookstore.*" %>
<%@ page import="bookstore.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.html");
        return;
    }
    
    ArrayList<Book> books = (ArrayList<Book>)session.getAttribute("foundBooks");
    int numBooks = 0;
    if (books != null) {
        numBooks = books.size();
    }
%>
<!DOCTYPE html>
<html>
<head>
<title>Book Info.</title>
<style>
    body { font-family: Arial, sans-serif; margin: 20px; }
    table { width: 100%; border-collapse: collapse; margin-top: 20px; }
    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
    th { background-color: #f2f2f2; }
</style>
</head>
<body>

    <p>Student Name: Li Ming Chun Simon</p>
    <p>Student ID: 25017659D</p>
    
    <center>The time now is: <%= new Date() %></center>
    
    <table align="center">
    <tr>
        <th>ISBN</th>
        <th>Title</th>
        <th>Author</th>
        <th>Edition Number</th>
        <th>Publisher</th>
        <th>Copyright</th>
        <th>Action</th>
    </tr>

    <% for (int i=0; i<numBooks; i++) { 
        Book book = books.get(i);
    %>
    <tr>
        <td><%= book.getIsbn() %></td>
        <td><%= book.getTitle() %></td>
        <td><%= book.getAuthor() %></td>
        <td><%= book.getEdition() %></td>
        <td><%= book.getPublisher() %></td>
        <td><%= book.getCopyright() %></td>
        <td>
            <a href="OrderServlet?selectedBook=<%= i %>" style="display: block; margin-bottom: 5px;">Add to Cart</a>
    		<a href="ReviewServlet?isbn=<%= book.getIsbn() %>" style="font-size: 16px;">Reviews</a>
        </td>
    </tr>
    <% } %>
    </table>
    
    <center><A href="SearchBook.jsp">Home</A></center>
</body>
</html>