package ml.shifu.core.di.builtin.fieldTypeSetter;

import ml.shifu.core.request.Request;
import ml.shifu.core.request.RequestDispatcher;
import ml.shifu.core.util.JSONUtils;
import org.testng.annotations.Test;

import java.io.File;

public class ByFileFieldTypeSetterTest {


    @Test
    public void test1() throws Exception {
        Request req = JSONUtils.readValue(new File("src/test/resources/unittest/ByFileFieldTypeSetter/1_create.json"), Request.class);
        RequestDispatcher.dispatch(req);
    }
}
