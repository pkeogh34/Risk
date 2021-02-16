//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

public class Main {
    public static void main (String args[]) {
        Initialisation initialisation = new Initialisation();
        GameLogic gameLogic=initialisation.initialisation();
        gameLogic.diceRoll();

    }
}
