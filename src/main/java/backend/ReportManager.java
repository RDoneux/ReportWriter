package backend;

import backend.constant.SettingsConstant;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ReportManager {

    public ReportManager() {
        //EMPTY
    }

    public void generateReport(ReportDetails reportDetails) {
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

            dump(doc);
            save(doc, reportDetails.getSaveLocation());
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    private void save(XWPFDocument document, String fileLocation) throws IOException {
        FileOutputStream out = new FileOutputStream(fileLocation);
        document.write(out);
        out.close();
        document.close();
    }

    private void completeDetails(XWPFTable _target, String _name, String _organisation, String _courseAttended,
                                 String _courseDate) {

        XWPFTableCell name = _target.getRow(0).getCell(1);
        XWPFTableCell organisation = _target.getRow(1).getCell(1);
        XWPFTableCell courseAttended = _target.getRow(2).getCell(1);
        XWPFTableCell courseDate = _target.getRow(3).getCell(1);

        name.setText(_name);
        organisation.setText(_organisation);
        courseAttended.setText(_courseAttended);
        courseDate.setText(_courseDate);

    }

    private void completeAssessmentOutcome(XWPFTable _target, boolean _pi, boolean _presentation, boolean _portfolio,
                                           int _theoryAssessment, ReportComponents _reportComponents) {

        XWPFTableCell pi = _target.getRow(1).getCell(1);
        XWPFTableCell presentation = _target.getRow(2).getCell(1);
        XWPFTableCell portfolio = _target.getRow(3).getCell(1);
        XWPFTableCell theoryAssessmentScore = _target.getRow(4).getCell(1);
        XWPFTableCell theoryAssessmentOutcome = _target.getRow(4).getCell(2);
        XWPFTableCell overallOutcome = _target.getRow(5).getCell(1);

        int theoryAssessmentTotal = Integer.parseInt(SettingsConstant.get("Test Total"));
        double theoryAssessmentPassMark = Double.parseDouble(SettingsConstant.get("Test Pass Mark"));
        double theoryAssessmentBorderlinePassMark = Double.parseDouble(SettingsConstant.get("Test Borderline Pass Mark"));

        pi.setText((_reportComponents.getAuditBasedInterventions()) ? (_pi) ? "Pass" : "Refer" : "N/A");
        presentation.setText((_reportComponents.getPresentation()) ? (_presentation) ? "Pass" : "Refer" : "N/A");
        portfolio.setText((_reportComponents.getPortfolio()) ? (_portfolio) ? "Pass" : "Refer" : "N/A");
        theoryAssessmentScore.setText((_reportComponents.getTheoryAssessment()) ? _theoryAssessment + " / 40" : "N/A");
        if (_reportComponents.getTheoryAssessment()) {
            if (_theoryAssessment > (theoryAssessmentPassMark * theoryAssessmentTotal)) theoryAssessmentOutcome.setText("Pass");
            else if (_theoryAssessment > (theoryAssessmentBorderlinePassMark * theoryAssessmentTotal)) theoryAssessmentOutcome.setText("Borderline Pass");
            else if (_theoryAssessment < (theoryAssessmentBorderlinePassMark * theoryAssessmentTotal)) theoryAssessmentOutcome.setText("Refer");
        } else {
            theoryAssessmentOutcome.setText("");
        }
        // calculate overall outcome
        boolean overallOutcomeBoolean = true;
        if (_reportComponents.getAuditBasedInterventions() && !_pi) overallOutcomeBoolean = false;
        if (_reportComponents.getPresentation() && !_presentation) overallOutcomeBoolean = false;
        if (_reportComponents.getTheoryAssessment() && !(_theoryAssessment > (theoryAssessmentBorderlinePassMark * theoryAssessmentTotal)))
            overallOutcomeBoolean = false;

        overallOutcome.setText((overallOutcomeBoolean) ? "Pass" : "Refer");

    }

    private void completeComments(XWPFTable _target, XWPFTable _assessmentOutcome, XWPFTable _details,
                                  String coTrainer, ReportComponents reportComponents) throws IOException,
            InvalidFormatException {

        String overallOutcome = _assessmentOutcome.getRow(5).getCell(1).getText();
        String instructorName = _details.getRow(0).getCell(1).getText();
        String courseTitle = _details.getRow(2).getCell(1).getText();
        String courseDate = _details.getRow(3).getCell(1).getText();
        Integer theoryAssessmentScore =
                Integer.valueOf(_assessmentOutcome.getRow(4).getCell(1).getText().split(" / ")[0]);
        Boolean portfolioOutcome = (_assessmentOutcome.getRow(3).getCell(1)).getText().equals("Pass");

        String[] paragraphs = new CommentGenerator().generateComments(overallOutcome, instructorName, courseTitle,
                courseDate, theoryAssessmentScore, portfolioOutcome, reportComponents).split("\\r?\\n");
        for (String paragraph : paragraphs) {
            XWPFRun run = _target.getRow(1).getCell(0).addParagraph().createRun();
            run.setText(paragraph);
            run.addCarriageReturn();
        }

        generateSignature(_target, 0, "Rob Doneux");
        generateSignature(_target, 1, coTrainer);

        XWPFParagraph signatureParagraph = _target.getRow(3).getCell(0).getParagraphArray(0);
        XWPFRun signatureRun = signatureParagraph.createRun();
        signatureParagraph.setAlignment(ParagraphAlignment.CENTER);
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("signatures/RobDoneuxSignature.png");
        signatureRun.addPicture(is, XWPFDocument.PICTURE_TYPE_PNG, "RobDoneuxSignature.png", Units.toEMU(125),
                Units.toEMU(42));
        assert is != null;
        is.close();

    }

    private void generateSignature(XWPFTable _target, int cell, String name) {
        XWPFParagraph paragraph = _target.getRow(2).getCell(cell).getParagraphArray(0);
        XWPFRun run = paragraph.createRun();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run.setFontSize(11);
        run.setFontFamily("Arial");
        run.setBold(true);
        run.setItalic(true);
        run.setText(name + "\n");

        paragraph = _target.getRow(2).getCell(cell).addParagraph();
        run = paragraph.createRun();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run.setFontSize(11);
        run.setFontFamily("Arial");
        run.setBold(true);
        run.setItalic(true);
        run.setText("Loddon Training & Consultancy" + "\n");
        run.addCarriageReturn();

        paragraph = _target.getRow(2).getCell(cell).addParagraph();
        run = paragraph.createRun();
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        run.setFontSize(10);
        run.setFontFamily("Arial");
        run.setText("PROACT-SCIPr-UK® Principal Instructor with External Registration");
    }

    private void dump(XWPFDocument document) {

        System.out.println("** Instructor Feedback Form **");
        System.out.println(" ");
        System.out.println("Details");
        System.out.printf("%-40s %-30s %n", "Participant Name:",
                document.getTableArray(0).getRow(0).getCell(1).getText());
        System.out.printf("%-40s %-30s %n", "Organisation:", document.getTableArray(0).getRow(1).getCell(1).getText());
        System.out.printf("%-40s %-30s %n", "Course Attended:",
                document.getTableArray(0).getRow(2).getCell(1).getText());
        System.out.printf("%-40s %-30s %n", "Course Date:", document.getTableArray(0).getRow(3).getCell(1).getText());
        System.out.println(" ");
        System.out.println("Assessment Outcome");
        System.out.printf("%-40s %-30s %n", "Audit-Based Interventions:",
                document.getTableArray(1).getRow(1).getCell(1).getText());
        System.out.printf("%-40s %-30s %n", "Presentation:", document.getTableArray(1).getRow(2).getCell(1).getText());
        System.out.printf("%-40s %-30s %n", "Portfolio:", document.getTableArray(1).getRow(3).getCell(1).getText());
        System.out.printf("%-40s %-30s %n", "Theory Assessment:",
                document.getTableArray(1).getRow(4).getCell(1).getText() + " | " + document.getTableArray(1).getRow(4).getCell(2).getText());
        System.out.printf("%-40s %-30s %n", "Overall Course Outcome:",
                document.getTableArray(1).getRow(5).getCell(1).getText());
        System.out.println(" ");
        System.out.println("Comments");
        System.out.println(document.getTableArray(2).getRow(1).getCell(0).getText());
        System.out.println(" ");
        System.out.println("Principal Instructor Details");
        System.out.printf("%-40s %-30s %n", "Instructor 1:",
                document.getTableArray(2).getRow(2).getCell(0).getText().split("\\r?\\n")[0]);
        System.out.printf("%-40s %-30s %n", "Instructor 2:",
                document.getTableArray(2).getRow(2).getCell(1).getText().split("\\r?\\n")[0]);

    }

}
