package ml.shifu.core.container;


import java.util.Comparator;

public class ContinuousValueObject {


    private String tag;
    private Double value;
    private Double weight;

    public ContinuousValueObject() {
        this.weight = 1.0;
    }

    public ContinuousValueObject(String tag, Double value, Double weight) {
        this.tag = tag;
        this.value = value;
        this.weight = weight;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public static class ContinuousValueObjectComparator implements Comparator<ContinuousValueObject> {

        public int compare(ContinuousValueObject a, ContinuousValueObject b) {
            int d = a.value.compareTo(b.value);
            if (d == 0) {
                return a.tag.compareTo(b.tag);
            } else {
                return d;
            }
        }
    }

}

