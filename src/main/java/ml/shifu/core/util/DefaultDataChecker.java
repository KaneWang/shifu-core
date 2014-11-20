package ml.shifu.core.util;

public class DefaultDataChecker {

    public static Boolean isMissing(Object raw) {
        // TODO: define missing
        if (raw == null || raw.toString().length() == 0) {
            return true;
        }
        return false;
    }

    public static Boolean isMissingNumber(Object raw) {
        if (raw == null || raw.toString().length() == 0 || raw.toString().equals("NaN")) {
            return true;
        }

        try {
            Double.valueOf(raw.toString());
        } catch (Exception e) {
            return true;
        }
        return false;
    }


    public static Boolean isInvalid(Object raw) {
        // TODO: define invalid
        if (raw.toString().equals("NaN")) {
          return true;
        }

        return false;
    }

}
