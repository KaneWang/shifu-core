package ml.shifu.core.di.builtin.fieldTypeSetter;


import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldBasics;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.di.spi.FieldTypeSetter;
import ml.shifu.core.util.FieldSelector;

import ml.shifu.core.util.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class BySelectorsFieldTypeSetter implements FieldTypeSetter {

    private static Logger log = LoggerFactory.getLogger(BySelectorsFieldTypeSetter.class);

    public void set(FieldMeta fieldMeta, Params params){

        log.info("Task Starts: FieldTypeSetter => BySelectorsFieldTypeSetter");

        List<String> categoricalSelectors = (List<String>)params.get("$categorical");
        List<String> continuousSelectors = (List<String>)params.get("$continuous");
        List<String> ordinalSelectors = (List<String>)params.get("$ordinal");

        FieldSelector fieldSelector = new FieldSelector();

        for (Field field : fieldSelector.select(fieldMeta, categoricalSelectors)) {
            log.info("    Set OpType to CATEGORICAL: " + field.getFieldBasics().getName());
            field.getFieldBasics().setOpType(FieldBasics.OpType.CATEGORICAL);
        }

        for (Field field : fieldSelector.select(fieldMeta, continuousSelectors)) {
            log.info("    Set OpType to CONTINUOUS: " + field.getFieldBasics().getName());
            field.getFieldBasics().setOpType(FieldBasics.OpType.CONTINUOUS);
        }

        for (Field field : fieldSelector        .select(fieldMeta,  ordinalSelectors)) {
            log.info("    Set OpType to ORDINAL: " + field.getFieldBasics().getName());
            field.getFieldBasics().setOpType(FieldBasics.OpType.ORDINAL);
        }

        log.info("Task Finished: FieldTypeSetter => BySelectorsFieldTypeSetter");


    }

}
