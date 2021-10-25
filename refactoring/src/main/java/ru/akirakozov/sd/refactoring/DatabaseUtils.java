package ru.akirakozov.sd.refactoring;

import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

import static ru.akirakozov.sd.refactoring.HTMLBuilder.*;

public class DatabaseUtils {

    public static void dbCommand(HttpServletResponse response, SQLType type, String sql, String phraseToAnswer) throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = c.createStatement();
            ResultSet rs;
            StringBuilder sb;

            switch (type) {
                case CREATE_INSERT_DROP:
                    stmt.executeUpdate(sql);
                    break;

                case MIN__MAX__GET_PRODUCTS:
                    rs = stmt.executeQuery(sql);
                    sb = new StringBuilder();
                    if (!Objects.equals(phraseToAnswer, ""))
                        sb.append(HTMLBuilder.tagWrap("h1", phraseToAnswer));

                    while (rs.next()) {
                        String name = rs.getString("name");
                        int price = rs.getInt("price");
                        sb.append(namePriceWrite(name, price));
                    }

                    response.getWriter().println(createHTML(sb.toString()));
                    rs.close();
                    break;

                case SUM_COUNT:
                    rs = stmt.executeQuery(sql);
                    sb = new StringBuilder();
                    sb.append(phraseToAnswer);

                    if (rs.next())
                        sb.append(rs.getInt(1));

                    response.getWriter().println(createHTML(sb.toString()));
                    rs.close();
                    break;
            }

            stmt.close();
        }
    }
}