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
            uiWindow.displayString("Turn "+ numTurns);
            currPlayer=players[playerOrder[i]];
            while(currPlayer.getStatus()){
                i++;
                currPlayer=players[playerOrder[i]];
            }

            uiWindow.displayString("" + currPlayer.getPlayerName() +" (" + Constants.PLAYER_COLOR_NAME[currPlayer.getPlayerCode()] + "), it is your turn\n");
            if(i<=1){
                turnPlayer();
            }else{
                currPlayer.addArmies(getTroops());
                while(currPlayer.getNumArmies()>0) {
                    System.out.println(currPlayer.getNumArmies());
                    Random random =new Random();
                    int troops;
                    if(currPlayer.getNumArmies()==1){
                        troops=1;
                    }else{
                        troops=(random.nextInt(currPlayer.getNumArmies()-1)+1);
                    }
                    randTroopPlacement(troops);
                }
                uiWindow.displayString("" + currPlayer.getPlayerName() + " has placed all their troops\n");
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
                randTroopPlacement(3);
            }else{
                uiWindow.displayString("" + currPlayer.getPlayerName() +" (" + Constants.PLAYER_COLOR_NAME[currPlayer.getPlayerCode()] + "), it is your turn\n\nYou must place 1 troop in a territory that you own\n");
                randTroopPlacement(1);
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
        uiWindow.displayString("You have placed all your troops. It is now your attack phase.\nPlease enter 'CONTINUE' to attack with your troops or 'SKIP' to skip the Attack phase. You may also 'SKIP' at any time to move to the Fortify phase\n");
        command=uiWindow.getCommand();
        checkCommand(new String[]{"CONTINUE", "SKIP"});
        while(command.equals("CONTINUE")){
            attack();
            if(command.equals("SKIP")){
                break;
            }
            uiWindow.displayString("Please enter 'CONTINUE' to continue attacking with your troops or 'SKIP' to move to the Fortify phase\n");
            command=uiWindow.getCommand();
            checkCommand(new String[]{"CONTINUE", "SKIP"});
        }

        uiWindow.displayString("It is now your fortify phase.\nPlease enter 'CONTINUE' to fortify one of your territories or 'END' to skip the Fortify phase and end your turn\n");
        command=uiWindow.getCommand();
        checkCommand(new String[]{"CONTINUE", "END"});
        if(command.equals("CONTINUE")){
            fortify();
        }

    }

    public void randTroopPlacement(int troops){
        Random random =new Random();
        territoryCode=currPlayer.getPlayerTerritory(random.nextInt(currPlayer.getNumPlayerTerritories())).territoryCode;
        uiWindow.board.addUnits(territoryCode,troops);
        currPlayer.addArmies(-troops);
        if(troops==1){
            uiWindow.displayString("" + currPlayer.getPlayerName() + " placed " + troops + " troop in " + uiWindow.board.getTerritory(territoryCode).territoryName + "\n");
        }else {
            uiWindow.displayString("" + currPlayer.getPlayerName() + " placed " + troops + " troops in " + uiWindow.board.getTerritory(territoryCode).territoryName + "\n");
        }
        uiWindow.displayMap();
    }

    public static int diceRoll(){
        Random random =new Random();
        return random.nextInt(5)+1;
    }

    private void placeTroops(boolean initial){
        do{
            uiWindow.displayString("Please enter the name of the territory in which you wish to place your troops\n");
            command = uiWindow.getCommand();
            territoryCode = checkHasTerritory(1);
            uiWindow.displayString("Do you wish to place your troops in " + uiWindow.board.getTerritory(territoryCode).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\n");
            command = uiWindow.getCommand();
            checkCommand(new String[]{"YES", "NO"});
        } while (command.equals("NO"));

        int numTroops=3;
        if(!initial){
            do {
                uiWindow.displayString("Please enter the number of troops you wish to place: \n");
                checkNumber(1);
                uiWindow.displayString("Do you wish to place " + numTroops + " troops in " + uiWindow.board.getTerritory(territoryCode).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\nYou may enter 'RETURN' to choose the territory again\n");
                command = uiWindow.getCommand();
                checkCommand(new String[]{"YES", "NO", "RETURN"});
                if(command.equals("RETURN")){
                    placeTroops(false);
                    return;
                }
            } while (command.equals("NO"));
        }

        uiWindow.board.addUnits(territoryCode,numTroops);
        currPlayer.addArmies(-numTroops);
        uiWindow.displayMap();
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
        int attackingTerritory,defendingTerritory;
        do{
            do{
                uiWindow.displayString("Please enter the name of the territory from which you wish to attack\n");
                command = uiWindow.getCommand();
                checkCommand(new String[]{"SKIP"});
            }while(command.equals("CONTINUE"));
            if(command.equals("SKIP")){
                return;
            }
            territoryCode = checkHasTerritory(1);
            if(uiWindow.board.getNumUnits(territoryCode)<=1){
                uiWindow.displayString("The attacking territory must have at least 2 troops. Please try again");
                continue;
            }
            attackingTerritory=territoryCode;
            do {
                uiWindow.displayString("Do you wish to attack from " + uiWindow.board.getTerritory(attackingTerritory).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\n");
                command = uiWindow.getCommand();
                checkCommand(new String[]{"YES", "NO", "SKIP"});
            }while(command.equals("CONTINUE"));
            if(command.equals("SKIP")){
                return;
            }
        } while (command.equals("NO"));

        do{
            do {
                uiWindow.displayString("Please enter the name of the territory you wish to attack\n");
                command = uiWindow.getCommand();
                checkCommand(new String[]{"SKIP"});
            }while(command.equals("CONTINUE"));
            if(command.equals("SKIP")){
                return;
            }

            territoryCode = checkHasTerritory(2);
            defendingTerritory=territoryCode;
            do {
                uiWindow.displayString("Do you wish to attack " + uiWindow.board.getTerritory(defendingTerritory).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\nYou may enter 'RETURN' to attack from another territory\n");
                command = uiWindow.getCommand();
                checkCommand(new String[]{"YES", "NO","RETURN","SKIP"});
            }while(command.equals("CONTINUE"));
            if(command.equals("SKIP")){
                return;
            }
            if(command.equals("RETURN")){
                attack();
                return;
            }
        } while (command.equals("NO"));

        //TODO later: add a check to see if territories are adjacent, functionality to choose
        // number of dice, a check to see if number of dice is valid, functionality for executing
        // the attack, functionality to stop attack, functionality to deal with outcome of the
        // attack (e.g. moving troops from attacking territory, transferring ownership
        // of the territory, altering continent ownership), functionality to check if the defending player
        // was wiped out

        uiWindow.displayMap();
    }

    private void fortify() {
        //TODO later: add functionality for choosing territory to move troops from
        // and the territory to them to, check to see if there is a valid path between
        // these territories, functionality to get number of troops to be transferred, a check
        // to see if troop number is valid, functionality to transfer the troops,
        // functionality to end turn/skip fortify phase
    }

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

        if(command.equals("SKIP")){
            uiWindow.displayString("Are sure you wish to move to the Fortify phase?\nEnter 'YES' to move to the Fortify phase or 'NO' to continue Attack phase\n");
            checkCommand(new String[]{"YES", "NO"});
            if(command.equals("YES")){
                command="SKIP";
            }else{
                command="CONTINUE";
            }
            return;
        }else if(command.equals("END")){
            uiWindow.displayString("Are you sure you wish to end your turn?\nEnter 'YES' to end your turn or 'NO' to continue Fortify phase\n");
            checkCommand(new String[]{"YES", "NO"});
            if(command.equals("YES")){
                command="END";
            }else{
                command="CONTINUE";
            }
            return;
        }


        if (!check&&(!correctInputs[correctInputs.length - 1].equals("SKIP")||!correctInputs[correctInputs.length - 1].equals("END"))){
            uiWindow.displayString("You must enter " + msg.toString()  + ". Please enter your command again\n");
            command = uiWindow.getCommand();
            checkCommand(correctInputs);
        }
    }

    //Perhaps implement check to see if territory is valid first (within this check)
    //Recursive function to check if a player owns the entered territory
    public int checkHasTerritory(int checkType) {
        boolean check = false;
        int territoryCode=0;
        for (int i = 0; i < currPlayer.getNumPlayerTerritories(); i++) {

            if (command.substring(0,3).equalsIgnoreCase(currPlayer.getPlayerTerritory(i).territoryName.replaceAll(" ", "").substring(0,3))) {
                check = true;
                territoryCode=currPlayer.getPlayerTerritory(i).territoryCode;
            }
        }
        if (!check&&checkType==1) {
            uiWindow.displayString("You do not own this territory. Please enter the name of another territory\n");
            command = uiWindow.getCommand();
            territoryCode=checkHasTerritory(1);
        }else if(check&&checkType==2){
            uiWindow.displayString("You already own this territory. Please enter the name of another territory\n");
            command = uiWindow.getCommand();
            territoryCode=checkHasTerritory(2);
        }

        return territoryCode;
    }

    //Recursive function to check if the number supplied by the player is valid
    public int checkNumber(int numType){
        int number;
        try{
            Integer.parseInt(command);
        }catch(Exception e){
            uiWindow.displayString("You must enter a number. Please try again");
            command=uiWindow.getCommand();
            checkNumber(numType);
        }

        number=Integer.parseInt(command);

        if(number<=0){
            uiWindow.displayString("You must enter a number greater than 0. Please try again");
            command=uiWindow.getCommand();
            number=checkNumber(numType);
        }

        if(numType==1){
            if(number>currPlayer.getNumArmies()){
                uiWindow.displayString("You do not have that many troops. Please try again");
                command=uiWindow.getCommand();
                number=checkNumber(numType);
            }
        }else if(numType==2){
            if(number>currPlayer.getNumArmies()){
                uiWindow.displayString("You do not have that many troops. Please try again");
                command=uiWindow.getCommand();
                number=checkNumber(numType);
            }
        }

        return number;
    }
}
