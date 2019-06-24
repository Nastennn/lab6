package story;
public class Tree implements Plant{

    private String name;
    private int height;

    Tree(String name, int height){
        this.name = name;
        this.height = height;
    }


    @Override
    public boolean grow() {
        return true;
    }



}
