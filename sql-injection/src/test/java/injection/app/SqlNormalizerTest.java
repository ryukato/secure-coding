package injection.app;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class SqlNormalizerTest {

    @Test
    public void filterSpecialChars() {
        String[] specialChars = new String[]{"[", "]", "'", "\\", "-", "#"};
        String testStatement = "[]'\\-#";
        SqlNormalizer sqlFilter = (s) -> null;
        testStatement = sqlFilter.filterSpecialChars(testStatement);

        for (int i = 0; i < specialChars.length; i++) {
            assertFalse("result not contain char: " + specialChars[i], testStatement.contains(specialChars[i]));
        }
    }

    @Test
    public void testOracleSqlFilter() throws Exception {
        SqlNormalizer sqlFilter = new OracleSqlNormalizer();
        String result = sqlFilter.normalize("exec openrowset");
        assertFalse("openrowset removed", result.contains("openrowset"));
    }
}
