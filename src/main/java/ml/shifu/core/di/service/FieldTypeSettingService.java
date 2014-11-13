package ml.shifu.core.di.service;

import com.google.inject.Inject;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.di.spi.FieldTypeSetter;
import ml.shifu.core.util.Params;

public class FieldTypeSettingService {
    @Inject
    private FieldTypeSetter setter;


    public void exec(FieldMeta fieldMeta, Params params) {
        setter.set(fieldMeta, params);
    }
}
