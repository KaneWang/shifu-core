package ml.shifu.core.di.builtin.fieldTypeSetter;


import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldBasics;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.di.spi.FieldTypeSetter;
import ml.shifu.core.util.FieldSelector;
import ml.shifu.core.util.JSONUtils;
import ml.shifu.core.util.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class BySelectorsFieldTypeSetter implements FieldTypeSetter {

    private static Logger log = LoggerFactory.getLogger(BySelectorsFieldTypeSetter.class);

    public void set(FieldMeta fieldMeta, Params params){

        log.info("Task Starts: FieldTypeSetter => NamePatternFieldTypeSetter");

        //Map<String, Object> patterns = (Map<String, Object>)params.get("patterns");

        List<String> categoricalSelectors = (List<String>)params.get("CATEGORICAL");
        List<String> continuousSelectors = (List<String>)params.get("CONTINUOUS");
        List<String> ordinalSelectors = (List<String>)params.get("ORDINAL");

        FieldSelector fieldSelector = new FieldSelector();

        for (Field field : fieldSelector.select(fieldMeta, categoricalSelectors)) {
            field.getFieldBasics().setOpType(FieldBasics.OpType.CATEGORICAL);
        }

        for (Field field : fieldSelector.select(fieldMeta, continuousSelectors)) {
            field.getFieldBasics().setOpType(FieldBasics.OpType.CONTINUOUS);
        }

        for (Field field : fieldSelector        .select(fieldMeta,  ordinalSelectors)) {
            field.getFieldBasics().setOpType(FieldBasics.OpType.ORDINAL);
        }
/*
        Params defaultParams = null;
        FieldBasics.OpType defaultOpType = null;
        FieldBasics.DataType defaultDataType = null;

        if (patterns.containsKey("$default")) {
            try {
                defaultParams = JSONUtils.reparse(patterns.get("$default"), Params.class);
                log.info("    Default Settings Found:");
                defaultOpType = FieldBasics.OpType.valueOf(defaultParams.get("opType").toString().toUpperCase());
                log.info("        - Default OpType: " + defaultOpType);
                defaultDataType = FieldBasics.DataType.valueOf(defaultParams.get("dataType").toString().toUpperCase());
                log.info("        - Default DataType: " + defaultDataType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        for (Field field : fieldMeta.getFields()) {
            FieldBasics fieldBasics = field.getFieldBasics();
            String fieldName = field.getFieldBasics().getName();

            // Check exact match
            if (patterns.containsKey(fieldName)) {
                log.info("    Exact Match Found: " + fieldName);
                Params p;
                try {
                    p = JSONUtils.reparse(patterns.get(fieldName), Params.class);

                    fieldBasics.setOpType(FieldBasics.OpType.valueOf(p.get("opType").toString().toUpperCase()));
                    log.info("        - Set OpType: " + fieldBasics.getOpType());

                    fieldBasics.setDataType(FieldBasics.DataType.valueOf(p.get("dataType").toString().toUpperCase()));
                    log.info("        - Set DataType: " + fieldBasics.getDataType());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                // Add more rules here

                // Default
                log.info("    Using Default: " + fieldName);
                if (defaultParams != null) {
                    fieldBasics.setOpType(defaultOpType);
                    fieldBasics.setDataType(defaultDataType);
                }
            }

        }

        log.info("Task Finished: FieldTypeSetter => NamePatternFieldTypeSetter");
*/


    }

}
