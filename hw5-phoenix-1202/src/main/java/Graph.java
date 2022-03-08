import java.util.ArrayList;
import java.util.List;

public abstract class Graph {
    /**
     * Bridge to drawing api
     */
    private final DrawingApi api;

    protected int vertices;

    protected Graph(DrawingApi api, int vertices) {
        this.api = api;
        this.vertices = vertices;
    }

    public abstract void addEdge(int from, int to);

    protected abstract List<Pair<Integer, Integer>> getEdges();

    public void draw() {
        List<Pair<Integer, Integer>> edges = getEdges();

        Pair<Double, Double> centerPoint = new Pair<>(DrawingApi.width / 2.0, DrawingApi.height / 2.0);
        double radius = Math.min(centerPoint.getFirst(), centerPoint.getSecond()) * 0.8;

        List<Pair<Double, Double>> coords = new ArrayList<>();
        for (int v = 0; v < vertices; ++v) {
            double rad = 2 * Math.PI * v / vertices;
            Pair<Double, Double> point = new Pair<>(
                    radius * Math.cos(rad) + centerPoint.getFirst(),
                    radius * Math.sin(rad) + centerPoint.getSecond()
            );
            api.drawCircle(point, radius / 100);
            coords.add(point);
        }
        for (Pair<Integer, Integer> edge : edges) {
            api.drawLine(coords.get(edge.getFirst()), coords.get(edge.getSecond()));
        }
        api.plot();
    }
}