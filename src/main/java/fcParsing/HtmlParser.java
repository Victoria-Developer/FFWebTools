package fcParsing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class HtmlParser {

    public static FreeCompany parseChosenFC(String fcNumber) throws IOException {
        FreeCompany freeCompany = new FreeCompany();
        String urlFCLink = "https://eu.finalfantasyxiv.com/lodestone/freecompany/" + fcNumber + "/member/";
        Document doc = Jsoup.connect(urlFCLink).get();
        Elements freeCompanyInfo = doc
                .getElementsByClass("entry__freecompany__name");
        freeCompany.setCompanyName(freeCompanyInfo.text());

        doc.getElementsByClass("entry__bg")
                .forEach(element -> {
                    Character character = new Character();
                    character.setCharacterName(element.getElementsByClass("entry__name").text());
                    character.setCharacterLink("https://eu.finalfantasyxiv.com/" +
                            element.select("a[href]").attr("href"));
                    freeCompany.addCharacter(character);
                });
        return freeCompany;
    }

    public static LinkedList<Character> parseCharacters(LinkedList<Character> characters,
                                                        PARSE_KEY parseKey) {
        Consumer<Character> consumer = null;
        switch (parseKey) {
            case MINIONS_NUMBER -> consumer = HtmlParser::parseMinionsNumber;
            case MOUNTS_NUMBER -> consumer = HtmlParser::parseMountsNumber;
            case CAPPED_JOBS -> consumer = HtmlParser::parseJobs;
        }
        characters.forEach(consumer);
        return characters;
    }

    private static int getItemsNumber(Character character, String link) {
        Document document = null;
        try {
            document = Jsoup
                    .connect(character.getCharacterLink() + link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String itemsNumberText = document
                .getElementsByClass("minion__sort__total")
                .select("span")
                .text();
        return Integer.parseInt(itemsNumberText);
    }

    private static void parseMinionsNumber(Character character) {
        character.setMinionsNumber(getItemsNumber(character, "minion"));
    }

    public static void parseMountsNumber(Character character) {
        character.setMountsNumber(getItemsNumber(character, "mount"));
    }

    private static ArrayList<String> parseMobilePage(Character character,
                                                     String category,
                                                     String className) {
        String link = character.getCharacterLink() + category;
        Document document = null;
        try {
            document = Jsoup.connect(link)
                    .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) " +
                            "AppleWebKit/601.1.46 (KHTML, like Gecko) Version/" +
                            "9.0 Mobile/13B143 Safari/601.1").get();
        } catch (IOException ignored) {
        }
        if (document == null) return null;

        Elements elements = document.getElementsByClass(className);
        return elements.stream().map(Element::text)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static void parseMinionsInfo(Character character) {
        character.setMinionsList(parseMobilePage(character, "/minion", "minion__name"));
    }

    private static void parseMountsInfo(Character character) {
        character.setMountsList(parseMobilePage(character, "/mount", "mount__name"));
    }

    public static void parseJobs(Character character) {
        String urlCharacterLink = character.getCharacterLink() + "/class_job/";
        Document doc = null;
        try {
            doc = Jsoup.connect(urlCharacterLink).get();
        } catch (IOException ignored) {
        }

        LinkedHashMap<JOB, Integer> jobs = new LinkedHashMap<>();
        doc.getElementsByClass("character__job clearfix")
                .select("li")
                .forEach(s -> {
                    String jobName = s
                            .getElementsByClass("js__tooltip")
                            .text();
                    String lvlString = s.getElementsByClass("character__job__level").text();
                    int lvl = lvlString.equals("-") ? 0 : Integer.parseInt(lvlString);
                    jobs.put(JOB.getJob(jobName), lvl);
                });
        character.setJobs(jobs);
        character.setCappedJobsNumber(character.calculateCid());
    }
}
