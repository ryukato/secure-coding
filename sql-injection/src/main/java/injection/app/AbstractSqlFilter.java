package injection.app;

import java.util.Optional;
import java.util.Set;

public abstract class AbstractSqlFilter implements SqlFilter {
    @Override
    public String doFilter(String input) {
        Set<String> filterSet = loadFilterSet();
        String result = input;
        for (String f : filterSet) {
            result = result.replaceAll(f, "");
        }
        return result;
    }

    abstract Set<String> loadFilterSet();
}
