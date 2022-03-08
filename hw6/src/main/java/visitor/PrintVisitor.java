package visitor;

import token.statepattern.BraceToken;
import token.statepattern.NumberToken;
import token.statepattern.OperationToken;

import java.io.OutputStream;
import java.io.PrintWriter;

public class PrintVisitor implements TokenVisitor {
    private final PrintWriter writer;

    public PrintVisitor(final OutputStream os) {
        writer = new PrintWriter(os);
    }

    private void write(final String formatted) {
        writer.write(String.format("%s ", formatted));
    }

    @Override
    public void visit(final NumberToken token) {
        write(Long.toString(token.getNumber()));
    }

    @Override
    public void visit(final BraceToken token) {
        write(Character.toString(token.getBracket()));
    }

    @Override
    public void visit(final OperationToken token) {
        write(Character.toString(token.getOperation()));
    }

    public void print() {
        writer.flush();
    }
}
