package Graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Engine.Engine;
import Engine.Main;
import LinearAlgebra.Vector2;
import PhysicsObjects.*;
import PhysicsObjects.Spring;

public class Canvas extends JPanel {

    private ScheduledExecutorService scheduler;
    private long lastRenderTime = System.nanoTime();
    private BufferedImage backgroundGridImage;

    public static final int GRID_SIZE = 60;
    private static final int CENTER_SIZE = 10;

    private static final int CURSOR_SIZE = 16;

    public static final Color COLOR_RED = new Color(0xB72A3F);
    public static final Color COLOR_GREEN = new Color(0x3AF86A);
    public static final Color COLOR_BLUE = new Color(0x205CE9);

    public static final Color COLOR_HIGHLIGHT = new Color(0, 0, 0, 50);

    public void startRender() {
        int fpsInMs = 1000 / getMonitorRefreshRate();

        createBackgroundGridImage();

        scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            SwingUtilities.invokeLater(this::repaint);
        }, 0, fpsInMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setStroke(new BasicStroke(2));

        g2d.setColor(new Color(0x0D0F11));
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        g2d.drawImage(backgroundGridImage, 0, 0, null);

        renderObjects(g2d);

        g2d.setColor(COLOR_RED);
        g2d.setStroke(new BasicStroke(2));
        g2d.fillArc(Window.mouseX - CURSOR_SIZE / 2, Window.mouseY - CURSOR_SIZE / 2, CURSOR_SIZE, CURSOR_SIZE, 1, 360);
        g2d.setColor(Color.black);
        g2d.drawArc(Window.mouseX - CURSOR_SIZE / 2, Window.mouseY - CURSOR_SIZE / 2, CURSOR_SIZE, CURSOR_SIZE, 1, 360);

        renderDebugView(g2d);

