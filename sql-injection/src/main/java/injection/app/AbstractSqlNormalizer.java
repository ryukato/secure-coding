package injection.app;

import java.util.Set;

public abstract class AbstractSqlNormalizer implements SqlNormalizer {
    @Override
    public String normalize(String input) {
        Set<String> filterSet = loadFilterSet();
        String result = input;
        for (String f : filterSet) {
            result = result.replaceAll(f, "");
        }
        return result;
    }

    abstract Set<String> loadFilterSet();
}
