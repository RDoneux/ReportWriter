import backend.constant.SettingsConstant;
import frontend.ApplicationWindow;

public class Client {

    public Client() {
        //EMPTY
    }

    public static void main(String[] args) {
        // load settings
        SettingsConstant.getInstance().load();

        ApplicationWindow.getInstance().display();
    }

}
