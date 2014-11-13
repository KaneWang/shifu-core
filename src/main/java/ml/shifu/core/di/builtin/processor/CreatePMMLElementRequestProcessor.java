package ml.shifu.core.di.builtin.processor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ml.shifu.core.di.builtin.ShifuPMMLCreator;
import ml.shifu.core.di.module.SimpleModule;
import ml.shifu.core.di.service.*;
import ml.shifu.core.di.spi.RequestProcessor;
import ml.shifu.core.request.Binding;
import ml.shifu.core.request.Request;
import ml.shifu.core.util.PMMLUtils;
import ml.shifu.core.util.Params;
import ml.shifu.core.util.RequestUtils;
import org.dmg.pmml.DataDictionary;
import org.dmg.pmml.Model;
import org.dmg.pmml.PMML;
import org.dmg.pmml.Targets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class CreatePMMLElementRequestProcessor implements RequestProcessor {

    private static Logger log = LoggerFactory.getLogger(CreatePMMLElementRequestProcessor.class);

    public void exec(Request req) throws Exception {


        Binding processorBinding = req.getProcessor();

        Params params = processorBinding.getParams();

        String pathPMML = params.get("pathPMML", "./model.xml").toString();

        if (params.containsKey("overrideExistingModel")) {
            Boolean overrideExistingModel = Boolean.valueOf(params.get("overrideExistingModel").toString());
            if (overrideExistingModel) {
                new File(pathPMML).delete();
            }
        }

        log.info("PMML Path: " + pathPMML);
        PMML pmml;

        if ((new File(pathPMML)).exists()) {
            log.info("    File already exists. Loading ...");
            pmml = PMMLUtils.loadPMML(pathPMML);
            log.info("PMML Loaded!");
        } else {
            log.info("    Creating new PMML ...");
            pmml = (new ShifuPMMLCreator()).create(params);
            log.info("PMML Created!");
        }


        // Set bindings in module
        SimpleModule module = new SimpleModule();

        Binding dataDictionaryCreatorBinding = RequestUtils.getUniqueBinding(req, "PMMLDataDictionaryCreator");
        module.set(dataDictionaryCreatorBinding);

        Binding modelElementCreatorBinding = RequestUtils.getUniqueBinding(req, "PMMLModelElementCreator");
        module.set(modelElementCreatorBinding);

        Binding targetsCreatorBinding = RequestUtils.getUniqueBinding(req, "PMMLTargetsCreator");
        module.set(targetsCreatorBinding);

        //module.set(RequestUtils.getUniqueBinding(req, "OutputCreator", false));

        Binding miningSchemaBinding = RequestUtils.getUniqueBinding(req, "PMMLMiningSchemaCreator");
        if (miningSchemaBinding != null) {
            module.set(miningSchemaBinding);
        }


        Binding localTransformationsBinding = RequestUtils.getBindingBySpi(req, "PMMLLocalTransformationsCreator");
        if (localTransformationsBinding != null) {
            module.set(localTransformationsBinding);
        }

        // Inject
        Injector injector = Guice.createInjector(module);

        if (module.has("PMMLDataDictionaryCreator")) {
            log.info("Creating <DataDictionary/> ...");
            PMMLDataDictionaryService service = injector.getInstance(PMMLDataDictionaryService.class);
            DataDictionary dataDictionary = service.getDataDictionary(dataDictionaryCreatorBinding.getParams());
            pmml.setDataDictionary(dataDictionary);
        }

        if (module.has("PMMLModelElementCreator")) {
            log.info("Creating <ModelElement/> ...");
            PMMLModelElementService service = injector.getInstance(PMMLModelElementService.class);
            Model model = service.getModelElement(modelElementCreatorBinding.getParams());
            for (Model m : pmml.getModels()) {
                if (m.getModelName().equalsIgnoreCase((String) modelElementCreatorBinding.getParams().get("modelName")))
                    throw new RuntimeException("    ModelName already exists: " + model.getModelName() + "; rename the model or remove the existing <ModelElement/>");
            }
            pmml.withModels(model);
        }

        if (module.has("PMMLTargetsCreator")) {
            log.info("Creating <Targets/> ...");
            PMMLTargetsService service = injector.getInstance(PMMLTargetsService.class);
            Targets targets = service.createTargetsElement(pmml, targetsCreatorBinding.getParams());
        }

        if (module.has("PMMLMiningSchemaCreator")) {
            log.info("Creating <MiningSchema/> ...");
            PMMLMiningSchemaService service = injector.getInstance(PMMLMiningSchemaService.class);
            service.createMiningSchema(pmml, miningSchemaBinding != null ? miningSchemaBinding.getParams() : null);
        }


        if (module.has("PMMLLocalTransformationsCreator")) {
            log.info("Creating <LocalTransformations/> ...");
            PMMLLocalTransformationsService service = injector.getInstance(PMMLLocalTransformationsService.class);

            service.createLocalTransformations(pmml, localTransformationsBinding != null ? localTransformationsBinding.getParams() : null);

        }

        PMMLUtils.savePMML(pmml, pathPMML);
    }

}
