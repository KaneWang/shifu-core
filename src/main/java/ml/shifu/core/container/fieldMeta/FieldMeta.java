package ml.shifu.core.container.fieldMeta;


import java.util.List;

public class FieldMeta {

    private String version;
    private List<Field> fields;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
