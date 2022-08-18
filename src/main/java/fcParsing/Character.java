package fcParsing;

import webapp.App;
import webapp.FcParserController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Character implements Serializable{
    private LinkedHashMap<JOB, Integer> jobs;
    private String characterLink;
    private String characterName;
    private int minionsNumber, mountsNumber, cappedJobsNumber;
    private ArrayList<String> minionsList;
    private ArrayList<String> mountsList;

    public Character() {
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getCharacterLink() {
        return characterLink;
    }

    public void setCharacterLink(String characterLink) {
        this.characterLink = characterLink;
    }

    public Map<JOB, Integer> getJobs() {
        return jobs;
    }

    public void setJobs(LinkedHashMap<JOB, Integer> jobs) {
        this.jobs = jobs;
    }

    public void setMinionsList(ArrayList<String> minionsList) {
        this.minionsList = minionsList;
    }

    public ArrayList<String> getMinionsList() {
        return minionsList;
    }

    public void setMinionsNumber(int minionsNumber) {
        this.minionsNumber = minionsNumber;
    }

    public int getMinionsNumber() {
        return minionsNumber;
    }

    public void setMountsList(ArrayList<String> mountsList) {
        this.mountsList = mountsList;
    }

    public ArrayList<String> getMountsList() {
        return mountsList;
    }

    public void setMountsNumber(int mountsNumber) {
        this.mountsNumber = mountsNumber;
    }

    public int getMountsNumber() {
        return mountsNumber;
    }

    public int calculateCid(){
        int cid = 0;

        cid += jobs.values().stream()
                .filter(val -> val == FcParserController.maxLvl).count();
        return cid;
    }

    public void setCappedJobsNumber(int cappedJobsNumber) {
        this.cappedJobsNumber = cappedJobsNumber;
    }

    public int getCappedJobsNumber() {
        return cappedJobsNumber;
    }
}
