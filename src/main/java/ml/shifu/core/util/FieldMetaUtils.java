package ml.shifu.core.util;

import ml.shifu.core.container.fieldMeta.FieldMeta;
import ml.shifu.core.request.Request;


import java.io.File;

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
}
