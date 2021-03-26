package frontend.setting;

import javax.imageio.ImageIO;
import javax.swing.*;

import frontend.ApplicationWindow;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class SettingsButton extends JLabel implements MouseListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ImageIcon selected;
    private ImageIcon unselected;

    public SettingsButton() {
        addMouseListener(this);
        try {
            unselected = new ImageIcon((ImageIO.read(Objects.requireNonNull(
                    this.getClass().getClassLoader().getResourceAsStream("images" + "/SettingsImage.png"))))
                            .getScaledInstance(20, 20, BufferedImage.SCALE_SMOOTH));
            selected = new ImageIcon((ImageIO.read(Objects.requireNonNull(
                    this.getClass().getClassLoader().getResourceAsStream("images" + "/SettingsImageSelected.png"))))
                            .getScaledInstance(20, 20, BufferedImage.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setIcon(unselected);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // EMPTY
    }

    @Override
    public void mousePressed(MouseEvent e) {
        ApplicationWindow.getInstance().add(new SettingsPage());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // EMPTY
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
