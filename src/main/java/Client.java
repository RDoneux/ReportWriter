import frontend.ApplicationWindow;

public class Client {

    public Client() {

//        ReportDetails details = ReportDetails.builder()
//                .participantName("Megan Doneux")
//                .organisation("New Organisation")
//                .courseAttended("Re-certification")
//                .courseDate("18/03/2021")
//                .auditBasedInterventions(true)
//                .presentation(true)
//                .portfolio(true)
//                .theoryAssessment(24)
//                .saveLocation("C:\\Users\\R.Doneux\\OneDrive - The Loddon School\\Desktop\\TestDocument.docx")
//                .build();
//
//        new ReportManager().generateReport(details);

        new ApplicationWindow();

    }

    public static void main(String[] args) {

        new Client();

    }

}
