<html>
<head>
<title>Check Out</title>
<script type="text/JavaScript">

/* When any of the text boxes is blank, display an error message "Missing Name or Credit Card Number". 
	If the input credit card number is not a number, display an error message "Invalid Credit Card Number".
	*/
	//Put your code here
	function validateForm() {
        var name = document.checkout.customerName.value;
        var cardNumber = document.checkout.cardNumber.value;
        
        // Check if any field is blank
        if (name.trim() === "" || cardNumber.trim() === "") {
            alert("Missing Name or Credit Card Number");
            return false;
        }
        
        // Check if credit card number is a number
        if (isNaN(cardNumber)) {
            alert("Invalid Credit Card Number");
            return false;
        }
        
        return true;
    }
	

</script>
</head>
<body>
<%@page import="bookstore.*" %>
<%@page import="java.util.ArrayList" %>

	<%
		// Get the ShoppingCart object through the session attribute.
		// Put your code here
		ShoppingCart cart = (ShoppingCart) session.getAttribute("bookstore.cart");
        double total = 0.0;
		
		if (cart != null) {
            // Compute the total price of all books in the shopping cart
            total = cart.getTotalPrice(); // Put your code here
        }
	%>
	Your total purchase is: <%=total %> <p></p>
	To purchase the item in your shopping cart, please provide us the following information:
	
	<form name="checkout" method="post" action="ReceiptServlet" onsubmit="return validateForm()">
	<b>Name: </b> <input type="text" name="customerName" size=20 value="Li Ming Chun Simon"><br>
	<b>Credit Card Number</b><input type="text" name="cardNumber" size=16 value="25017659"> <br>
	<input type="submit" value="Submit Information"> &nbsp; &nbsp; 
	<input type="button" value="Cancel" onClick="JavaScript:window.location='show-order.jsp';">
	</form>	
	
</body>
</html>