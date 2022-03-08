package visitor;

import token.Token;
import token.statepattern.BraceToken;
import token.statepattern.NumberToken;
import token.statepattern.OperationToken;

import java.util.Stack;

public class CalcVisitor implements TokenVisitor {
    private final Stack<Token> stack = new Stack<>();

    @Override
    public void visit(final NumberToken token) {
        stack.push(token);
    }

    @Override
    public void visit(final BraceToken token) {
        throw new IllegalStateException(token.getBracket() + " in rns");
    }

    @Override
    public void visit(final OperationToken token) {
        if (stack.empty()) {
            throw new IllegalStateException("No one token before operation " + token.getOperation());
        }
        final Token second = stack.pop();

        if (stack.empty()) {
            throw new IllegalStateException("Only one token before operation " + token.getOperation());
        }
        final Token first = stack.pop();

        if (!(second instanceof NumberToken) || !(first instanceof NumberToken)) {
            throw new IllegalStateException("Expected two numbers before operation " + token.getOperation());
        }

        final long firstNum = ((NumberToken) first).getNumber();
        final long secondNum = ((NumberToken) second).getNumber();
        stack.push(new NumberToken(token.getFunction().apply(firstNum, secondNum)));
    }

    public long getResult() {
        final Token result = stack.pop();
        if (!(result instanceof NumberToken)) {
            throw new IllegalStateException("Result is not a number");
        }
        if (!stack.empty()) {
            throw new IllegalStateException("Redundant tokens left");
        }
        
        return ((NumberToken) result).getNumber();
    }
}
