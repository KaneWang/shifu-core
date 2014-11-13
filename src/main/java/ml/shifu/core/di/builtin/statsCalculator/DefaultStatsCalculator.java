package ml.shifu.core.di.builtin.statsCalculator;


import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldStats;
import ml.shifu.core.di.spi.StatsCalculator;
import ml.shifu.core.util.Params;

import java.util.List;

public class DefaultStatsCalculator implements StatsCalculator {

    public void calculate(Field field, List<?> values, Params params) {
        field.setFieldStats(new FieldStats());

        DefaultCountsCalculator.calculate(field, values, params);
        DefaultContinuousStatsCalculator.calculate(field, values, params);

    }

}
