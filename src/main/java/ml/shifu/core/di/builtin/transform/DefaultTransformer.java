package ml.shifu.core.di.builtin.transform;


import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.di.spi.Transformer;

import java.util.ArrayList;
import java.util.List;

public class DefaultTransformer  {

    public List<Double> transform(FieldMeta fieldMeta, List<Object> raw) {

        List<Field> fields = fieldMeta.getFields();

        List<Double> normalized = new ArrayList<Double>();
        ZScoreNormalizer normalizer= new ZScoreNormalizer();
        for (Field field : fields) {
            if (field.getFieldControl() != null && field.getFieldControl().containsKey("selected")) {
                if (field.getFieldControl().get("selected").toString().equals("true")) {
                    normalized.add(normalizer.normalize(field, raw.get(field.getFieldBasics().getNum())));
                }
            }
        }

        return normalized;
    }

}
