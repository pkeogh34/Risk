public class Initialisation {
    public static GameLogic initialisation () {
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
        int countryId=0;
        for (i=0; i<Constants.NUM_PLAYERS; i++) {
            for (int j=0; j<Constants.INIT_COUNTRIES_PLAYER; j++) {
                board.addUnits(countryId, i, 1);
                countryId++;
            }
        }
        for (; i<Constants.NUM_PLAYERS_PLUS_NEUTRALS; i++) {
            for (int j=0; j<Constants.INIT_COUNTRIES_NEUTRAL; j++) {
                board.addUnits(countryId, i, 1);
                countryId++;
            }
        }
        // display map
        uiWindow.displayMap();

        GameLogic gameLogic = new GameLogic(board, uiWindow,players);

       return gameLogic;
    }
}
