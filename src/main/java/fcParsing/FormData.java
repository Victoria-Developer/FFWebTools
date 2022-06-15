package fcParsing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FormData {
    private final Map<PARSE_KEY, Boolean> parsedByKeys;
    private SORTING sorting;

    public FormData() {
        parsedByKeys = new HashMap<>();
        Arrays.asList(PARSE_KEY.values())
                .forEach(parse_key -> parsedByKeys.put(parse_key, false));
    }

    public Map<PARSE_KEY, Boolean> getParsedByKeys() {
        return parsedByKeys;
    }

    public void setSorting(SORTING sorting) {
        this.sorting = sorting;
    }

    public SORTING getSorting() {
        return sorting;
    }

    public boolean isSelectMethodDisabled() {
        return parsedByKeys.values().stream().noneMatch(v -> v);
    }

    public boolean isParseDisabled() {
        return parsedByKeys.values().stream().allMatch(v -> v);
    }
}
