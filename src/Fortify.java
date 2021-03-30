import java.util.ArrayList;

public class Fortify {
    public static void fortify() {
        int territory1=0,territory2=0;
        do{
            do{
                GameLogic.uiWindow.displayString("Please enter the name of the territory from which you wish to move your troops\n");
                Checks.checkCommand(new String[]{"END"});
            }while(GameLogic.command.equals("CONTINUE"));
            if(GameLogic.command.equals("END")){
                return;
            }
            GameLogic.territoryCode = Checks.checkHasTerritory(1);
            if(GameLogic.board.getNumUnits(GameLogic.territoryCode)<=1){
                GameLogic.uiWindow.displayString("The territory must have at least 2 troops. Please try again\n");
                continue;
            }
            territory1=GameLogic.territoryCode;
            do {
                GameLogic.uiWindow.displayString("Do you wish to move your troops from " + GameLogic.board.getTerritory(territory1).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\n");
                Checks.checkCommand(new String[]{"YES", "NO", "SKIP"});
            }while(GameLogic.command.equals("CONTINUE"));
            if(GameLogic.command.equals("SKIP")){
                return;
            }
        } while (GameLogic.command.equals("NO"));

        do{
            do {
                GameLogic.uiWindow.displayString("Please enter the name of the territory you wish to transfer your troops to\n");
                Checks.checkCommand(new String[]{"END"});
            } while (GameLogic.command.equals("CONTINUE"));
            if (GameLogic.command.equals("END")) {
                return;
            }

            do {
                boolean check=true;
                do {
                    GameLogic.territoryCode = Checks.checkHasTerritory(1);
                    ArrayList<Territory> temp = new ArrayList<>(GameLogic.currPlayer.getPlayerTerritories());
                    for (int i = 0; i < temp.size(); i++) {
                        if (temp.get(i).territoryCode == territory1) {
                            temp.remove(i);
                            break;
                        }
                    }

                    if (!Checks.checkHasValidPath(territory1, GameLogic.territoryCode, temp)) {
                        do {
                            GameLogic.uiWindow.displayString("There is no valid path between these territories. Please select another territory\n");
                            Checks.checkCommand(new String[]{"END"});
                        } while (GameLogic.command.equals("CONTINUE"));
                        if (GameLogic.command.equals("END")) {
                            return;
                        }
                        check=false;
                    }
                }while(!check);

                territory2=GameLogic.territoryCode;
                GameLogic.uiWindow.displayString("Do you wish to transfer troops from " + GameLogic.board.getTerritory(territory1).territoryName + " to " + GameLogic.board.getTerritory(territory2).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\nYou may enter 'RETURN' to move troops from another territory\n");
                Checks.checkCommand(new String[]{"YES", "NO","RETURN","END"});
            }while(GameLogic.command.equals("CONTINUE"));
            if(GameLogic.command.equals("END")){
                return;
            }
            if(GameLogic.command.equals("RETURN")){
                fortify();
                return;
            }
        } while (GameLogic.command.equals("NO"));

        int numTroopsToTransfer;
        do{
            GameLogic.uiWindow.displayString("Please enter the number of troops to be transferred\n");
            GameLogic.command= GameLogic.uiWindow.getCommand();
            GameLogic.territoryCode=territory1;
            numTroopsToTransfer= Checks.checkNumber(3);
            if(numTroopsToTransfer==1){
                GameLogic.uiWindow.displayString("Do you wish to transfer " + numTroopsToTransfer + " troop into " + GameLogic.board.getTerritory(territory2).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\n");
            }else{
                GameLogic.uiWindow.displayString("Do you wish to transfer " + numTroopsToTransfer + " troops into " + GameLogic.board.getTerritory(territory2).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\n");
            }
            Checks.checkCommand(new String[]{"YES", "NO"});
        } while (GameLogic.command.equals("NO"));

        GameLogic.board.addUnits(territory1,-numTroopsToTransfer);
        GameLogic.board.addUnits(territory2,numTroopsToTransfer);
        GameLogic.uiWindow.displayMap();
    }
}
