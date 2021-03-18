package frontend;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class ApplicationWindow {

    private final JFrame frame = new JFrame();
    private JPanel previousPanel;

    public ApplicationWindow() {

        frame.setTitle("Report Builder");
        frame.setSize(new Dimension(322, 315));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation(new Point(30,30));
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

    public void add(JPanel panel) {
        if (previousPanel != null) {
            frame.remove(previousPanel);
        }
        previousPanel = panel;
        frame.add(panel, BorderLayout.CENTER);
    }

}
