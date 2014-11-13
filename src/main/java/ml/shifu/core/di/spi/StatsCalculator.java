package ml.shifu.core.di.spi;

import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.util.Params;

import java.util.List;

public interface StatsCalculator {

    public void calculate(Field field, List<?> values, Params params);

}
