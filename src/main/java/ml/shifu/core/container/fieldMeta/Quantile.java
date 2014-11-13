package ml.shifu.core.container.fieldMeta;


public class Quantile {
    private Double quantileLimit;

    private Double quantileValue;

    public Double getQuantileValue() {
        return quantileValue;
    }

    public void setQuantileValue(Double quantileValue) {
        this.quantileValue = quantileValue;
    }

    public Double getQuantileLimit() {
        return quantileLimit;
    }

    public void setQuantileLimit(Double quantileLimit) {
        this.quantileLimit = quantileLimit;
    }

}
