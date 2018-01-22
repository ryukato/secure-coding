package injection.app;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OracleSqlFilter extends AbstractSqlFilter {
    private static final Set<String> FILTER_SET =  new HashSet<>(Arrays.asList("openrowset", "user_tables", "row_num", "..."));
    @Override
    Set<String> loadFilterSet() {
        return FILTER_SET;
    }
}
