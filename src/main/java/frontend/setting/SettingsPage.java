package frontend.setting;

import backend.constant.SettingsConstant;
import frontend.ApplicationWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class SettingsPage extends JPanel {

    private final JTextField examOutOf;
    private final JTextField examPass;
    private final JTextField examBorderlinePass;
    private final JTextField fileLocation;
    private final JTextField windowX;
    private final JTextField windowY;

    private final JCheckBox resizable;

    public SettingsPage() {
        setBackground(new Color(240, 240, 240));
        Dimension preferredTextFieldSize = new Dimension(190, 25);

        examOutOf = new JTextField(SettingsConstant.get("Test Total"));
        examOutOf.setPreferredSize(preferredTextFieldSize);

        examPass = new JTextField(SettingsConstant.get("Test Pass Mark"));
        examPass.setPreferredSize(preferredTextFieldSize);

        examBorderlinePass = new JTextField(SettingsConstant.get("Test Borderline Pass mark"));
        examBorderlinePass.setPreferredSize(preferredTextFieldSize);

        fileLocation = new JTextField(SettingsConstant.get("File Location"));
        fileLocation.setPreferredSize(preferredTextFieldSize);

        resizable = new JCheckBox("Resizable Window");
        resizable.setHorizontalTextPosition(SwingConstants.LEFT);
        resizable.setSelected(Boolean.parseBoolean(SettingsConstant.get("Resizable Window")));
        resizable.addActionListener(e -> {
            SettingsConstant.add("Resizable Window", String.valueOf(resizable.isSelected()));
            ApplicationWindow.getInstance().getFrame().setResizable(resizable.isSelected());
        });

        windowX = new JTextField(String.valueOf(ApplicationWindow.getInstance().getFrame().getWidth()));
        windowX.setPreferredSize(new Dimension(90, 25));

        windowY = new JTextField(String.valueOf(ApplicationWindow.getInstance().getFrame().getHeight()));
        windowY.setPreferredSize(new Dimension(90, 25));

        ApplicationWindow.getInstance().getFrame().addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                windowX.setText(String.valueOf(ApplicationWindow.getInstance().getFrame().getWidth()));
                windowY.setText(String.valueOf(ApplicationWindow.getInstance().getFrame().getHeight()));
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Exam out of: "), c);

        c.gridy = 1;
        add(new JLabel("Pass: "), c);

        c.gridy = 2;
        add(new JLabel("Borderline pass: "), c);

        c.gridy = 3;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        add(new JLabel("---"), c);

        c.anchor = GridBagConstraints.EAST;
        c.gridwidth = 1;
        c.gridy = 4;
        add(new JLabel("File Location: "), c);

        c.gridy = 5;
        add(new JLabel("Window Size: "), c);

        c.gridwidth = 2;
        c.gridy = 6;
        add(resizable, c);

        c.anchor = GridBagConstraints.WEST;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        add(examOutOf, c);

        c.gridy = 1;
        add(examPass, c);

        c.gridy = 2;
        add(examBorderlinePass, c);

        c.gridy = 4;
        add(fileLocation, c);

        c.gridy = 5;
        add(windowX, c);

        c.anchor = GridBagConstraints.EAST;
        add(windowY, c);

        c.anchor = GridBagConstraints.WEST;
        c.gridy = 7;
        c.gridx = 0;
        c.gridwidth = 2;
        add(new SaveSettings(this), c);

    }

    public JTextField getExamOutOf() {
        return examOutOf;
    }

    public JTextField getExamPass() {
        return examPass;
    }

    public JTextField getExamBorderlinePass() {
        return examBorderlinePass;
    }

    public JTextField getWindowX() {
        return windowX;
    }

    public JTextField getWindowY() {
        return windowY;
    }
}
