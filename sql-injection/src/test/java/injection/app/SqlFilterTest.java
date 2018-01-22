package injection.app;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SqlFilterTest {

    @Test
    public void filterSpecialChars() {
        String[] specialChars = new String[]{"[", "]", "'", "\\", "-", "#"};
        String testStatement = "[]'\\-#";
        SqlFilter sqlFilter = (s) -> null;
        testStatement = sqlFilter.filterSpecialChars(testStatement);

        for (int i = 0; i < specialChars.length; i++) {
            assertFalse("result not contain char: " + specialChars[i], testStatement.contains(specialChars[i]));
        }
    }

    @Test
    public void testOracleSqlFilter() throws Exception {
        SqlFilter sqlFilter = new OracleSqlFilter();
        String result = sqlFilter.doFilter("exec openrowset");
        assertFalse("openrowset removed", result.contains("openrowset"));
    }
}
