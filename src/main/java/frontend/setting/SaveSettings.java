package frontend.setting;

import backend.constant.SettingsConstant;
import frontend.ApplicationWindow;
import frontend.ReportInformationCollector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveSettings extends JButton implements ActionListener {

    private final SettingsPage settingsPage;

    public SaveSettings(SettingsPage settingsPage){
        this.settingsPage = settingsPage;
        setText("Save Settings");
        setPreferredSize(new Dimension(142, 20));
        setMaximumSize(new Dimension(142, 20));
        setMinimumSize(new Dimension(142, 20));

        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SettingsConstant constant = SettingsConstant.getInstance();
        constant.add("Test Total", settingsPage.getExamOutOf().getText());
        constant.add("Test Pass Mark", settingsPage.getExamPass().getText());
        constant.add("Test Borderline Pass Mark", settingsPage.getExamBorderlinePass().getText());
        constant.add("Window Width", settingsPage.getWindowX().getText());
        constant.add("Window Height", settingsPage.getWindowY().getText());
        constant.add("Lead Instructor", settingsPage.getLeadInstructor().getText());
        constant.save();
        ReportInformationCollector.setUpTheoryAssessmentScore();
        ApplicationWindow.getInstance().add(new ReportInformationCollector());
    }
}