        long currentTime = System.nanoTime();
        Main.renderTime = TimeUnit.NANOSECONDS.toMillis(currentTime - lastRenderTime);
        lastRenderTime = currentTime;
    }

    private void renderObjects(Graphics2D g2d) {
        // Rendering each object type separately
        g2d.setStroke(new BasicStroke(2));
        renderAnchor(g2d);
        renderCubes(g2d);
        renderSpheres(g2d);
        renderSprings(g2d);
        renderRods(g2d);
    }

    private void renderAnchor(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2));

        for (Anchor anchor : Engine.anchorArray) {
            int x = (int) anchor.getPosition().x;
            int y = (int) anchor.getPosition().y;

            int size = 30;

            int[] xPoints = new int[3];
            int[] yPoints = new int[3];

            xPoints[0] = x;
            yPoints[0] = y;

            xPoints[1] = x + size;
            yPoints[1] = y;

            xPoints[2] = x + size / 2;
            double v = (Math.sqrt(3) * size / 2);
            yPoints[2] = (int) (y + v);

            g2d.setColor(Color.WHITE);
            g2d.fillPolygon(xPoints, yPoints, 3);
            g2d.setColor(Color.BLACK);
            g2d.drawPolygon(xPoints, yPoints, 3);

            g2d.fillArc(x + size / 2 - CENTER_SIZE / 2, (int) (y + v / 2.56 - CENTER_SIZE / 2), CENTER_SIZE, CENTER_SIZE, 1, 360);

            if (anchor == Engine.currentHoverObject) {
                g2d.setColor(COLOR_HIGHLIGHT);
                g2d.fillPolygon(xPoints, yPoints, 3);
            }

        }
    }

    private void renderRods(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(6));

        for(Rod rod : Engine.rodArray) {
            PhysicsObject pointA = rod.pointA;
            PhysicsObject pointB = rod.pointB;

            g2d.setColor(Color.WHITE);
            g2d.drawLine((int) pointA.getPosition().x + pointA.getSize() / 2, (int) pointA.getPosition().y + pointA.getSize() / 2, (int) pointB.getPosition().x + pointB.getSize() / 2, (int) pointB.getPosition().y + pointB.getSize() / 2);
        }
    }

    private void renderCubes(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2));

        for (Cube cube : Engine.cubeArray) {
            g2d.setColor(Color.WHITE);
            int size = cube.getSize();
            int x = (int) cube.getPosition().x;
            int y = (int) cube.getPosition().y;

            // Draw the filled cube
            g2d.fillRect(x, y, size, size);

            // Draw the cube border
            g2d.setColor(Color.black);
            g2d.drawRect(x, y, size, size);
            g2d.fillOval(x + size / 2 - CENTER_SIZE / 2, y + size / 2 - CENTER_SIZE / 2, CENTER_SIZE, CENTER_SIZE);

            if (cube == Engine.currentHoverObject) {
                g2d.setColor(COLOR_HIGHLIGHT);
                g2d.fillRect(x, y, size, size);
            }
        }
    }

    private void renderSpheres(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2));

        for (Sphere sphere : Engine.sphereArray) {
            g2d.setColor(Color.WHITE);
            int x = (int) sphere.getPosition().x;
            int y = (int) sphere.getPosition().y;
            int size = sphere.getSize();

            g2d.fillOval(x, y, sphere.getSize(), size);
            g2d.setColor(Color.black);
            g2d.draw(new Ellipse2D.Double(x, y, size, size));
            g2d.fillOval(x + size / 2 - CENTER_SIZE / 2, y + size / 2 - CENTER_SIZE / 2, CENTER_SIZE, CENTER_SIZE);

            if (sphere == Engine.currentHoverObject) {
                g2d.setColor(COLOR_HIGHLIGHT);
                g2d.fill(new Ellipse2D.Double(x, y, size, size));
            }
        }
    }

    private void renderSprings(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));

        for (Spring spring : Engine.springArray) {
            g2d.drawLine((int) spring.pointA.x, (int) spring.pointA.y, (int) spring.pointB.x, (int) spring.pointB.y);
            g2d.fillOval((int) spring.pointA.x - 5, (int) spring.pointA.y - 5, 10, 10);
            g2d.fillOval((int) spring.pointB.x - 5, (int) spring.pointB.y - 5, 10, 10);
        }
    }

    private void renderDebugView(Graphics2D g2d) {
        g2d.setFont(new Font("Pursia", Font.BOLD, 15));

        int x = 10;
        int y = 10;
        int padding = 10;

        String tickTimeLabel = "tickTime:";
        String renderTimeLabel = "renderTime:";
        String physicsObjectCountLabel = "physicsObjectCount:";

        FontMetrics fontMetrics = g2d.getFontMetrics();
        int w = Math.max(fontMetrics.stringWidth(tickTimeLabel), fontMetrics.stringWidth(renderTimeLabel));
        int labelWidth = Math.max(w, fontMetrics.stringWidth(physicsObjectCountLabel));

        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRect(x, y, labelWidth + 80 + padding, 70);

        g2d.setColor(Color.WHITE);
        g2d.drawString(tickTimeLabel, x + padding, y + 20);
        g2d.drawString(renderTimeLabel, x + padding, y + 40);
        g2d.drawString(physicsObjectCountLabel, x + padding, y + 60);

        int valueX = x + labelWidth + 20 + padding; // Adjusted to add padding

        g2d.drawString(Main.tickTime + "ms", valueX, y + 20);
        g2d.drawString(Main.renderTime + "ms", valueX, y + 40);
        g2d.drawString(String.valueOf(Engine.physicsObjectCount), valueX, y + 60);
    }

    private int getMonitorRefreshRate() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        DisplayMode mode = gd.getDisplayMode();
        return mode.getRefreshRate();
    }

    private void createBackgroundGridImage() {
        int width = this.getWidth();
        int height = this.getHeight();

        backgroundGridImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = backgroundGridImage.createGraphics();

        g2d.setStroke(new BasicStroke(2));

        g2d.setColor(new Color(0x151819));
        renderBackgroundGrid(g2d, GRID_SIZE / 2);

        g2d.setColor(new Color(0x202224));
        renderBackgroundGrid(g2d, GRID_SIZE);

        g2d.dispose();
    }

    private void renderBackgroundGrid(Graphics2D g2d, int _size) {
        int width = this.getWidth();
        int height = this.getHeight();

        // Only draw the visible part of the grid
        int startX = 0;
        int startY = 0;
        int endX = width / _size;
        int endY = height / _size;

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                int rectX = x * _size;
                int rectY = y * _size;

                // Draw only if the rectangle is within the panel bounds
                if (rectX < width && rectY < height) {
                    g2d.drawRect(rectX, rectY, _size, _size);
                }
            }
        }
    }
}
