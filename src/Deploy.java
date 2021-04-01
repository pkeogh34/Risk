//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import java.util.ArrayList;

//todo: add functionality to allow the user to reenter instructions or not trade in cards
public class Deploy {
    public static int deploy(GameData gameData, int numSets){
        if(GameLogic.currPlayer.getTerritoryCards().size()>2){
            boolean check=false;
            if(GameLogic.currPlayer.getTerritoryCards().size()==3 || GameLogic.currPlayer.getTerritoryCards().size()==4){
                check=Checks.checkIsValidCombination(GameLogic.currPlayer.getCardTypes().get(0) + GameLogic.currPlayer.getCardTypes().get(1) +GameLogic.currPlayer.getCardTypes().get(2));
            }
            if(GameLogic.currPlayer.getTerritoryCards().size()==4 && !check) {
                check = Checks.checkIsValidCombination(GameLogic.currPlayer.getCardTypes().get(0) + GameLogic.currPlayer.getCardTypes().get(1) + GameLogic.currPlayer.getCardTypes().get(3));
                if(!check){
                    Checks.checkIsValidCombination(GameLogic.currPlayer.getCardTypes().get(0) + GameLogic.currPlayer.getCardTypes().get(2) + GameLogic.currPlayer.getCardTypes().get(3));
                }
                if(!check){
                    Checks.checkIsValidCombination(GameLogic.currPlayer.getCardTypes().get(1) + GameLogic.currPlayer.getCardTypes().get(2) + GameLogic.currPlayer.getCardTypes().get(3));
                }
            }

            if(GameLogic.currPlayer.getTerritoryCards().size()<5){
                if(check) {
                    GameLogic.uiWindow.displayString("Would you like to trade in territory cards?\nPlease enter 'YES' or 'NO'\nYou may also enter 'VIEW' to view you cards.\n");
                    Checks.checkCommand(new String[]{"YES", "NO", "VIEW"});
                }
                if(GameLogic.command.equals("VIEW")){
                    GameLogic.uiWindow.displayString(Deploy.showCards(gameData));
                    GameLogic.uiWindow.displayString("Would you like to trade in territory cards?\nPlease enter 'YES' or 'NO'");
                    Checks.checkCommand(new String[] {"YES", "NO"});
                }
            }

            if(GameLogic.currPlayer.getTerritoryCards().size()>=5){
                GameLogic.uiWindow.displayString("As you have more than four cards, you must trade in a set of territory cards");
                GameLogic.uiWindow.displayString("Would you like to view your territory cards?\nPlease enter 'YES' or 'NO'\n");
                Checks.checkCommand(new String[]{"YES", "NO"});

                if(GameLogic.command.equals("YES")){
                    GameLogic.uiWindow.displayString(Deploy.showCards(gameData));showCards(gameData);
                    GameLogic.uiWindow.displayString("Would you like to trade in territory cards?\nPlease enter 'YES' or 'NO'");
                    Checks.checkCommand(new String[] {"YES", "NO"});
                }
            }

            if(GameLogic.command.equals("YES") || GameLogic.currPlayer.getTerritoryCards().size()>=5) {
                numSets+=exchangeCards(gameData,numSets);//returns the number of sets turned in
            }
        }


        GameLogic.currPlayer.addArmies(GameLogic.getTroops());//Gives the current player the number of troops that they have earned
        GameLogic.placeTroops(false,gameData);//Allows the current player to place their troops
        while(GameLogic.currPlayer.getNumArmies()>0){//Loops until the player has placed all there troops
            GameLogic.uiWindow.displayString("You have " + GameLogic.currPlayer.getNumArmies() + " troops to place\n");
            GameLogic.placeTroops(false,gameData);
        }

        return numSets;
    }

    public static String showCards(GameData gameData) {
        StringBuilder str = new StringBuilder();
        GameLogic.uiWindow.displayString(("\nPlayer Cards: \n"));
        for (Deck.TerritoryCard territoryCard : GameLogic.currPlayer.getTerritoryCards()) {
            str.append(territoryCard.toString());
        }
        return str.toString();
    }

    private static int exchangeCards(GameData gameData,int numSets){
        int match;
        ArrayList<Deck.TerritoryCard> tempCards = new ArrayList<>(GameLogic.currPlayer.getTerritoryCards());
        ArrayList<String> tempTypes=new ArrayList<>(GameLogic.currPlayer.getCardTypes());
        int[] removedCards = new int[3];
        do{
            match=0;
            GameLogic.uiWindow.displayString("Please enter the insignia of the cars you wish to trade in: \n");
            GameLogic.command= GameLogic.uiWindow.getCommand();


            while(!Checks.checkIsValidCombination(GameLogic.command)){
                GameLogic.uiWindow.displayString("You must enter a valid combination\n");
                GameLogic.command= GameLogic.uiWindow.getCommand();
            }

            for(int i = 0; i < 3; i++){
                for (int j = 0; j < tempTypes.size(); j++) {
                    if(tempTypes.get(j).equals(("" + GameLogic.command.charAt(i)))){
                        match++;
                        removedCards[i]=tempCards.get(j).getTerritoryCode();
                        tempCards.remove(j);
                        tempTypes.remove(j);
                        break;
                    }
                }
            }
            if(match!=3){
                GameLogic.uiWindow.displayString("You do not have that combination of cards. Please try again\n");
            }
        }while(match!=3);

        GameLogic.currPlayer.setTerritoryCards(tempCards);
        GameLogic.currPlayer.setCardTypes(tempTypes);

        int cardMatchesTerritory=-1;
        for(int i = 0; i < 3 && cardMatchesTerritory<0; i++) {
            for(int j = 0; j < GameLogic.currPlayer.getNumPlayerTerritories() && cardMatchesTerritory<0; j++) {
                if(removedCards[i]==GameLogic.currPlayer.getPlayerTerritory(j).territoryCode){
                    cardMatchesTerritory=j;
                }
            }
        }

        numSets++;
        int numTroops;
        if(numSets<5){
            numTroops=Constants.NUM_ARMIES_FOR_SET[numSets-1];
        }else{
            numTroops=(15+((numSets-6)*5));
        }
        GameLogic.uiWindow.displayString("You have received " + numTroops + " troops for trading in set number " + numSets +"\n");
        GameLogic.currPlayer.addArmies(numTroops);

        if(cardMatchesTerritory>=0) {
            GameLogic.uiWindow.displayString("You have received two extra troops because you own " + GameLogic.currPlayer.getPlayerTerritory(cardMatchesTerritory).territoryName + ". They will be deployed directly\n");
            gameData.addUnits(GameLogic.currPlayer.getPlayerTerritory(cardMatchesTerritory).territoryCode, 2);
            GameLogic.uiWindow.displayMap();
        }

        return numSets;
    }
}
