package bookstore;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class ReceiptServlet
 */
@WebServlet("/ReceiptServlet")
public class ReceiptServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    public ReceiptServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the session object and get the ShoppingCart object through the session attribute.
        HttpSession session = request.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("bookstore.cart");
        
        // Remove all books in the shopping cart 
        if (cart != null) {
            cart.removeAllBooks();
            session.setAttribute("bookstore.cart", cart);
        }

        // Get the customer name that the user inputs in the check-out.jsp
        String customerName = request.getParameter("customerName");
        
        // Get a PrintWriter object and set content type to "text/html"
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        /*
         * Assume perform credit card transaction here
         */
        
        // Print transaction info and forward to SearchBook.html after 5 seconds
        String outStr = "";
        outStr += "Dear " + customerName + " (25017659D), thanks for purchasing books from BookStore<br>\n";
        outStr += "This page will be automatically go back to SearchBook.html<br>\n";
        outStr += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n";
        outStr += "<meta http-equiv=\"Refresh\" content=\"5; url=SearchBook.html\">\n";
        out.print(outStr);
        out.close();        
    }
}