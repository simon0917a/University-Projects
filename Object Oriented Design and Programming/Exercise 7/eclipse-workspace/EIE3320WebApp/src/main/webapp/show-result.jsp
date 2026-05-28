<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>Show Result</h1>
<%
	String person ="?"; 
	String id ="?"; 
	Object authorID = request.getAttribute("authorID"); 
	if(authorID !=null) id = authorID.toString(); 
	Object fullname = request.getAttribute("fullname"); 
	if(fullname !=null) person = 
fullname.toString(); 
%>
Full name of author <%=id%>:<%=person%> !
</body>
</html>