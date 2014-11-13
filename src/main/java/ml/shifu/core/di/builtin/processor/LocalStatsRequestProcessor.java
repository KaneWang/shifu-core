package ml.shifu.core.di.builtin.processor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.container.fieldMeta.FieldStats;
import ml.shifu.core.di.builtin.statsCalculator.DefaultCountsCalculator;
import ml.shifu.core.di.module.SimpleModule;
import ml.shifu.core.di.service.DataLoadingService;
import ml.shifu.core.di.service.StatsCalculatingService;
import ml.shifu.core.di.spi.RequestProcessor;
import ml.shifu.core.di.spi.StatsCalculator;
import ml.shifu.core.request.Request;
import ml.shifu.core.util.JSONUtils;
import ml.shifu.core.util.LocalDataTransposer;
import ml.shifu.core.util.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class LocalStatsRequestProcessor implements RequestProcessor {
    private static Logger log = LoggerFactory.getLogger(LocalStatsRequestProcessor.class);

    public void exec(Request req) throws Exception {

        Injector injector = Guice.createInjector(new SimpleModule(req.getBindings()));

        // Data Loading Service
        DataLoadingService dataLoadingService = injector.getInstance(DataLoadingService.class);
        List<List<Object>> rows = dataLoadingService.load(req.getParamsBySpi("DataLoader"));

        List<List<Object>> columns = LocalDataTransposer.transpose(rows);

        Params params = req.getProcessor().getParams();

        String pathFieldMeta = params.get("pathFieldMeta").toString();
        FieldMeta fieldMeta = JSONUtils.readValue(new File(pathFieldMeta), FieldMeta.class);

        int size = columns.size();
        if (size != fieldMeta.getFields().size()) {
            throw new RuntimeException("!!!Data does not match FieldMeta: Data length=" + size + "; # of Fields=" + fieldMeta.getFields().size());
        }

        // Stats Calculating Service
        StatsCalculatingService statsCalculatingService = injector.getInstance(StatsCalculatingService.class);
        for (int i = 0; i < size; i++) {
            Field field = fieldMeta.getFields().get(i);


            List<Object> values = columns.get(i);

            statsCalculatingService.exec(field, values, req.getParamsBySpi("StatsCalculator"));

        }

        JSONUtils.writeValue(new File(pathFieldMeta), fieldMeta);

    }
}
