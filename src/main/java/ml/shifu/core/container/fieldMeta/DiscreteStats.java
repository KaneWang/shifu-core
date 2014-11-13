package ml.shifu.core.container.fieldMeta;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscreteStats {

    private String modalValue;
    private List<String> binCategories;
    private List<Double> binCounts;
    private List<Double> binWeightedCounts;
    private Map<String, DiscreteStats> statsByClass;

    public String getModalValue() {
        return modalValue;
    }

    public void setModalValue(String modalValue) {
        this.modalValue = modalValue;
    }

    public List<String> getBinCategories() {
        return binCategories;
    }

    public void setBinCategories(List<String> binCategories) {
        this.binCategories = binCategories;
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

    public Map<String, DiscreteStats> getStatsByClass() {
        return statsByClass;
    }

    public void setStatsByClass(Map<String, DiscreteStats> statsByClass) {
        this.statsByClass = statsByClass;
    }
}





