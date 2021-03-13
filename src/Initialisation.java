//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Initialisation {

    public static void initialisation (UIWindow uiWindow, Player[] players, ArrayList<Integer> playerOrder) {
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
                uiWindow.board.addUnits(territoryId, 1);
                players[i].addArmies(-1);
                uiWindow.board.setOccupier(territoryId,i);
                players[i].addTerritory(uiWindow.board.getTerritory(territoryId));
                territoryId++;
            }
        }
        for (; i<Constants.NUM_PLAYERS_PLUS_NEUTRALS; i++) {
            for (int j=0; j<Constants.INIT_COUNTRIES_NEUTRAL; j++) {
                uiWindow.board.addUnits(territoryId, 1);
                players[i].addArmies(-1);
                uiWindow.board.setOccupier(territoryId,i);
                players[i].addTerritory(uiWindow.board.getTerritory(territoryId));
                territoryId++;
            }
        }
        // display map
        uiWindow.displayMap();
    }

    public static void firstPlayer(UIWindow uiWindow, Player[] players, ArrayList<Integer> playerOrder) {
        int roll1 = 0, roll2 = 0;
        boolean equal = true;

        while (equal) {
            uiWindow.displayString("" + players[0].getPlayerName() + " please enter 'ROLL' to roll the dice\n");
            GameLogic.command = uiWindow.getCommand();
            checkCommand(new String[]{"ROLL"},uiWindow);
            roll1 = GameLogic.diceRoll();
            uiWindow.displayString("" + players[0].getPlayerName() + " rolled " + roll1 + "\n");

            uiWindow.displayString("" + players[1].getPlayerName() + " please enter 'ROLL' to roll the dice\n");
            GameLogic.command = uiWindow.getCommand();
            checkCommand(new String[]{"ROLL"},uiWindow);
            roll2 = GameLogic.diceRoll();
            uiWindow.displayString("" + players[1].getPlayerName() + " rolled " + roll2 + "\n");

            if (roll1 != roll2) {
                equal = false;
            }

            if (equal) {
                uiWindow.displayString("Players rolled the same number. Please roll again.\n");
            }
        }

        if (roll1 > roll2) {
            uiWindow.displayString("" + players[1].getPlayerName() + " will go first\n");
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

    public static void checkCommand(String[] correctInputs,UIWindow uiWindow) {
        boolean check=false;
        StringBuilder msg = new StringBuilder(("'" + correctInputs[0] + "'"));
        for (int i = 0; i < correctInputs.length;i++) {
            if(GameLogic.command.length()>=correctInputs[i].length()) {
                if (GameLogic.command.substring(0,correctInputs[i].length()).equalsIgnoreCase(correctInputs[i])){
                    GameLogic.command=correctInputs[i];
                    check=true;
                }
            }
            if(i==correctInputs.length-1 && i!=0){
                msg.append(" or '").append(correctInputs[i]).append("'");
            }else if(i>1){
                msg.append(", ").append(correctInputs[i]);
            }
        }
        if (!check) {
            uiWindow.displayString("You must enter " + msg.toString()  + ". Please enter your command again\n");
            GameLogic.command = uiWindow.getCommand();
            checkCommand(correctInputs,uiWindow);
        }
    }
}
