package backend;

import backend.constant.SettingsConstant;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class ReportManager {

    public ReportManager() {
        //EMPTY
    }

    public boolean generateReport(ReportDetails reportDetails) {
        try {
            XWPFDocument doc =
                    new XWPFDocument(OPCPackage.open(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("documents/General - Record Sheet V1.docx"))));

            XWPFTable details = doc.getTableArray(0);
            completeDetails(details, reportDetails.getParticipantName(), reportDetails.getOrganisation(),
                    reportDetails.getCourseAttended(), reportDetails.getCourseDate());

            XWPFTable assessmentOutcome = doc.getTableArray(1);
            completeAssessmentOutcome(assessmentOutcome, reportDetails.getAuditBasedInterventions(),
                    reportDetails.getPresentation(), reportDetails.getPortfolio(),
                    reportDetails.getTheoryAssessment(), reportDetails.getReportComponents());

            XWPFTable comments = doc.getTableArray(2);
            completeComments(comments, assessmentOutcome, details, reportDetails.getCoTrainer(),
                    reportDetails.getReportComponents());

            return save(doc, reportDetails.getSaveLocation());
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean save(XWPFDocument document, String fileLocation) throws IOException {
        try {
            FileOutputStream out = new FileOutputStream(fileLocation);
            document.write(out);
            out.close();
            document.close();
        } catch (FileNotFoundException e){
            return false;
        }
        return true;
    }

    private void completeDetails(XWPFTable target, String inputName, String inputOrganisation, String inputCourseAttended,
                                 String inputCourseDate) {

        XWPFTableCell name = target.getRow(0).getCell(1);
        XWPFTableCell organisation = target.getRow(1).getCell(1);
        XWPFTableCell courseAttended = target.getRow(2).getCell(1);
        XWPFTableCell courseDate = target.getRow(3).getCell(1);

        name.setText(inputName);
        organisation.setText(inputOrganisation);
        courseAttended.setText(inputCourseAttended);
        courseDate.setText(inputCourseDate);

    }

    private void completeAssessmentOutcome(XWPFTable target, boolean inputPi, boolean inputPresentation, boolean inputPortfolio,
                                           int inputTheoryAssessment, ReportComponents inputReportContents) {

        XWPFTableCell pi = target.getRow(1).getCell(1);
        XWPFTableCell presentation = target.getRow(2).getCell(1);
        XWPFTableCell portfolio = target.getRow(3).getCell(1);
        XWPFTableCell theoryAssessmentScore = target.getRow(4).getCell(1);
        XWPFTableCell theoryAssessmentOutcome = target.getRow(4).getCell(2);
        XWPFTableCell overallOutcome = target.getRow(5).getCell(1);

        int theoryAssessmentTotal = Integer.parseInt(SettingsConstant.get("Test Total"));
        double theoryAssessmentPassMark = Double.parseDouble(SettingsConstant.get("Test Pass Mark"));
        double theoryAssessmentBorderlinePassMark = Double.parseDouble(SettingsConstant.get("Test Borderline Pass Mark"));

        pi.setText(Boolean.TRUE.equals((inputReportContents.getAuditBasedInterventions())) ? (inputPi) ? "Pass" : "Refer" : "N/A");
        presentation.setText(Boolean.TRUE.equals((inputReportContents.getPresentation())) ? (inputPresentation) ? "Pass" : "Refer" : "N/A");
        portfolio.setText(Boolean.TRUE.equals((inputReportContents.getPortfolio())) ? (inputPortfolio) ? "Pass" : "Refer" : "N/A");
        theoryAssessmentScore.setText(Boolean.TRUE.equals((inputReportContents.getTheoryAssessment())) ? inputTheoryAssessment + " / 40" : "");
        if (Boolean.TRUE.equals(inputReportContents.getTheoryAssessment())) {
            if (inputTheoryAssessment > (theoryAssessmentPassMark * theoryAssessmentTotal)) theoryAssessmentOutcome.setText("Pass");
            else if (inputTheoryAssessment > (theoryAssessmentBorderlinePassMark * theoryAssessmentTotal)) theoryAssessmentOutcome.setText("Borderline Pass");
            else if (inputTheoryAssessment < (theoryAssessmentBorderlinePassMark * theoryAssessmentTotal)) theoryAssessmentOutcome.setText("Refer");
        } else {
            theoryAssessmentOutcome.setText("N/A");
        }
        // calculate overall outcome
        boolean overallOutcomeBoolean = true;
        if (Boolean.TRUE.equals(inputReportContents.getAuditBasedInterventions()) && Boolean.FALSE.equals(inputPi)) overallOutcomeBoolean = false;
        if (Boolean.TRUE.equals(inputReportContents.getPresentation()) && Boolean.FALSE.equals(inputPresentation)) overallOutcomeBoolean = false;
        if (Boolean.TRUE.equals(inputReportContents.getTheoryAssessment()) && Boolean.FALSE.equals((inputTheoryAssessment > (theoryAssessmentBorderlinePassMark * theoryAssessmentTotal))))
            overallOutcomeBoolean = false;

        overallOutcome.setText((overallOutcomeBoolean) ? "Pass" : "Refer");

    }

    private void completeComments(XWPFTable target, XWPFTable inputAssessmentOutcome, XWPFTable inputDetails,
                                  String coTrainer, ReportComponents reportComponents) throws IOException,
            InvalidFormatException {

        String overallOutcome = inputAssessmentOutcome.getRow(5).getCell(1).getText();
        String auditBasedInterventionOutcome = inputAssessmentOutcome.getRow(1).getCell(1).getText();
        String instructorName = inputDetails.getRow(0).getCell(1).getText();
        String courseTitle = inputDetails.getRow(2).getCell(1).getText();
        String courseDate = inputDetails.getRow(3).getCell(1).getText();
        String theoryAssessmentScore =
                inputAssessmentOutcome.getRow(4).getCell(1).getText().split(" / ")[0];
        Boolean portfolioOutcome = (inputAssessmentOutcome.getRow(3).getCell(1)).getText().equals("Pass");

        String[] paragraphs = new CommentGenerator().generateComments(overallOutcome, auditBasedInterventionOutcome, instructorName, courseTitle,
                courseDate, theoryAssessmentScore, portfolioOutcome, reportComponents).split("\\r?\\n");
        for (String paragraph : paragraphs) {
            XWPFRun run = target.getRow(1).getCell(0).addParagraph().createRun();
            run.setText(paragraph);
            run.addCarriageReturn();
        }

        generateSignature(target, 0, SettingsConstant.get("Lead Instructor"));
        generateSignature(target, 1, coTrainer);

        XWPFParagraph signatureParagraph = target.getRow(3).getCell(0).getParagraphArray(0);
        XWPFRun signatureRun = signatureParagraph.createRun();
        signatureParagraph.setAlignment(ParagraphAlignment.CENTER);
        if(SettingsConstant.get("Lead Instructor").equals("Rob Doneux")) {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("signatures/RobDoneuxSignature.png");
            signatureRun.addPicture(is, XWPFDocument.PICTURE_TYPE_PNG, "RobDoneuxSignature.png", Units.toEMU(125),
                    Units.toEMU(42));
            assert is != null;
            is.close();
        } else {
            signatureRun.setFontFamily("French Script MT");
            signatureRun.setFontSize(26);
            signatureRun.setText(SettingsConstant.get("Lead Instructor"));
        }

    }

    private void generateSignature(XWPFTable target, int cell, String name) {
        XWPFParagraph paragraph = target.getRow(2).getCell(cell).getParagraphArray(0);
        XWPFRun run = paragraph.createRun();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run.setFontSize(11);
        run.setFontFamily("Arial");
        run.setBold(true);
        run.setItalic(true);
        run.setText(name + "\n");

        paragraph = target.getRow(2).getCell(cell).addParagraph();
        run = paragraph.createRun();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run.setFontSize(11);
        run.setFontFamily("Arial");
        run.setBold(true);
        run.setItalic(true);
        run.setText("Loddon Training & Consultancy" + "\n");
        run.addCarriageReturn();

        paragraph = target.getRow(2).getCell(cell).addParagraph();
        run = paragraph.createRun();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run.setFontSize(10);
        run.setFontFamily("Arial");
        run.setText("PROACT-SCIPr-UK® Principal Instructor with External Registration");
    }
}
