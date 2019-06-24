package story;
public class Harp {
    public enum Size{
        Small, Middle, Big, Giant
    }


    private Size size;

    void setSize(Size size){
        this.size = size;
    }

    Size getSize(){
        return size;
    }

    void play(Character character) throws GiantHarpException {
        if(size == Size.Giant & character.getHeight() < 20){
            throw new GiantHarpException("Эта арфа слишком большая!");
        } else{
            System.out.println(character.getName() + " играет на арфе");
        }
    }

    void playWithLadder(Character character){
        if(size == Size.Giant & character.getHeight() < 20){
            System.out.println(character.getName() + " играет на арфе, стоя на стремянке.");
        }
    }

}
