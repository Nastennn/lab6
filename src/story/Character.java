package story;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.lang.Math;
import java.util.Date;
import java.util.Objects;

@XmlRootElement
public class Character
        extends Creature
        implements Activity, Comparable<Character>, Serializable {

    private String name;
    private Town town;
    private int height;
    transient private Appearance appearance;
    private double x;
    private double y;
    private Date dateOfBirth;


    public Character(String name, Town town, int height, double x, double y) {
        this.name = name;
        this.town = town;
        this.height = height;
        this.appearance = new Appearance();
        this.x=x;
        this.y =y;
        dateOfBirth = new Date();
    }

    String getAppearance() {
        return name + " выглядел так: " + this.appearance.getAppearance();
    }

    void setHeight(int height) {
        this.height = height;
    }

    int getHeight() {
        return height;
    }

    @Override
    public int hashCode() {
        int result = 23;
        result = 12 * result + name.length();
        result = 4 * result + name.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Character)) {
            return false;
        }

        Character character = (Character) o;
        return Objects.equals(name, character.name) &&
                Objects.equals(town, character.town);
    }

    @Override
    public String toString() {
        return "Имя: " + name + ". Город: " + town + ". Рост: " + height;
    }

    @Override
    public int compareTo(Character character) {
      return this.name.compareTo(character.name);
    }


    public static class Vocabulary {
        public static final String[] vocabulary = {
                "Бояться надо не смерти, а пустой жизни",
                "Любить - значит видеть человека таким, каким его задумал Бог",
                "Если не бегаешь, пока здоров, придется побегать, когда заболеешь",
                "Птицы поднимаются выше, когда летят против ветра",
                "Время - это песок. Жизнь - это вода. Слова - это ветер...",
                "Всю свою жизнь я проплавал в унитазе стилем баттерфляй",
                "Я как старая пальма на вокзале - никому не нужен, а выбросить жалко"
        };
    }


    private String generatePhrase() {
        int length = Vocabulary.vocabulary.length;
        int randomNum = (int) Math.round(Math.random() * (length - 1));
        return Vocabulary.vocabulary[randomNum];
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }


    public void setTown(Town town) {
        this.town = town;
    }


    public Town getTown() {
        return town;
    }

    public String think() {
        String thought = name + " подумал: \"" + generatePhrase() + "\"";
        return thought;
    }

    public String say() {
        Harp harp = new Harp();
        harp.setSize(Harp.Size.Giant);
        String phrase = "\"" + generatePhrase() + "\" - сказал " + name;
        return phrase;
    }

    public String dance() {
        return name + " танцует.";
    }

    void playHarp(Harp harp) throws GiantHarpException {
        try {
            harp.play(this);
        } catch (GiantHarpException e) {
            System.out.println("Это слишком большая арфа для малышки.");
            harp.playWithLadder(this);
        }
    }

    public String sitOnTree() {
        return name + " забрался на дерево.";
    }


}
