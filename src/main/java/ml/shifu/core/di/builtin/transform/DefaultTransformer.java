package ml.shifu.core.di.builtin.transform;


import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.di.spi.Transformer;
import ml.shifu.core.util.TransformPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultTransformer {

    private ZScoreNormalizer zScoreNormalizer = new ZScoreNormalizer();

    public List<Object> transform(List<Field> fields, List<Object> row) {
        List<Object> normalized = new ArrayList<Object>();
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            Field field = fields.get(i);

            if (field.getFieldControl().getTransformPlan() != null) {
                normalized.add(transform(field, row.get(i)));
            }
        }
        return normalized;
    }

    public List<Object> transformSelected(List<Field> fields, List<Object> row) {
        List<Object> normalized = new ArrayList<Object>();
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            Field field = fields.get(i);

            if (field.getFieldControl().getTransformPlan() != null && field.getFieldControl().getIsSelected() != null && field.getFieldControl().getIsSelected() == true) {
                normalized.add(transform(field, row.get(i)));
            }
        }
        return normalized;
    }

    public List<Object> transformTarget(List<Field> fields, List<Object> row) {
        List<Object> normalized = new ArrayList<Object>();
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            Field field = fields.get(i);

            if (field.getFieldControl().getTransformPlan() != null && field.getFieldControl().getIsTarget() != null && field.getFieldControl().getIsTarget() == true) {
                normalized.add(transform(field, row.get(i)));
            }
        }
        return normalized;
    }


    public Object transform(Field field, Object raw) {
        TransformPlan transformPlan = field.getFieldControl().getTransformPlan();
        String method = transformPlan.getMethod();
        if (method.equals("map")) {
            Map<String, String> mapTo = (Map<String, String>) transformPlan.getParams().get("mapTo");
            return mapTo.get(raw);
        } else if (method.equalsIgnoreCase("ZScore")) {
            return zScoreNormalizer.normalize(field, raw);
        } else {
            throw new RuntimeException("Method not supported: " + method);
        }

    }

}
