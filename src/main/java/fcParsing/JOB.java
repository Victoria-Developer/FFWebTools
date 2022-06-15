package fcParsing;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.*;
import java.util.function.Function;

public enum JOB {
    GLADIATOR(new Builder().jobString("Gladiator").role(TANK.TANK)),
    PALADIN(new Builder().jobString("Paladin").role(TANK.TANK)),
    MARAUDER(new Builder().jobString("Marauder").role(TANK.TANK)),
    WARRIOR(new Builder().jobString("Warrior").role(TANK.TANK)),
    DARK_KNIGHT(new Builder().jobString("Dark Knight").role(TANK.TANK)),
    GUNBREAKER(new Builder().jobString("Gunbreaker").role(TANK.TANK)),

    LANCER(new Builder().jobString("Lancer").role(DPS.MELEE)),
    DRAGOON(new Builder()
            .jobString("Dragoon")
            .role(DPS.MELEE)),
    PUGILIST(new Builder()
            .jobString("Pugilist")
            .role(DPS.MELEE)),
    MONK(new Builder()
            .jobString("Monk")
            .role(DPS.MELEE)),
    ROGUE(new Builder()
            .jobString("Rogue")
            .role(DPS.MELEE)),
    NINJA(new Builder()
            .jobString("Ninja")
            .role(DPS.MELEE)),
    REAPER(new Builder()
            .jobString("Reaper")
            .role(DPS.MELEE)),
    SAMURAI(new Builder()
            .jobString("Samurai")
            .role(DPS.MELEE)),

    ARCHER(new Builder()
            .jobString("Archer")
            .role(DPS.RANGE_MELEE)),
    BARD(new Builder()
            .jobString("Bard")
            .role(DPS.RANGE_MELEE)),
    DANCER(new Builder()
            .jobString("Dancer")
            .role(DPS.RANGE_MELEE)),
    MACHINIST(new Builder()
            .jobString("Machinist")
            .role(DPS.RANGE_MELEE)),

    THAUMATURGE(new Builder()
            .jobString("Thaumaturge")
            .role(DPS.MAGIC)),
    BLACK_MAGE(new Builder()
            .jobString("Black Mage")
            .role(DPS.MAGIC)),
    SUMMONER(new Builder()
            .jobString("Summoner")
            .role(DPS.MAGIC)),
    BLUE_MAGE(new Builder()
            .jobString("Blue Mage")
            .role(DPS.MAGIC)),
    RED_MAGE(new Builder()
            .jobString("Red Mage")
            .role(DPS.MAGIC)),
    ARCANIST(new Builder()
            .jobString("Arcanist")
            .role(DPS.MAGIC)),

    SCHOLAR(new Builder()
            .jobString("Scholar")
            .role(HEALER.BARRIER_HEALER)),
    CONJURER(new Builder()
            .jobString("Conjurer")
            .role(HEALER.PURE_HEALER)),
    WHITE_MAGE(new Builder()
            .jobString("White Mage")
            .role(HEALER.PURE_HEALER)),
    ASTROLOGIAN(new Builder()
            .jobString("Astrologian")
            .role(HEALER.PURE_HEALER)),
    SAGE(new Builder()
            .jobString("Sage")
            .role(HEALER.BARRIER_HEALER)),

    CARPENTER(new Builder()
            .jobString("Carpenter")
            .role(CRAFTER.DISCIPLE_OF_HAND)),
    BLACKSMITH(new Builder()
            .jobString("Blacksmith")
            .role(CRAFTER.DISCIPLE_OF_HAND)),
    ARMORER(new Builder()
            .jobString("Armorer")
            .role(CRAFTER.DISCIPLE_OF_HAND)),
    GOLDSMITH(new Builder()
            .jobString("Goldsmith")
            .role(CRAFTER.DISCIPLE_OF_HAND)),
    LEATHERWORKER(new Builder()
            .jobString("Leatherworker")
            .role(CRAFTER.DISCIPLE_OF_HAND)),
    WEAVER(new Builder()
            .jobString("Weaver")
            .role(CRAFTER.DISCIPLE_OF_HAND)),
    ALCHEMIST(new Builder()
            .jobString("Alchemist")
            .role(CRAFTER.DISCIPLE_OF_HAND)),
    CULINARIAN(new Builder()
            .jobString("Culinarian")
            .role(CRAFTER.DISCIPLE_OF_HAND)),

    MINER(new Builder()
            .jobString("Miner")
            .role(CRAFTER.DISCIPLE_OF_LAND)),
    BOTANIST(new Builder()
            .jobString("Botanist")
            .role(CRAFTER.DISCIPLE_OF_LAND)),
    FISHER(new Builder()
            .jobString("Fisher")
            .role(CRAFTER.DISCIPLE_OF_LAND));


    private static final Map<String, JOB> jobLookUp =
            Collections.unmodifiableMap(initMap(JOB::getJobString));

    enum TANK implements Role {
        TANK
    }

    enum CRAFTER implements Role {
        DISCIPLE_OF_HAND, DISCIPLE_OF_LAND;
    }

    enum HEALER implements Role {
        PURE_HEALER, BARRIER_HEALER
    }

    enum DPS implements Role {
        MAGIC, RANGE_MELEE, MELEE
    }

    private final String jobString;
    private final Role role;

    JOB(Builder builder) {

        this.jobString = builder.jobString;
        this.role = builder.role;
    }

    static class Builder {
        private String jobString;
        private Role role;


        Builder role(Role role) {
            this.role = role;
            return this;
        }

        Builder jobString(String jobString) {
            this.jobString = jobString;
            return this;
        }

    }

    @JsonValue
    public String getJobString() {
        return jobString;
    }

    public Role getRole() {
        return role;
    }

    private static Map<String, JOB> initMap(Function<JOB, String> func) {
        Map<String, JOB> map = new HashMap<>();
        Arrays.stream(JOB.values()).forEach(job ->
                map.put(func.apply(job), job));
        return map;
    }

    public static JOB getJob(String jobName) {
        return jobLookUp.get(jobName);
    }
}
