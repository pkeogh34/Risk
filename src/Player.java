//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import java.awt.*;
import java.util.ArrayList;

public class Player {
    private final String playerName;
    private final int playerCode;
    private final Color playerColour;
    private int numArmies;
    private int[] numTerritoriesInContinent = {0, 0, 0, 0, 0, 0};
    private ArrayList<Territory> playerTerritories = new ArrayList<>();
    private ArrayList<TerritoryCard> territoryCards = new ArrayList<TerritoryCard>();
    private boolean defeated=false;

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

    public void removeTerritory(int territoryCode) {
        for(int i=0;i<playerTerritories.size();i++){
            if(playerTerritories.get(i).territoryCode==territoryCode){
                numTerritoriesInContinent[playerTerritories.get(i).continentCode]--;
                this.playerTerritories.remove(i);
                break;
            }
        }
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

    public ArrayList<Territory> getPlayerTerritories(){
        return playerTerritories;
    }


    public void drawCard(Deck d)
    {
        TerritoryCard tc = d.drawCard();
        territoryCards.add(tc);
    }

    public String showCards()
    {
        String s = "";
        for (int i = 0; i < territoryCards.size(); i++)
        {
            s += territoryCards.get(i);

        }
        return s;

    }

    public boolean getStatus() {
        return defeated;
    }

    public void setStatus() {
        this.defeated = true;
    }
}

