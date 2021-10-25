import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Tests {

    private Server server;

    @Before
    public void preprocess() throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }

        server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet()), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet()),"/get-products");
        context.addServlet(new ServletHolder(new QueryServlet()),"/query");

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

    @Test
    public void testWithNoProduct() throws Exception {

        Assert.assertEquals("<html><body></body></html>", request("http://localhost:8081/get-products"));

        Assert.assertEquals("<html><body>Summary price: 0</body></html>", request("http://localhost:8081/query?command=sum"));

        Assert.assertEquals("<html><body><h1>Product with max price: </h1></body></html>", request("http://localhost:8081/query?command=max"));

        Assert.assertEquals("<html><body><h1>Product with min price: </h1></body></html>", request("http://localhost:8081/query?command=min"));

        Assert.assertEquals("<html><body>Number of products: 0</body></html>", request("http://localhost:8081/query?command=count"));
    }

    @Test
    public void testAllCommandsWithProducts() throws Exception {
        Assert.assertEquals("OK", request("http://localhost:8081/add-product?name=abc&price=500"));

        Assert.assertEquals("OK", request("http://localhost:8081/add-product?name=def&price=300"));

        Assert.assertEquals("OK", request("http://localhost:8081/add-product?name=ghi&price=700"));

        Assert.assertEquals("<html><body>abc\t500</br>def\t300</br>ghi\t700</br></body></html>", request("http://localhost:8081/get-products"));

        Assert.assertEquals("<html><body>Summary price: 1500</body></html>", request("http://localhost:8081/query?command=sum"));

        Assert.assertEquals("<html><body><h1>Product with max price: </h1>ghi\t700</br></body></html>", request("http://localhost:8081/query?command=max"));

        Assert.assertEquals("<html><body><h1>Product with min price: </h1>def	300</br></body></html>", request("http://localhost:8081/query?command=min"));

        Assert.assertEquals("<html><body>Number of products: 3</body></html>", request("http://localhost:8081/query?command=count"));
    }

    @Test
    public void testWithQueryResultChanges() throws Exception {

        testAllCommandsWithProducts();

        Assert.assertEquals("OK", request("http://localhost:8081/add-product?name=wxz&price=100"));
        Assert.assertEquals("OK", request("http://localhost:8081/add-product?name=pqr&price=900"));

        Assert.assertEquals("<html><body>abc\t500</br>def\t300</br>ghi\t700</br>wxz\t100</br>pqr\t900</br></body></html>", request("http://localhost:8081/get-products"));

        Assert.assertEquals("<html><body>Summary price: 2500</body></html>", request("http://localhost:8081/query?command=sum"));

        Assert.assertEquals("<html><body><h1>Product with max price: </h1>pqr\t900</br></body></html>", request("http://localhost:8081/query?command=max"));

        Assert.assertEquals("<html><body><h1>Product with min price: </h1>wxz\t100</br></body></html>", request("http://localhost:8081/query?command=min"));

        Assert.assertEquals("<html><body>Number of products: 5</body></html>", request("http://localhost:8081/query?command=count"));

    }

    @After
    public void postprocess() throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DROP TABLE IF EXISTS PRODUCT";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
        server.stop();
    }
}
