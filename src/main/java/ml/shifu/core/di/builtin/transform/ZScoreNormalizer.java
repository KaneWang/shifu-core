package ml.shifu.core.di.builtin.transform;

import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldBasics;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.di.spi.Normalizer;

public class ZScoreNormalizer  {

    private Double stdDevCutOff = 4.0;

    /**
     * Compute the zscore, by original value, mean, standard deviation and standard deviation cutoff
     *
     * @param var          - original value
     * @param mean         - mean value
     * @param stdDev       - standard deviation
     * @param stdDevCutOff - standard deviation cutoff
     * @return zscore
     */
    public static double computeZScore(double var, double mean, double stdDev, double stdDevCutOff) {
        double maxCutOff = mean + stdDevCutOff * stdDev;
        if (var > maxCutOff) {
            var = maxCutOff;
        }

        double minCutOff = mean - stdDevCutOff * stdDev;
        if (var < minCutOff) {
            var = minCutOff;
        }

        if (stdDev > 0.00001) {
            return (var - mean) / stdDev;
        } else {
            return 0.0;
        }
    }


    public Double normalize(Field field, Object raw) {

        if (field.getFieldControl() != null && field.getFieldControl().containsKey("stdDevCutOff")) {
            stdDevCutOff = Double.valueOf(field.getFieldControl().get("stdDevCutOff").toString());
        }

        //if (field.getFieldBasics().getOpType().equals(FieldBasics.OpType.CATEGORICAL)) {

           // TODO: need bin pos rate
           /* int index = field.getFieldStats().getDiscreteStats().getBinCategories().indexOf(raw);
            // TODO: use default. Not 0 !!!
            // Using the most frequent categorical value?
            if (index == -1) {
                return 0.0;
            } else {
                return computeZScore(config.getBinPosRate().get(index), config.getMean(), config.getStdDev(), stdDevCutOff);
            }*/
        //} else {
            Double value = null;
            Double mean = field.getFieldStats().getContinuousStats().getMean();
            Double stdDev = field.getFieldStats().getContinuousStats().getStdDev();
            try {
                value = Double.parseDouble(raw.toString());
            } catch (Exception e) {
                //log.debug("Not decimal format " + raw + ", using default!");
                //value = ((config.getMean() == null) ? 0.0 : config.getMean());
            }

            return computeZScore(value, mean, stdDev, stdDevCutOff);
        //}
    }
}
