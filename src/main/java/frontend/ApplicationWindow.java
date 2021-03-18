package frontend;

import javax.swing.*;
import java.awt.*;

public class ApplicationWindow {

    private final JFrame frame;
    private JPanel previousPanel;

    public ApplicationWindow() {

        frame = new JFrame();
        frame.setTitle("Report Builder");
        frame.setSize(new Dimension(320, 315));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(new Point(30,30));
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());

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
