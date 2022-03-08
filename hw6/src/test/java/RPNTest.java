import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import token.Token;
import token.Tokenizer;
import visitor.CalcVisitor;
import visitor.ParserVisitor;
import visitor.PrintVisitor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class RPNTest {
    private List<Token> getRPN(final String expression) throws IOException {
        final InputStream is = new ByteArrayInputStream(expression.getBytes(StandardCharsets.UTF_8));
        final Tokenizer tokenizer = new Tokenizer(is);
        final List<Token> tokens = tokenizer.tokenize();

        final ParserVisitor parser = new ParserVisitor();
        tokens.forEach(token -> token.accept(parser));

        return parser.getTokens();
    }

    private long calc(final String expression) throws IOException {
        final List<Token> rpn = getRPN(expression);
        final CalcVisitor calcer = new CalcVisitor();
        rpn.forEach(token -> token.accept(calcer));
        return calcer.getResult();
    }

    private String print(final String expression) throws IOException {
        final List<Token> rpn = getRPN(expression);
        final OutputStream out = new ByteArrayOutputStream();
        final PrintVisitor printer = new PrintVisitor(out);
        rpn.forEach(token -> token.accept(printer));

        printer.print();
        return out.toString();
    }

    @Test
    public void simple() throws IOException {
        assertEquals(1L, calc("1"));
        assertEquals("1 ", print("1"));
    }

    private void checkError(final ThrowingRunnable runnable) {
        assertThrows(IllegalStateException.class, runnable);
    }

    @Test
    public void errors() {
        checkError(() -> calc("/"));
        checkError(() -> calc("3 -"));
        checkError(() -> calc("(1))"));
        checkError(() -> calc("((1 / 2)"));
    }

    @Test
    public void complex() throws IOException {
        assertEquals(-6L, calc("((1) + ((2) + 3)) * (((4))-5)"));
        assertEquals("1 2 3 + + 4 5 - * ", print("        ((1) + ((2) + 3)) * (((4))-5)\r\n"));
        assertEquals(140795424L, calc("((((((114514))) * ((1919) - 931))) / ((810))) * (1008)"));
        assertEquals("114514 1919 931 - * 810 / 1008 * ", print("((((((114514))) * ((1919) - 931))) / ((810))) * (1008)"));
    }
}
