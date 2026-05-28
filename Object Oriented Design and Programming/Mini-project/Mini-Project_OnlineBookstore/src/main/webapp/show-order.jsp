<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>Show Order</title>
</head>
<body>

<%@page import="bookstore.*" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Date" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.html");
        return;
    }
%>

    <p>
	  Student Name: Li Ming Chun Simon <br />
    Student ID: 25017659D
    </p>

	<center>The time now is: <%= new Date() %></center>
	<%
		// Get the shopping cart object. From the cart object, get the number of books
		// Put your code here
		ShoppingCart cart = (ShoppingCart) session.getAttribute("bookstore.cart");
        int numBooks = 0;
        double totalPrice = 0.0;
        
        if (cart != null) {
            numBooks = cart.getNumBooks();
            totalPrice = cart.getTotalPrice();
        }
	%>
	
	<p style="text-align: center;">
		You have <%=numBooks %> item(s) in your shopping cart <br> 
	</p>
	<% if (numBooks > 0) { %>	
		<table align="center" border=1  >
		<tr>
			<th></th>
			<th>Title</th>
			<th>Price</th>
		</tr>
		<% for (int i=0; i<numBooks; i++) { 
		    Book book = cart.getBook(i);
		%>
				<tr>
					<td><%=i+1 %></td> 
					<td><%= book.getTitle() %></td>
                    <td align="right"><%= String.format("%.1f", book.getPrice()) %></td>
				</tr>
		<% } %>
		<tr>
			<td></td>
			<td>Total:</td>
			<td align="right"><%= String.format("%.1f", totalPrice) %></td>
		</tr>
		</table>
	<% } %>	
	<center>
		<input type="button" value="Back" onClick="JavaScript:window.location='BookInfo.jsp';">
		<input type="button" value="Check Out" onClick="JavaScript:window.location='check-out.jsp';"> &nbsp; &nbsp;
		<input type="button" value="Remove All" onClick="JavaScript:window.location='remove-all.jsp';">
	</center>
	
	
</body>
</html>