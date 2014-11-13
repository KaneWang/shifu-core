package ml.shifu.core.container.fieldMeta;


public class FieldBasics {

    public enum DataType {STRING, NUMBER, BOOLEAN, DATETIME};
    public enum OpType {CONTINUOUS, CATEGORICAL, ORDINAL};

    private String name;
    private Integer num;
    private DataType dataType;
    private OpType opType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public OpType getOpType() {
        return opType;
    }

    public void setOpType(OpType opType) {
        this.opType = opType;
    }
}
