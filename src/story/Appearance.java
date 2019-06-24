package story;
public class Appearance {
    Hair hair;

    private class Hair {
        int length;
        String colour;

        void setColour(String colour) {
            this.colour = colour;
        }

        void setLength(int length) {
            this.length = length;
        }
    }

    private String[] adjectivesShirt ={
            "удобная","мятая","грязная","поношенная","чистая", "опрятная", "красивая"
    };
    private String[] adjectivesPants ={
            "удобные","мятые","грязные","поношенные","чистые", "опрятные", "выглаженные"
    };


    private String getRandomAdjective(String[] adjectives){
        int randomNum = (int) Math.round(Math.random() * (adjectives.length - 1));
        return adjectives[randomNum];
    }
    public String getAppearance(){
        return " рубашка была " + getRandomAdjective(adjectivesShirt) + ". Штаны были " + getRandomAdjective(adjectivesPants);
    }
}
