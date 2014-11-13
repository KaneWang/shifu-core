package ml.shifu.core.di.service;

import com.google.inject.Inject;
import ml.shifu.core.di.spi.DataLoader;
import ml.shifu.core.util.Params;

import java.util.List;

public class DataLoadingService {
    @Inject
    private DataLoader loader;

    public List<List<Object>> load(Params params) {
        return loader.load(params);
    }
}
