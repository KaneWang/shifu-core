package ml.shifu.core.container.fieldMeta;

import java.util.Map;

public class FieldStats {

    private Counts counts;
    private ContinuousStats continuousStats;
    private DiscreteStats discreteStats;
    private Map<String, Object> extensions;

    public Counts getCounts() {
        return counts;
    }

    public void setCounts(Counts counts) {
        this.counts = counts;
    }

    public ContinuousStats getContinuousStats() {
        return continuousStats;
    }

    public void setContinuousStats(ContinuousStats continuousStats) {
        this.continuousStats = continuousStats;
    }

    public DiscreteStats getDiscreteStats() {
        return discreteStats;
    }

    public void setDiscreteStats(DiscreteStats discreteStats) {
        this.discreteStats = discreteStats;
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }
}
