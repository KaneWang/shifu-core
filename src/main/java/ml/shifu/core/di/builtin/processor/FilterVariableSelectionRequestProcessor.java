package ml.shifu.core.di.builtin.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ml.shifu.core.container.fieldMeta.FieldControl;
import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.di.spi.RequestProcessor;
import ml.shifu.core.request.Request;
import ml.shifu.core.util.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public class FilterVariableSelectionRequestProcessor implements RequestProcessor {
    private static Logger log = LoggerFactory.getLogger(FilterVariableSelectionRequestProcessor.class);

    ObjectMapper objectMapper = new ObjectMapper();

    public void exec(Request req) throws Exception {

        Params params = req.getProcessor().getParams();

        String pathFieldMeta = params.get("pathFieldMeta").toString();
        FieldMeta fieldMeta = objectMapper.readValue(new File(pathFieldMeta), FieldMeta.class);

        List<Field> fields = fieldMeta.getFields();
        List<Field> candidateFields = new ArrayList<Field>();

        final String metric = params.get("metric").toString();


        log.info("Resetting ACTIVE fields to UNSET...");
        for (Field field : fields) {
            if (field.getFieldStats() != null && field.getFieldStats().getExtensions() != null && field.getFieldStats().getExtensions().containsKey(metric)) {
                candidateFields.add(field);
            }

            if (field.getFieldControl().getUsageType().equals(FieldControl.UsageType.ACTIVE)) {
                field.getFieldControl().setUsageType(FieldControl.UsageType.UNSET);
            }
        }

        Collections.sort(candidateFields, new Comparator<Field>() {
            public int compare(Field f1, Field f2) {
                Double v1 = Double.valueOf(f1.getFieldStats().getExtensions().get(metric).toString());
                Double v2 = Double.valueOf(f2.getFieldStats().getExtensions().get(metric).toString());
                return v2.compareTo(v1);
            }
        });

        Integer numSelected = Integer.valueOf(params.get("numSelected").toString());

        Integer cnt = 0;
        int i = 0;
        while (cnt < numSelected) {

            FieldControl fieldControl = candidateFields.get(i).getFieldControl();

            FieldControl.UsageType currentUsageType = fieldControl.getUsageType();
            if (fieldControl.getUsageType().equals(FieldControl.UsageType.UNSET) ||
                    fieldControl.getUsageType().equals(FieldControl.UsageType.ACTIVE)) {
                log.info("    Set UsageType from " + currentUsageType.toString() + " to ACTIVE: " + candidateFields.get(i).getFieldBasics().getName());
                log.info("        Metric: " + metric + "=" + candidateFields.get(i).getFieldStats().getExtensions().get(metric));

                fieldControl.setUsageType(FieldControl.UsageType.ACTIVE);
                cnt++;
            }
            i++;

        }

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(pathFieldMeta), fieldMeta);

    }
}
