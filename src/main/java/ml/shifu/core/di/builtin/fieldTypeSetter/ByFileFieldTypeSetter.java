package ml.shifu.core.di.builtin.fieldTypeSetter;

import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldBasics;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.di.spi.FieldTypeSetter;
import ml.shifu.core.util.JSONUtils;
import ml.shifu.core.util.Params;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ByFileFieldTypeSetter implements FieldTypeSetter {

    private static Logger log = LoggerFactory.getLogger(ByFileFieldTypeSetter.class);

    public void set(FieldMeta fieldMeta, Params params) {

        log.info("Task Starts: FieldTypeSetter => ByFileFieldTypeSetter");

        Map<String, String> fileMap = (Map<String, String>) params.get("fileMap");

        String continuous = fileMap.get("CONTINUOUS");
        String categorical = fileMap.get("CATEGORICAL");
        String ordinal = fileMap.get("ORDINAL");

        Set<String> continuousSet = null;
        Set<String> categoricalSet = null;
        Set<String> ordinalSet = null;


        FieldBasics.OpType defaultOpType = null;
        FieldBasics.DataType defaultDataType = null;

        if (continuous.equals("$default")) {
            defaultDataType = FieldBasics.DataType.NUMBER;
            defaultOpType = FieldBasics.OpType.CONTINUOUS;
            continuousSet = new HashSet<String>();
        } else {
            continuousSet = getNameSet(continuous);
        }

        if (categorical.equals("$default")) {
            defaultDataType = FieldBasics.DataType.STRING;
            defaultOpType = FieldBasics.OpType.CATEGORICAL;
            categoricalSet = new HashSet<String>();
        } else {
            categoricalSet = getNameSet(categorical);
        }

        if (ordinal.equals("$default")) {
            defaultDataType = FieldBasics.DataType.STRING;
            defaultOpType = FieldBasics.OpType.ORDINAL;
            ordinalSet = new HashSet<String>();
        } else {
            ordinalSet = getNameSet(ordinal);
        }


        for (Field field : fieldMeta.getFields()) {
            FieldBasics fieldBasics = field.getFieldBasics();
            String fieldName = field.getFieldBasics().getName();

            // Check exact match
            if (continuousSet.contains(fieldName)) {
                log.info(    "Setting Type: " + fieldName);
                fieldBasics.setOpType(FieldBasics.OpType.CONTINUOUS);
                log.info("        - Set OpType: " + fieldBasics.getOpType());

                fieldBasics.setDataType(FieldBasics.DataType.NUMBER);
                log.info("        - Set DataType: " + fieldBasics.getDataType());
            } else if (categoricalSet.contains(fieldName)) {
                log.info(    "Setting Type: " + fieldName);
                fieldBasics.setOpType(FieldBasics.OpType.CATEGORICAL);
                log.info("        - Set OpType: " + fieldBasics.getOpType());

                fieldBasics.setDataType(FieldBasics.DataType.STRING);
                log.info("        - Set DataType: " + fieldBasics.getDataType());
            } else if (ordinalSet.contains(fieldName)) {
                log.info(    "Setting Type: " + fieldName);
                fieldBasics.setOpType(FieldBasics.OpType.ORDINAL);
                log.info("        - Set OpType: " + fieldBasics.getOpType());

                fieldBasics.setDataType(FieldBasics.DataType.STRING);
                log.info("        - Set DataType: " + fieldBasics.getDataType());
            } else {
                log.info("    Using Default: " + fieldName);
                fieldBasics.setOpType(defaultOpType);
                fieldBasics.setDataType(defaultDataType);
            }

        }

        log.info("Task Finished: FieldTypeSetter => ByFileFieldTypeSetter");


    }

    private Set<String> getNameSet(String path) {
        Scanner scanner = null;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(new File(path))));
            Set<String> nameSet = new HashSet<String>();
            while (scanner.hasNext()) {
                nameSet.add(scanner.nextLine().trim());
            }

            return nameSet;
        } catch (IOException e) {
            throw new RuntimeException("Cannot read field name list file: " + path);
        }
    }

}
