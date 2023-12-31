package Graphics;

import Engine.Engine;
import Engine.Main;
import LinearAlgebra.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Window extends JFrame {

    public static boolean isFullscreen = true;

    public static final int WINDOWED_WIDTH = 1260;
    public static final int WINDOWED_HEIGHT = 709;

    public static boolean isDragging = false;

    public static boolean snapToGrid = false;

    public static int mouseX;
    public static int mouseY;

    public Window(String _title, Canvas _canvas) {
        this.setTitle(_title);
        this.setSize(WINDOWED_WIDTH, WINDOWED_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        _canvas.setSize(this.getSize());
        this.add(_canvas);

        this.setResizable(false);

        this.setUndecorated(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        BufferedImage cursorImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor invisibleCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), "invisibleCursor");

        // Set the transparent cursor for the JFrame
        this.setCursor(invisibleCursor);

        addMouseMotionListener();
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
                    case KeyEvent.VK_CONTROL -> snapToGrid = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_CONTROL) snapToGrid = false;
            }
        });
    }

    void addMouseMotionListener() {
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                isDragging = true;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();

                if(!isDragging) {
                    Engine.currentHoverObject = Main.Engine.findPhysicsObject(new Vector2(e.getX(), e.getY()));
                }
            }
        });
    }

    void addMouseListener() {
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                Engine.currentDragObject = Main.Engine.findPhysicsObject(new Vector2(e.getX(), e.getY()));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
                Engine.currentDragObject = null;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

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
