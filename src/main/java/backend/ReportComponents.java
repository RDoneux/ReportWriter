package backend;

public class ReportComponents {

    private final boolean portfolio;
    private final boolean theoryAssessment;
    private final boolean auditBasedInterventions;
    private final boolean presentation;

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
