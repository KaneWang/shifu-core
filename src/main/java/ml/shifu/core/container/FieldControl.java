package ml.shifu.core.container;


import ml.shifu.core.util.TransformPlan;

public class FieldControl {

    private Boolean isSelected;
    private Boolean isTarget;
    private String mapMissingTo;
    private String mapInvalidTo;
    private TransformPlan transformPlan;

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Boolean getIsTarget() {
        return isTarget;
    }

    public void setIsTarget(Boolean isTarget) {
        this.isTarget = isTarget;
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
