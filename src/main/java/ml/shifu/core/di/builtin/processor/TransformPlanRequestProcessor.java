package ml.shifu.core.di.builtin.processor;


import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.di.spi.RequestProcessor;
import ml.shifu.core.request.Request;
import ml.shifu.core.util.FieldSelector;
import ml.shifu.core.util.TransformPlan;
import ml.shifu.core.util.JSONUtils;
import ml.shifu.core.util.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class TransformPlanRequestProcessor implements RequestProcessor {
    private static Logger log = LoggerFactory.getLogger(TransformPlanRequestProcessor.class);

    public void exec(Request req) throws Exception {
        Params params = req.getProcessor().getParams();

        String pathFieldMeta = params.get("pathFieldMeta").toString();
        String pathFieldMetaOutput = params.get("pathFieldMetaOutput").toString();

        Boolean transformSelectedOnly = Boolean.valueOf(params.get("transformSelectedOnly").toString());

        FieldMeta fieldMeta = JSONUtils.readValue(new File(pathFieldMeta), FieldMeta.class);

        List<Object> rawTransformPlans = (List<Object>)params.get("transforms");

        FieldSelector fieldSelector = new FieldSelector();

        for (Object rawTransformPlan : rawTransformPlans) {
            TransformPlan transformPlan = JSONUtils.reparse(rawTransformPlan, TransformPlan.class);

            List<Field> fields = fieldSelector.select(fieldMeta, transformPlan.getSelectors());


            for (Field field : fields) {
                if (transformSelectedOnly) {
                    if ((field.getFieldControl().getIsSelected() != null && field.getFieldControl().getIsSelected() == true)
                            || (field.getFieldControl().getIsTarget() != null && field.getFieldControl().getIsTarget() == true)) {
                        field.getFieldControl().setTransformPlan(transformPlan);
                    }
                } else {
                    field.getFieldControl().setTransformPlan(transformPlan);
                }
            }
        }



        JSONUtils.writeValue(new File(pathFieldMetaOutput), fieldMeta);

    }


}
