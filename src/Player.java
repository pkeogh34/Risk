import java.awt.*;
import java.util.ArrayList;

public class Player {
    private String playerName;
    private Color playerColour;
    private int numArmies;
    private int[] numTerritoriesInContinent = {0,0,0,0,0,0};
    private ArrayList<Territory> playerTerritories= new ArrayList<Territory>();
    private ArrayList<TerritoryCard> territoryCards = new ArrayList<TerritoryCard>();
    private boolean isNeutral ;

    public Player(String playerName, Color playerColour,int numArmies, boolean isNeutral){
        this.playerName = playerName;
        this.playerColour = playerColour;
        this.numArmies = numArmies;
        this.isNeutral=isNeutral;

    }

    public String getPlayerName() {
        return playerName;
    }

    public int getNumArmies()
    {
       return numArmies;
    }
    public boolean getPlayerType()
    {
       return isNeutral;
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
}

