import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.akirakozov.sd.refactoring.DatabaseUtils;
import ru.akirakozov.sd.refactoring.SQLType;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static ru.akirakozov.sd.refactoring.HTMLBuilder.*;

public class Tests {

    private Server server;
    private final String URL_PREFIX = "http://localhost:8081/";

    @Before
    public void preprocess() throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)";
        DatabaseUtils.dbCommand(null, SQLType.CREATE_INSERT_DROP, sql, "");

        server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet()), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet()), "/get-products");
        context.addServlet(new ServletHolder(new QueryServlet()), "/query");

        server.start();
    }

    private String request(String url) throws Exception {
        URL curURL = new URL(url);
        URLConnection uc = curURL.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        uc.getInputStream()));
        String inputLine;
        StringBuilder sb = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            sb.append(inputLine);
        in.close();
        return sb.toString();
    }

    private String requestGet() throws Exception {
        return request(URL_PREFIX + "get-products");
    }

    private String requestAdd(String name, int price) throws Exception {
        return request(URL_PREFIX + "add-product?name=" + name + "&price=" + price);
    }

    private String requestQuery(String command) throws Exception {
        return request(URL_PREFIX + "query?command=" + command);
    }

    @Test
    public void testWithNoProduct() throws Exception {
        Assert.assertEquals(createHTML(), requestGet());

        Assert.assertEquals(createHTML("Summary price: ", "0"), requestQuery("sum"));

        Assert.assertEquals(createHTML(tagWrap("h1", "Product with max price: ")), requestQuery("max"));

        Assert.assertEquals(createHTML(tagWrap("h1", "Product with min price: ")), requestQuery("min"));

        Assert.assertEquals(createHTML("Number of products: ", "0"), requestQuery("count"));
    }

    @Test
    public void testAllCommandsWithProducts() throws Exception {
        Assert.assertEquals("OK", requestAdd("abc", 500));

        Assert.assertEquals("OK", requestAdd("def", 300));

        Assert.assertEquals("OK", requestAdd("ghi", 700));

        Assert.assertEquals(createHTML(
                namePriceWrite("abc", 500),
                namePriceWrite("def", 300),
                namePriceWrite("ghi", 700)), requestGet());

        Assert.assertEquals(createHTML("Summary price: ", "1500"), requestQuery("sum"));

        Assert.assertEquals(createHTML(
                tagWrap("h1", "Product with max price: "),
                namePriceWrite("ghi", 700)), requestQuery("max"));

        Assert.assertEquals(createHTML(
                tagWrap("h1", "Product with min price: "),
                namePriceWrite("def", 300)), requestQuery("min"));

        Assert.assertEquals(createHTML("Number of products: ", "3"), requestQuery("count"));
    }

    @Test
    public void testWithQueryResultChanges() throws Exception {

        testAllCommandsWithProducts();

        Assert.assertEquals("OK", requestAdd("wxz", 100));
        Assert.assertEquals("OK", requestAdd("pqr", 900));

        Assert.assertEquals(createHTML(
                namePriceWrite("abc", 500),
                namePriceWrite("def", 300),
                namePriceWrite("ghi", 700),
                namePriceWrite("wxz", 100),
                namePriceWrite("pqr", 900)), requestGet());

        Assert.assertEquals(createHTML("Summary price: ", "2500"), requestQuery("sum"));

        Assert.assertEquals(createHTML(
                tagWrap("h1", "Product with max price: "),
                namePriceWrite("pqr", 900)), requestQuery("max"));

        Assert.assertEquals(createHTML(
                tagWrap("h1", "Product with min price: "),
                namePriceWrite("wxz", 100)), requestQuery("min"));

        Assert.assertEquals(createHTML("Number of products: ", "5"), requestQuery("count"));
    }

    @After
    public void postprocess() throws Exception {
        String sql = "DROP TABLE IF EXISTS PRODUCT";
        DatabaseUtils.dbCommand(null, SQLType.CREATE_INSERT_DROP, sql, "");
        server.stop();
    }
}
