package Graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Engine.Engine;
import Engine.Main;
import LinearAlgebra.Vector2;
import PhysicsObjects.Cube;
import PhysicsObjects.Sphere;
import PhysicsObjects.Spring;

public class Canvas extends JPanel {

    private ScheduledExecutorService scheduler;

    private long lastRenderTime = System.nanoTime();

    private BufferedImage backgroundGridImage;

    private static final int GRID_SIZE = 60;
    private static final int CENTER_SIZE = 10;

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
        g2d.setStroke(new BasicStroke(2));

        g2d.setColor(new Color(0x0D0F11));
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        g2d.drawImage(backgroundGridImage, 0, 0, null);

        //engine code

        for(Cube cube : Engine.cubeArray) {
            Vector2 position = cube.getPosition();
            g2d.setColor(Color.WHITE);
            g2d.fillRect((int) position.x, (int) position.y, cube.getSize(), cube.getSize());
            g2d.setColor(Color.black);
            g2d.fillArc((int) position.x + cube.getSize() / 2 - CENTER_SIZE / 2, (int) position.y + cube.getSize()  / 2 - CENTER_SIZE / 2, CENTER_SIZE, CENTER_SIZE, 1, 360);
            g2d.drawRect((int) position.x, (int) position.y, cube.getSize(), cube.getSize());

            if(cube == Engine.currentHoverObject) {
                g2d.setColor(COLOR_HIGHLIGHT);
                g2d.fillRect((int) position.x, (int) position.y, cube.getSize(), cube.getSize());
            }
        }

        for(Sphere sphere : Engine.sphereArray) {
            Vector2 position = sphere.getPosition();
            g2d.setColor(Color.WHITE);
            g2d.fillArc((int) position.x, (int) position.y, sphere.getSize(), sphere.getSize(), 1, 360);
            g2d.setColor(Color.black);
            g2d.fillArc((int) position.x + sphere.getSize() / 2 - CENTER_SIZE / 2, (int) sphere.position.y + sphere.radius / 2 - CENTER_SIZE / 2, CENTER_SIZE, CENTER_SIZE, 1, 360);
            g2d.drawArc((int) position.x, (int) position.y, sphere.getSize(), sphere.getSize(), 1, 360);
        }

        for(Spring spring : Engine.springArray) {
            g2d.setColor(Color.WHITE);
            g2d.drawLine((int) spring.pointA.x, (int) spring.pointA.y, (int) spring.pointB.x, (int) spring.pointB.y);
            g2d.fillArc((int) spring.pointA.x - 5, (int) spring.pointA.y - 5, 10, 10, 1, 360);
            g2d.fillArc((int) spring.pointB.x - 5, (int) spring.pointB.y - 5, 10, 10, 1, 360);
        }

        renderDebugView(g2d);

        long currentTime = System.nanoTime();
        Main.renderTime = TimeUnit.NANOSECONDS.toMillis(currentTime - lastRenderTime);
        lastRenderTime = currentTime;
    }

    private void renderDebugView(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD, 15));

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
