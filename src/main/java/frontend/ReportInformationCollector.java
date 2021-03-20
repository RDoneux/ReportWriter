package frontend;

import backend.ReportComponents;
import backend.ReportDetails;
import backend.ReportManager;
import backend.constant.SettingsConstant;
import frontend.setting.SettingsButton;
import org.jdatepicker.JDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ReportInformationCollector extends JPanel {

    private final JTextField participantName;
    private final JTextField organisation;
    private final JTextField courseAttended;
    private final JDatePicker courseDate;
    private final JTextField coTrainer;

    private final JCheckBox auditBasedInterventions;
    private final JCheckBox presentation;
    private final JCheckBox portfolio;
    private static JComboBox<Integer> theoryAssessment;
    private final JLabel theoryAssessmentDisplay;

    private final JCheckBox openDocumentOnceCreated;
    private final JFileChooser fileChooser;
    private final JLabel errorMessage;

    public static final String DEFAULT_FILE_LOCATION = "DEFAULT_FILE_LOCATION";
    private final JLabel fileLocationDisplay;
    private String fileLocation;

    private final JButton createReport;

    public ReportInformationCollector() {

        setBackground(new Color(240, 240, 240));
        Dimension preferredTextFieldSize = new Dimension(190, 25);

        participantName = new JTextField();
        participantName.setPreferredSize(preferredTextFieldSize);

        organisation = new JTextField();
        organisation.setPreferredSize(preferredTextFieldSize);

        courseAttended = new JTextField();
        courseAttended.setPreferredSize(preferredTextFieldSize);

        coTrainer = new JTextField();
        coTrainer.setPreferredSize(preferredTextFieldSize);

        courseDate = new JDatePicker();
        courseDate.setPreferredSize(preferredTextFieldSize);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        courseDate.getFormattedTextField().setText(LocalDateTime.now().format(formatter));

        auditBasedInterventions = new JCheckBox("Audit-Based Interventions");
        auditBasedInterventions.setHorizontalTextPosition(SwingConstants.LEFT);
        auditBasedInterventions.setOpaque(false);
        auditBasedInterventions.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    auditBasedInterventions.setEnabled(!auditBasedInterventions.isEnabled());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        presentation = new JCheckBox("Presentation");
        presentation.setHorizontalTextPosition(SwingConstants.LEFT);
        presentation.setOpaque(false);
        presentation.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    presentation.setEnabled(!presentation.isEnabled());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        portfolio = new JCheckBox("Portfolio");
        portfolio.setHorizontalTextPosition(SwingConstants.LEFT);
        portfolio.setOpaque(false);
        portfolio.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    portfolio.setEnabled(!portfolio.isEnabled());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        theoryAssessmentDisplay = new JLabel("Theory Assessment: ");
        theoryAssessmentDisplay.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    theoryAssessment.setEnabled(!theoryAssessment.isEnabled());
                    if (theoryAssessment.isEnabled()) {
                        theoryAssessmentDisplay.setForeground(Color.BLACK);
                    } else {
                        theoryAssessmentDisplay.setForeground(Color.LIGHT_GRAY);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        theoryAssessment = new JComboBox<>();
        setUpTheoryAssessmentScore();
        theoryAssessment.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    theoryAssessment.setEnabled(!theoryAssessment.isEnabled());
                    if (theoryAssessment.isEnabled()) {
                        theoryAssessmentDisplay.setForeground(Color.BLACK);
                    } else {
                        theoryAssessmentDisplay.setForeground(Color.LIGHT_GRAY);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        createReport = new JButton("Build Report");
        createReport.addActionListener(e -> generateReport());

        errorMessage = new JLabel();
        errorMessage.setForeground(Color.RED);

        openDocumentOnceCreated = new JCheckBox("ODoC");
        openDocumentOnceCreated.setOpaque(false);
        openDocumentOnceCreated.setSelected(Boolean.parseBoolean(SettingsConstant.get("Open Document on Creation")));
        openDocumentOnceCreated.addActionListener(e -> {
            SettingsConstant.add("Open Document on Creation", String.valueOf(openDocumentOnceCreated.isSelected()));
            SettingsConstant.save();
        });

        fileLocationDisplay = new JLabel(shortenString(SettingsConstant.get("File Location")));
        fileLocation = SettingsConstant.get("File Location");
        fileLocationDisplay.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //EMPTY
            }

            @Override
            public void mousePressed(MouseEvent e) {
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.showOpenDialog(null);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //EMPTY
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                fileLocationDisplay.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                fileLocationDisplay.setForeground(Color.BLACK);
            }
        });

        fileChooser = new JFileChooser();
        fileChooser.addActionListener(e -> {
            fileLocation = fileChooser.getSelectedFile().getAbsolutePath() + "\\FeedbackForm.docx";
            fileLocationDisplay.setText(shortenString(fileChooser.getSelectedFile().getAbsolutePath()));
            SettingsConstant.add("File Location", fileChooser.getSelectedFile().getAbsolutePath());
            SettingsConstant.save();
        });

        setLayout(new GridBagLayout());

        layoutComponents();

    }

    private void layoutComponents() {

        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Candidate Name: "), c);

        c.gridy = 1;
        add(new JLabel("Organisation: "), c);

        c.gridy = 2;
        add(new JLabel("Course Attended: "), c);

        c.gridy = 3;
        add(new JLabel("Co-Trainer: "), c);

        c.gridy = 4;
        add(new JLabel("Course Date: "), c);

        c.gridy = 6;
        c.anchor = GridBagConstraints.EAST;
        add(openDocumentOnceCreated, c);

        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridwidth = 1;
        add(new SettingsButton(), c);

        c.gridy = 7;
        //c.anchor = GridBagConstraints.WEST;
        add(fileLocationDisplay, c);

        c.anchor = GridBagConstraints.EAST;
        c.gridx = 1;
        c.gridy = 5;
        add(auditBasedInterventions, c);

        c.gridy = 6;
        add(presentation, c);

        c.gridy = 7;
        add(portfolio, c);

        c.gridy = 8;
        add(theoryAssessment, c);
        c.anchor = GridBagConstraints.WEST;
        add(theoryAssessmentDisplay, c);

        c.gridy = 9;
        c.gridx = 0;
        c.insets = new Insets(10, 0, 0, 0);
        c.anchor = GridBagConstraints.EAST;
        add(createReport, c);

        c.gridx = 1;
        c.anchor = GridBagConstraints.CENTER;
        add(errorMessage, c);

        c.gridy = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 0, 0);
        add(participantName, c);

        c.gridy = 1;
        add(organisation, c);

        c.gridy = 2;
        add(courseAttended, c);

        c.gridy = 3;
        add(coTrainer, c);

        c.gridy = 4;
        add(courseDate, c);

    }

    private String shortenString(String string) {
        return (string.length() > 12) ? "..." + string.substring(string.length() - 12) : string;
    }

    public static void setUpTheoryAssessmentScore(){
        theoryAssessment.removeAllItems();
        for (int i = 0; i <= Integer.parseInt(SettingsConstant.get("Test Total")); i++) {
            theoryAssessment.addItem(i);
        }
    }


    private void generateReport() {

        if (participantName.getText().isEmpty() || organisation.getText().isEmpty() || courseAttended.getText().isEmpty() || courseDate.getFormattedTextField().getText().isEmpty() || coTrainer.getText().isEmpty()) {
            errorMessage.setText("Empty fields detected");
            return;
        }
        if (fileLocation.equals(DEFAULT_FILE_LOCATION)) {
            errorMessage.setText("Default file location");
            return;
        }
        if (!new File(fileLocation).getParentFile().exists()) {
            errorMessage.setText("File location doesn't exist");
            return;
        }

        errorMessage.setText("");
        createReport.setEnabled(false);

        participantName.requestFocus();

        new Thread(() -> {
            ReportDetails details = null;
            try {
                details = ReportDetails.builder()
                        .participantName(participantName.getText())
                        .organisation(organisation.getText())
                        .courseAttended(courseAttended.getText())
                        .courseDate(new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd MMM yyyy").parse(courseDate.getFormattedTextField().getText())))
                        .coTrainer(coTrainer.getText())
                        .auditBasedInterventions(auditBasedInterventions.isSelected())
                        .presentation(presentation.isSelected())
                        .portfolio(portfolio.isSelected())
                        .theoryAssessment(Integer.valueOf(theoryAssessment.getSelectedItem().toString()))
                        .saveLocation(fileLocation + "\\" + participantName.getText() + " Feedback.docx")
                        .reportComponents(new ReportComponents(portfolio.isEnabled(), theoryAssessment.isEnabled(),
                                auditBasedInterventions.isEnabled(), presentation.isEnabled()))
                        .build();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            participantName.setText("");
            organisation.setText("");

            new ReportManager().generateReport(Objects.requireNonNull(details));

            try {
                if (openDocumentOnceCreated.isSelected()) {
                    Desktop.getDesktop().open(new File(details.getSaveLocation()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            createReport.setEnabled(true);
        }).start();

    }

}
