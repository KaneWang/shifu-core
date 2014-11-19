package ml.shifu.core.di.builtin.processor;

import com.google.common.base.Splitter;
import ml.shifu.core.container.BinaryConfusionMatrix;
import ml.shifu.core.container.ClassificationResult;
import ml.shifu.core.di.builtin.BinaryConfusionMatrixCalculator;
import ml.shifu.core.di.spi.RequestProcessor;
import ml.shifu.core.request.Request;
import ml.shifu.core.util.JSONUtils;
import ml.shifu.core.util.Params;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class CSVModelEvalRequestProcessor implements RequestProcessor {

    public void exec(Request req) throws Exception {
        Params params = req.getProcessor().getParams();

        String pathScoreResult = (String) params.get("pathScoreResult");

        List<String> posTags = (List<String>) params.get("posTags");
        List<String> negTags = (List<String>) params.get("negTags");

        List<ClassificationResult> classificationResultList = new ArrayList<ClassificationResult>();//Arrays.asList(JSONUtils.readValue(new File(pathScoreResult), ClassificationResult[].class));

        Scanner scanner = null;
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(pathScoreResult)));
            while(scanner.hasNext()) {
                String line = scanner.nextLine();
                List<String> parts = Arrays.asList(line.split(","));
                ClassificationResult result = new ClassificationResult();
                result.setTrueClass(parts.get(1));
                Map<String, Double> scoreMap = new HashMap<String, Double>();
                scoreMap.put("testModel", Double.valueOf(parts.get(0)));
                result.setScoreMap(scoreMap);
                classificationResultList.add(result);
            }


        } catch (Exception e) {
            throw e;
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }



        BinaryConfusionMatrixCalculator calculator = new BinaryConfusionMatrixCalculator();
        List<BinaryConfusionMatrix> confusionMatrixList = calculator.calculate(classificationResultList, posTags, negTags);


        int size = confusionMatrixList.size();
        List<Double> samplePoints = (List<Double>) params.get("samplePoints");

        //JSONUtils.writeValue(new File("tmp/confusionmatrix.json"), confusionMatrixList);

        List<BinaryConfusionMatrix> sampledList = new ArrayList<BinaryConfusionMatrix>();
        for (Double samplePoint : samplePoints) {
            int index = (int) Math.round(samplePoint * (size - 1));
            sampledList.add(confusionMatrixList.get(index));
        }

        String pathPerformance = params.get("pathPerformance", "performance.json").toString();

        JSONUtils.writeValue(new File(pathPerformance), sampledList);

    }


}
