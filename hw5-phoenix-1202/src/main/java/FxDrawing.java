import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FxDrawing extends Application implements DrawingApi {
    private static final List<Shape> shapes = new ArrayList<>();

    @Override
    public void start(final Stage stage) {
        Group figures = new Group(shapes.stream().map(Group::new).collect(Collectors.toList()));
        stage.setScene(new Scene(figures, width, height));
        stage.show();
    }

    @Override
    public void drawCircle(Pair<Double, Double> center, double radius) {
        shapes.add(new Circle(center.getFirst(), center.getSecond(), radius));
    }

    @Override
    public void drawLine(Pair<Double, Double> from, Pair<Double, Double> to) {
        shapes.add(new Line(from.getFirst(), from.getSecond(), to.getFirst(), to.getSecond()));
    }

    @Override
    public void plot() {
        launch(this.getClass());
    }
}