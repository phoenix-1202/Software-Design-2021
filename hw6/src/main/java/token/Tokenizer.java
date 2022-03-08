package token;

import token.state.State;
import token.state.statepattern.WhitespaceState;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private final InputStream is;
    private final List<Token> tokens = new ArrayList<>();
    private State currentState = new WhitespaceState();

    public Tokenizer(final InputStream is) {
        this.is = is;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setCurrentState(final State state) {
        this.currentState = state;
    }

    public List<Token> tokenize() throws IOException {
        final String data = new String(is.readAllBytes(), StandardCharsets.UTF_8) + " ";
        for (int pos = 0; pos < data.length(); ++pos) {
            currentState.consume(this, data.charAt(pos));
        }
        return tokens;
    }
}
