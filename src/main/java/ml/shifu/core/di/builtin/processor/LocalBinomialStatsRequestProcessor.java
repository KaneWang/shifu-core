package ml.shifu.core.di.builtin.processor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ml.shifu.core.container.ContinuousValueObject;
import ml.shifu.core.container.FieldControl;
import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldBasics;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.di.module.SimpleModule;
import ml.shifu.core.di.service.DataLoadingService;
import ml.shifu.core.di.service.StatsCalculatingService;
import ml.shifu.core.di.spi.RequestProcessor;
import ml.shifu.core.request.Request;
import ml.shifu.core.util.JSONUtils;
import ml.shifu.core.util.LocalDataTransposer;
import ml.shifu.core.util.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocalBinomialStatsRequestProcessor implements RequestProcessor {
    private static Logger log = LoggerFactory.getLogger(LocalBinomialStatsRequestProcessor.class);

    public void exec(Request req) throws Exception {

        log.info("Request Received: LocalBinomialStatsRequestProcessor");
        Injector injector = Guice.createInjector(new SimpleModule(req.getBindings()));

        // Data Loading Service
        DataLoadingService dataLoadingService = injector.getInstance(DataLoadingService.class);
        List<List<Object>> rows = dataLoadingService.load(req.getParamsBySpi("DataLoader"));

        List<List<Object>> columns = LocalDataTransposer.transpose(rows);

        Params params = req.getProcessor().getParams();

        String pathFieldMeta = params.get("pathFieldMeta").toString();
        FieldMeta fieldMeta = JSONUtils.readValue(new File(pathFieldMeta), FieldMeta.class);

        String targetFieldName = params.get("targetFieldName").toString();




        List<Object> targetColumn = null;
        Integer targetFieldNum = null;

        List<Field> fields = fieldMeta.getFields();
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            if (fields.get(i).getFieldBasics().getName().equals(targetFieldName)) {
                targetFieldNum = i;
                targetColumn = columns.get(i);
            }
        }

        if (targetFieldNum == null) {
            throw new RuntimeException("!!! Cannot find target field: " + targetFieldName);
        }



        FieldControl fieldControl = fieldMeta.getFields().get(targetFieldNum).getFieldControl();
        fieldControl.setIsTarget(true);

        List<String> posTags = (List<String>)params.get("posTags");
        List<String> negTags = (List<String>)params.get("negTags");

        List<String> convertedTargetColumn = new ArrayList<String>();
        for (int i = 0; i < targetColumn.size(); i++) {
            String tag;
            if (posTags.contains(targetColumn.get(i))) {
                tag = "POS";
            } else if (negTags.contains(targetColumn.get(i))) {
                tag = "NEG";
            } else if (posTags.contains("$default")) {
                tag = "POS";
            } else if (negTags.contains("$default")) {
                tag = "NEG";
            } else {
                tag = null;
            }

            convertedTargetColumn.add(tag);
        }

        if (size != columns.size()) {
            throw new RuntimeException("!!! Data does not match FieldMeta: Data length=" + size + "; # of Fields=" + fieldMeta.getFields().size());
        }



        // Stats Calculating Service
        StatsCalculatingService statsCalculatingService = injector.getInstance(StatsCalculatingService.class);
        for (int i = 0; i < size; i++) {
            Field field = fieldMeta.getFields().get(i);


            List<Object> rawValues = columns.get(i);

            int numValues = rawValues.size();
            if (field.getFieldBasics().getOpType().equals(FieldBasics.OpType.CONTINUOUS)) {
                List<ContinuousValueObject> values = new ArrayList<ContinuousValueObject>();

                for (int j = 0; j < numValues; j++) {
                    if (convertedTargetColumn.get(j) == null) {
                        continue;
                    }

                    ContinuousValueObject value = new ContinuousValueObject(convertedTargetColumn.get(j), Double.valueOf(columns.get(i).get(j).toString()), 1.0);
                    values.add(value);
                }

                statsCalculatingService.exec(field, values, req.getParamsBySpi("StatsCalculator"));
            } else if (field.getFieldBasics().getOpType().equals(FieldBasics.OpType.CATEGORICAL)) {

            }
        }

        JSONUtils.writeValue(new File(pathFieldMeta), fieldMeta);

    }
}
