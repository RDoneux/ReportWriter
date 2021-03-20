package backend.constant;

import frontend.ReportInformationCollector;

public class SettingsConstant extends AbstractConstant {

    private static SettingsConstant settingsConstant;

    private SettingsConstant(String fileName) {
        super(fileName);

        add("Test Total", "40");
        add("Test Pass Mark", "0.6");
        add("Test Borderline Pass Mark", "0.5");
        add("File Location", ReportInformationCollector.DEFAULT_FILE_LOCATION);
        add("Resizable Window", "false");
        add("Window Width", "322");
        add("Window Height", "315");
        add("Open Document on Creation", "true");

    }

    public static SettingsConstant getInstance(){
        if(settingsConstant == null) settingsConstant = new SettingsConstant("Settings Constant");
        return settingsConstant;
    }
}
