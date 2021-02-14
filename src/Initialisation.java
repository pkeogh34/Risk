public class Initialisation {
    public Initialisation(){
        initialisation();
    }
    public static void initialisation () {
        Board board = new Board();
        UIWindow uiWindow = new UIWindow(board);
        Player[] players = new Player[6];
        String name;

        // display blank board
        uiWindow.displayMap();

        int i;
        //Initialise active players
        for(i = 0; i<Constants.NUM_PLAYERS; i++) {
            uiWindow.displayString("Enter the name of player " + (i+1));
            name = uiWindow.getCommand();
            uiWindow.displayString("> " + name);
            Player player = new Player(name, Constants.getPlayerColors(i),Constants.INIT_UNITS_PLAYER);
            players[i]=player;
        }

        //Initialise neutral players
        for(;i<Constants.NUM_NEUTRALS+2;i++){
            Player neutralPlayer = new Player(("Neutral Player " + i),Constants.getPlayerColors(i),Constants.INIT_UNITS_NEUTRAL);
            players[i]=neutralPlayer;
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
