public class Attack {
    public static boolean attack(GameData gameData){
        int attackingTerritory,defendingTerritory;
        do{
            boolean check=true;
            do {
                do {
                    GameLogic.uiWindow.displayString("Please enter the name of the territory from which you wish to attack\n");
                    Checks.checkCommand(new String[]{"SKIP"});
                } while (GameLogic.command.equals("CONTINUE"));
                if (GameLogic.command.equals("SKIP")) {
                    return false;
                }
                GameLogic.territoryCode = Checks.checkHasTerritory(1);
                if (gameData.getNumUnits(GameLogic.territoryCode) <= 1) {
                    GameLogic.uiWindow.displayString("The attacking territory must have at least 2 troops. Please try again");
                    check=false;
                }
            }while(!check);

            attackingTerritory=GameLogic.territoryCode;
            do {
                GameLogic.uiWindow.displayString("Do you wish to attack from " + gameData.getTerritory(attackingTerritory).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\n");
                Checks.checkCommand(new String[]{"YES", "NO", "SKIP"});
            }while(GameLogic.command.equals("CONTINUE"));
            if(GameLogic.command.equals("SKIP")){
                return false;
            }
        } while (GameLogic.command.equals("NO"));

        do{
            do {
                GameLogic.uiWindow.displayString("Please enter the name of the territory you wish to attack\n");
                Checks.checkCommand(new String[]{"SKIP"});
            } while (GameLogic.command.equals("CONTINUE"));
            if (GameLogic.command.equals("SKIP")) {
                return false;
            }
            GameLogic.territoryCode = Checks.checkHasTerritory(2);
            Checks.checkAdjacent(attackingTerritory,GameLogic.territoryCode,1);
            defendingTerritory=GameLogic.territoryCode;

            do {
                GameLogic.uiWindow.displayString("Do you wish to attack " + gameData.getTerritory(defendingTerritory).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\nYou may enter 'RETURN' to attack from another territory\n");
                Checks.checkCommand(new String[]{"YES", "NO","RETURN","SKIP"});
            }while(GameLogic.command.equals("CONTINUE"));
            if(GameLogic.command.equals("SKIP")){
                return false;
            }
            if(GameLogic.command.equals("RETURN")){
                attack(gameData);
                return false;
            }
        } while (GameLogic.command.equals("NO"));

        GameLogic.territoryCode=attackingTerritory;

        int numRedDice=executeAttack(attackingTerritory,defendingTerritory,gameData);
        if (numRedDice==-1){
            return false;
        }

        Player defendingPlayer=gameData.players[gameData.getOccupier(defendingTerritory)];
        if(gameData.getTerritory(defendingTerritory).numOccupyingArmies==0){
            GameLogic.uiWindow.displayString("" + defendingPlayer.getPlayerName() + " has lost " + gameData.getTerritory(defendingTerritory).territoryName);
            defendingPlayer.removeTerritory(defendingTerritory);
            gameData.setOccupier(defendingTerritory, GameLogic.currPlayer.getPlayerCode());
            GameLogic.currPlayer.addTerritory(gameData.getTerritory(defendingTerritory));

            int numTroopsToTransfer;
            GameLogic.uiWindow.displayString("" + GameLogic.currPlayer.getPlayerName() + ", you must enter the number of troops you wish to transfer to " + gameData.getTerritory(defendingTerritory).territoryName +"\n");
            GameLogic.uiWindow.displayString("As you rolled " + numRedDice + " dice on your last attack, you must transfer at least " + numRedDice + "\n");
            do {
                GameLogic.uiWindow.displayString("Please enter the number of troops to be transferred\n");
                GameLogic.command= GameLogic.uiWindow.getCommand();
                numTroopsToTransfer=Checks.checkNumber(4+(numRedDice),gameData);
                if(numTroopsToTransfer==1){
                    GameLogic.uiWindow.displayString("Do you wish to transfer " + numTroopsToTransfer + " troop into " + gameData.getTerritory(defendingTerritory).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\n");
                }else{
                    GameLogic.uiWindow.displayString("Do you wish to transfer " + numTroopsToTransfer + " troops into " + gameData.getTerritory(defendingTerritory).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\n");
                }
                Checks.checkCommand(new String[]{"YES", "NO"});
            } while (GameLogic.command.equals("NO"));

            gameData.addUnits(attackingTerritory,-numTroopsToTransfer);
            gameData.addUnits(defendingTerritory,numTroopsToTransfer);
            GameLogic.uiWindow.displayMap();

        }else if(gameData.getTerritory(attackingTerritory).numOccupyingArmies==1){
            GameLogic.uiWindow.displayString("" + GameLogic.currPlayer.getPlayerName() + " has failed to take over " + gameData.getTerritory(defendingTerritory).territoryName + "\n");
            return false;
        }

        if(defendingPlayer.getNumPlayerTerritories()==0){
            GameLogic.uiWindow.displayString("" + defendingPlayer.getPlayerName() + " has been wiped out!\n");
            GameLogic.currPlayer.transferCards(defendingPlayer.getTerritoryCards());
            defendingPlayer.getTerritoryCards().clear();
            int i=0;
            while(gameData.playerOrder.get(i)!=defendingPlayer.getPlayerCode()){
                i++;
            }
            if(defendingPlayer.getPlayerCode()==0||defendingPlayer.getPlayerCode()==1){
                GameLogic.command="GAME OVER";
            }
            gameData.playerOrder.remove(i);
        }

        return true;
    }

    private static int executeAttack(int attackingTerritory, int defendingTerritory,GameData gameData){
        int numRedDice = 0;
        int numWhiteDice;
        label:
        while(gameData.getTerritory(attackingTerritory).numOccupyingArmies>1&&gameData.getTerritory(defendingTerritory).numOccupyingArmies>0) {
            numRedDice = 0;
            numWhiteDice = 1;
            if(!GameLogic.command.equals("BLITZ")) {
                do {
                    GameLogic.uiWindow.displayString("Please enter the number of dice you wish to roll.\nEnter 'STOP' if you want to stop attacking or enter 'BLITZ' to auto-run the attack sequence\n");
                    Checks.checkCommand(new String[]{"SKIP", "STOP", "BLITZ"});
                } while (GameLogic.command.equals("CONTINUE"));
                if (GameLogic.command.equals("SKIP")) {
                    return -1;
                } else if (GameLogic.command.equals("STOP")) {
                    break;
                }
            }

            if (gameData.getTerritory(defendingTerritory).numOccupyingArmies > 1) {
                numWhiteDice = 2;
            }

            if(!GameLogic.command.equals("BLITZ")) {
                numRedDice = Checks.checkNumber(2,gameData);
                if(numRedDice==-1){
                    return numRedDice;
                }

                do {
                    GameLogic.uiWindow.displayString("Please enter 'ATTACK' to attack " + gameData.getTerritory(defendingTerritory).territoryName + ", 'CHANGE' to change the number of dice to attack with or 'STOP' if you wish to stop attacking\n");
                    Checks.checkCommand(new String[]{"ATTACK", "CHANGE", "STOP", "SKIP"});
                } while (GameLogic.command.equals("CONTINUE"));
                switch (GameLogic.command) {
                    case "SKIP":
                        return -1;
                    case "CHANGE":
                        continue;
                    case "STOP":
                        break label;
                }
            }else{
                numRedDice=gameData.getTerritory(attackingTerritory).numOccupyingArmies-1;
                if(numRedDice>3){
                    numRedDice=3;
                }
            }

            int[] redDice = new int[numRedDice];
            int[] whiteDice = new int[numWhiteDice];
            StringBuilder msg = new StringBuilder("" + GameLogic.currPlayer.getPlayerName() + " rolled ");
            for (int i = 0; i < numRedDice; i++) {
                redDice[i] = GameLogic.diceRoll();
                msg.append(redDice[i]);
                if (i != numRedDice - 1) {
                    msg.append(", ");
                } else {
                    msg.append("\n");
                }

                if (i>0) {
                    if(i>1){
                        if (redDice[2] > redDice[1]) {
                            int tmp = redDice[2];
                            redDice[2] = redDice[1];
                            redDice[1] = tmp;
                        }
                    }
                    if (redDice[1] > redDice[0]) {
                        int tmp = redDice[1];
                        redDice[1] = redDice[0];
                        redDice[0] = tmp;
                    }
                }
            }
            GameLogic.uiWindow.displayString(msg.toString());

            msg = new StringBuilder("" + gameData.players[gameData.getOccupier(defendingTerritory)].getPlayerName() + " rolled ");
            for (int i = 0; i < numWhiteDice; i++) {
                whiteDice[i] = GameLogic.diceRoll();
                msg.append(whiteDice[i]);
                if (i != numWhiteDice - 1) {
                    msg.append(", ");
                } else{
                    msg.append("\n");
                }

                if (i==1) {
                    if (whiteDice[1] > whiteDice[0]) {
                        int tmp = whiteDice[1];
                        whiteDice[1] = whiteDice[0];
                        whiteDice[0] = tmp;
                    }
                }
            }
            GameLogic.uiWindow.displayString(msg.toString());

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
                    GameLogic.uiWindow.displayString("" + GameLogic.currPlayer.getPlayerName() + " lost " + redLoss + " troops\n");
                }else {
                    GameLogic.uiWindow.displayString("" + GameLogic.currPlayer.getPlayerName() + " lost " + redLoss + " troop\n");
                }
            }
            if (whiteLoss > 0) {
                if(whiteLoss > 1) {
                    GameLogic.uiWindow.displayString("" + gameData.players[gameData.getOccupier(defendingTerritory)].getPlayerName() + " lost " + whiteLoss + " troops\n");
                }else {
                    GameLogic.uiWindow.displayString("" + gameData.players[gameData.getOccupier(defendingTerritory)].getPlayerName() + " lost " + whiteLoss + " troop\n");
                }
            }

            gameData.getTerritory(attackingTerritory).numOccupyingArmies -= redLoss;
            gameData.getTerritory(defendingTerritory).numOccupyingArmies -= whiteLoss;
            GameLogic.uiWindow.displayMap();
        }

        return numRedDice;
    }
}
