//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import java.util.Random;

public class GameLogic {
    Initialisation I;
    private final UIWindow uiWindow;
    private Player[] players;
    private final int[] playerOrder;
    private int numPlayers=6;
    private Player currPlayer;
    private int territoryCode;
    private int numTurns=1;
    public static String command; /*Maybe change to enum when all commands
                                    are known (will require changing return type
                                    of getCommand() function also).
                                    Creating a drop down menu is another option*/
    public GameLogic(){
        I=new Initialisation();
        I.initialisation();
        uiWindow=I.getUiWindow();
        players=I.getPlayers();
        playerOrder=I.getPlayerOrder();
    }

    public void game(){
        initialTroopPlacement();
        for(int i=0;numPlayers>1;i++){

            currPlayer=players[playerOrder[i]];
            uiWindow.displayString("Turn "+ numTurns);
            uiWindow.displayString("" + currPlayer.getPlayerName() +" (" + Constants.PLAYER_COLOR_NAME[currPlayer.getPlayerCode()] + "), it is your turn\n");
            if(i<=1){
               turnPlayer();
            }else{
                turnNeutral();
            }
            if(i==numPlayers){
                i=-1;
            }
            numTurns++;

        }
        uiWindow.displayString("" + currPlayer.getPlayerName() +" has one the game!");
    }

    public void initialTroopPlacement(){
        for(int i=0;numTurns<54;i++){
            currPlayer=players[playerOrder[i]];
            uiWindow.displayString("Turn "+ numTurns );
            if(i<=1){
                uiWindow.displayString("" + currPlayer.getPlayerName() +" (" + Constants.PLAYER_COLOR_NAME[currPlayer.getPlayerCode()] + "), it is your turn\n\nYou must place 3 troops in a territory that you own\n");
                //placeTroops(true);
                uiWindow.displayString("" + currPlayer.getPlayerName() +" (" + Constants.PLAYER_COLOR_NAME[currPlayer.getPlayerCode()] + "), it is your turn\n\nYou must place 1 troop in a territory that you own\n");
                Random random =new Random();
                territoryCode=currPlayer.getPlayerTerritory(random.nextInt(currPlayer.getNumPlayerTerritories())).territoryCode;
                uiWindow.board.addUnits(territoryCode,3);
                currPlayer.addArmies(-3);
                uiWindow.displayString("" + currPlayer.getPlayerName() + " placed their troops in " + uiWindow.board.getTerritory(territoryCode).territoryName + "\n");
                uiWindow.displayMap();
            }else{
                uiWindow.displayString("" + currPlayer.getPlayerName() +" (" + Constants.PLAYER_COLOR_NAME[currPlayer.getPlayerCode()] + "), it is your turn\n\nYou must place 1 troop in a territory that you own\n");
                Random random =new Random();
                territoryCode=currPlayer.getPlayerTerritory(random.nextInt(currPlayer.getNumPlayerTerritories())).territoryCode;
                uiWindow.board.addUnits(territoryCode,1);
                currPlayer.addArmies(-1);
                uiWindow.displayString("" + currPlayer.getPlayerName() + " placed their troops in " + uiWindow.board.getTerritory(territoryCode).territoryName + "\n");
                uiWindow.displayMap();
            }

            if(i==numPlayers-1){
                i=-1;
            }
            numTurns++;
        }
    }

    public void turnPlayer(){
        currPlayer.addArmies(getTroops());
        placeTroops(false);
        while(currPlayer.getNumArmies()>0){
            uiWindow.displayString("You have " + currPlayer.getNumArmies() + " troops to place\n");
            placeTroops(false);
        }
        uiWindow.displayString("You have placed all your troops. It is now your attack phase.\nPLease enter 'CONTINUE' to attack with your troops or 'SKIP' to skip attack phase\n");
        uiWindow.getCommand();
        checkCommand(new String[]{"CONTINUE", "SKIP"});
        if(command.equals("CONTINUE")){
            attack();
        }
    }

    public void turnNeutral(){

    }

    public static int diceRoll(){
        Random random =new Random();
        return random.nextInt(5)+1;
    }

