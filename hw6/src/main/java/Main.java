import token.Token;
import token.Tokenizer;
import visitor.ParserVisitor;
import visitor.PrintVisitor;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(final String[] args) throws IOException {
        final Tokenizer tokenizer = new Tokenizer(System.in);
        final List<Token> tokens = tokenizer.tokenize();

        final ParserVisitor parser = new ParserVisitor();
        for (final Token token : tokens) {
            token.accept(parser);
        }

        final List<Token> rpn = parser.getTokens();
        final PrintVisitor printer0 = new PrintVisitor(System.out);
        for (final Token token : tokens) {
            token.accept(printer0);
        }
        printer0.print();
        System.out.println();
        for (final Token token : rpn) {
            token.accept(printer0);
        }
        printer0.print();
    }
}
