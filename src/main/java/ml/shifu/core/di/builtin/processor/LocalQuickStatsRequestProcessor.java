package ml.shifu.core.di.builtin.processor;

import com.google.common.base.Splitter;
import com.google.inject.Guice;
import com.google.inject.Injector;
import ml.shifu.core.container.ContinuousValueObject;
import ml.shifu.core.container.fieldMeta.FieldControl;
import ml.shifu.core.container.fieldMeta.*;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class LocalQuickStatsRequestProcessor implements RequestProcessor {
    private static Logger log = LoggerFactory.getLogger(LocalQuickStatsRequestProcessor.class);

    public void exec(Request req) throws Exception {

        log.info("Request Received: LocalQuickStatsRequestProcessor");


        Params params = req.getProcessor().getParams();

        String pathFieldMeta = params.get("pathFieldMeta").toString();
        String pathData = params.get("pathData").toString();
        String delimiter = params.get("delimiter").toString();
        FieldMeta fieldMeta = JSONUtils.readValue(new File(pathFieldMeta), FieldMeta.class);

        List<Field> fields = fieldMeta.getFields();
        Scanner scanner = null;


        int size = fields.size();
        double[] totalFreq = new double[size];
        double[] missingFreq = new double[size];
        double[] invalidFreq = new double[size];

        List<Set<Object>> uniqueList = new ArrayList<Set<Object>>();
        for (int i = 0; i < size; i++) {
            uniqueList.add(new HashSet<Object>());
        }

        double[] max = new double[size];
        double[] min = new double[size];
        double[] sum = new double[size];

        for (int i = 0; i < size; i++) {
            max[i] = Double.NEGATIVE_INFINITY;
            min[i] = Double.POSITIVE_INFINITY;
            sum[i] = 0;
        }

        Set<Integer> continuousSet = new HashSet<Integer>();
        Set<Integer> categoricalSet = new HashSet<Integer>();

        List<Map<String, Double>> categoricalCounters = new ArrayList<Map<String, Double>>();


        for (int i = 0; i < size; i++) {
            Field field = fields.get(i);
            categoricalCounters.add( new HashMap<String, Double>());
            if (field.getFieldBasics().getOpType().equals(FieldBasics.OpType.CATEGORICAL)) {
                categoricalSet.add(i);

            } else if (field.getFieldBasics().getOpType().equals(FieldBasics.OpType.CONTINUOUS)) {
                continuousSet.add(i);

            }
        }



        try {
            scanner = new Scanner(new BufferedReader(new FileReader(new File(pathData))));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                if (line == null || line.length() == 0) {
                    continue;
                }


                int i = 0;
                for (Object raw : Splitter.on(delimiter).split(line)) {

                    boolean valid = true;

                    totalFreq[i]++;

                    // TODO: define missing
                    if (raw == null || raw.toString().length() == 0) {
                        missingFreq[i]++;
                        valid = false;
                    }


                    // TODO: define invalid
                    if (raw.toString().equals("NaN")) {
                        invalidFreq[i]++;
                        valid = false;
                    }

                    if (continuousSet.contains(i)) {

                        try {
                            Double.valueOf(raw.toString());
                        } catch(Exception e) {
                            invalidFreq[i]++;
                            valid = false;
                        }
                    }

                    uniqueList.get(i).add(raw);


                    if (valid && continuousSet.contains(i)) {
                        Double value = Double.valueOf(raw.toString());
                        if (max[i] < value) {
                            max[i] = value;
                        }
                        if (min[i] > value) {
                            min[i] = value;
                        }
                        sum[i] += value;
                    } else if (valid && categoricalSet.contains(i)) {
                        incMapCnt(categoricalCounters.get(i), raw.toString());

                    }
                    i++;
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        for (int i = 0; i < size; i++) {
            Field field = fields.get(i);

            Counts counts = new Counts();
            counts.setTotalFreq(totalFreq[i]);
            counts.setMissingFreq(missingFreq[i]);
            counts.setMissingRate(missingFreq[i] / totalFreq[i]);
            counts.setInvalidFreq(invalidFreq[i]);
            counts.setInvalidRate(invalidFreq[i] / totalFreq[i]);
            counts.setCardinality(uniqueList.get(i).size());
            field.getFieldStats().setCounts(counts);


            if (continuousSet.contains(i)) {
                ContinuousStats stats = new ContinuousStats();
                stats.setMin(min[i]);
                stats.setMax(max[i]);
                stats.setMean(sum[i] / totalFreq[i]);
                stats.setTotalValuesSum(sum[i]);
                field.getFieldStats().setContinuousStats(stats);
            } else if (categoricalSet.contains(i)) {
                DiscreteStats discreteStats = new DiscreteStats();
                List<String> binCategories = new ArrayList<String>();
                List<Double> binCounts = new ArrayList<Double>();
                String modalString = null;
                Double modalValue = Double.NEGATIVE_INFINITY;
                for (Map.Entry<String, Double> entry : categoricalCounters.get(i).entrySet()) {
                    binCategories.add(entry.getKey());
                    binCounts.add(entry.getValue());
                    if (modalValue < entry.getValue()) {
                        modalString = entry.getKey();
                        modalValue = entry.getValue();
                    }
                }
                discreteStats.setBinCounts(binCounts);
                discreteStats.setBinCategories(binCategories);
                discreteStats.setModalValue(modalString);
                field.getFieldStats().setDiscreteStats(discreteStats);
            }
        }


        JSONUtils.writeValue(new File(pathFieldMeta), fieldMeta);

    }

    private void incMapCnt(Map<String, Double> map, String key) {
        double cnt = map.containsKey(key) ? map.get(key) : 0;
        map.put(key, cnt + 1);
    }
}
