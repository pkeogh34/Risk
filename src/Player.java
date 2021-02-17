import java.awt.*;
import java.util.ArrayList;

public class Player {
    private String playerName;
    private Color playerColour;
    private int numArmies;
    private int[] numTerritoriesInContinent = {0,0,0,0,0,0};
    private ArrayList<Territory> playerTerritories= new ArrayList<Territory>();
    private TerritoryCard territoryCards[] = new TerritoryCard[5];

    public Player(String playerName, Color playerColour,int numArmies){
        this.playerName = playerName;
        this.playerColour = playerColour;
        this.numArmies = numArmies;

    }

    public String getPlayerName() {
        return playerName;
    }
}

