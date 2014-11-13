package ml.shifu.core.container.fieldMeta;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContinuousStats {

    private Double max;
    private Double min;
    private Double mean;
    private Double median;
    private Double stdDev;
    private Double totalValuesSum;
    private Double totalSquaresSum;
    private Double interQuartileRange;
    private List<Double> binBoundaries;
    private List<Double> binCounts;
    private List<Double> binWeightedCounts;
    private List<Quantile> quantiles;
    private Map<String, ContinuousStats> statsByClass;

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMean() {
        return mean;
    }

    public void setMean(Double mean) {
        this.mean = mean;
    }

    public Double getMedian() {
        return median;
    }

    public void setMedian(Double median) {
        this.median = median;
    }

    public Double getStdDev() {
        return stdDev;
    }

    public void setStdDev(Double stdDev) {
        this.stdDev = stdDev;
    }

    public Double getTotalValuesSum() {
        return totalValuesSum;
    }

    public void setTotalValuesSum(Double totalValuesSum) {
        this.totalValuesSum = totalValuesSum;
    }

    public Double getTotalSquaresSum() {
        return totalSquaresSum;
    }

    public void setTotalSquaresSum(Double totalSquaresSum) {
        this.totalSquaresSum = totalSquaresSum;
    }

    public Double getInterQuartileRange() {
        return interQuartileRange;
    }

    public void setInterQuartileRange(Double interQuartileRange) {
        this.interQuartileRange = interQuartileRange;
    }

    public List<Double> getBinBoundaries() {
        return binBoundaries;
    }

    public void setBinBoundaries(List<Double> binBoundaries) {
        this.binBoundaries = binBoundaries;
    }

    public List<Double> getBinCounts() {
        return binCounts;
    }

    public void setBinCounts(List<Double> binCounts) {
        this.binCounts = binCounts;
    }

    public List<Double> getBinWeightedCounts() {
        return binWeightedCounts;
    }

    public void setBinWeightedCounts(List<Double> binWeightedCounts) {
        this.binWeightedCounts = binWeightedCounts;
    }

    public List<Quantile> getQuantiles() {
        return quantiles;
    }

    public void setQuantiles(List<Quantile> quantiles) {
        this.quantiles = quantiles;
    }

    public Map<String, ContinuousStats> getStatsByClass() {
        return statsByClass;
    }

    public void setStatsByClass(Map<String, ContinuousStats> statsByClass) {
        this.statsByClass = statsByClass;
    }
}



