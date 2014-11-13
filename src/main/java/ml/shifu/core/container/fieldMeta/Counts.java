package ml.shifu.core.container.fieldMeta;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Counts {

    private Double totalFreq;
    private Double missingFreq;
    private Double invalidFreq;
    private Integer cardinality;

    public Integer getCardinality() {
        return cardinality;
    }

    public void setCardinality(Integer cardinality) {
        this.cardinality = cardinality;
    }

    public Double getTotalFreq() {
        return totalFreq;
    }

    public void setTotalFreq(Double totalFreq) {
        this.totalFreq = totalFreq;
    }

    public Double getMissingFreq() {
        return missingFreq;
    }

    public void setMissingFreq(Double missingFreq) {
        this.missingFreq = missingFreq;
    }

    public Double getInvalidFreq() {
        return invalidFreq;
    }

    public void setInvalidFreq(Double invalidFreq) {
        this.invalidFreq = invalidFreq;
    }




}



