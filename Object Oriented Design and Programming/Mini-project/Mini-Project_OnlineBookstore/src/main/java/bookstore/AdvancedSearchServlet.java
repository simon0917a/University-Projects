package bookstore;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AdvancedSearchServlet")
public class AdvancedSearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/books";
    private static final String DATABASE_USER = "guest";
    private static final String DATABASE_PASSWORD = "guest";
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String minPrice = request.getParameter("minPrice");
        String maxPrice = request.getParameter("maxPrice");
        String publisher = request.getParameter("publisher");
        String copyright = request.getParameter("copyright");
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");
        
        ArrayList<Book> foundBooks = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            
            StringBuilder sql = new StringBuilder("SELECT * FROM bookinfo WHERE 1=1");
            ArrayList<Object> parameters = new ArrayList<>();
            
            if (isbn != null && !isbn.trim().isEmpty()) {
                sql.append(" AND isbn LIKE ?");
                parameters.add("%" + isbn + "%");
            }
            
            if (title != null && !title.trim().isEmpty()) {
                sql.append(" AND title LIKE ?");
                parameters.add("%" + title + "%");
            }
            
            if (author != null && !author.trim().isEmpty()) {
                sql.append(" AND author LIKE ?");
                parameters.add("%" + author + "%");
            }
            
            if (minPrice != null && !minPrice.trim().isEmpty()) {
                sql.append(" AND price >= ?");
                parameters.add(Double.parseDouble(minPrice));
            }
            
            if (maxPrice != null && !maxPrice.trim().isEmpty()) {
                sql.append(" AND price <= ?");
                parameters.add(Double.parseDouble(maxPrice));
            }
            
            if (publisher != null && !publisher.trim().isEmpty()) {
                sql.append(" AND publisher LIKE ?");
                parameters.add("%" + publisher + "%");
            }
            
            if (copyright != null && !copyright.trim().isEmpty()) {
                sql.append(" AND copyright LIKE ?");
                parameters.add("%" + copyright + "%");
            }
            
            if (sortBy != null && !sortBy.trim().isEmpty()) {
                sql.append(" ORDER BY ").append(sortBy);
                if (sortOrder != null && !sortOrder.trim().isEmpty()) {
                    sql.append(" ").append(sortOrder);
                }
            }
            
            statement = connection.prepareStatement(sql.toString());
            
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            
            System.out.println("Advanced Search Query: " + sql.toString());
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                String bookIsbn = resultSet.getString("isbn");
                String bookTitle = resultSet.getString("title");
                String bookAuthor = resultSet.getString("author");
                int edition = resultSet.getInt("editionNumber");
                String bookPublisher = resultSet.getString("publisher");
                String bookCopyright = resultSet.getString("copyright");
                double price = resultSet.getDouble("price");
                
                Book book = new Book(bookIsbn, bookAuthor, bookTitle, price, edition, bookPublisher, bookCopyright);
                foundBooks.add(book);
            }
            
            session.setAttribute("foundBooks", foundBooks);
            request.getRequestDispatcher("BookInfo.jsp").forward(request, response);
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error during advanced search: " + e.getMessage());
            request.getRequestDispatcher("advanced-search.jsp").forward(request, response);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}