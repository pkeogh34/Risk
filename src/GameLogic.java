//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import java.util.ArrayList;
import java.util.Random;

public class GameLogic {
    private final Board board = new Board();
    private final UIWindow uiWindow = new UIWindow(board);
    private final Player[] players = new Player[6];
    private ArrayList<Integer> playerOrder = new ArrayList<>();
    private Player currPlayer;
    private int territoryCode;
    private int numTurns=1;
    private int numSets=0;
    public static String command; /*Maybe change to enum when all commands
                                    are known (will require changing return type
                                    of getCommand() function also).
                                    Creating a drop down menu is another option*/
    public GameLogic(){
        Initialisation.initialisation(uiWindow,players,playerOrder);
        Initialisation.firstPlayer(uiWindow,players,playerOrder);
    }

    public void game(){
        initialTroopPlacement();
        for(int i = 0; !command.equals("GAME OVER"); i++){
            uiWindow.displayString("Turn "+ numTurns);
            currPlayer=players[playerOrder.get(i)];
            while(currPlayer.getStatus()){
                i++;
                currPlayer=players[playerOrder.get(i)];
            }

            uiWindow.displayString("" + currPlayer.getPlayerName() +" (" + Constants.PLAYER_COLOR_NAME[currPlayer.getPlayerCode()] + "), it is your turn\n");
            if(i<=1){
                turnPlayer();
            }else{
                currPlayer.addArmies(getTroops());
                while(currPlayer.getNumArmies()>0) {
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
            if(i==playerOrder.size()-1){
                i=-1;
            }
            numTurns++;
        }
        uiWindow.displayString("" + currPlayer.getPlayerName() +" has one the game!");
    }

    public void initialTroopPlacement(){
        for(int i=0;numTurns<=54;i++){
            currPlayer=players[playerOrder.get(i)];
            uiWindow.displayString("Turn "+ numTurns );
            if(i<=1){
                uiWindow.displayString("" + currPlayer.getPlayerName() +" (" + Constants.PLAYER_COLOR_NAME[currPlayer.getPlayerCode()] + "), it is your turn\n\nYou must place 3 troops in a territory that you own\n");
                //placeTroops(true);
                randTroopPlacement(3);
            }else{
                uiWindow.displayString("" + currPlayer.getPlayerName() +" (" + Constants.PLAYER_COLOR_NAME[currPlayer.getPlayerCode()]+ "), it is your turn\n\nYou must place 2 troops in a territory that you own\n");
                randTroopPlacement(2);
            }

            if(i==playerOrder.size()-1){
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

        uiWindow.displayString("You have placed all your troops. It is now your attack phase.\nPlease enter 'CONTINUE' to attack with your troops or 'SKIP' to skip the Attack phase. You may also enter 'SKIP' at any time to move to the Fortify phase\n");
        checkCommand(new String[]{"CONTINUE", "SKIP"});
        while(command.equals("CONTINUE")){
            attack();
            if(command.equals("GAME OVER")){
                return;
            }
            if(command.equals("SKIP")){
                break;
            }
            uiWindow.displayString("Please enter 'CONTINUE' to continue attacking with your troops or 'SKIP' to move to the Fortify phase\n");
            checkCommand(new String[]{"CONTINUE", "SKIP"});
        }

        uiWindow.displayString("It is now your fortify phase.\nPlease enter 'CONTINUE' to fortify one of your territories or 'END' to skip the Fortify phase and end your turn\n");
        checkCommand(new String[]{"CONTINUE", "END"});
        if(command.equals("CONTINUE")){
            fortify();
        }

    }

    public void randTroopPlacement(int troops){
        Random random =new Random();
        territoryCode=currPlayer.getPlayerTerritory(random.nextInt(currPlayer.getNumPlayerTerritories())).territoryCode;
        board.addUnits(territoryCode,troops);
        currPlayer.addArmies(-troops);
        if(troops==1){
            uiWindow.displayString("" + currPlayer.getPlayerName() + " placed " + troops + " troop in " + board.getTerritory(territoryCode).territoryName + "\n");
        }else {
            uiWindow.displayString("" + currPlayer.getPlayerName() + " placed " + troops + " troops in " + board.getTerritory(territoryCode).territoryName + "\n");
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
            uiWindow.displayString("Do you wish to place your troops in " + board.getTerritory(territoryCode).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\n");
            checkCommand(new String[]{"YES", "NO"});
        } while (command.equals("NO"));

        int numTroops=3;
        if(!initial){
            do {
                uiWindow.displayString("Please enter the number of troops you wish to place: \n");
                command= uiWindow.getCommand();
                numTroops=checkNumber(1);
                uiWindow.displayString("Do you wish to place " + numTroops + " troops in " + board.getTerritory(territoryCode).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\nYou may enter 'RETURN' to choose the territory again\n");
                checkCommand(new String[]{"YES", "NO", "RETURN"});
                if(command.equals("RETURN")){
                    placeTroops(false);
                    return;
                }
            } while (command.equals("NO"));
        }

        board.addUnits(territoryCode,numTroops);
        currPlayer.addArmies(-numTroops);
        uiWindow.displayMap();
    }

    private int getTroops(){
        int numTroops;
        StringBuilder strForNumTroops= new StringBuilder();
        numTroops= (int) Math.floor(currPlayer.getNumPlayerTerritories()/3.0);
        strForNumTroops.append("You received ").append(numTroops).append(" troops for holding ").append(currPlayer.getNumPlayerTerritories()).append(" territories\n");
        for(int i=0;i<Constants.NUM_CONTINENTS;i++){
            if(currPlayer.getNumTerritoriesInContinent(i)==Constants.CONTINENT_VALUES[0][i]){
                numTroops+=Constants.CONTINENT_VALUES[1][i];
                strForNumTroops.append("You received ").append(Constants.CONTINENT_VALUES[1][i]).append(" troops for holding ").append(Constants.CONTINENT_NAMES[i]).append("\n");
            }
        }

        if((currPlayer.getNumPlayerTerritories()<9&&numTroops<3)) {
            numTroops=3;
            uiWindow.displayString("You have received " + numTroops + " troops in total\n");
        }else{
            uiWindow.displayString("You have received " + numTroops + " troops in total\n");
            uiWindow.displayString(strForNumTroops.toString());
        }
        return numTroops;
    }

    private void attack(){
        //Perhaps find a way to generalise getting territory name
        int attackingTerritory=0,defendingTerritory;
        do{
            do{
                uiWindow.displayString("Please enter the name of the territory from which you wish to attack\n");
                checkCommand(new String[]{"SKIP"});
            }while(command.equals("CONTINUE"));
            if(command.equals("SKIP")){
                return;
            }
            territoryCode = checkHasTerritory(1);
            if(board.getNumUnits(territoryCode)<=1){
                uiWindow.displayString("The attacking territory must have at least 2 troops. Please try again");
                continue;
            }
            attackingTerritory=territoryCode;
            do {
                uiWindow.displayString("Do you wish to attack from " + board.getTerritory(attackingTerritory).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\n");
                checkCommand(new String[]{"YES", "NO", "SKIP"});
            }while(command.equals("CONTINUE"));
            if(command.equals("SKIP")){
                return;
            }
        } while (command.equals("NO"));

        do{
            do {
                uiWindow.displayString("Please enter the name of the territory you wish to attack\n");
                checkCommand(new String[]{"SKIP"});
                } while (command.equals("CONTINUE"));
                if (command.equals("SKIP")) {
                    return;
                }
                territoryCode = checkHasTerritory(2);
                checkAdjacent(attackingTerritory,territoryCode,1);
                defendingTerritory=territoryCode;

            do {
                uiWindow.displayString("Do you wish to attack " + board.getTerritory(defendingTerritory).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\nYou may enter 'RETURN' to attack from another territory\n");
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

        territoryCode=attackingTerritory;

        int numRedDice = 0;
        int numWhiteDice = 1;
        label:
        while(board.getTerritory(attackingTerritory).numOccupyingArmies>1&&board.getTerritory(defendingTerritory).numOccupyingArmies>0) {
            if(!command.equals("BLITZ")) {
                do {
                    uiWindow.displayString("Please enter the number of dice you wish to roll.\nEnter 'STOP' if you want to stop attacking or enter 'BLITZ' to auto-run the attack sequence\n");
                    checkCommand(new String[]{"SKIP", "STOP", "BLITZ"});
                } while (command.equals("CONTINUE"));
                if (command.equals("SKIP")) {
                    return;
                } else if (command.equals("STOP")) {
                    break;
                }
            }

            if (board.getTerritory(defendingTerritory).numOccupyingArmies > 1) {
                numWhiteDice = 2;
            }

            if(!command.equals("BLITZ")) {
                numRedDice = checkNumber(2);
                do {
                    uiWindow.displayString("Please enter 'ATTACK' to attack " + board.getTerritory(defendingTerritory).territoryName + ", 'CHANGE' to change the number of dice to attack with or 'STOP' if you wish to stop attacking\n");
                    checkCommand(new String[]{"ATTACK", "CHANGE", "STOP", "SKIP"});
                } while (command.equals("CONTINUE"));
                switch (command) {
                    case "SKIP":
                        return;
                    case "CHANGE":
                        continue;
                    case "STOP":
                        break label;
                }
            }else{
                numRedDice=board.getTerritory(attackingTerritory).numOccupyingArmies-1;
                if(numRedDice>3){
                    numRedDice=3;
                }
            }

            int[] redDice = new int[numRedDice];
            int[] whiteDice = new int[numWhiteDice];
            StringBuilder msg = new StringBuilder("" + currPlayer.getPlayerName() + " rolled ");
            for (int i = 0; i < numRedDice; i++) {
                redDice[i] = diceRoll();
                msg.append(redDice[i]);
                if (i != numRedDice - 1) {
                    msg.append(", ");
                } else {
                    msg.append("\n");
                }

                if (i > 0) {
                    if (redDice[i] > redDice[1]) {
                        int tmp = redDice[i];
                        redDice[i] = redDice[1];
                        redDice[1] = tmp;
                    }
                    if (redDice[1] > redDice[0]) {
                        int tmp = redDice[i];
                        redDice[i] = redDice[0];
                        redDice[0] = tmp;
                    }
                }
            }
            uiWindow.displayString(msg.toString());

            msg = new StringBuilder("" + players[board.getOccupier(defendingTerritory)].getPlayerName() + " rolled ");
            for (int i = 0; i < numWhiteDice; i++) {
                whiteDice[i] = diceRoll();
                msg.append(whiteDice[i]);
                if (i != numWhiteDice - 1) {
                    msg.append(", ");
                } else {
                    msg.append("\n");
                }

                if (i > 0) {
                    if (whiteDice[i] > whiteDice[0]) {
                        int tmp = whiteDice[i];
                        whiteDice[i] = whiteDice[0];
                        whiteDice[0] = tmp;
                    }
                }
            }
            uiWindow.displayString(msg.toString());

            int redLoss = 0, whiteLoss = 0;
            if (redDice[0] > whiteDice[0]) {
                whiteLoss++;
            } else {
                redLoss++;
            }

            if (numWhiteDice > 1 && numRedDice > 1) {
                if (redDice[1] > whiteDice[1]) {
                    whiteLoss++;
                } else {
                    redLoss++;
                }
            }

            if (redLoss > 0){
                if(redLoss > 1) {
                    uiWindow.displayString("" + currPlayer.getPlayerName() + " lost " + redLoss + " troops\n");
                }else {
                    uiWindow.displayString("" + currPlayer.getPlayerName() + " lost " + redLoss + " troop\n");
                }
            }
            if (whiteLoss > 0) {
                if(whiteLoss > 1) {
                    uiWindow.displayString("" + players[board.getOccupier(defendingTerritory)].getPlayerName() + " lost " + whiteLoss + " troops\n");
                }else {
                    uiWindow.displayString("" + players[board.getOccupier(defendingTerritory)].getPlayerName() + " lost " + whiteLoss + " troop\n");
                }
            }

            board.getTerritory(attackingTerritory).numOccupyingArmies -= redLoss;
            board.getTerritory(defendingTerritory).numOccupyingArmies -= whiteLoss;
            uiWindow.displayMap();
        }

        int defendingPlayer=board.getOccupier(defendingTerritory);
        if(board.getTerritory(defendingTerritory).numOccupyingArmies==0){
            uiWindow.displayString("" + players[defendingPlayer].getPlayerName() + " has lost " + board.getTerritory(defendingTerritory).territoryName);
            players[defendingPlayer].removeTerritory(defendingTerritory);
            board.setOccupier(defendingTerritory,currPlayer.getPlayerCode());
            currPlayer.addTerritory(board.getTerritory(defendingTerritory));

            int numTroopsToTransfer;
            uiWindow.displayString("" + currPlayer.getPlayerName() + ", you must enter the number of troops you wish to transfer to " + board.getTerritory(defendingTerritory).territoryName +"\n");
            uiWindow.displayString("As you rolled " + numRedDice + " dice on your last attack, you must transfer at least " + numRedDice + "\n");
            do {
                uiWindow.displayString("Please enter the number of troops to be transferred\n");
                command= uiWindow.getCommand();
                numTroopsToTransfer=checkNumber(4+(numRedDice));
                if(numTroopsToTransfer==1){
                    uiWindow.displayString("Do you wish to transfer " + numTroopsToTransfer + " troop into " + board.getTerritory(defendingTerritory).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\n");
                }else{
                    uiWindow.displayString("Do you wish to transfer " + numTroopsToTransfer + " troops into " + board.getTerritory(defendingTerritory).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\n");
                }
                checkCommand(new String[]{"YES", "NO"});
            } while (command.equals("NO"));

            board.addUnits(attackingTerritory,-numTroopsToTransfer);
            board.addUnits(defendingTerritory,numTroopsToTransfer);
            uiWindow.displayMap();

        }else if(board.getTerritory(attackingTerritory).numOccupyingArmies==1){
            uiWindow.displayString("" + currPlayer.getPlayerName() + " has failed to take over " + board.getTerritory(defendingTerritory).territoryName + "\n");
        }

        if(players[defendingPlayer].getNumPlayerTerritories()==0){
            uiWindow.displayString("" + players[defendingPlayer].getPlayerName() + " has been wiped out!\n");
            int i=0;
            while(playerOrder.get(i)!=defendingPlayer){
                i++;
            }
            if(defendingPlayer==0||defendingPlayer==1){
                command="GAME OVER";
            }
            playerOrder.remove(i);
        }
    }

    private void fortify() {
        int territory1=0,territory2=0;
        do{
            do{
                uiWindow.displayString("Please enter the name of the territory from which you wish to move your troops\n");
                checkCommand(new String[]{"END"});
            }while(command.equals("CONTINUE"));
            if(command.equals("END")){
                return;
            }
            territoryCode = checkHasTerritory(1);
            if(board.getNumUnits(territoryCode)<=1){
                uiWindow.displayString("The territory must have at least 2 troops. Please try again\n");
                continue;
            }
            territory1=territoryCode;
            do {
                uiWindow.displayString("Do you wish to move your troops from " + board.getTerritory(territory1).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\n");
                checkCommand(new String[]{"YES", "NO", "SKIP"});
            }while(command.equals("CONTINUE"));
            if(command.equals("SKIP")){
                return;
            }
        } while (command.equals("NO"));

        do{
            do {
                uiWindow.displayString("Please enter the name of the territory you wish to transfer your troops to\n");
                checkCommand(new String[]{"END"});
            } while (command.equals("CONTINUE"));
            if (command.equals("END")) {
                return;
            }

            do {
                territoryCode = checkHasTerritory(1);
                if(!checkHasValidPath(territory1,territoryCode,new ArrayList<>(currPlayer.getPlayerTerritories()))){
                    uiWindow.displayString("There is no valid path between these territories. Please select another territory\n");
                    continue;
                }
                territory2=territoryCode;
                uiWindow.displayString("Do you wish to transfer troops from " + board.getTerritory(territory1).territoryName + " to " + board.getTerritory(territory2).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\nYou may enter 'RETURN' to move troops from another territory\n");
                checkCommand(new String[]{"YES", "NO","RETURN","END"});
            }while(command.equals("CONTINUE"));
            if(command.equals("END")){
                return;
            }
            if(command.equals("RETURN")){
                fortify();
                return;
            }
        } while (command.equals("NO"));

        int numTroopsToTransfer;
        do{
            uiWindow.displayString("Please enter the number of troops to be transferred\n");
            command= uiWindow.getCommand();
            territoryCode=territory1;
            numTroopsToTransfer=checkNumber(3);
            if(numTroopsToTransfer==1){
                uiWindow.displayString("Do you wish to transfer " + numTroopsToTransfer + " troop into " + board.getTerritory(territory2).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\n");
            }else{
                uiWindow.displayString("Do you wish to transfer " + numTroopsToTransfer + " troops into " + board.getTerritory(territory2).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\n");
            }
            checkCommand(new String[]{"YES", "NO"});
        } while (command.equals("NO"));

        board.addUnits(territory1,-numTroopsToTransfer);
        board.addUnits(territory2,numTroopsToTransfer);
        uiWindow.displayMap();
    }

    //Recursive function to check if a player has entered a valid command
    public void checkCommand(String[] correctInputs) {
        command= uiWindow.getCommand();
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

        switch (command) {
            case "SKIP" -> {
                uiWindow.displayString("Are sure you wish to move to the Fortify phase?\nEnter 'YES' to move to the Fortify phase or 'NO' to continue Attack phase\n");
                checkCommand(new String[]{"YES", "NO"});
                if (command.equals("YES")) {
                    command = "SKIP";
                } else {
                    command = "CONTINUE";
                }
                return;
            }
            case "STOP" -> {
                uiWindow.displayString("Are you sure you wish to stop attacking?\nEnter 'YES' to stop attacking or 'NO' to continue attacking\n");
                checkCommand(new String[]{"YES", "NO"});
                if (command.equals("YES")) {
                    command = "STOP";
                } else {
                    command = "CONTINUE";
                }
                return;
            }
            case "END" -> {
                uiWindow.displayString("Are you sure you wish to end your turn?\nEnter 'YES' to end your turn or 'NO' to continue Fortify phase\n");
                checkCommand(new String[]{"YES", "NO"});
                if (command.equals("YES")) {
                    command = "END";
                } else {
                    command = "CONTINUE";
                }
                return;
            }
        }


        if (!check&&!correctInputs[0].equals("SKIP")&&!correctInputs[0].equals("END")){
            uiWindow.displayString("You must enter " + msg.toString()  + ". Please enter your command again\n");
            checkCommand(correctInputs);
        }
    }

    //todo: Perhaps implement check to see if territory is valid first (within this check)
    //Recursive function to check if a player owns the entered territory
    public int checkHasTerritory(int checkType) {
        boolean check = false;
        int territoryCode=0,cutOff=4;
        if(command.length()<4){
            cutOff=command.length();
            if(command.length()<2) {
                uiWindow.displayString("You must enter a valid territory name. Please try again\n");
                command = uiWindow.getCommand();
                territoryCode = checkHasTerritory(1);
            }
        }
        for (int i = 0; i < currPlayer.getNumPlayerTerritories(); i++) {
            if (command.substring(0,cutOff).equalsIgnoreCase(currPlayer.getPlayerTerritory(i).territoryName.replaceAll(" ", "").substring(0,cutOff))) {
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

        if(checkType==2){
            for (int i = 0; i < Constants.COUNTRY_NAMES.length; i++) {
                if (command.substring(0,3).equalsIgnoreCase(Constants.COUNTRY_NAMES[i].replaceAll(" ", "").substring(0,3))) {
                    territoryCode=i;
                }
            }
        }

        return territoryCode;
    }

    //Recursive function to check if the number supplied by the player is valid
    public int checkNumber(int numType){
        int number;
        try{
            Integer.parseInt(command);
        }catch(Exception e){
            uiWindow.displayString("You must enter a number. Please try again\n");
            command=uiWindow.getCommand();
            checkNumber(numType);
        }

        number=Integer.parseInt(command);

        if(number<=0){
            uiWindow.displayString("You must enter a number greater than 0. Please try again\n");
            command=uiWindow.getCommand();
            number=checkNumber(numType);
        }

        if(numType==1){
            if(number>currPlayer.getNumArmies()){
                uiWindow.displayString("You do not have that many troops. Please try again\n");
                command=uiWindow.getCommand();
                number=checkNumber(numType);
            }
        }else if(numType==2){
            if(number>3){
                uiWindow.displayString("The maximum number of dice that can be rolled is 3. Please try again\n");
                checkCommand(new String[]{"SKIP"});//todo
                number=checkNumber(numType);
            }else if(number>=board.getNumUnits(territoryCode)){
                uiWindow.displayString("You can only roll " + (board.getNumUnits(territoryCode)-1) + " dice. Please try again\n");
                checkCommand(new String[]{"SKIP"});//todo
                number=checkNumber(numType);
            }
        }else if(number==board.getTerritory(territoryCode).numOccupyingArmies){
            uiWindow.displayString("You must leave at least one troop in your territory at all times\n");
            command=uiWindow.getCommand();
            number=checkNumber(numType);
        }else if(number>board.getTerritory(territoryCode).numOccupyingArmies){
            uiWindow.displayString("You do not have that many troops to transfer\n");
            command=uiWindow.getCommand();
            number=checkNumber(numType);
        }else if(numType>3) {
            if (number < (numType - 4)) {
                if ((numType - 4) == 1) {
                    uiWindow.displayString("You must transfer at least " + (numType - 4) + " troop");
                } else {
                    uiWindow.displayString("You must transfer at least " + (numType - 4) + " troops");
                }
                command = uiWindow.getCommand();
                number = checkNumber(numType);
            }
        }

        return number;
    }

    public boolean checkAdjacent(int territory1, int territory2, int checkType) {
        boolean check = false;
        for (int i = 0; i < Constants.ADJACENT[territory1].length; i++) {
            if (Constants.ADJACENT[territory1][i] == territory2) {
                check=true;
                break;
            }
        }
        if(!check&&checkType==1) {
            uiWindow.displayString("These territories are not adjacent. Please enter the name of another territory\n");
            command = uiWindow.getCommand();
            territoryCode=checkHasTerritory(2);
            checkAdjacent(territory1,territoryCode,1);//maybe remove territory2
        }else return check || checkType != 2;

        return true;
    }

    private boolean checkHasValidPath(int territory1,int territory2,ArrayList<Territory> playerTerritories){
        int i=0;
        while(playerTerritories.size()>0&&i<playerTerritories.size()){
            if(checkAdjacent(territory1,territory2,2)){
                return true;
            }
            if(checkAdjacent(territory1,playerTerritories.get(i).territoryCode,2)){
                territory1=playerTerritories.get(i).territoryCode;
                playerTerritories.remove(i);
                i--;
                if(checkHasValidPath(territory1,territory2,playerTerritories)){
                    return true;
                }
            }
            i++;
        }
        return false;
    }
}
