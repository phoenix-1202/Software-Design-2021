import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class AwtDrawing extends Frame implements DrawingApi {
    private final List<Ellipse2D.Double> circles = new ArrayList<>();
    private final List<Line2D.Double> lines = new ArrayList<>();

    @Override
    public void paint(final Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;

        graphics2D.setPaint(Color.BLUE);
        lines.forEach(graphics2D::draw);

        graphics2D.setPaint(Color.BLACK);
        circles.forEach(graphics2D::fill);
    }

    @Override
    public void drawCircle(Pair<Double, Double> center, double radius) {
        circles.add(new Ellipse2D.Double(center.getFirst(), center.getSecond(), radius, radius));
    }

    @Override
    public void drawLine(Pair<Double, Double> from, Pair<Double, Double> to) {
        lines.add(new Line2D.Double(from.getFirst(), from.getSecond(), to.getFirst(), to.getSecond()));
    }

    @Override
    public void plot() {
        addWindowListener(new WindowAdapter() {
            /**
             * on close
             */
            @Override
            public void windowClosing(final WindowEvent e) {
                System.exit(0);
            }
        });
        setSize((int) width, (int) height);
        setVisible(true);
    }
}