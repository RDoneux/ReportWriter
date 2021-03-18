package backend;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CommentGenerator {

    private String instructorName;
    private String courseTitle;
    private String courseDate;
    private String overallOutcome;
    private Integer grade;

    private ArrayList<String> openingStatements;
    private MultipleEntityMap<Integer, String> theoryAssessmentStatements;
    private ArrayList<String> physicalInterventionStatements;
    private MultipleEntityMap<String, String> portfolioStatements;
    private MultipleEntityMap<String, String> closingStatements;

    public CommentGenerator() {
        read();
    }

    private void read() {
        Scanner scan = new Scanner(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("responses/OpeningStatements.txt")));
        openingStatements = new ArrayList<>();
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.startsWith("##")) continue;
            openingStatements.add(line);
        }
        scan = new Scanner(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("responses/TheoryAssessmentStatements.txt")));
        theoryAssessmentStatements = new MultipleEntityMap<>();
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.startsWith("##")) continue;
            String[] splitLine = line.split("~");
            theoryAssessmentStatements.add(Integer.parseInt(splitLine[0]), splitLine[1]);
        }
        scan = new Scanner(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("responses/Audit-basedInterventionStatements.txt")));
        physicalInterventionStatements = new ArrayList<>();
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.startsWith("##")) continue;
            physicalInterventionStatements.add(line);
        }
        scan = new Scanner(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("responses/PortfolioStatements.txt")));
        portfolioStatements = new MultipleEntityMap<>();
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.startsWith("##")) continue;
            String[] splitLine = line.split("~");
            portfolioStatements.add(splitLine[0], splitLine[1]);
        }
        scan = new Scanner(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("responses/ClosingStatements.txt")));
        closingStatements = new MultipleEntityMap<>();
        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.startsWith("##")) continue;
            String[] splitLine = line.split("~");
            closingStatements.add(splitLine[0], splitLine[1]);
        }

    }

    public String generateComments(String _overallOutcome, String _name, String _courseTitle, String _courseDate, Integer _grade, Boolean _portfolio) {

        this.instructorName = _name;
        this.courseTitle = _courseTitle;
        this.courseDate = _courseDate;
        this.overallOutcome = _overallOutcome;
        this.grade = _grade;

        Random r = new Random();

        return formatter(openingStatements.get(r.nextInt(openingStatements.size()))) +
                formatter(theoryAssessmentStatements.getRandom(getAppropriateTheoryAssessmentScore(grade))) +
                formatter(physicalInterventionStatements.get(r.nextInt(physicalInterventionStatements.size()))) +
                System.getProperty("line.separator") +
                formatter(portfolioStatements.getRandom((_portfolio) ? "Pass" : "Refer")) +
                System.getProperty("line.separator") +
                formatter(closingStatements.getRandom(overallOutcome));
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
                    if (grade > 24) segment = " pass";
                    if (grade >= 30) segment = " good";
                    if (grade >= 35) segment = "n excellent";
                    if (grade >= 38) segment = " near perfect";
                    if (grade == 40) segment = " perfect";
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

    private Integer getAppropriateTheoryAssessmentScore(int score) {
        if (score < 0 || score > 40) throw new IllegalArgumentException("score is outside of bounds");
        if (score == 40) return 40;
        if (score > 24) return 24;
        if (score >= 20) return 20;
        return 0;
    }
}
