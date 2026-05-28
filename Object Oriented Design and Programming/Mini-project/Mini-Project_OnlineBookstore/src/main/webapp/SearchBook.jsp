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
<title>Online Bookstore</title>
<script type="text/JavaScript">
function validateForm() {
	var isbn = document.search.isbn.value;
	var author = document.search.author.value;
	
    if (isBlank(isbn) && isBlank(author)) {
        alert("Fill in at least one entry");
        return false;
    }
    
    if (!isBlank(isbn) && isNaN(isbn)) {
        alert("Invalid ISBN");
        return false;
    }
    
    return true;
}

function isBlank(s) {
	var i
	for (i = 0; i < s.length; i++) {
		if (s.charAt(i) != " ")
			return false
	}
	return true
}

</script>
</head>

<body>

	<p>
	  Student Name: Li Ming Chun Simon <br />
    Student ID: 25017659D
  </p>
	
	<script language="javascript"> 

	  var now = new Date();
    var hours = now.getHours();
    var minutes = now.getMinutes();
    var date = now.getDate();
    var month = now.getMonth() + 1;
    var year = now.getFullYear();
    
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    if (hours < 10) {
        hours = "0" + hours;
    }
    
    document.write("<p>The time now is: " + hours + ":" + minutes + "<br />");
    document.write("The date is: " + date + "/" + month + "/" + year + "</p>");
	
	</script> 

    <div style="margin-bottom: 15px;">
        <a href="student-dashboard.jsp">Back to Dashboard</a> | 
        <a href="LogoutServlet">Logout</a>
    </div>

	<form name="search" onsubmit="return validateForm()" method="post" action="QueryServlet">
		<p>
			Please choose <b>either ISBN or Author</b> to search books: <br>Search
			by ISBN: <input type="text" name="isbn" size=20 value="0131450913"> <br>Search
			by Author: <input type="text" name="author" size=19 value="Paul Deitel"> <br>
			<input type="submit" value="Submit">
	</form>
</body>
</html>