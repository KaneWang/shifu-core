package ml.shifu.core.di.builtin.processor;

import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldControl;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.di.spi.RequestProcessor;
import ml.shifu.core.request.Request;
import ml.shifu.core.util.FieldMetaUtils;
import ml.shifu.core.util.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreliminaryUsageTypeSettingRequestProcessor implements RequestProcessor {

    private static Logger log = LoggerFactory.getLogger(PreliminaryUsageTypeSettingRequestProcessor.class);

    public void exec(Request req) throws Exception {
        Params params = req.getProcessor().getParams();

        Integer cardinalityUpperLimit = Integer.valueOf(params.get("cardinalityUpperLimit").toString());
        Integer cardinalityLowerLimit = Integer.valueOf(params.get("cardinalityLowerLimit").toString());

        Double missingRateLimit = Double.valueOf(params.get("missingRateLimit").toString());

        FieldMeta fieldMeta = FieldMetaUtils.loadFieldMeta(params);

        for (Field field : fieldMeta.getFields()) {
            if (field.getFieldControl().getUsageType().equals(FieldControl.UsageType.ACTIVE)) {
                int cardinality = field.getFieldStats().getCounts().getCardinality();
                if (cardinality > cardinalityUpperLimit || cardinality < cardinalityLowerLimit) {
                    log.info("    Change ACTIVE to SUPPLEMENTARY: " + field.getFieldBasics().getName());
                    log.info("        - Exceed Cardinality Limit: cardinality=" + cardinality);
                    field.getFieldControl().setUsageType(FieldControl.UsageType.SUPPLEMENTARY);
                }

                if (field.getFieldStats().getCounts().getMissingRate() > missingRateLimit) {
                    log.info("    Change ACTIVE to SUPPLEMENTARY: " + field.getFieldBasics().getName());
                    log.info("        - Exceed MissingRate Limit: missingRate=" + field.getFieldStats().getCounts().getMissingRate());
                    field.getFieldControl().setUsageType(FieldControl.UsageType.SUPPLEMENTARY);
                }
            }
        }

        FieldMetaUtils.saveFieldMeta(fieldMeta, params.get("pathFieldMeta").toString());
    }



}
