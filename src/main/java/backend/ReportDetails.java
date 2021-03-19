package backend;

public class ReportDetails {

    private final String participantName;
    private final String organisation;
    private final String courseAttended;
    private final String courseDate;
    private final String coTrainer;

    private final Boolean auditBasedInterventions;
    private final Boolean presentation;
    private final Boolean portfolio;
    private final Integer theoryAssessment;

    private final String saveLocation;

    private final ReportComponents reportComponents;

    public ReportDetails(ReportDetailsBuilder builder){

        this.participantName = builder.getParticipantName();
        this.organisation = builder.getOrganisation();
        this.courseAttended = builder.getCourseAttended();
        this.courseDate = builder.getCourseDate();
        this.coTrainer = builder.getCoTrainer();
        this.auditBasedInterventions = builder.getAuditBasedInterventions();
        this.presentation = builder.getPresentation();
        this.portfolio = builder.getPortfolio();
        this.theoryAssessment = builder.getTheoryAssessment();
        this.saveLocation = builder.getSaveLocation();
        this.reportComponents = builder.getReportComponents();

    }

    public static ReportDetailsBuilder builder () {
        return new ReportDetailsBuilder();
    }

    public String getParticipantName() {
        return participantName;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getCourseAttended() {
        return courseAttended;
    }

    public String getCourseDate() {
        return courseDate;
    }

    public String getCoTrainer() {
        return coTrainer;
    }

    public Boolean getAuditBasedInterventions() {
        return auditBasedInterventions;
    }

    public Boolean getPresentation() {
        return presentation;
    }

    public Boolean getPortfolio() {
        return portfolio;
    }

    public Integer getTheoryAssessment() {
        return theoryAssessment;
    }

    public String getSaveLocation() {
        return saveLocation;
    }

    public ReportComponents getReportComponents() {
        return reportComponents;
    }
}
