package ml.shifu.core.di.builtin.processor;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.inject.Guice;
import com.google.inject.Injector;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.di.builtin.transform.DefaultTransformationExecutor;
import ml.shifu.core.di.builtin.transform.DefaultTransformer;
import ml.shifu.core.di.module.SimpleModule;
import ml.shifu.core.di.service.DataLoadingService;
import ml.shifu.core.di.spi.RequestProcessor;
import ml.shifu.core.request.Request;
import ml.shifu.core.util.Params;
import org.dmg.pmml.DataField;
import org.dmg.pmml.DerivedField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalTransformRequestProcessor implements RequestProcessor {

    private static Logger log = LoggerFactory.getLogger(FilterVariableSelectionRequestProcessor.class);

    ObjectMapper objectMapper = new ObjectMapper();

    public void exec(Request req) throws Exception {
        Params params = req.getProcessor().getParams();

        String pathFieldMeta = params.get("pathFieldMeta").toString();
        String pathOutput = params.get("pathOutput").toString();

        FieldMeta fieldMeta = objectMapper.readValue(new File(pathFieldMeta), FieldMeta.class);
        Injector injector = Guice.createInjector(new SimpleModule(req.getBindings()));

        // Data Loading Service
        DataLoadingService dataLoadingService = injector.getInstance(DataLoadingService.class);
        List<List<Object>> rows = dataLoadingService.load(req.getParamsBySpi("DataLoader"));

        DefaultTransformer transformer = new DefaultTransformer();


        PrintWriter writer = null;
        PrintWriter headerWriter = null;

       // DefaultTransformationExecutor executor = new DefaultTransformationExecutor();
        //AllInclusiveTransformationExecutor executor = new AllInclusiveTransformationExecutor();

        try {

            (new File(pathOutput.substring(0, pathOutput.lastIndexOf(File.separator) + 1))).mkdirs();


            //List<Object> result = new ArrayList<Object>();

            writer = new PrintWriter(pathOutput, "UTF-8");

            for (List<Object> row : rows) {

                List<Double> result = transformer.transform(fieldMeta, row);
                writer.println(Joiner.on(",").join(result));

            }

            //result.addAll(executor.transform(activeFields, rawDataMap));

            //headerWriter = new PrintWriter(pathOutputActiveHeader);
/*
            List<String> header = new ArrayList<String>();
            for (DerivedField derivedField : targetFields) {
                header.add("TARGET::" + derivedField.getName().getValue());
            }
            for (DerivedField derivedField : activeFields) {
                header.add("ACTIVE::" + derivedField.getName().getValue());
            }
            headerWriter.print(Joiner.on(",").join(header));
*/
           // List<DataField> dataFields = pmml.getDataDictionary().getDataFields();
           // int size = dataFields.size();
            //Map<String, Object> rawDataMap = new HashMap<String, Object>();

            //for (List<Object> row : rows) {

              //  for (int i = 0; i < size; i++) {
               //     rawDataMap.put(dataFields.get(i).getName().getValue(), row.get(i));
               // }
               // List<Object> result = executor.transform(targetFields, rawDataMap);
               // result.addAll(executor.transform(activeFields, rawDataMap));
                //writer.println(Joiner.on(",").join(result));
            //}


        } catch (Exception e) {
            log.error(e.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (headerWriter != null) {
                headerWriter.close();
            }
        }
    }

}
