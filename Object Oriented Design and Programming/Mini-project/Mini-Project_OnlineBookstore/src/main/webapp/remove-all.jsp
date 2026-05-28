<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>Remove All Items</title>
</head>
<body>
<%@page import="bookstore.*" %>
<%@page import="java.util.ArrayList" %>
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

	<%
		// Get the ShoppingCart object through the session attribute
		// Put your code here
		ShoppingCart cart = (ShoppingCart) session.getAttribute("bookstore.cart");

		// Remove all books in the shopping cart
		// Set the cart to session attribute
		// Put your code here
		if (cart != null) {
            cart.removeAllBooks();
            session.setAttribute("bookstore.cart", cart);
            System.out.println("DEBUG: All items removed from shopping cart");
        }

	%>
	<jsp:forward page="show-order.jsp"/>

</body>
</html>