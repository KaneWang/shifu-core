package ml.shifu.core.util;

import ml.shifu.core.container.fieldMeta.FieldControl;
import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldMeta;

import java.util.List;

public class UsageTypeSetter {

    public static void set(FieldMeta fieldMeta, Params params) {
        String[] types = new String[]{"$active", "$target", "$supplementary", "$group", "$order", "$frequency_weight", "$analysis_weight"};

        FieldSelector fieldSelector = new FieldSelector();
        for (String type : types) {
            FieldControl.UsageType usageType = FieldControl.UsageType.valueOf(type.substring(1));

            List<String> selectors = (List<String>) params.get(type);
            List<Field> selectedFields = fieldSelector.select(fieldMeta, selectors);
            for (Field field : selectedFields) {
                field.getFieldControl().setUsageType(usageType);
            }
        }
    }

}
