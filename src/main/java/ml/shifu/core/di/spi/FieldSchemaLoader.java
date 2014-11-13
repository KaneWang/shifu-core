package ml.shifu.core.di.spi;

import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.util.Params;

import java.util.List;

public interface FieldSchemaLoader {

    public FieldMeta load(Params params);

}
