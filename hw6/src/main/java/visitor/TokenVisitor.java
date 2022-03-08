package visitor;

import token.statepattern.BraceToken;
import token.statepattern.NumberToken;
import token.statepattern.OperationToken;

public interface TokenVisitor {
    void visit(final NumberToken token);
    void visit(final BraceToken token);
    void visit(final OperationToken token);
}
