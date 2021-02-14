import java.awt.*;
import java.util.ArrayList;

public class Player {
    private static String playerName;
    private static Color playerColour;
    private static int numArmies;
    private static int[] numTerritoriesInContinent = {0,0,0,0,0,0};
    private static ArrayList<Territory> playerTerritories= new ArrayList<Territory>();
    private static TerritoryCard territoryCards[] = new TerritoryCard[5];

    public Player(String playerName, Color playerColour,int numArmies){
        this.playerName = playerName;
        this.playerColour = playerColour;
        this.numArmies = numArmies;

    }

}

