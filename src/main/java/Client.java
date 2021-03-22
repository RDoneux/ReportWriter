import backend.constant.SettingsConstant;
import frontend.ApplicationWindow;

public class Client {

    public static void main(String[] args) {
        SettingsConstant.getInstance().load(); // load settings
        ApplicationWindow.getInstance().display();
    }

}
