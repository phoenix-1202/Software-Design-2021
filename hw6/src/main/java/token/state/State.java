package token.state;

import token.Tokenizer;
import token.state.statepattern.BraceState;
import token.state.statepattern.NumberState;
import token.state.statepattern.OperationState;
import token.state.statepattern.WhitespaceState;

import java.util.Set;

public abstract class State {
    private static final Set<Character> operations = Set.of('+', '-', '*', '/');
    private static final Set<Character> braces = Set.of('(', ')');

    public abstract void consume(final Tokenizer tokenizer, final char ch);

    public State next(final char ch) {
        if (operations.contains(ch)) {
            return new OperationState(ch);
        }
        if (braces.contains(ch)) {
            return new BraceState(ch);
        }
        if (Character.isDigit(ch)) {
            return new NumberState(ch);
        }
        if (Character.isWhitespace(ch)) {
            return new WhitespaceState();
        }
        throw new IllegalStateException("Cannot recognize: '" + ch + "', code = " + (int) ch);
    }
}
