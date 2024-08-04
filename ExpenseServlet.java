package com.expense;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpenseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            response.sendRedirect("login.html");
            return;
        }

        int userId = getUserId(username);
        double amount = Double.parseDouble(request.getParameter("amount"));
        String category = request.getParameter("category");
        String date = request.getParameter("date");
        String description = request.getParameter("description");

        Database.addExpense(userId, amount, category, date, description);
        response.sendRedirect("expenses.html");
    }

    private int getUserId(String username) {
        // Implement a method to get user id by username
        // For simplicity, assuming userId is 1 for now
        return 1;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            response.sendRedirect("login.html");
            return;
        }

        int userId = getUserId(username);
        ResultSet expenses = Database.getExpenses(userId);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        out.println("[");
        try {
            while (expenses.next()) {
                out.println("{");
                out.println("\"id\": " + expenses.getInt("id") + ",");
                out.println("\"amount\": " + expenses.getDouble("amount") + ",");
                out.println("\"category\": \"" + expenses.getString("category") + "\",");
                out.println("\"date\": \"" + expenses.getString("date") + "\",");
                out.println("\"description\": \"" + expenses.getString("description") + "\"");
                out.println("},");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        out.println("]");
    }
}
