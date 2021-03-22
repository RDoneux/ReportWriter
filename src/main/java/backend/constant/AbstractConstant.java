package backend.constant;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class AbstractConstant {

    protected static final Map<String, String> constants = new HashMap<>();

    private final String filePath = System.getProperty("user.dir") + "\\constants\\";
    protected final String fileName;

    protected AbstractConstant(String fileName) {
        this.fileName = fileName;
    }

    public boolean read() {
        if (!new File(filePath + fileName + ".txt").exists()) return false;
        constants.clear();
        try {
            Scanner scan = new Scanner(new File(filePath + fileName + ".txt"));
            while (scan.hasNext()) {
                String[] split = scan.nextLine().split(" : ");
                constants.put(split[0], split[1]);
            }
            scan.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void write() {
        try {
            File file = new File(filePath);
            if (!file.exists()) //noinspection ResultOfMethodCallIgnored
                file.mkdirs();

            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + fileName + ".txt"));
            for (String constant : constants.keySet()) {
                writer.write(constant + " : " + constants.get(constant));
                writer.newLine();
            }
            writer.close();
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(String key, String constant) {
        constants.put(key, constant);
    }

    public static String get(String key){
        return constants.get(key);
    }

    public void save(){
        write();
    }

    public void load(){
        if(!read()) write();
    }

}
