package ml.shifu.core.di.builtin.statsCalculator;

import ml.shifu.core.container.RawValueObject;
import ml.shifu.core.container.fieldMeta.Counts;
import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldStats;
import ml.shifu.core.util.Params;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultCountsCalculator {

    public static void calculate(Field field, List<?> values, Params params) {
        FieldStats fieldStats = field.getFieldStats();
        Counts counts = new Counts();

        double totalFreq = 0;
        double missingFreq = 0;
        double invalidFreq = 0;

        Set<Object> uniqueValues = new HashSet<Object>();

        for (Object value : values) {
            if (value == null) {
                missingFreq += 1.0;
            } else if (value.toString().equals("NaN")) {
                invalidFreq += 1.0;
            }

            totalFreq += 1.0;

            uniqueValues.add(value);
        }

        counts.setCardinality(uniqueValues.size());
        counts.setInvalidFreq(invalidFreq);
        counts.setMissingFreq(missingFreq);
        counts.setTotalFreq(totalFreq);

        fieldStats.setCounts(counts);
    }
}
