import java.awt.*;
import java.util.ArrayList;

public class Player {
    private final String playerName;
    private final int playerCode;
    private final Color playerColour;
    private int numArmies;
    private int[] numTerritoriesInContinent = {0, 0, 0, 0, 0, 0};
    private ArrayList<Territory> playerTerritories = new ArrayList<Territory>();
    private TerritoryCard[] territoryCards = new TerritoryCard[5];

    public Player(String playerName, int playerCode, Color playerColour, int numArmies) {
        this.playerName = playerName;
        this.playerCode = playerCode;
        this.playerColour = playerColour;
        this.numArmies = numArmies;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getNumPlayerTerritories(){
        return playerTerritories.size();
    }

    public Territory getPlayerTerritory(int i) {
        return playerTerritories.get(i);
    }

    public void addTerritory(Territory territory) {
        numTerritoriesInContinent[territory.continentCode]++;
        this.playerTerritories.add(territory);
    }

    public int getNumTerritoriesInContinent(int continentCode) {
        return numTerritoriesInContinent[continentCode];
    }

    public int getNumArmies() {
        return numArmies;
    }

    public void addArmies(int numArmies) {
        this.numArmies += numArmies;
    }

    public int getPlayerCode() {
        return playerCode;
    }
}

