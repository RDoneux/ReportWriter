package backend;

import backend.constant.SettingsConstant;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class CommentGenerator {

    private String instructorName;
    private String courseTitle;
    private String courseDate;
    private String overallOutcome;
    private Integer grade;

    private ArrayList<String> openingStatements;
    private MultipleEntityMap<String, String> theoryAssessmentStatements;
    private ArrayList<String> physicalInterventionStatements;
    private MultipleEntityMap<String, String> portfolioStatements;
    private MultipleEntityMap<String, String> closingStatements;

    public CommentGenerator() {
        read();
    }

    private void read() {
        Scanner scan = new Scanner(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream(
                "responses/OpeningStatements.txt")));
        openingStatements = new ArrayList<>();
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.startsWith("##")) continue;
            openingStatements.add(line);
        }
        scan = new Scanner(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("responses" +
                "/TheoryAssessmentStatements.txt")));
        theoryAssessmentStatements = new MultipleEntityMap<>();
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.startsWith("##")) continue;
            String[] splitLine = line.split("~");
            theoryAssessmentStatements.add(splitLine[0], splitLine[1]);
        }
        scan = new Scanner(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("responses" +
                "/Audit-basedInterventionStatements.txt")));
        physicalInterventionStatements = new ArrayList<>();
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.startsWith("##")) continue;
            physicalInterventionStatements.add(line);
        }
        scan = new Scanner(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("responses" +
                "/PortfolioStatements.txt")));
        portfolioStatements = new MultipleEntityMap<>();
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.startsWith("##")) continue;
            String[] splitLine = line.split("~");
            portfolioStatements.add(splitLine[0], splitLine[1]);
        }
        scan = new Scanner(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("responses" +
                "/ClosingStatements.txt")));
        closingStatements = new MultipleEntityMap<>();
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.startsWith("##")) continue;
            String[] splitLine = line.split("~");
            closingStatements.add(splitLine[0], splitLine[1]);
        }

    }

    public String generateComments(String _overallOutcome, String _name, String _courseTitle, String _courseDate,
                                   Integer _grade, Boolean _portfolio, ReportComponents reportComponents) {

        this.instructorName = _name;
        this.courseTitle = _courseTitle;
        this.courseDate = _courseDate;
        this.overallOutcome = _overallOutcome;
        this.grade = _grade;

        Random r = new Random();

        StringBuilder sb = new StringBuilder();

        sb.append(formatter(openingStatements.get(r.nextInt(openingStatements.size())) + " "));
        if (reportComponents.getTheoryAssessment())
            sb.append(formatter(theoryAssessmentStatements.getRandom(getAppropriateTheoryAssessmentScore(grade)) + " "
            ));
        if (reportComponents.getAuditBasedInterventions())
            sb.append(formatter(physicalInterventionStatements.get(r.nextInt(physicalInterventionStatements.size())) + " "));
        sb.append(System.getProperty("line.separator"));
        if (reportComponents.getPortfolio()) {
            sb.append(formatter(portfolioStatements.getRandom((_portfolio) ? "Pass" : "Refer") + " "));
            sb.append(System.getProperty("line.separator"));
        }
        sb.append(formatter(closingStatements.getRandom(overallOutcome)));

        return sb.toString();

    }

    private String formatter(String _input) {
        String[] segments = _input.split("%");
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
                    int testTotal = Integer.parseInt(SettingsConstant.get("Test Total"));
                    if (grade > (Double.parseDouble(SettingsConstant.get("Test Pass Mark")) * testTotal)) segment = " pass";
                    if (grade >= 0.8 * testTotal) segment = " good";
                    if (grade >= 0.85 * testTotal) segment = "n excellent";
                    if (grade >= 0.95 * testTotal) segment = " near perfect";
                    if (grade == (Double.parseDouble(SettingsConstant.get("Test Pass Mark")))) segment = " perfect";
                    break;
                case "deadline_date":
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate courseDateAsDate = LocalDate.parse(courseDate, formatter);
                    segment = " " + courseDateAsDate.plusMonths(1).format(formatter);
                    break;
                default:
                    break;
            }
            if (!segment.isEmpty()) sb.append(segment);
        }
        return sb.toString();
    }

    private String getAppropriateTheoryAssessmentScore(int score) {
        if (score < 0 || score > Integer.parseInt(SettingsConstant.get("Test Total")))
            throw new IllegalArgumentException("score is outside of bounds");
        if (score == Integer.parseInt(SettingsConstant.get("Test Total"))) return "Perfect";
        if (score > Double.parseDouble(SettingsConstant.get("Test Pass Mark")) * Integer.parseInt(SettingsConstant.get("Test Total"))) return "Pass";
        System.out.println((SettingsConstant.get("Test Borderline Pass Mark")) + " : "+ (SettingsConstant.get("Test Total")));
        if (score >= Double.parseDouble(SettingsConstant.get("Test Borderline Pass Mark")) * Integer.parseInt(SettingsConstant.get("Test Total"))) return "Borderline";
        return "Refer";
    }
}
