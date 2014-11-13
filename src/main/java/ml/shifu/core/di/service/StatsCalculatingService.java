package ml.shifu.core.di.service;


import com.google.inject.Inject;
import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.di.spi.StatsCalculator;
import ml.shifu.core.util.Params;

import java.util.List;

public class StatsCalculatingService {

    @Inject
    private StatsCalculator statsCalculator;

    public void exec(Field field, List<?> values, Params params) {
        statsCalculator.calculate(field, values, params);
    }
}
