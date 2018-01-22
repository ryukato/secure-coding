package injection.app;

import java.util.Optional;
import java.util.regex.Pattern;

public interface SqlFilter {
    Pattern PATTERN = Pattern.compile("[\\[\\]'\\-#()@;=*/+\\\\]");

    default String filterSpecialChars(String testStatement) {
        return Optional.ofNullable(testStatement)
                .map(t -> PATTERN.matcher(t).replaceAll(""))
                .orElse("");
    }

    String doFilter(String input);
}
