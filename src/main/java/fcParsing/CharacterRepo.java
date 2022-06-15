package fcParsing;

import org.springframework.data.jpa.repository.JpaRepository;

interface CharacterRepo <Character> extends JpaRepository<Character,Integer> {
}
