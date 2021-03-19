package backend;

public class ReportDetailsBuilder {

    private String participantName;
    private String organisation;
    private String courseAttended;
    private String courseDate;
    private String coTrainer;

    private Boolean auditBasedInterventions;
    private Boolean presentation;
    private Boolean portfolio;
    private Integer theoryAssessment;

    private String saveLocation;

    private ReportComponents reportComponents;

    public ReportDetails build() {
        return new ReportDetails(this);
    }

    public ReportDetailsBuilder participantName(String participantName) {
        this.participantName = participantName;
        return this;
    }

    public ReportDetailsBuilder organisation(String organisation) {
        this.organisation = organisation;
        return this;
    }

    public ReportDetailsBuilder courseAttended(String courseAttended) {
        this.courseAttended = courseAttended;
        return this;
    }

    public ReportDetailsBuilder courseDate(String courseDate) {
        this.courseDate = courseDate;
        return this;
    }

    public ReportDetailsBuilder coTrainer(String coTrainer){
        this.coTrainer = coTrainer;
        return this;
    }

    public ReportDetailsBuilder auditBasedInterventions(Boolean auditBasedInterventions) {
        this.auditBasedInterventions = auditBasedInterventions;
        return this;
    }

    public ReportDetailsBuilder presentation(Boolean presentation) {
        this.presentation = presentation;
        return this;
    }

    public ReportDetailsBuilder portfolio(Boolean portfolio) {
        this.portfolio = portfolio;
        return this;
    }

    public ReportDetailsBuilder theoryAssessment(Integer theoryAssessment) {
        this.theoryAssessment = theoryAssessment;
        return this;
    }

    public ReportDetailsBuilder saveLocation(String saveLocation){
        this.saveLocation = saveLocation;
        return this;
    }

    public ReportDetailsBuilder reportComponents (ReportComponents reportComponents){
        this.reportComponents = reportComponents;
        return this;
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
