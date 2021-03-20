package backend.constant;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class AbstractConstant {

    protected static Map<String, String> constants;

    private static String filePath;
    protected static String fileName;

    protected AbstractConstant(String fileName) {
        filePath = System.getProperty("user.dir") + "\\constants\\";
        this.fileName = fileName;

        constants = new HashMap<>();
    }

    public static boolean read() {
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

    public static void write() {
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

    public static void add(String key, String constant) {
        constants.put(key, constant);
    }

    public static String get(String key){
        return constants.get(key);
    }

    public static void save(){
        write();
    }

    public static void load(){
        if(!read()) write();
    }

}
