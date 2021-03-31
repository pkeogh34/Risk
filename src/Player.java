//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import java.awt.*;
import java.util.ArrayList;

public class Player {
    private final String playerName;
    private final int playerCode;
    private final Color playerColour;
    private int numArmies;
    private final int[] numTerritoriesInContinent = {0, 0, 0, 0, 0, 0};
    private final ArrayList<Territory> playerTerritories = new ArrayList<>();
    private ArrayList<Deck.TerritoryCard> territoryCards = new ArrayList<>();
    private ArrayList<String> cardTypes = new ArrayList<>();

    public Player(String playerName, int playerCode, Color playerColour, int numArmies) {
        this.playerName = playerName;
        this.playerCode = playerCode;
        this.playerColour = playerColour;
        this.numArmies = numArmies;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getNumPlayerTerritories() {
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
        for (int i = 0; i < playerTerritories.size(); i++) {
            if (playerTerritories.get(i).territoryCode == territoryCode) {
                numTerritoriesInContinent[playerTerritories.get(i).continentCode]--;
                this.playerTerritories.remove(i);
                break;
            }
        }
    }

    public ArrayList<String> getCardTypes(){
        return cardTypes;
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

    public ArrayList<Territory> getPlayerTerritories() {
        return playerTerritories;
    }

    public void addTerritoryCard(Deck.TerritoryCard territoryCard){
        territoryCards.add(territoryCard);
        cardTypes.add(territoryCard.getType());
    }

    public ArrayList<Deck.TerritoryCard> getTerritoryCards(){
        return territoryCards;
    }

    public void transferCards(ArrayList<Deck.TerritoryCard> opponentCards){
        territoryCards.addAll(opponentCards);
    }

    public void setTerritoryCards(ArrayList<Deck.TerritoryCard> territoryCards){
        this.territoryCards = new ArrayList<>(territoryCards);
    }

    public void setCardTypes(ArrayList<String> cardTypes){
        this.cardTypes = new ArrayList<>(cardTypes);
    }

    public String showCards() {
        StringBuilder str = new StringBuilder();
        GameLogic.uiWindow.displayString(("Player Cards: \n" + territoryCards));
        for (Deck.TerritoryCard territoryCard : territoryCards) {
            str.append(territoryCard.toString());
        }
        return str.toString();

    }
}

