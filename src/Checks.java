import java.util.ArrayList;

public class Checks {
    //Recursive function to check if a player has entered a valid command
    public static String checkCommand(String[] correctInputs) {
        String command= GameLogic.uiWindow.getCommand();//Get player command
        boolean check=false;//Check to see if command is correct
        StringBuilder msg = new StringBuilder(("'" + correctInputs[0] + "'"));//Creates a String to hold the correct inputs
        for (int i = 0; i < correctInputs.length;i++) {
            if(command.length()>=correctInputs[i].length()) {
                if (command.substring(0,correctInputs[i].length()).equalsIgnoreCase(correctInputs[i])){
                    command=correctInputs[i];
                    check=true;
                }
            }
            if(i==correctInputs.length-1 && i!=0){
                msg.append(" or '").append(correctInputs[i]).append("'");
            }else if(i>=1){
                msg.append(", ").append(correctInputs[i]);
            }
        }

        //Switch statement for various commands that allow a player to end a phase early or stop attacking
        switch (command) {
            case "SKIP" -> {//Moves turn from attack phase to fortify phase
                GameLogic.uiWindow.displayString("Are sure you wish to move to the Fortify phase?\nEnter 'YES' to move to the Fortify phase or 'NO' to continue Attack phase\n");
                command=checkCommand(new String[]{"YES", "NO"});
                if (command.equals("YES")) {
                    command = "SKIP";
                } else {
                    command = "CONTINUE";
                }
                return command;
            }
            case "STOP" -> {//Stops an attack
                GameLogic.uiWindow.displayString("Are you sure you wish to stop attacking?\nEnter 'YES' to stop attacking or 'NO' to continue attacking\n");
                command=checkCommand(new String[]{"YES", "NO"});
                if (command.equals("YES")) {
                    command = "STOP";
                } else {
                    command = "CONTINUE";
                }
                return command;
            }
            case "END" -> {//Ends the players turn
                GameLogic.uiWindow.displayString("Are you sure you wish to end your turn?\nEnter 'YES' to end your turn or 'NO' to continue Fortify phase\n");
                command=checkCommand(new String[]{"YES", "NO"});
                if (command.equals("YES")) {
                    command = "END";
                } else {
                    command = "CONTINUE";
                }
                return command;
            }
        }

        //Displays the correct inputs that the player must enter and asks them to enter a command again
        if (!check&&!correctInputs[0].equals("SKIP")&&!correctInputs[0].equals("END")){
            GameLogic.uiWindow.displayString("You must enter " + msg.toString()  + ". Please enter your command again\n");
            command=checkCommand(correctInputs);//Checks the new command
        }

        return command;
    }

    //Recursive function to check if a player owns the entered territory
    public static int checkHasTerritory(int checkType,String command) {
        boolean check = false;
        int territoryCode=0,cutOff=4;
        if(command.length()<4){
            cutOff=command.length();
            if(cutOff==3 && command.substring(0,cutOff).equalsIgnoreCase("gre" ) || command.substring(0,cutOff).equalsIgnoreCase("ind")){
                cutOff=2;
            }
        }

        for (int i = 0; i < Constants.COUNTRY_NAMES.length; i++) {
            if (command.substring(0,cutOff).equalsIgnoreCase(Constants.COUNTRY_NAMES[i].replaceAll(" ", "").substring(0,cutOff))) {
                command=Constants.COUNTRY_NAMES[i];
                check=true;
            }
        }

        if(cutOff<=2) {
            check=false;
        }
        if(!check){
            System.out.println(command);
            GameLogic.uiWindow.displayString("You must enter a valid territory name. Please try again\n");
            command = GameLogic.uiWindow.getCommand();
            System.out.println(command);
            territoryCode = checkHasTerritory(checkType,command);
            return territoryCode;
        }

        check=false;
        for (int i = 0; i < GameLogic.currPlayer.getNumPlayerTerritories(); i++) {
            if (command.equalsIgnoreCase(GameLogic.currPlayer.getPlayerTerritory(i).territoryName)) {
                check = true;
                territoryCode=GameLogic.currPlayer.getPlayerTerritory(i).territoryCode;
            }
        }

        if (!check&&checkType==1) {//Tells the player that they do not own the territory and must select another one
            GameLogic.uiWindow.displayString("You do not own " + command + ". Please enter the name of another territory\n");
            command = GameLogic.uiWindow.getCommand();
            territoryCode=checkHasTerritory(1,command);
            return territoryCode;
        }else if(check&&checkType==2){//Tells the player that they do not own the territory and must select another one (for attacking)
            GameLogic.uiWindow.displayString("You already own " + command + ". Please enter the name of another territory\n");
            command = GameLogic.uiWindow.getCommand();
            territoryCode=checkHasTerritory(2,command);
            return territoryCode;
        }

        //If a territory that a player does not own is required,  this searches all territories
        if(checkType==2){
            for (int i = 0; i < Constants.COUNTRY_NAMES.length; i++) {
                if (command.equalsIgnoreCase(Constants.COUNTRY_NAMES[i])) {
                    territoryCode=i;
                }
            }
        }

        return territoryCode;//Returns the code of the territory
    }

