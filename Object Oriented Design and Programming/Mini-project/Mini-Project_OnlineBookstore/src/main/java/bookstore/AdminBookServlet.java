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

@WebServlet("/AdminBookServlet")
public class AdminBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/books";
    private static final String DATABASE_USER = "guest";
    private static final String DATABASE_PASSWORD = "guest";
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        if (role == null || !"admin".equals(role)) {
            response.sendRedirect("login.html");
            return;
        }
        
        String action = request.getParameter("action");
        
        if (action == null) {
            listBooks(request, response);
        } else {
            switch (action) {
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteBook(request, response);
                    break;
                default:
                    listBooks(request, response);
                    break;
            }
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        if (role == null || !"admin".equals(role)) {
            response.sendRedirect("login.html");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            addBook(request, response);
        } else if ("update".equals(action)) {
            updateBook(request, response);
        } else {
            listBooks(request, response);
        }
    }
    
    private void listBooks(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        ArrayList<Book> books = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            
            String sql = "SELECT * FROM bookinfo";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                String isbn = resultSet.getString("isbn");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                int edition = resultSet.getInt("editionNumber");
                String publisher = resultSet.getString("publisher");
                String copyright = resultSet.getString("copyright");
                double price = resultSet.getDouble("price");
                
                Book book = new Book(isbn, author, title, price, edition, publisher, copyright);
                books.add(book);
            }
            
            request.setAttribute("books", books);
            request.getRequestDispatcher("admin-books.jsp").forward(request, response);
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("admin-books.jsp?error=Database error");
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
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String isbn = request.getParameter("isbn");
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            
            String sql = "SELECT * FROM bookinfo WHERE isbn = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, isbn);
            resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                int edition = resultSet.getInt("editionNumber");
                String publisher = resultSet.getString("publisher");
                String copyright = resultSet.getString("copyright");
                double price = resultSet.getDouble("price");
                
                Book book = new Book(isbn, author, title, price, edition, publisher, copyright);
                request.setAttribute("book", book);
                request.getRequestDispatcher("edit-book.jsp").forward(request, response);
            } else {
                response.sendRedirect("admin-books.jsp?error=Book not found");
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("admin-books.jsp?error=Database error");
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
    
    private void addBook(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        int edition = Integer.parseInt(request.getParameter("edition"));
        String publisher = request.getParameter("publisher");
        String copyright = request.getParameter("copyright");
        double price = Double.parseDouble(request.getParameter("price"));
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            
            String sql = "INSERT INTO bookinfo (isbn, title, author, editionNumber, publisher, copyright, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, isbn);
            statement.setString(2, title);
            statement.setString(3, author);
            statement.setInt(4, edition);
            statement.setString(5, publisher);
            statement.setString(6, copyright);
            statement.setDouble(7, price);
            
            int rows = statement.executeUpdate();
            if (rows > 0) {
                response.sendRedirect("admin-books.jsp?success=Book added successfully");
            } else {
                response.sendRedirect("add-book.jsp?error=Failed to add book");
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("add-book.jsp?error=Database error: " + e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void updateBook(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        int edition = Integer.parseInt(request.getParameter("edition"));
        String publisher = request.getParameter("publisher");
        String copyright = request.getParameter("copyright");
        double price = Double.parseDouble(request.getParameter("price"));
        
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            
            String sql = "UPDATE bookinfo SET title = ?, author = ?, editionNumber = ?, publisher = ?, copyright = ?, price = ? WHERE isbn = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setInt(3, edition);
            statement.setString(4, publisher);
            statement.setString(5, copyright);
            statement.setDouble(6, price);
            statement.setString(7, isbn);
            
            int rows = statement.executeUpdate();
            if (rows > 0) {
                response.sendRedirect("admin-books.jsp?success=Book updated successfully");
            } else {
                response.sendRedirect("edit-book.jsp?error=Failed to update book");
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("edit-book.jsp?error=Database error: " + e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void deleteBook(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String isbn = request.getParameter("isbn");
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            
            String sql = "DELETE FROM bookinfo WHERE isbn = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, isbn);
            
            int rows = statement.executeUpdate();
            if (rows > 0) {
                response.sendRedirect("admin-books.jsp?success=Book deleted successfully");
            } else {
                response.sendRedirect("admin-books.jsp?error=Failed to delete book");
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("admin-books.jsp?error=Database error: " + e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}