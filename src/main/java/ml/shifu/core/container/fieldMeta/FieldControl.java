package ml.shifu.core.container.fieldMeta;


import ml.shifu.core.util.TransformPlan;

public class FieldControl {

    public enum UsageType {UNSET, ACTIVE, TARGET, SUPPLEMENTARY, GROUP, ORDER, FREQUENCY_WEIGHT, ANALYSIS_WEIGHT}

    private UsageType usageType;


    private String mapMissingTo;
    private String mapInvalidTo;
    private TransformPlan transformPlan;

    public UsageType getUsageType() {
        return usageType;
    }

    public void setUsageType(UsageType usageType) {
        this.usageType = usageType;
    }

    public String getMapMissingTo() {
        return mapMissingTo;
    }

    public void setMapMissingTo(String mapMissingTo) {
        this.mapMissingTo = mapMissingTo;
    }

    public String getMapInvalidTo() {
        return mapInvalidTo;
    }

    public void setMapInvalidTo(String mapInvalidTo) {
        this.mapInvalidTo = mapInvalidTo;
    }

    public TransformPlan getTransformPlan() {
        return transformPlan;
    }

    public void setTransformPlan(TransformPlan transformPlan) {
        this.transformPlan = transformPlan;
    }
}
