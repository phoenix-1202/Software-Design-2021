package visitor;

import token.Token;
import token.statepattern.BraceToken;
import token.statepattern.NumberToken;
import token.statepattern.OperationToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ParserVisitor implements TokenVisitor {
    private final List<Token> tokens = new ArrayList<>();
    private final Stack<Token> stack = new Stack<>();

    @Override
    public void visit(final NumberToken token) {
        tokens.add(token);
    }

    @Override
    public void visit(final BraceToken token) {
        if (token.isOpen()) {
            stack.push(token);
        } else {
            while (true) {
                if (stack.empty()) {
                    throw new IllegalStateException(") found, but ( not found");
                }
                final Token onTop = stack.pop();
                if (onTop instanceof BraceToken && ((BraceToken) onTop).isOpen()) {
                    break;
                }
                tokens.add(onTop);
            }
        }
    }

    @Override
    public void visit(final OperationToken token) {
        while (!stack.empty()) {
            final Token onTop = stack.peek();
            if (onTop instanceof OperationToken && ((OperationToken) onTop).getRank() >= token.getRank()) {
                tokens.add(onTop);
                stack.pop();
            } else {
                break;
            }
        }
        stack.push(token);
    }

    public List<Token> getTokens() {
        while (!stack.empty()) {
            tokens.add(stack.pop());
        }
        return tokens;
    }
}
