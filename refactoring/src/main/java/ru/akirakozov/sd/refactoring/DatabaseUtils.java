package ru.akirakozov.sd.refactoring;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseUtils {

    public static void dbCommand(HttpServletResponse response, SQLType type, String sql, String phraseToAnswer) throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs;
            switch (type) {
                case CREATE_DROP:
                    stmt.executeUpdate(sql);
                    break;
                case MIN_MAX:
                    rs = stmt.executeQuery(sql);
                    response.getWriter().println("<html><body>");
                    response.getWriter().println("<h1>Product with " + phraseToAnswer + " price: </h1>");

                    while (rs.next()) {
                        String name = rs.getString("name");
                        int price = rs.getInt("price");
                        response.getWriter().println(name + "\t" + price + "</br>");
                    }
                    response.getWriter().println("</body></html>");

                    rs.close();
                    break;
                case SUM_COUNT:
                    rs = stmt.executeQuery(sql);
                    response.getWriter().println("<html><body>");
                    response.getWriter().println(phraseToAnswer);

                    if (rs.next()) {
                        response.getWriter().println(rs.getInt(1));
                    }
                    response.getWriter().println("</body></html>");

                    rs.close();
                    break;
            }
            stmt.close();
        }
    }
}