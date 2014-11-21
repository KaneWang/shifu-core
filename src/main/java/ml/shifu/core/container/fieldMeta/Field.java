package ml.shifu.core.container.fieldMeta;

import ml.shifu.core.container.fieldMeta.FieldControl;

import java.util.Map;

public class Field {


    private FieldBasics fieldBasics;
    private FieldStats fieldStats;
    private FieldControl fieldControl;



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

    public FieldControl getFieldControl() {
        return fieldControl;
    }

    public void setFieldControl(FieldControl fieldControl) {
        this.fieldControl = fieldControl;
    }
}
