package fcParsing;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.LinkedList;

@Entity
public class FreeCompany implements Serializable {
    int idNumber;
    String companyName;
    LinkedList<Character> characters;

    public FreeCompany(){
        characters = new LinkedList<>();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }

    public LinkedList<Character> getCharacters() {
        return characters;
    }
}
