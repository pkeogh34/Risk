import java.util.ArrayList;

public class Fortify {
    public static void fortify() {
        int territory1=0,territory2=0;
        do{
            do{
                GameLogic.uiWindow.displayString("Please enter the name of the territory from which you wish to move your troops\n");
                GameLogic.checkCommand(new String[]{"END"});
            }while(GameLogic.command.equals("CONTINUE"));
            if(GameLogic.command.equals("END")){
                return;
            }
            GameLogic.territoryCode = GameLogic.checkHasTerritory(1);
            if(GameLogic.board.getNumUnits(GameLogic.territoryCode)<=1){
                GameLogic.uiWindow.displayString("The territory must have at least 2 troops. Please try again\n");
                continue;
            }
            territory1=GameLogic.territoryCode;
            do {
                GameLogic.uiWindow.displayString("Do you wish to move your troops from " + GameLogic.board.getTerritory(territory1).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\n");
                GameLogic.checkCommand(new String[]{"YES", "NO", "SKIP"});
            }while(GameLogic.command.equals("CONTINUE"));
            if(GameLogic.command.equals("SKIP")){
                return;
            }
        } while (GameLogic.command.equals("NO"));

        do{
            do {
                GameLogic.uiWindow.displayString("Please enter the name of the territory you wish to transfer your troops to\n");
                GameLogic.checkCommand(new String[]{"END"});
            } while (GameLogic.command.equals("CONTINUE"));
            if (GameLogic.command.equals("END")) {
                return;
            }

            do {
                GameLogic.territoryCode = GameLogic.checkHasTerritory(1);
                if(!GameLogic.checkHasValidPath(territory1,GameLogic.territoryCode,new ArrayList<>(GameLogic.currPlayer.getPlayerTerritories()))){
                    GameLogic.uiWindow.displayString("There is no valid path between these territories. Please select another territory\n");
                    continue;
                }
                territory2=GameLogic.territoryCode;
                GameLogic.uiWindow.displayString("Do you wish to transfer troops from " + GameLogic.board.getTerritory(territory1).territoryName + " to " + GameLogic.board.getTerritory(territory2).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\nYou may enter 'RETURN' to move troops from another territory\n");
                GameLogic.checkCommand(new String[]{"YES", "NO","RETURN","END"});
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
            numTroopsToTransfer= GameLogic.checkNumber(3);
            if(numTroopsToTransfer==1){
                GameLogic.uiWindow.displayString("Do you wish to transfer " + numTroopsToTransfer + " troop into " + GameLogic.board.getTerritory(territory2).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\n");
            }else{
                GameLogic.uiWindow.displayString("Do you wish to transfer " + numTroopsToTransfer + " troops into " + GameLogic.board.getTerritory(territory2).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\n");
            }
            GameLogic.checkCommand(new String[]{"YES", "NO"});
        } while (GameLogic.command.equals("NO"));

        GameLogic.board.addUnits(territory1,-numTroopsToTransfer);
        GameLogic.board.addUnits(territory2,numTroopsToTransfer);
        GameLogic.uiWindow.displayMap();
    }
}
