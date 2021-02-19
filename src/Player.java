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

    public void checkHasTerritory(String territoryName ){
        boolean check=false;
        for(int i=0;i<playerTerritories.size();i++){
            if(!GameLogic.command.equals(territoryName)){
                uiWindow.displayString("You must enter '" + correctInput + "'. Please enter your command again\n");
                GameLogic.command=uiWindow.getCommand();
                checkCommand(correctInput);
            }
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public ArrayList<Territory> getPlayerTerritories() {
        return playerTerritories;
    }

    public void addTerritory(Territory territory) {
        numTerritoriesInContinent[territory.continentCode]++;
        this.playerTerritories.add(territory);
    }

    public int getNumTerritoriesInContinent(int continentCode) {
        return numTerritoriesInContinent[continentCode];
    }
}

