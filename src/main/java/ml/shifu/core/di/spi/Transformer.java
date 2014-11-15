package ml.shifu.core.di.spi;

import ml.shifu.core.container.fieldMeta.FieldMeta;

import java.util.List;

public interface Transformer {

    public void transform(FieldMeta fieldMeta, List<Object> raw);

}
