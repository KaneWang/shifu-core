package ml.shifu.core.di.builtin.transform;

import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldBasics;
import ml.shifu.core.util.DefaultDataChecker;

import java.util.ArrayList;
import java.util.List;


public class DefaultDataCorrector {


    public List<Object> transform(List<Field> fields, List<Object> row) {
        int size = fields.size();

        List<Object> transformed = new ArrayList<Object>();
        for (int i = 0; i < size; i++) {
            Object o = transform(fields.get(i), row.get(i));
            if (o == null) {
                return null;
            }
            transformed.add(o);
        }

        return transformed;
    }

    public Object transform(Field field, Object raw) {

        if (field.getFieldBasics().getOpType().equals(FieldBasics.OpType.CONTINUOUS)) {
            if (DefaultDataChecker.isMissingNumber(raw)) {
                return getMissingReplacement(field, raw);
            }
        } else {
            if (DefaultDataChecker.isMissing(raw)) {
                return getMissingReplacement(field, raw);
            }
        }
        return raw;
    }



    private Object getMissingReplacement(Field field, Object raw) {
        String treatment = field.getFieldControl().getMapMissingTo();

        if (treatment == null) {
            return raw;
        }

        if (treatment.equalsIgnoreCase("$mean")) {
            return field.getFieldStats().getContinuousStats().getMean();
        } else if (treatment.equalsIgnoreCase("$modal")) {
            return field.getFieldStats().getDiscreteStats().getModalValue();
        } else if (treatment.equalsIgnoreCase("$skip")) {
            return null;
        } else {
            throw new RuntimeException("Missing treatment not supported: " + treatment);
        }
    }

    private Object getInvalidReplacement(Field field, Object raw) {
        String treatment = field.getFieldControl().getMapInvalidTo();

        if (treatment == null) {
            return raw;
        }

        if (treatment.equalsIgnoreCase("$mean")) {
            return field.getFieldStats().getContinuousStats().getMean();
        } else if (treatment.equalsIgnoreCase("$modal")) {
            return field.getFieldStats().getDiscreteStats().getModalValue();
        } else if (treatment.equalsIgnoreCase("$skip")) {
            return null;
        } else {
            throw new RuntimeException("Missing treatment not supported: " + treatment);
        }

    }
}
