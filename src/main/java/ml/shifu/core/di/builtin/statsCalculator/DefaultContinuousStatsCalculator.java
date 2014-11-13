package ml.shifu.core.di.builtin.statsCalculator;

import ml.shifu.core.container.ColumnBinningResult;
import ml.shifu.core.container.ContinuousValueObject;
import ml.shifu.core.container.fieldMeta.ContinuousStats;
import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldStats;
import ml.shifu.core.di.builtin.EqualPositiveBinningCalculator;
import ml.shifu.core.di.builtin.KSIVCalculator;
import ml.shifu.core.di.builtin.QuantileCalculator;
import ml.shifu.core.di.builtin.WOECalculator;
import ml.shifu.core.util.CommonUtils;
import ml.shifu.core.util.PMMLUtils;
import ml.shifu.core.util.Params;
import org.dmg.pmml.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DefaultContinuousStatsCalculator {
    private Logger log = LoggerFactory.getLogger(DefaultContinuousStatsCalculator.class);

    public static void calculate(Field field, List<?> values, Params params) {

        FieldStats fieldStats = field.getFieldStats();
        field.setFieldStats(fieldStats);
        Double sum = 0.0;
        Double squaredSum = 0.0;
        Double min = Double.MAX_VALUE;
        Double max = -Double.MAX_VALUE;

        Double EPS = 1e-6;


        List<Double> validValues = new ArrayList<Double>();

        for (ContinuousValueObject vo : (List<ContinuousValueObject>)values) {

            if (CommonUtils.isValidNumber(vo.getValue())) {

                Double value = vo.getValue();

                max = Math.max(max, value);
                min = Math.min(min, value);

                sum += value;
                squaredSum += value * value;

                validValues.add(value);
            }
        }


        ContinuousStats stats = new ContinuousStats();
        fieldStats.setContinuousStats(stats);

        stats.setMax(max);
        stats.setMin(min);


        int validSize = validValues.size();
        // mean and stdDev defaults to NaN
        if (validSize == 0 || sum.isInfinite() || squaredSum.isInfinite()) {
            return;
        }


        //it's ok while the voList is sorted;
        Collections.sort(validValues);
        stats.setMedian(validValues.get(validSize / 2));
        stats.setMean(sum / validSize);
        Double stdDev = Math.sqrt((squaredSum - (sum * sum) / validSize + EPS)/ (validSize - 1));


        Double interQuartileRange = validValues.get((int) Math.floor(validSize * 0.75)) - validValues.get((int) Math.floor(validSize * 0.25));

        stats.setStdDev(stdDev);
        stats.setInterQuartileRange(interQuartileRange);

        QuantileCalculator quantileCalculator = new QuantileCalculator();




        //stats.withQuantiles(quantileCalculator.getEvenlySpacedQuantiles(validValues, 11));

        stats.setTotalValuesSum(sum);
        stats.setTotalSquaresSum(squaredSum);


        Integer expectedBinNum = Integer.valueOf(params.get("numBins").toString());

        EqualPositiveBinningCalculator calculator = new EqualPositiveBinningCalculator();
        calculator.calculate(fieldStats.getContinuousStats(), (List<ContinuousValueObject>)values, expectedBinNum);


        Map<String, Object> extensionMap = new HashMap<String, Object>();

        ContinuousStats continuousStats = fieldStats.getContinuousStats();

        List<Integer> posCount = continuousStats.getStatsByClass().get("POS").getBinCounts();
        List<Integer> negCount = continuousStats.getStatsByClass().get("NEG").getBinCounts();

        List<Double> woe = WOECalculator.calculate(posCount.toArray(), negCount.toArray());
        extensionMap.put("BinWOE", woe.toString());

        KSIVCalculator ksivCalculator = new KSIVCalculator();
        ksivCalculator.calculateKSIV(negCount, posCount);
        extensionMap.put("KS", Double.valueOf(ksivCalculator.getKS()).toString());
        extensionMap.put("IV", Double.valueOf(ksivCalculator.getIV()).toString());

        fieldStats.setExtensions(extensionMap);

    }

}