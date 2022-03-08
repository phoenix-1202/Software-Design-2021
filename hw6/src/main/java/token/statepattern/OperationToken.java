package token.statepattern;

import token.Token;
import visitor.TokenVisitor;

import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class OperationToken implements Token {
    private static final Map<Character, Integer> rank = Map.of('+', 1, '-', 1, '*', 2, '/', 2);
    private final char operation;

    public OperationToken(final char operation) {
        this.operation = operation;
    }

    public int getRank() {
        return rank.get(operation);
    }

    public BiFunction<Long, Long, Long> getFunction() {
        switch (operation) {
            case '+':
                return Long::sum;
            case '-':
                return (x, y) -> x - y;
            case '*':
                return (x, y) -> x * y;
            case '/':
                return (x, y) -> x / y;
            default:
                throw new IllegalStateException("Unknown operation " + operation);
        }
    }

    @Override
    public void accept(final TokenVisitor visitor) {
        visitor.visit(this);
    }

    public char getOperation() {
        return operation;
    }
}
