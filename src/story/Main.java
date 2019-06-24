package story;
import java.util.*;


public class Main {
    public static void main(String[] args) throws RoomOverflowException, GiantHarpException {

        story.Location danceFloor = new Location("Танцевальная площадка", 5, "Вокруг танцевальной площадки красовались нарядные палатки. Над площадкой были протянуты веревочки, на которых висели разноцветные фонарики и флажки.");
        Location stall = new Location("Палатки", 5, "Вокруг танцевальной площадки красовались нарядные палатки. Они сверкали яркими красками, словно пряничные избушки.");
        Location pergola = new Location("Беседка", 5, "Беседка была украшена цветами. На втором этаже играл оркестр из 10 малышек.");
        HashSet<Location> locationList = new HashSet<>();
        locationList.add(danceFloor);
        locationList.add(stall);
        locationList.add(pergola);


       /* Character Neznaika = new Character("Незнайка", Town.Flower, 70);
        danceFloor.addCharacter(Neznaika);
        Character Gvozdik = new Character("Гвоздик", Town.Zmeevka, 66);
        danceFloor.addCharacter(Gvozdik);
        Character Shurupchik = new Character("Шурупчик", Town.Zmeevka, 71);
        danceFloor.addCharacter(Shurupchik);
        Character Bublik = new Character("Бублик", Town.Zmeevka, 76);
        stall.addCharacter(Bublik);
        Character Baby1 = new Character("Первая малышка", Town.Flower, 10);
        pergola.addCharacter(Baby1);
        Character Baby2 = new Character("Вторая малышка", Town.Flower, 10);
        pergola.addCharacter(Baby2);
        Harp harp1 =new Harp();
        harp1.setSize(Harp.Size.Giant);
        Harp harp2 = new Harp();
        harp2.setSize(Harp.Size.Small);
*/





        // Нет стремянки, добавить, стремянкой добавлять рост малышке, малышка может стоять на стремянке, стремянка
        // объект класса тоНаЧемМожноСтоять, создавать стремянку, ставить малышку, тогда будет возможность
        // играть на гигантской арфе

        /*System.out.println("Начался бал. Прибывают первые гости: ");
        System.out.println(Neznaika.getAppearance());
        System.out.println(Neznaika.sitOnTree());
        System.out.println(Gvozdik.getAppearance());
        Scanner in = new Scanner(System.in);
        String txt;
        boolean isFound = false;
        do {
            System.out.println("Выберите локацию: ");
            for (Location location : locationList) {
                System.out.print(location.getLocationName() + ", ");
            }
            System.out.println("Выйти");
            txt = in.nextLine();
            for (Location location : locationList) {
                if (location.getLocationName().toLowerCase().equals(txt.toLowerCase())) {
                    System.out.println(location.getDescription());
                    location.generateEvent();
                    if(location == pergola){
                        System.out.println("Малышки взялись за арфы.");
                        Baby1.playHarp(harp1);
                        Baby2.playHarp(harp2);
                    }
                    isFound = true;
                    break;
                }
            }
            try {
                if (!isFound && !txt.toLowerCase().equals("выйти")) {
                    throw new LocationDoesNotExistException("Такой локации не существует");
                }

            } catch (LocationDoesNotExistException e) {
                System.out.println("Такой локации не существует!");
            }
            isFound = false;
        } while (!txt.toLowerCase().equals("выйти"));*/
    }
}
