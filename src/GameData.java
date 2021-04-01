import java.util.ArrayList;

public class GameData {
    private final Territory[] territories =new Territory[Constants.NUM_COUNTRIES];
    public final Player[] players = new Player[6];
    public final Deck gameDeck = new Deck();//Territory card deck
    public ArrayList<Integer> playerOrder = new ArrayList<>();//Arraylist of the playing order, containing player codes

    GameData(){
        initialiseTerritories();
    }

    public void initialiseTerritories(){
        for (int i=0; i<Constants.NUM_COUNTRIES; i++) {
            String territoryName=Constants.COUNTRY_NAMES[i];
            String territoryContinent=Constants.CONTINENT_NAMES[Constants.CONTINENT_IDS[i]];
            int continentCode= Constants.CONTINENT_IDS[i];
            Territory territory = new Territory(territoryName,i, territoryContinent,continentCode, false,0);
            territories[i]=territory;
        }
    }
    public void initialisePlayersAndUnits() {
        String name;

        // display blank board
        GameLogic.uiWindow.displayMap();

        int i;
        //Initialise active players
        for(i = 0; i<Constants.NUM_PLAYERS; i++) {
            GameLogic.uiWindow.displayString("Enter the name of player " + (i + 1));
            name = GameLogic.uiWindow.getCommand();
            GameLogic.uiWindow.displayString("> " + name + "\n");
            Player player = new Player(name, i, Constants.getPlayerColors(i), Constants.INIT_UNITS_PLAYER);
            players[i] = player;
            if (i>=1){
                if(players[0].getPlayerName().equals(players[1].getPlayerName())) {
                    GameLogic.uiWindow.displayString("Players must have different names. Please enter player name again.\n");
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
                addUnits(territoryId, 1);
                players[i].addArmies(-1);
                setOccupier(territoryId,i);
                players[i].addTerritory(getTerritory(territoryId));
                territoryId++;
            }
        }
        for (; i<Constants.NUM_PLAYERS_PLUS_NEUTRALS; i++) {
            for (int j=0; j<Constants.INIT_COUNTRIES_NEUTRAL; j++) {
                addUnits(territoryId, 1);
                players[i].addArmies(-1);
                setOccupier(territoryId,i);
                players[i].addTerritory(getTerritory(territoryId));
                territoryId++;
            }
        }
        // display map
        GameLogic.uiWindow.displayMap();
    }

    public void firstPlayer() {
        int roll1 = 0, roll2 = 0;
        boolean equal = true;

        while (equal) {
            GameLogic.uiWindow.displayString("" + players[0].getPlayerName() + " please enter 'ROLL' to roll the dice\n");
            Checks.checkCommand(new String[]{"ROLL"});
            roll1 = GameLogic.diceRoll();
            GameLogic.uiWindow.displayString("" + players[0].getPlayerName() + " rolled " + roll1 + "\n");

            GameLogic.uiWindow.displayString("" + players[1].getPlayerName() + " please enter 'ROLL' to roll the dice\n");
            Checks.checkCommand(new String[]{"ROLL"});
            roll2 = GameLogic.diceRoll();
            GameLogic.uiWindow.displayString("" + players[1].getPlayerName() + " rolled " + roll2 + "\n");

            if (roll1 != roll2) {
                equal = false;
            }

            if (equal) {
                GameLogic.uiWindow.displayString("Players rolled the same number. Please roll again.\n");
            }
        }

        if (roll1 > roll2) {
            GameLogic.uiWindow.displayString("" + players[1].getPlayerName() + " will go first\n");
            playerOrder.add(0);
            playerOrder.add(1);
        }else{
            playerOrder.add(1);
            playerOrder.add(0);
        }

        playerOrder.add(2);
        playerOrder.add(3);
        playerOrder.add(4);
        playerOrder.add(5);
    }

    public void addUnits (int territory, int addNumUnits) {
        // prerequisite: country must be unoccupied or already occupied by this player
        if (!territories[territory].occupied) {
            territories[territory].occupied = true;
        }
        territories[territory].numOccupyingArmies += addNumUnits;
    }

    //todo for Patrick: Create just one getter and add getters in Territory class???
    public boolean isOccupied(int territory) {
        return territories[territory].occupied;
    }

    public int getOccupier(int territory){
        return territories[territory].playerCode;
    }

    public void setOccupier(int territory, int player){
        territories[territory].playerCode=player;
    }

    public int getNumUnits(int territory) {
        return territories[territory].numOccupyingArmies;
    }

    public Territory getTerritory(int territory){
        return territories[territory];
    }
}
