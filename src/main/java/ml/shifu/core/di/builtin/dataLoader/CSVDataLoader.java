package ml.shifu.core.di.builtin.dataLoader;

import com.google.common.base.Splitter;
import ml.shifu.core.di.spi.DataLoader;
import ml.shifu.core.di.spi.SingleThreadFileLoader;
import ml.shifu.core.util.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVDataLoader implements DataLoader {

    private static Logger log = LoggerFactory.getLogger(CSVDataLoader.class);


    public List<List<Object>> load(Params params) {
        Scanner scanner = null;

        String pathData = params.get("pathData").toString();

        String delimiter = ",";
        if (params.containsKey("delimiter")) {
            delimiter = params.get("delimiter").toString();
        }

        List<List<Object>> rows = new ArrayList<List<Object>>();
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(pathData)));

            // Discard Header
            scanner.nextLine();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line == null || line.length() == 0) {
                    continue;
                }

                List<Object> fields = new ArrayList<Object>();
                for (String s : Splitter.on(delimiter).split(line)) {
                    fields.add(s);
                }
                rows.add(fields);
            }
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException("Cannot load file");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        return rows;
    }



}
