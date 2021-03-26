package frontend.setting;

import backend.AdditionalCommentGenerator;
import backend.constant.SettingsConstant;
import frontend.ApplicationWindow;
import frontend.ReportInformationCollector;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Objects;

public class SettingsPage extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final JTextField examOutOf;
    private final JTextField examPass;
    private final JTextField examBorderlinePass;
    private final JTextField windowX;
    private final JTextField windowY;
    private final JTextField leadInstructor;

    private final JCheckBox resizable;

    private final JTextArea additionalCommentArea;
    private final JComboBox<String> additionalCommentOption;
    private JComboBox<String> keySelector = null;

    public SettingsPage() {

        setBackground(new Color(240, 240, 240));
        Dimension preferredTextFieldSize = new Dimension(190, 25);

        JLabel backIcon = new JLabel();
        try {
            ImageIcon unselectedBackIcon = new ImageIcon(ImageIO.read(Objects
                    .requireNonNull(this.getClass().getClassLoader().getResourceAsStream("images/BackIcon.png"))));
            ImageIcon selectedBackIcon = new ImageIcon(ImageIO.read(Objects.requireNonNull(
                    this.getClass().getClassLoader().getResourceAsStream("images/BackIconSelected.png"))));
            backIcon.setIcon(unselectedBackIcon);
            backIcon.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // EMPTY
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    ApplicationWindow.getInstance().add(new ReportInformationCollector());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // EMPTY
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    backIcon.setIcon(selectedBackIcon);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    backIcon.setIcon(unselectedBackIcon);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        examOutOf = new JTextField(SettingsConstant.get("Test Total"));
        examOutOf.setPreferredSize(preferredTextFieldSize);

        examPass = new JTextField(SettingsConstant.get("Test Pass Mark"));
        examPass.setPreferredSize(preferredTextFieldSize);

        examBorderlinePass = new JTextField(SettingsConstant.get("Test Borderline Pass Mark"));
        examBorderlinePass.setPreferredSize(preferredTextFieldSize);

        JTextField fileLocation = new JTextField(SettingsConstant.get("File Location"));
        fileLocation.setPreferredSize(preferredTextFieldSize);

        resizable = new JCheckBox("Resizable Window");
        resizable.setHorizontalTextPosition(SwingConstants.LEFT);
        resizable.setSelected(Boolean.parseBoolean(SettingsConstant.get("Resizable Window")));
        resizable.addActionListener(e -> {
            SettingsConstant.getInstance().add("Resizable Window", String.valueOf(resizable.isSelected()));
            SettingsConstant.getInstance().save();
            ApplicationWindow.getInstance().getFrame().setResizable(resizable.isSelected());
        });

        windowX = new JTextField(String.valueOf(ApplicationWindow.getInstance().getFrame().getWidth()));
        windowX.setPreferredSize(new Dimension(80, 25));

        windowY = new JTextField(String.valueOf(ApplicationWindow.getInstance().getFrame().getHeight()));
        windowY.setPreferredSize(new Dimension(80, 25));

        ApplicationWindow.getInstance().getFrame().addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                windowX.setText(String.valueOf(ApplicationWindow.getInstance().getFrame().getWidth()));
                windowY.setText(String.valueOf(ApplicationWindow.getInstance().getFrame().getHeight()));
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                // EMPTY
            }

            @Override
            public void componentShown(ComponentEvent e) {
                // EMPTY
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                // EMPTY
            }
        });

        additionalCommentArea = new JTextArea();
        additionalCommentArea.setLineWrap(true);
        additionalCommentArea.setWrapStyleWord(true);
        additionalCommentArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        additionalCommentOption = new JComboBox<>(new String[] { "Opening Statement", "Exam Statement", "PI Statement",
                "Portfolio Statement", "Closing Statement" });
        additionalCommentOption.addActionListener(e -> {

            keySelector.removeAllItems();
            keySelector.setEnabled(true);
            switch (additionalCommentOption.getSelectedItem().toString()) {
            case "Opening Statement":
            case "PI Statement":
                keySelector.setEnabled(false);
                break;
            case "Exam Statement":
                keySelector.addItem("Perfect");
                keySelector.addItem("Pass");
                keySelector.addItem("Borderline");
                keySelector.addItem("Refer");
                break;
            case "Closing Statement":
            case "Portfolio Statement":
                keySelector.addItem("Pass");
                keySelector.addItem("Refer");
                break;
            default:
                break;
            }
        });

        keySelector = new JComboBox<>();
        keySelector.setEnabled(false);

        JButton commitComment = new JButton("Commit Comment");
        commitComment.setPreferredSize(new Dimension(142, 20));
        commitComment.setMaximumSize(new Dimension(142, 20));
        commitComment.setMinimumSize(new Dimension(142, 20));
        commitComment.addActionListener(e -> {
            if (additionalCommentArea.getText().isEmpty())
                return;
            try {
                AdditionalCommentGenerator additionalCommentGenerator = new AdditionalCommentGenerator();
                switch (additionalCommentOption.getSelectedItem().toString()) {
                case "Opening Statement":
                    additionalCommentGenerator.addAdditionalOpeningStatement(additionalCommentArea.getText());
                    break;
                case "Exam Statement":
                    additionalCommentGenerator.addAdditionalTheoryAssessmentStatement(
                            keySelector.getSelectedItem().toString(), additionalCommentArea.getText());
                    break;
                case "PI Statement":
                    additionalCommentGenerator
                            .addAdditionalAuditBasedInterventionAssessment(additionalCommentArea.getText());
                    break;
                case "Portfolio Statement":
                    additionalCommentGenerator.addAdditionalPortfolioStatement(keySelector.getSelectedItem().toString(),
                            additionalCommentArea.getText());
                    break;
                case "Closing Statement":
                    additionalCommentGenerator.addAdditionalClosingStatement(keySelector.getSelectedItem().toString(),
                            additionalCommentArea.getText());
                    break;
                default:
                    break;
                }
                additionalCommentArea.setText("");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        leadInstructor = new JTextField(SettingsConstant.get("Lead Instructor"));
        leadInstructor.setPreferredSize(preferredTextFieldSize);

        JPanel displayPanel = new JPanel();
        JScrollPane sp = new JScrollPane(displayPanel);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        displayPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 10, 0, 0);
        displayPanel.add(backIcon, c);

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(0, 0, 0, 0);
        displayPanel.add(new JLabel("Exam out of: "), c);

        c.gridy = 1;
        displayPanel.add(new JLabel("Pass: "), c);

        c.gridy = 2;
        displayPanel.add(new JLabel("Borderline pass: "), c);

        c.gridy = 3;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.CENTER;
        displayPanel.add(new JLabel("---"), c);

        c.anchor = GridBagConstraints.EAST;
        c.gridwidth = 1;
        c.gridy = 4;
        displayPanel.add(new JLabel("File Location: "), c);

        c.gridy = 5;
        displayPanel.add(new JLabel("Lead Instructor: "), c);

        c.gridy = 6;
        displayPanel.add(new JLabel("Window Size: "), c);

        c.gridwidth = 2;
        c.gridy = 7;
        displayPanel.add(resizable, c);

        c.gridy = 8;
        c.gridwidth = 1;
        displayPanel.add(new JLabel("Additional Comments: "), c);

        c.gridy = 9;
        displayPanel.add(additionalCommentOption, c);

        c.anchor = GridBagConstraints.WEST;
        c.gridx = 1;
        c.gridy = 0;
        displayPanel.add(examOutOf, c);

        c.gridy = 1;
        displayPanel.add(examPass, c);

        c.gridy = 2;
        displayPanel.add(examBorderlinePass, c);

        c.gridy = 4;
        displayPanel.add(fileLocation, c);

        c.gridy = 5;
        displayPanel.add(leadInstructor, c);

        c.gridy = 6;
        displayPanel.add(windowX, c);

        c.anchor = GridBagConstraints.EAST;
        displayPanel.add(windowY, c);

        c.gridy = 9;
        c.gridheight = 4;
        c.fill = GridBagConstraints.BOTH;
        displayPanel.add(additionalCommentArea, c);

        c.gridy = 10;
        c.gridx = 0;
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;
        displayPanel.add(keySelector, c);

        c.gridy = 11;
        displayPanel.add(commitComment, c);

        c.anchor = GridBagConstraints.WEST;
        c.gridy = 12;
        c.gridx = 0;
        c.gridheight = 1;
        c.gridwidth = 2;
        displayPanel.add(new SaveSettings(this), c);

        setLayout(new BorderLayout());
        add(sp, BorderLayout.CENTER);
        ApplicationWindow.getInstance().getFrame().setTitle("Report Builder - Settings");

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

    public JTextField getLeadInstructor() {
        return leadInstructor;
    }
}