    //Recursive function to check if the number supplied by the player is valid
    public static int checkNumber(int numType, GameData gameData,String command, int territoryCode){
        int number;
        //Checks if the String entered is indeed a number
        try{
            Integer.parseInt(command);
        }catch(Exception e){
            //If not a number, ask the player to enter another number
            GameLogic.uiWindow.displayString("You must enter a number.\n");
            return -2;
        }

        //Converts the String to an int
        number=Integer.parseInt(command);

        if(number<=0){//If not less than 0, ask the player to enter another number
            GameLogic.uiWindow.displayString("You must enter a number greater than 0. Please try again\n");
            number=-2;
        }

        if(numType==1){//If placing troops
            if(number>GameLogic.currPlayer.getNumArmies()){//If more troops than player has in reserve, ask the player to enter another number
                GameLogic.uiWindow.displayString("You do not have that many troops. Please try again\n");
                number=-2;
            }
        }else if(numType==2){//If rolling dice
            if(number>3){//If greater than 3, ask the player to enter another number
                GameLogic.uiWindow.displayString("The maximum number of dice that can be rolled is 3. Please try again\n");
                return -2;
            }else if(number>= gameData.getNumUnits(territoryCode)){//If greater than or equal to number of troops in the territory, ask the player to enter another number
                GameLogic.uiWindow.displayString("You can only roll " + (gameData.getNumUnits(territoryCode) - 1) + " dice. Please try again\n");
                return -2;
            }

        }else if(number==gameData.getTerritory(territoryCode).numOccupyingArmies){//If equal to number of troops in the territory, ask the player to enter another number
            GameLogic.uiWindow.displayString("You must leave at least one troop in your territory at all times\n");
            number=-2;
        }else if(number> gameData.getTerritory(territoryCode).numOccupyingArmies){//If greater than the number of troops in the territory, ask the player to enter another number
            GameLogic.uiWindow.displayString("You do not have that many troops to transfer\n");
            number=-2;
        }else if(numType>3) {//Tell the player the minimum number of troops they must transfer based on the last number of dice they rolled
            if (number < (numType - 4)) {
                if ((numType - 4) == 1) {
                    GameLogic.uiWindow.displayString("You must transfer at least " + (numType - 4) + " troop");
                } else {
                    GameLogic.uiWindow.displayString("You must transfer at least " + (numType - 4) + " troops");
                }
                number = -2;
            }
        }

        return number;//returns the validated number
    }

    //Recursive function to check the two territories are adjacent
    public static boolean checkAdjacent(int territory1, int territory2, int checkType) {
        boolean check = false;
        for (int i = 0; i < Constants.ADJACENT[territory1].length; i++) {
            if (Constants.ADJACENT[territory1][i] == territory2) {
                check=true;
                break;
            }
        }

        //If territories must be adjacent
        if(!check&&checkType==1) {
            GameLogic.uiWindow.displayString("These territories are not adjacent. Please enter the name of another territory\n");
            String command = GameLogic.uiWindow.getCommand();
            territory2=checkHasTerritory(2,command);
            checkAdjacent(territory1, territory2,1);
        }else return check || checkType != 2;//If territories need not be adjacent

        return true;
    }

    public static int checkHasSurrounding(int territoryCode, int playerCode, GameData gameData, int checkType){
        boolean check=true;
        for(int i = 0; i < Constants.ADJACENT[territoryCode].length; i++) {
            if(gameData.getOccupier(Constants.ADJACENT[territoryCode][i])!=playerCode){
                check = false;
                break;
            }
        }

        if(checkType==1) {
            if (check) {
                GameLogic.uiWindow.displayString("You already own all of the territories surrounding " + gameData.getTerritory(territoryCode).territoryName + "\n");
                String command = GameLogic.skipOption(("Please enter the name of another territory\n"), (new String[]{"SKIP"}), "SKIP");
                if (command.equals("SKIP")) {
                    return -1;
                }

                territoryCode = Checks.checkHasTerritory(1, command);
                if (territoryCode == -1) {
                    return -1;
                }
                territoryCode = Checks.checkHasSurrounding(territoryCode, GameLogic.currPlayer.getPlayerCode(), gameData, checkType);
            }
        }

        if(checkType==2){
            if(check){
                return -2;
            }
        }

        return territoryCode;
    }

    public static boolean checkCanAttack(ArrayList<Territory> playerTerritories, int playerCode, GameData gameData){
        for(int i = 0; i < playerTerritories.size(); i++) {
            if(checkHasSurrounding(playerTerritories.get(i).territoryCode, playerCode,gameData,2)!=-2 && gameData.getTerritory(i).numOccupyingArmies!=1){
                return true;
            }
        }

        GameLogic.uiWindow.displayString("You have no territories from which you can attack.\n");
        return false;
    }

    //Recursive function to find if there is a valid path between the territories a player wishes to move troops between
    public static boolean checkHasValidPath(int territory1, int territory2, ArrayList<Territory> playerTerritories){
        int i=0;
        /*Checks all the paths through a players owned territories to check if there
        is path to the destination. Each territory is only visited once*/
        while(playerTerritories.size()>0&&i<playerTerritories.size()){
            if(checkAdjacent(territory1,territory2,2)){//If has a valid path
                return true;
            }

            if(checkAdjacent(territory1,playerTerritories.get(i).territoryCode,2)){
                int tmp=playerTerritories.get(i).territoryCode;
                playerTerritories.remove(i);//Removes the territory once it is visited
                i=-1;
                //Recursive call
                if(checkHasValidPath(tmp,territory2,playerTerritories)){
                    return true;
                }
            }

            i++;
        }
        return false;
    }

    public static boolean checkIsValidCombination(String cards){
        for(int i = 0; i < Constants.VALID_COMBINATIONS.length; i++){
            if(cards.equalsIgnoreCase(Constants.VALID_COMBINATIONS[i])){
                return true;
            }
        }
        return false;
    }

}
