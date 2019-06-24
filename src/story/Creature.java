package story;
public abstract class Creature {
    private transient String name;

    void setName(String name){
        this.name = name;
    }

    String getName(){
        return name;
    }

}

