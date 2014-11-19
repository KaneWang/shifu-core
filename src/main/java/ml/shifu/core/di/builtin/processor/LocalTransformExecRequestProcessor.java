package ml.shifu.core.di.builtin.processor;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.inject.Guice;
import com.google.inject.Injector;

import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.di.builtin.transform.DefaultTransformer;
import ml.shifu.core.di.builtin.transform.ZScoreNormalizer;
import ml.shifu.core.di.module.SimpleModule;
import ml.shifu.core.di.service.DataLoadingService;
import ml.shifu.core.di.spi.RequestProcessor;
import ml.shifu.core.request.Request;
import ml.shifu.core.util.Params;
import ml.shifu.core.util.TransformPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocalTransformExecRequestProcessor implements RequestProcessor {

    private static Logger log = LoggerFactory.getLogger(LocalTransformExecRequestProcessor.class);

    ObjectMapper objectMapper = new ObjectMapper();

    private ZScoreNormalizer zScoreNormalizer = new ZScoreNormalizer();

    public void exec(Request req) throws Exception {
        Params params = req.getProcessor().getParams();

        DefaultTransformer defaultTransformer = new DefaultTransformer();

        String pathFieldMeta = params.get("pathFieldMeta").toString();
        String pathOutput = params.get("pathOutput").toString();

        FieldMeta fieldMeta = objectMapper.readValue(new File(pathFieldMeta), FieldMeta.class);
        Injector injector = Guice.createInjector(new SimpleModule(req.getBindings()));

        // Data Loading Service
        DataLoadingService dataLoadingService = injector.getInstance(DataLoadingService.class);
        List<List<Object>> rows = dataLoadingService.load(req.getParamsBySpi("DataLoader"));




        PrintWriter writer = null;
        PrintWriter headerWriter = null;

        // DefaultTransformationExecutor executor = new DefaultTransformationExecutor();
        //AllInclusiveTransformationExecutor executor = new AllInclusiveTransformationExecutor();

        String transformSelectedOnly = params.get("transformSelectedOnly").toString();


        try {

            (new File(pathOutput.substring(0, pathOutput.lastIndexOf(File.separator) + 1))).mkdirs();


            //List<Object> result = new ArrayList<Object>();

            writer = new PrintWriter(pathOutput, "UTF-8");

            for (List<Object> row : rows) {

                List<Object> result = defaultTransformer.transform(fieldMeta.getFields(), row);
                //log.info(Joiner.on(",").join(result));
                writer.println(Joiner.on(",").join(result));

            }


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
