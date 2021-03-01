//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

public class Initialisation {
    private final Board board = new Board();
    private final UIWindow uiWindow = new UIWindow(board);
    private Player[] players = new Player[6];
    private int[] playerOrder={0,1,2,3,4,5};

    public void initialisation () {
        String name;
        // display blank board
        uiWindow.displayMap();

        int i;
        //Initialise active players
        for(i = 0; i<Constants.NUM_PLAYERS; i++) {
            uiWindow.displayString("Enter the name of player " + (i + 1));
            name = uiWindow.getCommand();
            uiWindow.displayString("> " + name + "\n");
            Player player = new Player(name, i, Constants.getPlayerColors(i), Constants.INIT_UNITS_PLAYER);
            players[i] = player;
            if (i>=1){
                if(players[0].getPlayerName().equals(players[1].getPlayerName())) {
                    uiWindow.displayString("Players must have different names. Please enter player name again.\n");
                    i--;
                }
            }
        }

        //Initialise neutral players
        for(;i<Constants.NUM_NEUTRALS+2;i++){
            Player neutralPlayer = new Player(("Neutral Player " + (i-1)),i,Constants.getPlayerColors(i),Constants.INIT_UNITS_NEUTRAL);
            players[i]=neutralPlayer;
        }

        // add units
        int territoryId=0;
        for (i=0; i<Constants.NUM_PLAYERS; i++) {
            for (int j=0; j<Constants.INIT_COUNTRIES_PLAYER; j++) {
                board.addUnits(territoryId, 1);
                players[i].addArmies(-1);
                board.setOccupier(territoryId,i);
                players[i].addTerritory(board.getTerritory(territoryId));
                territoryId++;
            }
        }
        for (; i<Constants.NUM_PLAYERS_PLUS_NEUTRALS; i++) {
            for (int j=0; j<Constants.INIT_COUNTRIES_NEUTRAL; j++) {
                board.addUnits(territoryId, 1);
                players[i].addArmies(-1);
                board.setOccupier(territoryId,i);
                players[i].addTerritory(board.getTerritory(territoryId));
                territoryId++;
            }
        }
        // display map
        uiWindow.displayMap();

        firstPlayer();
    }

    public void firstPlayer() {
        int roll1 = 0, roll2 = 0;
        boolean equal = true;

        while (equal) {
            uiWindow.displayString("" + players[0].getPlayerName() + " please enter 'ROLL' to roll the dice\n");
            GameLogic.command = uiWindow.getCommand();
            checkCommand("ROLL");
            roll1 = GameLogic.diceRoll();
            uiWindow.displayString("" + players[0].getPlayerName() + " rolled " + roll1 + "\n");

            uiWindow.displayString("" + players[1].getPlayerName() + " please enter 'ROLL' to roll the dice\n");
            GameLogic.command = uiWindow.getCommand();
            checkCommand("ROLL");
            roll2 = GameLogic.diceRoll();
            uiWindow.displayString("" + players[1].getPlayerName() + " rolled " + roll2 + "\n");

            if (roll1 != roll2) {
                equal = false;
            }

            if (equal) {
                uiWindow.displayString("Players rolled the same number. Please roll again.\n");
            }
        }

        if (roll1 < roll2) {
            uiWindow.displayString("" + players[1].getPlayerName() + " will go first\n");
            playerOrder[0]=1;
            playerOrder[1]=0;
        }
    }



    public void checkCommand(String correctInput){
        if(!GameLogic.command.substring(0,correctInput.length()).equalsIgnoreCase(correctInput)){
            uiWindow.displayString("You must enter '" + correctInput + "'. Please enter your command again\n");
            GameLogic.command=uiWindow.getCommand();
            checkCommand(correctInput);
        }
    }

    public UIWindow getUiWindow() {
        return uiWindow;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int[] getPlayerOrder() {
        return playerOrder;
    }
}
