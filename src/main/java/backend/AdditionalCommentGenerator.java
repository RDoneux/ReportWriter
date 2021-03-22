package backend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AdditionalCommentGenerator {

    private BufferedWriter writer;
    private final File writeFile;

    public AdditionalCommentGenerator() {
        writeFile = new File(System.getProperty("user.dir") + "\\AC\\");
        if (!writeFile.exists()) //noinspection ResultOfMethodCallIgnored
            writeFile.mkdirs();
    }

    public void addAdditionalOpeningStatement(String comment) throws IOException {
        writer = new BufferedWriter(new FileWriter(writeFile + "\\OS.txt"));
        writer.write("OS~" + comment);
        writer.close();
    }

    public void addAdditionalTheoryAssessmentStatement(String level, String comment) throws IOException {
        writer = new BufferedWriter(new FileWriter(writeFile + "\\TA.txt"));
        writer.write(level + "~" + comment);
        writer.close();
    }

    public void addAdditionalAuditBasedInterventionAssessment(String comment) throws IOException {
        writer = new BufferedWriter(new FileWriter(writeFile + "\\ABI.txt"));
        writer.write("PI~" + comment);
        writer.close();
    }

    public void addAdditionalPortfolioStatement(String outcome, String comment) throws IOException {
        writer = new BufferedWriter(new FileWriter(writeFile + "\\PS.txt"));
        writer.write(outcome + "~" + comment);
        writer.close();
    }

    public void addAdditionalClosingStatement(String outcome, String comment) throws IOException {
        writer = new BufferedWriter(new FileWriter(writeFile + "\\CS.txt"));
        writer.write(outcome + "~" + comment);
        writer.close();
    }

}
