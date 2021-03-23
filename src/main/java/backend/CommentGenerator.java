package backend;

import backend.constant.SettingsConstant;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;

public class CommentGenerator {

    private static final String TEST_TOTAL_STRING = "Test Total";
    private static final String TEST_PASS_MARK_STRING = "Test Pass Mark";

    private String instructorName;
    private String courseTitle;
    private String courseDate;
    private String overallOutcome;
    private Integer grade;
    private ReportComponents reportComponents;

    private MultipleEntityMap<String, String> openingStatements;
    private MultipleEntityMap<String, String> theoryAssessmentStatements;
    private MultipleEntityMap<String, String> physicalInterventionStatements;
    private MultipleEntityMap<String, String> portfolioStatements;
    private MultipleEntityMap<String, String> closingStatements;

    public CommentGenerator() {
        read();
    }

    private void read() {

        loadDefaultStatements();
        try {
            loadAdditionalStatementsIfCreated();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadDefaultStatements() {
        Scanner scan = new Scanner(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(
                "responses/OpeningStatements.txt")));
        openingStatements = new MultipleEntityMap<>();
        String defaultFileLocation = "responses";
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.startsWith("##")) continue;
            String[] splitLine = line.split("~");
            openingStatements.add(splitLine[0], splitLine[1]);
        }
        scan.close();
        scan = new Scanner(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(defaultFileLocation +
                "/TheoryAssessmentStatements.txt")));
        theoryAssessmentStatements = new MultipleEntityMap<>();
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.startsWith("##")) continue;
            String[] splitLine = line.split("~");
            theoryAssessmentStatements.add(splitLine[0], splitLine[1]);
        }
        scan.close();
        scan = new Scanner(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(defaultFileLocation +
                "/Audit-basedInterventionStatements.txt")));
        physicalInterventionStatements = new MultipleEntityMap<>();
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.startsWith("##")) continue;
            String[] splitLine = line.split("~");
            physicalInterventionStatements.add(splitLine[0], splitLine[1]);
        }
        scan.close();
        scan = new Scanner(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(defaultFileLocation +
                "/PortfolioStatements.txt")));
        portfolioStatements = new MultipleEntityMap<>();
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.startsWith("##")) continue;
            String[] splitLine = line.split("~");
            portfolioStatements.add(splitLine[0], splitLine[1]);
        }
        scan.close();
        scan = new Scanner(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(defaultFileLocation +
                "/ClosingStatements.txt")));
        closingStatements = new MultipleEntityMap<>();
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.startsWith("##")) continue;
            String[] splitLine = line.split("~");
            closingStatements.add(splitLine[0], splitLine[1]);
        }
        scan.close();
    }

    private void loadAdditionalStatementsIfCreated() throws FileNotFoundException {
        String userDir = "user.dir";
        File file = new File(System.getProperty(userDir) + "\\AC\\OS.txt");
        Scanner scan;
        if (file.exists()) {
            scan = new Scanner(file);
            while (scan.hasNext()) {
                String line = scan.nextLine();
                if (line.startsWith("##")) continue;
                String[] splitLine = line.split("~");
                openingStatements.add(splitLine[0], splitLine[1]);
            }
        }
        file = new File(System.getProperty(userDir) + "\\AC\\TA.txt");
        if (file.exists()) {
            scan = new Scanner(file);
            while (scan.hasNext()) {
                String line = scan.nextLine();
                if (line.startsWith("##")) continue;
                String[] splitLine = line.split("~");
                theoryAssessmentStatements.add(splitLine[0], splitLine[1]);
            }
        }
        file = new File(System.getProperty(userDir) + "\\AC\\ABI.txt");
        if (file.exists()) {
            scan = new Scanner(file);
            while (scan.hasNext()) {
                String line = scan.nextLine();
                if (line.startsWith("##")) continue;
                String[] splitLine = line.split("~");
                physicalInterventionStatements.add(splitLine[0], splitLine[1]);
            }
        }
        file = new File(System.getProperty(userDir) + "\\AC\\PS.txt");
        if (file.exists()) {
            scan = new Scanner(file);
            while (scan.hasNext()) {
                String line = scan.nextLine();
                if (line.startsWith("##")) continue;
                String[] splitLine = line.split("~");
                portfolioStatements.add(splitLine[0], splitLine[1]);
            }
        }
        file = new File(System.getProperty(userDir) + "\\AC\\CS.txt");
        if (file.exists()) {
            scan = new Scanner(file);
            while (scan.hasNext()) {
                String line = scan.nextLine();
                if (line.startsWith("##")) continue;
                String[] splitLine = line.split("~");
                closingStatements.add(splitLine[0], splitLine[1]);
            }
        }
    }

    public String generateComments(String overallOutcome, String physicalInterventionOutcome, String name, String courseTitle, String courseDate,
                                   String grade, Boolean portfolio, ReportComponents reportComponents) {

        this.instructorName = name;
        this.courseTitle = courseTitle;
        this.courseDate = courseDate;
        this.overallOutcome = overallOutcome;
        this.reportComponents = reportComponents;
        if (!grade.isEmpty()) this.grade = Integer.parseInt(grade);

        StringBuilder sb = new StringBuilder();

        sb.append(formatter(openingStatements.getRandom("OS") + " "));
        if (Boolean.TRUE.equals(reportComponents.getTheoryAssessment()))
            sb.append(formatter(theoryAssessmentStatements.getRandom(getAppropriateTheoryAssessmentScore(this.grade)) + " "
            ));
        if (Boolean.TRUE.equals(reportComponents.getAuditBasedInterventions()))
            sb.append(formatter(physicalInterventionStatements.getRandom(physicalInterventionOutcome))).append(" ");
        sb.append(System.getProperty("line.separator"));
        if (Boolean.TRUE.equals(reportComponents.getPortfolio())) {
            sb.append(formatter(portfolioStatements.getRandom((Boolean.TRUE.equals(portfolio)) ? "Pass" : "Refer") + " "));
            sb.append(System.getProperty("line.separator"));
        }
        sb.append(formatter(closingStatements.getRandom(this.overallOutcome)));

        return sb.toString();

    }

    private String formatter(String input) {
        String[] segments = input.split("%");
        StringBuilder sb = new StringBuilder();
        for (String segment : segments) {
            switch (segment) {
                case "instructor_name":
                    segment = " " + instructorName.split(" ")[0];
                    break;
                case "course_title":
                    segment = " " + courseTitle;
                    break;
                case "conditional_overall":
                    segment = (overallOutcome.equals("Pass") ? "" : " overall");
                    break;
                case "optional_score":
                    int testTotal = Integer.parseInt(SettingsConstant.get(TEST_TOTAL_STRING));
                    if (grade > (Double.parseDouble(SettingsConstant.get(TEST_PASS_MARK_STRING)) * testTotal))
                        segment = " pass";
                    if (grade >= 0.8 * testTotal) segment = " good";
                    if (grade >= 0.85 * testTotal) segment = "n excellent";
                    if (grade >= 0.95 * testTotal) segment = " near perfect";
                    if (grade == (Double.parseDouble(SettingsConstant.get(TEST_PASS_MARK_STRING))))
                        segment = " perfect";
                    break;
                case "deadline_date":
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate courseDateAsDate = LocalDate.parse(courseDate, formatter);
                    segment = " " + courseDateAsDate.plusMonths(1).format(formatter);
                    break;
                case "optional_also":
                    segment = (Boolean.TRUE.equals(reportComponents.getTheoryAssessment())) ? " also " : "";
                    break;
                case "optional_time_frame":
                    segment = "on your next PROACT-SCIPr-UK® course";
                    if (courseTitle.toLowerCase().contains("re-certification") || courseTitle.equalsIgnoreCase("assessment day"))
                        segment = "in a year's time";
                    break;
                default:
                    break;
            }
            if (!segment.isEmpty()) sb.append(segment);
        }
        return sb.toString();
    }

    private String getAppropriateTheoryAssessmentScore(int score) {
        if (score < 0 || score > Integer.parseInt(SettingsConstant.get(TEST_TOTAL_STRING)))
            throw new IllegalArgumentException("score is outside of bounds");
        if (score == Integer.parseInt(SettingsConstant.get(TEST_TOTAL_STRING))) return "Perfect";
        if (score > Double.parseDouble(SettingsConstant.get(TEST_PASS_MARK_STRING)) * Integer.parseInt(SettingsConstant.get(TEST_TOTAL_STRING)))
            return "Pass";
        if (score >= Double.parseDouble(SettingsConstant.get("Test Borderline Pass Mark")) * Integer.parseInt(SettingsConstant.get(TEST_TOTAL_STRING)))
            return "Borderline";
        return "Refer";
    }
}