package Graphics;

import javax.swing.*;
import java.awt.event.*;

public class Window extends JFrame {

    public float lastMX;
    public float lastMY;

    public static boolean isFullscreen = true;

    public static final int WINDOWED_WIDTH = 1280;
    public static final int WINDOWED_HEIGHT = 720;

    public Window(String _title, Canvas _canvas) {
        this.setTitle(_title);
        this.setSize(WINDOWED_WIDTH, WINDOWED_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        _canvas.setSize(this.getSize());
        this.add(_canvas);

        //this.setResizable(false);

        this.setUndecorated(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        addMouseListener();
        addKeyListener();

        this.setVisible(true);
    }

    void addKeyListener() {
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_F11 -> toggleFullScreen();
                    case KeyEvent.VK_ESCAPE -> System.exit(0);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    void addMouseListener() {
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                float diffX = e.getX() - lastMX;
                float diffY = e.getY() - lastMY;
            }
        });
    }

    public void toggleFullScreen() {
        System.out.println("toggle");
        isFullscreen = !isFullscreen;

        if (isFullscreen) {
            this.dispose(); // Dispose the frame before changing undecorated state
            this.setUndecorated(true);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setVisible(true);
        } else {
            this.dispose(); // Dispose the frame before changing undecorated state
            this.setUndecorated(false);
            this.pack();
            this.setSize(WINDOWED_WIDTH, WINDOWED_HEIGHT); // Set your desired window size when exiting fullscreen
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }
    }
}
