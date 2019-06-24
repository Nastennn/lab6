package story;

public class Location {
    private String locationName;
    private int counter;
    private int space;
    private Character[] characters;
    private String description;

    Location(String locationName, int space, String description) {
        counter = 0;
        this.locationName = locationName;
        this.space = space;
        characters = new Character[getSpace()];
        this.description = description;
    }



    String getDescription(){
        return description;
    }

    String getLocationName(){
        return locationName;
    }

    void addCharacter(Character character) throws RoomOverflowException {
        if (getFreeSpace() > 0) {
            characters[counter] = character;
            counter++;
        } else {
            throw new RoomOverflowException("Локация переполнена");
        }
    }

    public void generateEvent() {
        class Event {
            void execute() {
                if (counter == 1) {
                    System.out.println("Тем временем в локации " + locationName + ": ");
                    System.out.println(characters[randomNum()].think());
                } else if (counter > 0) {
                    System.out.println("Тем временем в локации  " + locationName + ": ");
                    System.out.println(characters[randomNum()].think());
                    System.out.println(characters[randomNum()].say());
                }
            }

            private int randomNum() {
                return (int) Math.floor(Math.random() * counter);
            }
        }
        Event event = new Event();
        event.execute();
    }

    private int getSpace() {
        return space;
    }

    private int getFreeSpace() {
        int freeSpace = space - counter;
        return freeSpace;
    }

}
