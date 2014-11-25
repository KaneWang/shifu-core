package ml.shifu.core.util;

import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LocalHeaderLoader {

    private static Logger log = LoggerFactory.getLogger(LocalHeaderLoader.class);


    public static List<String> load(String filePath, String delimiter) {
        Scanner scanner = null;
        List<String> header = new ArrayList<String>();
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(filePath)));


            String line = scanner.nextLine();


            for (String s : Splitter.on(delimiter).split(line)) {
                header.add(s);
            }


        } catch (Exception e) {
            log.error(e.toString());

            throw new RuntimeException("Cannot load file");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return header;
    }


}
