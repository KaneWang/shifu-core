package ml.shifu.core.util;

import com.google.common.base.Joiner;
import ml.shifu.core.container.fieldMeta.Field;
import ml.shifu.core.container.fieldMeta.FieldControl;
import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.request.Request;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FieldMetaUtils {


    public static FieldMeta loadFieldMeta(Request req) throws Exception {
        Params params = req.getProcessor().getParams();
        return loadFieldMeta(params);
    }

    public static FieldMeta loadFieldMeta(Params params) throws Exception {
        String pathFieldMeta = params.get("pathFieldMeta").toString();
        return loadFieldMeta(pathFieldMeta);
    }

    public static FieldMeta loadFieldMeta(String pathFieldMeta) throws Exception {
        return JSONUtils.readValue(new File(pathFieldMeta), FieldMeta.class);
    }

    public static void saveFieldMeta(FieldMeta fieldMeta, String pathFieldMeta) throws Exception {
        JSONUtils.writeValue(new File(pathFieldMeta), fieldMeta);
    }

    public static String getTransformHeader(FieldMeta fieldMeta, String delimiter) {
        List<String> parts = new ArrayList<String>();

        for (Field field : fieldMeta.getFields()) {
            if (field.getFieldControl().getUsageType().equals(FieldControl.UsageType.TARGET)) {
                parts.add("TARGET::" + field.getFieldBasics().getName());
            }
        }

        for (Field field : fieldMeta.getFields()) {
            if (field.getFieldControl().getUsageType().equals(FieldControl.UsageType.ACTIVE)) {
                parts.add("ACTIVE::" + field.getFieldBasics().getName());
            }
        }

        return Joiner.on(delimiter).join(parts);
    }
}
