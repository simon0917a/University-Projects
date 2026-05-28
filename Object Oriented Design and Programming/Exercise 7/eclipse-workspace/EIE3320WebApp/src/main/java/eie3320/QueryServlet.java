package eie3320;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher; 
import java.io.IOException;

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String authorID = request.getParameter("authorID");
	    String error = "";

	    if (authorID == null || authorID.trim().isEmpty()) {
	        error = "Please enter an Author ID";
	        request.setAttribute("error", error);
	        request.getRequestDispatcher("user-query.jsp").forward(request, response);
	        return;
	    }

	    String fullName = "Li Ming Chun Simon";

	    request.setAttribute("authorID", authorID);
	    request.setAttribute("fullname", fullName);

	    RequestDispatcher dispatcher = request.getRequestDispatcher("show-result.jsp");
	    dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
