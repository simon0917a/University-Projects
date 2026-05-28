<html>
<head>
<title>Book Info.</title>
</head>
<body>

	<%@page import="java.util.ArrayList" %>
	<%@page import="java.util.Date" %>
	<%@page import="bookstore.*" %>
	
	<p>
	  Student Name: Li Ming Chun Simon <br />
    Student ID: 25017659D
    </p>
	
	<%
		ArrayList<Book> books = (ArrayList<Book>)session.getAttribute("foundBooks");
		int numBooks = books.size();
	%>
	<center>The time now is: <%= new Date() %></center>
	
	<table align="center" border=1  >
	<tr>
		<th>ISBN</th>
		<th>Title</th>
		<th>Author</th>
		<th>Edition Number</th>
		<th>Publisher</th>
		<th>Copyright</th>
		<th></th>
	</tr>

	<% for (int i=0; i<numBooks; i++) { 
		Book book = books.get(i);
	%>
	<tr>
		<!-- For each i, retrieve the information of the i-th book from the ArrayList. -->
		<!-- Display the information in one row per book. -->
		<!-- The last element of each row should contain a link to OrderServlet.class --> 
		<!-- with input parameter "selectedBook" and value equal to i -->
		<!-- Put your code here -->
		<td><%= book.getIsbn() %></td>
        <td><%= book.getTitle() %></td>
        <td><%= book.getAuthor() %></td>
        <td><%= book.getEdition() %></td>
        <td><%= book.getPublisher() %></td>
        <td><%= book.getCopyright() %></td>
        <td>
            <a href="OrderServlet?selectedBook=<%= i %>">Add to Cart</a>
        </td>
	</tr>
	<% } %>
	</table>
	<center><A href="SearchBook.html">Home</A></center>
</body>
</html>