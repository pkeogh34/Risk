public class Attack {
    public static void attack(){
        //Perhaps find a way to generalise getting territory name
        int attackingTerritory=0,defendingTerritory;
        do{
            do{
                GameLogic.uiWindow.displayString("Please enter the name of the territory from which you wish to attack\n");
                GameLogic.checkCommand(new String[]{"SKIP"});
            }while(GameLogic.command.equals("CONTINUE"));
            if(GameLogic.command.equals("SKIP")){
                return;
            }
            GameLogic.territoryCode = GameLogic.checkHasTerritory(1);
            if(GameLogic.board.getNumUnits(GameLogic.territoryCode)<=1){
                GameLogic.uiWindow.displayString("The attacking territory must have at least 2 troops. Please try again");
                continue;
            }
            attackingTerritory=GameLogic.territoryCode;
            do {
                GameLogic.uiWindow.displayString("Do you wish to attack from " + GameLogic.board.getTerritory(attackingTerritory).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\n");
                GameLogic.checkCommand(new String[]{"YES", "NO", "SKIP"});
            }while(GameLogic.command.equals("CONTINUE"));
            if(GameLogic.command.equals("SKIP")){
                return;
            }
        } while (GameLogic.command.equals("NO"));

        do{
            do {
                GameLogic.uiWindow.displayString("Please enter the name of the territory you wish to attack\n");
                GameLogic.checkCommand(new String[]{"SKIP"});
            } while (GameLogic.command.equals("CONTINUE"));
            if (GameLogic.command.equals("SKIP")) {
                return;
            }
            GameLogic.territoryCode = GameLogic.checkHasTerritory(2);
            GameLogic.checkAdjacent(attackingTerritory,GameLogic.territoryCode,1);
            defendingTerritory=GameLogic.territoryCode;

            do {
                GameLogic.uiWindow.displayString("Do you wish to attack " + GameLogic.board.getTerritory(defendingTerritory).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\nYou may enter 'RETURN' to attack from another territory\n");
                GameLogic.checkCommand(new String[]{"YES", "NO","RETURN","SKIP"});
            }while(GameLogic.command.equals("CONTINUE"));
            if(GameLogic.command.equals("SKIP")){
                return;
            }
            if(GameLogic.command.equals("RETURN")){
                attack();
                return;
            }
        } while (GameLogic.command.equals("NO"));

        GameLogic.territoryCode=attackingTerritory;

        int numRedDice = 0;
        int numWhiteDice;
        label:
        while(GameLogic.board.getTerritory(attackingTerritory).numOccupyingArmies>1&&GameLogic.board.getTerritory(defendingTerritory).numOccupyingArmies>0) {
            numRedDice = 0;
            numWhiteDice = 1;
            if(!GameLogic.command.equals("BLITZ")) {
                do {
                    GameLogic.uiWindow.displayString("Please enter the number of dice you wish to roll.\nEnter 'STOP' if you want to stop attacking or enter 'BLITZ' to auto-run the attack sequence\n");
                    GameLogic.checkCommand(new String[]{"SKIP", "STOP", "BLITZ"});
                } while (GameLogic.command.equals("CONTINUE"));
                if (GameLogic.command.equals("SKIP")) {
                    return;
                } else if (GameLogic.command.equals("STOP")) {
                    break;
                }
            }

            if (GameLogic.board.getTerritory(defendingTerritory).numOccupyingArmies > 1) {
                numWhiteDice = 2;
            }

            if(!GameLogic.command.equals("BLITZ")) {
                numRedDice = GameLogic.checkNumber(2);
                if(numRedDice==-1){
                    return;
                }

                do {
                    GameLogic.uiWindow.displayString("Please enter 'ATTACK' to attack " + GameLogic.board.getTerritory(defendingTerritory).territoryName + ", 'CHANGE' to change the number of dice to attack with or 'STOP' if you wish to stop attacking\n");
                    GameLogic.checkCommand(new String[]{"ATTACK", "CHANGE", "STOP", "SKIP"});
                } while (GameLogic.command.equals("CONTINUE"));
                switch (GameLogic.command) {
                    case "SKIP":
                        return;
                    case "CHANGE":
                        continue;
                    case "STOP":
                        break label;
                }
            }else{
                numRedDice=GameLogic.board.getTerritory(attackingTerritory).numOccupyingArmies-1;
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

            msg = new StringBuilder("" + GameLogic.players[GameLogic.board.getOccupier(defendingTerritory)].getPlayerName() + " rolled ");
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
                    GameLogic.uiWindow.displayString("" + GameLogic.players[GameLogic.board.getOccupier(defendingTerritory)].getPlayerName() + " lost " + whiteLoss + " troops\n");
                }else {
                    GameLogic.uiWindow.displayString("" + GameLogic.players[GameLogic.board.getOccupier(defendingTerritory)].getPlayerName() + " lost " + whiteLoss + " troop\n");
                }
            }

            GameLogic.board.getTerritory(attackingTerritory).numOccupyingArmies -= redLoss;
            GameLogic.board.getTerritory(defendingTerritory).numOccupyingArmies -= whiteLoss;
            GameLogic.uiWindow.displayMap();
        }

        int defendingPlayer=GameLogic.board.getOccupier(defendingTerritory);
        if(GameLogic.board.getTerritory(defendingTerritory).numOccupyingArmies==0){
            GameLogic.uiWindow.displayString("" + GameLogic.players[defendingPlayer].getPlayerName() + " has lost " + GameLogic.board.getTerritory(defendingTerritory).territoryName);
            GameLogic.players[defendingPlayer].removeTerritory(defendingTerritory);
            GameLogic.board.setOccupier(defendingTerritory, GameLogic.currPlayer.getPlayerCode());
            GameLogic.currPlayer.addTerritory(GameLogic.board.getTerritory(defendingTerritory));

            int numTroopsToTransfer;
            GameLogic.uiWindow.displayString("" + GameLogic.currPlayer.getPlayerName() + ", you must enter the number of troops you wish to transfer to " + GameLogic.board.getTerritory(defendingTerritory).territoryName +"\n");
            GameLogic.uiWindow.displayString("As you rolled " + numRedDice + " dice on your last attack, you must transfer at least " + numRedDice + "\n");
            do {
                GameLogic.uiWindow.displayString("Please enter the number of troops to be transferred\n");
                GameLogic.command= GameLogic.uiWindow.getCommand();
                numTroopsToTransfer=GameLogic.checkNumber(4+(numRedDice));
                if(numTroopsToTransfer==1){
                    GameLogic.uiWindow.displayString("Do you wish to transfer " + numTroopsToTransfer + " troop into " + GameLogic.board.getTerritory(defendingTerritory).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\n");
                }else{
                    GameLogic.uiWindow.displayString("Do you wish to transfer " + numTroopsToTransfer + " troops into " + GameLogic.board.getTerritory(defendingTerritory).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\n");
                }
                GameLogic.checkCommand(new String[]{"YES", "NO"});
            } while (GameLogic.command.equals("NO"));

            GameLogic.board.addUnits(attackingTerritory,-numTroopsToTransfer);
            GameLogic.board.addUnits(defendingTerritory,numTroopsToTransfer);
            GameLogic.uiWindow.displayMap();

        }else if(GameLogic.board.getTerritory(attackingTerritory).numOccupyingArmies==1){
            GameLogic.uiWindow.displayString("" + GameLogic.currPlayer.getPlayerName() + " has failed to take over " + GameLogic.board.getTerritory(defendingTerritory).territoryName + "\n");
        }

        if(GameLogic.players[defendingPlayer].getNumPlayerTerritories()==0){
            GameLogic.uiWindow.displayString("" + GameLogic.players[defendingPlayer].getPlayerName() + " has been wiped out!\n");
            int i=0;
            while(GameLogic.playerOrder.get(i)!=defendingPlayer){
                i++;
            }
            if(defendingPlayer==0||defendingPlayer==1){
                GameLogic.command="GAME OVER";
            }
            GameLogic.playerOrder.remove(i);
        }
    }
}
