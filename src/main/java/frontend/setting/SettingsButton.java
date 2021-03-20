package frontend.setting;

import frontend.ApplicationWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class SettingsButton extends JLabel implements MouseListener {

    private ImageIcon selected;
    private ImageIcon unselected;

    public SettingsButton() {
        addMouseListener(this);
        try {
            unselected =
                    new ImageIcon(getScaledImage((ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("images" +
                            "/SettingsImage.png")))), 20, 20));
            selected =
                    new ImageIcon(getScaledImage((ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("images" +
                            "/SettingsImageSelected.png")))), 20, 20));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setIcon(unselected);
    }

    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //EMPTY
    }

    @Override
    public void mousePressed(MouseEvent e) {
        ApplicationWindow.getInstance().add(new SettingsPage());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //EMPTY
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setIcon(selected);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setIcon(unselected);
    }
}
