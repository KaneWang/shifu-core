package ml.shifu.core.util;


import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import ml.shifu.core.request.Request;
import ml.shifu.core.request.RequestDispatcher;
import ml.shifu.core.request.Workflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ShifuCLI {

    private static final Logger log = LoggerFactory.getLogger(ShifuCLI.class);

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.exit(0);
        }

        log.info("Loading: " + args[0]);
        File reqFile = new File(args[0]);

        Workflow workflow = null;

        try {
            log.info("Trying to load as Workflow");
            workflow = JSONUtils.readValue(reqFile, Workflow.class);
            log.info("Loading Workflow ...");
            for (Request request : workflow.getRequests()) {
                RequestDispatcher.dispatch(request);
            }
        } catch(UnrecognizedPropertyException e) {

            log.info("Loading Standalone Request ...");
            Request req = JSONUtils.readValue(reqFile, Request.class);
            RequestDispatcher.dispatch(req);
        }


        log.info("Job Finished.");
        System.exit(0);

        //SimpleModule module = new SimpleModule();
        //module.set(req);
        //Injector injector = Guice.createInjector(module);
        //RequestDispatchService service = injector.getInstance(RequestDispatchService.class);
        //service.dispatch(req);
    }

}