    private void placeTroops(boolean initial){
        do{
            uiWindow.displayString("Please enter the name of the territory in which you wish to place your troops\n");
            command = uiWindow.getCommand();
            territoryCode = checkHasTerritory();
            uiWindow.displayString("Do you wish to place your troops in " + uiWindow.board.getTerritory(territoryCode).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\n");
            command = uiWindow.getCommand();
            checkCommand(new String[]{"YES", "NO"});
        } while (command.equals("NO"));

        int numTroops=3;
        if(!initial){
            do {
                uiWindow.displayString("Please enter the number of troops you wish to place: \n");
                numTroops = Integer.parseInt(uiWindow.getCommand());
                //Check has sufficient troops
                uiWindow.displayString("Do you wish to place " + numTroops + " troops in " + uiWindow.board.getTerritory(territoryCode).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\nYou may enter 'RETURN' to choose the territory again\n");
                command = uiWindow.getCommand();
                checkCommand(new String[]{"YES", "NO", "RETURN"});
            } while (command.equals("NO"));
        }
        if(!command.equals("RETURN")){
            uiWindow.board.addUnits(territoryCode,numTroops);
            currPlayer.addArmies(-numTroops);
            uiWindow.displayMap();
        }

    }

    private int getTroops(){
        int numTroops;
        StringBuilder strForNumTroops= new StringBuilder();
        numTroops= (int) Math.floor(currPlayer.getNumPlayerTerritories()/3);
        strForNumTroops.append("You received ").append(numTroops).append(" troops for holding ").append(currPlayer.getNumPlayerTerritories()).append(" territories\n");
        for(int i=0;i<Constants.NUM_CONTINENTS;i++){
            if(currPlayer.getNumTerritoriesInContinent(i)==Constants.CONTINENT_VALUES[0][i]){
                numTroops+=Constants.CONTINENT_VALUES[1][i];
                strForNumTroops.append("You received ").append(Constants.CONTINENT_VALUES[1][i]).append(" troops for holding ").append(Constants.CONTINENT_NAMES[i]).append("\n");
            }
        }
        if(numTroops<3){
            numTroops=3;
        }

        uiWindow.displayString("You have received " + numTroops + " troops in total");
        uiWindow.displayString(strForNumTroops.toString());
        return numTroops;
    }

    private void attack(){
        //Perhaps find a way to generalise getting territory name
        do{
            //check if attacking territory has more than one troop
            uiWindow.displayString("Please enter the name of the territory from which you wish to attack\n");
            command = uiWindow.getCommand();
            territoryCode = checkHasTerritory();
            uiWindow.displayString("Do you wish to attack from " + uiWindow.board.getTerritory(territoryCode).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\n");
            command = uiWindow.getCommand();
            checkCommand(new String[]{"YES", "NO"});
        } while (command.equals("NO"));

        do{
            uiWindow.displayString("Please enter the name of the territory you wish to attack\n");
            command = uiWindow.getCommand();
            territoryCode = checkHasTerritory();
            uiWindow.displayString("Do you wish to attack " + uiWindow.board.getTerritory(territoryCode).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\nYou may enter 'RETURN' to attack from another territory\n");
            command = uiWindow.getCommand();
            checkCommand(new String[]{"YES", "NO","RETURN"});
        } while (command.equals("NO"));

        if(!command.equals("RETURN")){
            uiWindow.displayMap();
        }
    }
    //Maybe just use checkCommand from initialisation class?
    //Recursive function to check if a player has entered a valid command
    public void checkCommand(String[] correctInputs) {
        boolean check=false;
        StringBuilder msg = new StringBuilder(("'" + correctInputs[0] + "'"));
        for (int i = 0; i < correctInputs.length;i++) {
            if(command.length()>=correctInputs[i].length()) {
                if (command.substring(0,correctInputs[i].length()).equalsIgnoreCase(correctInputs[i])){
                    command=correctInputs[i];
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
            command = uiWindow.getCommand();
            checkCommand(correctInputs);
        }
    }

    //Recursive function to check if a player owns the entered territory
    public int checkHasTerritory() {
        boolean check = false;
        int territoryCode=0;
        for (int i = 0; i < currPlayer.getNumPlayerTerritories(); i++) {

            if (command.substring(0,3).equalsIgnoreCase(currPlayer.getPlayerTerritory(i).territoryName.replaceAll(" ", "").substring(0,3))) {
                check = true;
                territoryCode=currPlayer.getPlayerTerritory(i).territoryCode;
            }
        }
        if (!check) {
            uiWindow.displayString("You do not own this territory. Please enter the name of another territory\n");
            GameLogic.command = uiWindow.getCommand();
            territoryCode=checkHasTerritory();
        }

        return territoryCode;
    }
}
