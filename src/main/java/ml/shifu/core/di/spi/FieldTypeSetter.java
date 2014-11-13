package ml.shifu.core.di.spi;


import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.util.Params;

public interface FieldTypeSetter {

    public void set(FieldMeta fieldMeta, Params params);
}
