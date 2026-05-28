package eie3320;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/debit")
public class DebitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String amountStr = request.getParameter("Amount");
        int amount = Integer.parseInt(amountStr);
        
        HttpSession session = request.getSession();
        Integer balance = (Integer) session.getAttribute("balance");
        
        if (balance == null) {
            balance = 0;
        }
        
        if (balance >= amount) {
            balance -= amount;
            session.setAttribute("balance", balance);
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Withdrawal Result</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<p>Your account balance is " + balance + "</p>");
        } else {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Withdrawal Failed</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Withdrawal Failed</h1>");
            out.println("<p>Insufficient balance! Your current balance is " + balance + "</p>");
        }
        
        out.println("<p><a href='ATM.html'>Click here to return to the main page</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
}