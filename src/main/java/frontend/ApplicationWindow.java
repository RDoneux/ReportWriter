package frontend;

import backend.constant.SettingsConstant;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class ApplicationWindow {

    private final JFrame frame = new JFrame();
    private JPanel previousPanel;

    private static ApplicationWindow applicationWindow;

    private ApplicationWindow() {
        //EMPTY
    }

    public void display() {
        frame.setTitle("Report Builder");
        frame.setSize(new Dimension(Integer.parseInt(SettingsConstant.get("Window Width")),
                Integer.parseInt(SettingsConstant.get("Window Height"))));
        frame.setResizable(Boolean.parseBoolean(SettingsConstant.get("Resizable Window")));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(new Point(30, 30));
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());

        try {
            frame.setIconImage(ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("images/Icon.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        add(new ReportInformationCollector());
        frame.add(new Signature(), BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
    }

    public static ApplicationWindow getInstance() {
        if (applicationWindow == null) {
            applicationWindow = new ApplicationWindow();
        }
        return applicationWindow;
    }

    public void add(JPanel panel) {
        if (previousPanel != null) {
            frame.remove(previousPanel);
        }
        previousPanel = panel;
        frame.add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    public JFrame getFrame() {
        return frame;
    }

}
