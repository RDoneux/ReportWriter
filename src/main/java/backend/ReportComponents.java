package backend;

public class ReportComponents {

    private Boolean portfolio;
    private Boolean theoryAssessment;
    private Boolean auditBasedInterventions;
    private Boolean presentation;

    public ReportComponents(Boolean portfolio, Boolean theoryAssessment, Boolean auditBasedInterventions,
                            Boolean presentation) {
        this.portfolio = portfolio;
        this.theoryAssessment = theoryAssessment;
        this.auditBasedInterventions = auditBasedInterventions;
        this.presentation = presentation;
    }

    public Boolean getPortfolio() {
        return portfolio;
    }

    public Boolean getTheoryAssessment() {
        return theoryAssessment;
    }

    public Boolean getAuditBasedInterventions() {
        return auditBasedInterventions;
    }

    public Boolean getPresentation() {
        return presentation;
    }
}
