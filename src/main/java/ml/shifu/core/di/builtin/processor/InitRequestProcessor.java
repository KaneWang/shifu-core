package ml.shifu.core.di.builtin.processor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ml.shifu.core.container.FieldControl;
import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.container.fieldMeta.FieldStats;
import ml.shifu.core.di.module.SimpleModule;
import ml.shifu.core.di.service.FieldSchemaLoadingService;
import ml.shifu.core.di.service.FieldTypeSettingService;
import ml.shifu.core.di.spi.RequestProcessor;
import ml.shifu.core.request.Request;
import ml.shifu.core.util.JSONUtils;
import ml.shifu.core.util.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class InitRequestProcessor implements RequestProcessor {

    private static Logger log = LoggerFactory.getLogger(InitRequestProcessor.class);

    public void exec(Request req) throws Exception {
        Params params = req.getProcessor().getParams();
        String pathOutput = params.get("pathOutput").toString();

        new File(pathOutput.substring(0, pathOutput.lastIndexOf(File.separator) + 1)).mkdirs();


        SimpleModule module = new SimpleModule(req.getBindings());
        Injector injector = Guice.createInjector(module);

        FieldSchemaLoadingService service = injector.getInstance(FieldSchemaLoadingService.class);
        FieldMeta fieldMeta = service.exec(req.getParamsBySpi("FieldSchemaLoader"));

        if (req.existsSpi("FieldTypeSetter")) {
            FieldTypeSettingService fieldTypeSettingService = injector.getInstance(FieldTypeSettingService.class);
            fieldTypeSettingService.exec(fieldMeta, req.getParamsBySpi("FieldTypeSetter"));
        }

        for (Field field : fieldMeta.getFields()) {
            field.setFieldStats(new FieldStats());
            field.setFieldControl(new FieldControl());
        }

        JSONUtils.writeValue(new File(pathOutput), fieldMeta);
    }
}
