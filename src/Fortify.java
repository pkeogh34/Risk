import java.util.ArrayList;

public class Fortify {
    public static void fortify(GameData gameData) {
        int territory1,territory2;
        String command;
        do{
            boolean check;
            do {
                check=true;
                command =GameLogic.skipOption(("Please enter the name of the territory from which you wish to move your troops\n"),(new String[]{"END"}),"END");
                if(command.equals("END")){
                    return;
                }

                territory1 = Checks.checkHasTerritory(1, command);
                if(gameData.getNumUnits(territory1)<=1){
                    GameLogic.uiWindow.displayString("The territory must have at least 2 troops. Please try again\n");
                    check=false;
                }
            }while(!check);

            command =GameLogic.skipOption(("Do you wish to move your troops from " + gameData.getTerritory(territory1).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\n"),(new String[]{"YES", "NO", "END"}),"END");
            if(command.equals("END")){
                return;
            }
        } while (command.equals("NO"));

        do{
            do {
                boolean check;
                do {
                    do{
                        command =GameLogic.skipOption(("Please enter the name of the territory you wish to transfer your troops to\n"),(new String[]{"END"}),"END");
                        if(command.equals("END")){
                            return;
                        }

                        if(Checks.checkHasTerritory(1, command)==territory1){
                            GameLogic.uiWindow.displayString("You cannot select the same territory\n");
                        }
                    }while (Checks.checkHasTerritory(1, command)==territory1);

                    check=true;
                    territory2 = Checks.checkHasTerritory(1, command);
                    ArrayList<Territory> temp = new ArrayList<>(GameLogic.currPlayer.getPlayerTerritories());
                    for (int i = 0; i < temp.size(); i++) {
                        if (temp.get(i).territoryCode == territory1) {
                            temp.remove(i);
                            break;
                        }
                    }

                    if (!Checks.checkHasValidPath(territory1, territory2, new ArrayList<>(temp))) {
                        GameLogic.uiWindow.displayString("There is no valid path between these territories.\n");
                        check=false;
                    }
                }while(!check);

                territory2=Checks.checkHasTerritory(1, command);
                command =GameLogic.skipOption(("Do you wish to move your troops from " + gameData.getTerritory(territory1).territoryName + " to " + gameData.getTerritory(territory2).territoryName +"?\nEnter 'YES' to continue or 'NO' to choose another territory.\nYou may also enter 'RETURN' to move troops from a different territory\n"),(new String[]{"YES", "NO","RETURN","END"}),"END");
                if(command.equals("END")){
                    return;
                }
            }while(command.equals("CONTINUE"));
            if(command.equals("RETURN")){
                fortify(gameData);
                return;
            }
        }while(command.equals("NO"));

        int numTroopsToTransfer;
        do{
            GameLogic.uiWindow.displayString("Please enter the number of troops to be transferred\n");
            command = GameLogic.uiWindow.getCommand();
            numTroopsToTransfer = Checks.checkNumber(3,gameData, command,territory1);

            if(numTroopsToTransfer==1){
                GameLogic.uiWindow.displayString("Do you wish to transfer " + numTroopsToTransfer + " troop into " + gameData.getTerritory(territory2).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\nYou may also enter 'RETURN' to move troops from different territories\n");
            }else{
                GameLogic.uiWindow.displayString("Do you wish to transfer " + numTroopsToTransfer + " troops into " + gameData.getTerritory(territory2).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\\nYou may also enter 'RETURN' to move troops from different territories\n");
            }
            command = Checks.checkCommand(new String[]{"YES", "NO"});
        } while (command.equals("NO"));
        if(command.equals("RETURN")){
            fortify(gameData);
            return;
        }

        gameData.addUnits(territory1,-numTroopsToTransfer);
        gameData.addUnits(territory2,numTroopsToTransfer);
        GameLogic.uiWindow.displayMap();
    }
}
