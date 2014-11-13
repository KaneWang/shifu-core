package ml.shifu.core.di.builtin.fieldSchemaLoader;

import com.google.common.base.Splitter;
import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldBasics;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.di.spi.FieldSchemaLoader;
import ml.shifu.core.util.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVFieldSchemaLoader implements FieldSchemaLoader {

    private static Logger log = LoggerFactory.getLogger(CSVFieldSchemaLoader.class);


    public FieldMeta load(Params params) {
        log.info("Task Starts: FieldSchemaLoader => CSVFieldSchemaLoader");

        FieldMeta fieldMeta = new FieldMeta();
        List<Field> fields = new ArrayList<Field>();
        String path = params.get("pathHeader").toString();
        log.info("    - path: " + path);

        String delimiter = params.get("delimiter").toString();
        log.info("    - delimiter: " + delimiter);

        Scanner scanner = null;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(path)));

            String line = scanner.nextLine();

            int fieldNumCnt = 0;


            for (String str : Splitter.on(delimiter).split(line)) {
                Field field = new Field();
                FieldBasics fieldBasics = new FieldBasics();
                fieldBasics.setName(str);
                fieldBasics.setNum(fieldNumCnt);
                fieldNumCnt += 1;
                field.setFieldBasics(fieldBasics);
                fields.add(field);
                log.info("    Field #" + fieldBasics.getNum() + ": " + fieldBasics.getName());
            }


        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException("Cannot load file");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        fieldMeta.setFields(fields);

        log.info("Task Finished: FieldSchemaLoader => CSVFieldSchemaLoader");
        return fieldMeta;
    }
}
