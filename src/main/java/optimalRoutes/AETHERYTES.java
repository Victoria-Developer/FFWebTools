package optimalRoutes;

public enum AETHERYTES {
    SINUS_LACRIMARUM("Sinus Lacrimarum", new DoublePoint(10.6, 34.3)),
    BESTWAYS_BURROW("Bestways Burrow", new DoublePoint(21.7, 11.1)),

    REAH_TAHRA("Reah Tahr", new DoublePoint(10.5, 26.7)),
    ABODE_OF_THE_EA("Abode of the Ea", new DoublePoint(22.7, 8.3)),
    BASE_OMICRON("Base Omicron", new DoublePoint(31.3, 28.0)),

    PALAKAS_STAND("Palaka's Stand", new DoublePoint(29.5, 16.5)),
    YEDLIHMAD("Yedlihmad", new DoublePoint(25.3, 34.0)),
    THE_GREAT_WORK("The Great Work", new DoublePoint(10.9, 22.2)),

    CAMP_BROKEN_GLASS("Camp Broken Glass", new DoublePoint(13.3, 31.1)),
    TERTIUM("Tertium", new DoublePoint(31.7, 18.0)),

    APORIA("Aporia", new DoublePoint(6.9, 27.4)),
    THE_ARCHEION("The Archeion", new DoublePoint(30.3, 11.9)),
    SHARLAYAN_HAMLET("Sharlayan Hamlet", new DoublePoint(21.6 ,20.5)),

    ANAGNORISIS("Anagnorisis", new DoublePoint(24.6, 24.0)),
    THE_TWELVE_WONDERS("The Twelve Wonders ", new DoublePoint(8.3, 32.3)),
    POIETEN_OIKOS("Poieten Oikos", new DoublePoint(10.8, 17.0));

    private final String name;
    private final DoublePoint coordinates;

    AETHERYTES(String name, DoublePoint coordinates) {
        this.name = name;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public DoublePoint getCoordinates() {
        return coordinates;
    }
}
