import bg.sofia.uni.fmi.mjt.show.elimination.LowestRatingEliminationRule;
import bg.sofia.uni.fmi.mjt.show.ergenka.Ergenka;
import bg.sofia.uni.fmi.mjt.show.ergenka.HumorousErgenka;
import bg.sofia.uni.fmi.mjt.show.ergenka.RomanticErgenka;

public class Main {
    static void main() {
        Ergenka e1 = new HumorousErgenka("Name1", (short)20, 4, 5, 10);
        Ergenka e2 = new HumorousErgenka("Name2", (short)20, 4, 5, 9);
        Ergenka e3 = new HumorousErgenka("Name3", (short)20, 4, 5, 8);
        Ergenka e4 = new RomanticErgenka("Name4", (short)20, 4, 5, 7, "Mqsto1");
        Ergenka e5 = new RomanticErgenka("Name5", (short)20, 4, 5, 8, "Mqsto2");
        Ergenka e6 = new RomanticErgenka("Name6", (short)20, 4, 5, 10, "Mqsto3");

        Ergenka[] arr = new Ergenka[9];
        arr[0] = e1;
        arr[1] = null;
        arr[2] = e2;
        arr[3] = null;
        arr[4] = null;
        arr[5] = e3;
        arr[6] = e4;
        arr[7] = e5;
        arr[8] = e6;

        printErgenkas(arr);
        System.out.println();
        printErgenkas(new LowestRatingEliminationRule().eliminateErgenkas(arr));
    }

    static void printErgenkas(Ergenka[] ergenkas) {
        for (Ergenka ergenka : ergenkas) {
            if (ergenka == null) {
                System.out.println("null");
            }
            else {
                System.out.println(ergenka.getName() + ", " + ergenka.getRating());
            }
        }
    }
}
