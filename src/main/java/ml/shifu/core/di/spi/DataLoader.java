package ml.shifu.core.di.spi;

import ml.shifu.core.util.Params;

import java.util.List;

public interface DataLoader {
    public List<List<Object>> load(Params params);
}
