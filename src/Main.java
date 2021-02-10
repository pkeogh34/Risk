//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

public class Main {

    public static void main (String args[]) {
        Board board = new Board();
        UIWindow uiWindow = new UIWindow(board);
        int playerId, countryId;
        String name;

        // display blank board
        uiWindow.displayMap();

        // get player names
        for (playerId=0; playerId<Constants.NUM_PLAYERS; playerId++) {
            uiWindow.displayString("Enter the name of player " + (playerId+1));
            name = uiWindow.getCommand();
            uiWindow.displayString("> " + name);
        }

        // add units
        countryId = 0;
        for (playerId=0; playerId<Constants.NUM_PLAYERS; playerId++) {
            for (int i=0; i<Constants.INIT_COUNTRIES_PLAYER; i++) {
                board.addUnits(countryId, playerId, 1);
                countryId++;
            }
        }
        for (; playerId<Constants.NUM_PLAYERS_PLUS_NEUTRALS; playerId++) {
            for (int i=0; i<Constants.INIT_COUNTRIES_NEUTRAL; i++) {
                board.addUnits(countryId, playerId, 1);
                countryId++;
            }
        }

        // display map
        uiWindow.displayMap();

        return;
    }
}
