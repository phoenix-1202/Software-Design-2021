public class Main {
    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.err.println("Please enter: fx/awt list/matrix");
            return;
        }

        DrawingApi api;
        switch (args[0]) {
            case "fx" -> api = new FxDrawing();
            case "awt" -> api = new AwtDrawing();
            default -> {
                System.err.println("Please enter fx or awt");
                return;
            }
        }

        int vertices = 13;
        Graph graph;
        switch (args[1]) {
            case "list" -> graph = new ListGraph(api, vertices);
            case "matrix" -> graph = new MatrixGraph(api, vertices);
            default -> {
                System.err.println("Please enter: list or matrix");
                return;
            }
        }

        for (int v = 0; v < vertices; ++v) {
            for (int u = v + 1; u < vertices; ++u) {
                graph.addEdge(v, u);
            }
        }
        graph.draw();
    }
}