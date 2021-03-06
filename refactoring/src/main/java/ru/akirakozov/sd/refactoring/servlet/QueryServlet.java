package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.SQLType;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.akirakozov.sd.refactoring.DatabaseUtils.dbCommand;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        try {
            switch (command) {
                case "min":
                    dbCommand(response,
                            SQLType.MIN__MAX__GET_PRODUCTS,
                            "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1",
                            "Product with min price: ");
                    break;
                case "max":
                    dbCommand(response,
                            SQLType.MIN__MAX__GET_PRODUCTS,
                            "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1",
                            "Product with max price: ");
                    break;
                case "sum":
                    dbCommand(response,
                            SQLType.SUM_COUNT,
                            "SELECT SUM(price) FROM PRODUCT",
                            "Summary price: ");
                    break;
                case "count":
                    dbCommand(response,
                            SQLType.SUM_COUNT,
                            "SELECT COUNT(*) FROM PRODUCT",
                            "Number of products: ");
                    break;
                default:
                    response.getWriter().println("Unknown command: " + command);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
