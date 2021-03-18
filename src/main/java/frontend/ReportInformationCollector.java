package frontend;

import backend.ReportDetails;
import backend.ReportManager;
import org.jdatepicker.JDatePicker;


import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class ReportInformationCollector extends JPanel {

    private final JTextField participantName;
    private final JTextField organisation;
    private final JTextField courseAttended;
    private final JDatePicker courseDate;
    private final JTextField coTrainer;

    private final JCheckBox auditBasedInterventions;
    private final JCheckBox presentation;
    private final JCheckBox portfolio;
    private final JComboBox<Integer> theoryAssessment;

    private final JLabel errorMessage;

    private final JButton createReport;

    private final Dimension preferredTextFieldSize = new Dimension(190, 25);

    public ReportInformationCollector() {

        setBackground(Color.WHITE);

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

        auditBasedInterventions = new JCheckBox("Audit-Based Interventions");
        auditBasedInterventions.setHorizontalTextPosition(SwingConstants.LEFT);
        auditBasedInterventions.setOpaque(false);

        presentation = new JCheckBox("Presentation");
        presentation.setHorizontalTextPosition(SwingConstants.LEFT);
        presentation.setOpaque(false);

        portfolio = new JCheckBox("Portfolio");
        portfolio.setHorizontalTextPosition(SwingConstants.LEFT);
        portfolio.setOpaque(false);

        theoryAssessment = new JComboBox<>();
        for (int i = 0; i < 40; i++) {
            theoryAssessment.addItem(i);
        }

        createReport = new JButton("Build Report");
        createReport.addActionListener(e -> {
            try {
                generateReport();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        });

        errorMessage = new JLabel();
        errorMessage.setForeground(Color.RED);

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
        add(new JLabel("Course Date: "), c);

        c.gridy = 4;
        add(new JLabel("Co-Trainer: "), c);

        c.gridwidth = 2;
        c.gridy = 5;
        add(auditBasedInterventions, c);

        c.gridy = 6;
        add(presentation, c);

        c.gridy = 7;
        add(portfolio, c);

        c.gridwidth = 1;
        c.gridy = 8;
        c.gridx = 1;
        add(theoryAssessment, c);
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("Theory Assessment: "), c);

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
        add(courseDate, c);

        c.gridy = 4;
        add(coTrainer, c);

    }

    private void generateReport() throws ParseException {

        if (participantName.getText().isEmpty() || organisation.getText().isEmpty() || courseAttended.getText().isEmpty() || courseDate.getFormattedTextField().getText().isEmpty() || coTrainer.getText().isEmpty()) {
            errorMessage.setText("Empty fields detected");
            return;
        }

        errorMessage.setText("");
        ReportDetails details = ReportDetails.builder()
                .participantName(participantName.getText())
                .organisation(organisation.getText())
                .courseAttended(courseAttended.getText())
                .courseDate(new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("dd MMM yyyy").parse(courseDate.getFormattedTextField().getText())))
                .coTrainer(coTrainer.getText())
                .auditBasedInterventions(auditBasedInterventions.isSelected())
                .presentation(presentation.isSelected())
                .portfolio(portfolio.isSelected())
                .theoryAssessment(Integer.valueOf(theoryAssessment.getSelectedItem().toString()))
                .saveLocation("C:\\Users\\R.Doneux\\OneDrive - The Loddon School\\Desktop\\TestDocument.docx")
                .build();

        participantName.setText("");
        organisation.setText("");

        new ReportManager().generateReport(details);

    }

}
