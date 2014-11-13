package ml.shifu.core.container.fieldMeta;

import java.util.Map;

public class Field {


    private FieldBasics fieldBasics;
    private FieldStats fieldStats;
    private Map<String, Object> fieldControl;



    public FieldBasics getFieldBasics() {
        return fieldBasics;
    }

    public void setFieldBasics(FieldBasics fieldBasics) {
        this.fieldBasics = fieldBasics;
    }

    public FieldStats getFieldStats() {
        return fieldStats;
    }

    public void setFieldStats(FieldStats fieldStats) {
        this.fieldStats = fieldStats;
    }

    public Map<String, Object> getFieldControl() {
        return fieldControl;
    }

    public void setFieldControl(Map<String, Object> fieldControl) {
        this.fieldControl = fieldControl;
    }
}
