package fcParsing;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.function.UnaryOperator;

public enum SORTING {
    MINIONS("minions", list -> {
        list.sort(Comparator.comparingInt(Character::getMinionsNumber).reversed());
        return list;
    }, PARSE_KEY.MINIONS),
    MOUNTS("mounts", list -> {
        list.sort(Comparator.comparingInt(Character::getMountsNumber).reversed());
        return list;
    }, PARSE_KEY.MOUNTS),
    CAPPED_JOBS("capped jobs", list -> {
        list.sort(Comparator.comparingInt(Character::getCappedJobsNumber).reversed());
        return list;
    }, PARSE_KEY.CAPPED_JOBS);
    private final String name;
    private final UnaryOperator<LinkedList<Character>> listUnaryOperator;
    private final PARSE_KEY parseKey;

    SORTING(String name, UnaryOperator<LinkedList<Character>> listUnaryOperator, PARSE_KEY parseKey) {
        this.name = name;
        this.listUnaryOperator = listUnaryOperator;
        this.parseKey = parseKey;
    }

    public String getName() {
        return name;
    }

    public UnaryOperator<LinkedList<Character>> getListUnaryOperator() {
        return listUnaryOperator;
    }

    public PARSE_KEY getParseKey() {
        return parseKey;
    }
}
