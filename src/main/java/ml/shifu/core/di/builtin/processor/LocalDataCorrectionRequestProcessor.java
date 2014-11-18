package ml.shifu.core.di.builtin.processor;


import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldBasics;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.di.spi.RequestProcessor;
import ml.shifu.core.request.Request;
import ml.shifu.core.util.FieldSelector;
import ml.shifu.core.util.JSONUtils;
import ml.shifu.core.util.Params;
import ml.shifu.core.util.TransformPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class LocalDataCorrectionRequestProcessor implements RequestProcessor {
    private static Logger log = LoggerFactory.getLogger(LocalDataCorrectionRequestProcessor.class);

    public void exec(Request req) throws Exception {
        Params params = req.getProcessor().getParams();

        String pathFieldMeta = params.get("pathFieldMeta").toString();
        String pathData = params.get("pathData").toString();
        String pathDataOutput = params.get("pathDataOutput").toString();
        String delimiter = params.get("delimiter").toString();
        FieldMeta fieldMeta = JSONUtils.readValue(new File(pathFieldMeta), FieldMeta.class);
        List<Field> fields = fieldMeta.getFields();
        Integer size = fields.size();
        Set<Integer> continuousSet = new HashSet<Integer>();
        Set<Integer> categoricalSet = new HashSet<Integer>();

        Map<String, String> mapMissingTo = (Map<String, String>) params.get("mapMissingTo");



        String continuousMissing = mapMissingTo.get("$continuous");
        String categoricalMissing = mapMissingTo.get("$categorical");

        for (int i = 0; i < size; i++) {
            Field field = fields.get(i);
            if (field.getFieldBasics().getOpType().equals(FieldBasics.OpType.CATEGORICAL)) {
                categoricalSet.add(i);
                field.getFieldControl().setMapMissingTo(categoricalMissing);
            } else if (field.getFieldBasics().getOpType().equals(FieldBasics.OpType.CONTINUOUS)) {
                continuousSet.add(i);
                field.getFieldControl().setMapMissingTo(continuousMissing);
            }
        }



        Scanner scanner = null;
        PrintWriter writer = null;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(new File(pathData))));

            writer = new PrintWriter(pathDataOutput, "UTF-8");

            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                if (line == null || line.length() == 0) {
                    continue;
                }

                List<Object> corrected = new ArrayList<Object>();

                int i = 0;
                for (Object raw : Splitter.on(delimiter).split(line)) {

                    boolean valid = true;


                    // TODO: define missing
                    if (raw == null || raw.toString().length() == 0) {

                        Object replaced = getMissingReplacement(fields.get(i));
                        if (replaced  == null) {
                            continue;
                        }
                        corrected.add(replaced);
                        valid = false;

                    }


                    // TODO: define invalid
                    if (raw.toString().equals("NaN")) {
                        Object replaced = getInvalidReplacement(fields.get(i));
                        if (replaced  == null) {
                            continue;
                        }
                        corrected.add(replaced);
                        valid = false;
                    }




                    if (valid ) {
                        corrected.add(raw);
                    }


                    i++;
                }

                writer.println(Joiner.on(",").join(corrected));
            }



        } catch (Exception e) {
            throw e;
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        JSONUtils.writeValue(new File(pathFieldMeta), fieldMeta);

    }

    private Object getMissingReplacement(Field field) {
        String treatment = field.getFieldControl().getMapMissingTo();
        if (treatment.equalsIgnoreCase("$mean")) {
            return field.getFieldStats().getContinuousStats().getMean();
        } else if (treatment.equalsIgnoreCase("$modal")) {
            return field.getFieldStats().getDiscreteStats().getModalValue();
        } else if (treatment.equalsIgnoreCase("$skip")) {
            return null;
        } else {
            throw new RuntimeException("Missing treatment not supported: " + treatment);
        }
    }

    private Object getInvalidReplacement(Field field) {
        String treatment = field.getFieldControl().getMapInvalidTo();
        if (treatment.equalsIgnoreCase("$mean")) {
            return field.getFieldStats().getContinuousStats().getMean();
        } else if (treatment.equalsIgnoreCase("$modal")) {
            return field.getFieldStats().getDiscreteStats().getModalValue();
        } else if (treatment.equalsIgnoreCase("$skip")) {
            return null;
        } else {
            throw new RuntimeException("Missing treatment not supported: " + treatment);
        }

    }


}
