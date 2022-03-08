public interface DrawingApi {
    long width = 1000;
    long height = 1000;

    void drawCircle(Pair<Double, Double> center, double radius);
    void drawLine(Pair<Double, Double> from, Pair<Double, Double> to);

    void plot();
}