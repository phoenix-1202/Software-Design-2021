import java.util.ArrayList;
import java.util.List;

public class ListGraph extends Graph {
    private final List<Pair<Integer, Integer>> edges;

    public ListGraph(DrawingApi api, int vertices) {
        super(api, vertices);
        edges = new ArrayList<>();
    }

    @Override
    public void addEdge(int from, int to) {
        edges.add(new Pair<>(from, to));
    }

    @Override
    protected List<Pair<Integer, Integer>> getEdges() {
        return edges;
    }
}