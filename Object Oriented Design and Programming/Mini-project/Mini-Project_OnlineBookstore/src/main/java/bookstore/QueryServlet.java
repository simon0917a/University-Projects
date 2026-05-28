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
 * Servlet implementation class QueryServlet
 */
@WebServlet("/QueryServlet")
public class QueryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		java.sql.Connection connection = null; // manages connection
		java.sql.Statement statement = null; // query statement
		java.sql.ResultSet resultSet = null; // manages results
		String DATABASE_URL = "jdbc:mysql://localhost:3306/books";

		//Put your code here
		//create a new ArrayList of Book objects
		ArrayList<Book> foundBooks = new ArrayList<Book>();
		//get the session object; if there is no existing session, create a new one
		HttpSession session = request.getSession();
		//get the ISBN value from the form
		String isbn = request.getParameter("isbn");
		//get the Author name from the form
		String author = request.getParameter("author");

		/*
		 * 	Specify to the DriverManager which JDBC drivers to try to make Connections with.
		 */
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// connect to database books and query database
		try {		
			
			// establish connection to database
			connection = java.sql.DriverManager.getConnection(DATABASE_URL, "guest", "guest");

			// create Statement for querying database
			statement = connection.createStatement();

			// query database
			//Put your code here
			//Retrieve the book information for the input ISBN or Author name
			String query = "SELECT * FROM bookinfo WHERE 1=1";
			
			if (isbn != null && !isbn.trim().isEmpty()) {
                query += " AND isbn = '" + isbn + "'";
            }
            
            if (author != null && !author.trim().isEmpty()) {
                query += " AND author LIKE '%" + author + "%'";
            }

			System.out.println(query);
			resultSet = statement.executeQuery(query);

			// process query results
			while (resultSet.next() == true) {
				isbn = (String) resultSet.getObject(1);
				String title = (String) resultSet.getObject(2);
				author = (String) resultSet.getObject(3);
				int edition = (Integer)resultSet.getObject(4);
				String publisher = (String)resultSet.getObject(5);
				String copyright = (String)resultSet.getObject(6);
				double price = (Double)resultSet.getObject(7);

				//Put your code here
				//Create a new Book object with the above information and add this Book object to foundBooks
				Book book = new Book(isbn, author, title, price, edition, publisher, copyright);
				foundBooks.add(book);
				System.out.println(title + "; " + edition + "; " + publisher + "; " + copyright);
			}
			session.setAttribute("foundBooks", foundBooks);

			//Put your code here
			//Forward the control to BookInfo.jsp
			RequestDispatcher dispatcher = request.getRequestDispatcher("BookInfo.jsp");
            dispatcher.forward(request, response);


		} // end try
		catch (java.sql.SQLException sqlException) {
			sqlException.printStackTrace();
		} // end catch
		finally // ensure resultSet, statement and connection are closed
		{
			try {
				if (resultSet != null) resultSet.close();
		        if (statement != null) statement.close();
		        if (connection != null) connection.close();
			} // end try
			catch (Exception exception) {
				exception.printStackTrace();
			} // end catch
		} // end finally		
	}

}
