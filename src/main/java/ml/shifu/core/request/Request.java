package ml.shifu.core.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ml.shifu.core.util.Params;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {

    private String name;
    private String description;
    private Binding processor;
    private List<Binding> bindings;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Binding getProcessor() {
        return processor;
    }

    public void setProcessor(Binding processor) {
        this.processor = processor;
    }

    public List<Binding> getBindings() {
        return bindings;
    }

    public void setBindings(List<Binding> bindings) {
        this.bindings = bindings;
    }


    public Params getParamsBySpi(String spi) {
        for (Binding binding : bindings) {
            if (binding.getSpi().equals(spi)) {
                return binding.getParams();
            }
        }
        throw new RuntimeException("No such SPI: " + spi);
    }

    public Boolean existsSpi(String spi) {
        for (Binding binding : bindings) {
            if (binding.getSpi().equals(spi)) {
                return true;
            }
        }
        return false;
    }
}