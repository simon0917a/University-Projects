package bookstore;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/show-order.jsp";
        ShoppingCart cart;
        ArrayList<Book> books;
        
        // Get the session object, make sure that the user cannot access this servlet directly
        // If the user attempts to access this servlet directly, forward the user to SearchBook.html.
        /* Put your code here */
        HttpSession session = request.getSession();
        
        // Get the ShoppingCart object (namely cart) from the session attribute "bookstore.cart". 
        // If cart is null, create a new ShoppingCart object.
        // Set the session attribute by associating the String "bookstore.cart" with the newly created object.
        /* Put your code here */
        cart = (ShoppingCart) session.getAttribute("bookstore.cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("bookstore.cart", cart);
        }

        // Get the ArrayList object (namely books) from the session attribute foundBooks. 
        // This ArrayList object, which was created in QueryServlet.class, 
        // contains the book objects that match the search criteria specified in SearchBook.html
        /* Put your code here */
        books = (ArrayList<Book>) session.getAttribute("foundBooks");
        if (books == null) {
            // If no books found in session, redirect to search page
            response.sendRedirect("SearchBook.html");
            return;
        }
        
        // Get the index of the selected book from BookInfo.jsp
        /* Put your code here */
        String selectedBookIndex = request.getParameter("selectedBook");
        if (selectedBookIndex == null) {
            // If no book selected, redirect to search page
            response.sendRedirect("SearchBook.html");
            return;
        }
        
        int bookIndex = Integer.parseInt(selectedBookIndex);
        
        // Add the selected book object to the Shopping cart 
        // Set the cart to session attribute
        /* Put your code here */
        if (bookIndex >= 0 && bookIndex < books.size()) {
            Book selectedBook = books.get(bookIndex);
            cart.addBook(selectedBook);
            session.setAttribute("bookstore.cart", cart);
        }
        
        // Forward the control to either show-order.jsp or SearchBook.html
        /* Put your code here */
        RequestDispatcher dispatcher = request.getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }
}