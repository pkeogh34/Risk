import java.util.Random;

public class GameLogic {
    Initialisation I;
    private final UIWindow uiWindow;
    private Player[] players;
    private final int[] playerOrder;
    private int numPlayers=6;
    private Player currPlayer;
    private int territoryCode;
    private int numTurns=0;
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
        for(int i=0;i<54;i++){
            currPlayer=players[playerOrder[i]];
            uiWindow.displayString("" + currPlayer.getPlayerName() +", it is your turn\n You must place 3 troops in a territory that you own\n");
            if(i<=1){
                placeTroops(3);
            }else{
                turnNeutral();
            }

            if(i==numPlayers){
                i=0;
            }
            numTurns++;
        }

        for(int i=0;numPlayers>1;i++){
            currPlayer=players[playerOrder[i]];
            uiWindow.displayString("" + currPlayer.getPlayerName() +", it is your turn\n");
            if(i<=1){
               turnPlayer();
            }else{
                turnNeutral();
            }
            if(i==numPlayers){
                i=0;
            }
            numTurns++;

        }
        uiWindow.displayString("" + currPlayer.getPlayerName() +" has one the game!");
    }

    public void turnPlayer(){
      //placeTroops();
    }

    public void turnNeutral(){

    }

    //Maybe just use checkCommmand from initialisation class?
    public void checkCommand(String correctInput){
        if(!command.equals(correctInput)){
            uiWindow.displayString("You must enter '" + correctInput + "'. Please enter your command again\n");
            command=uiWindow.getCommand();
            checkCommand(correctInput);
        }
    }

    public int checkHasTerritory() {
        boolean check = false;
        int territoryCode=0;
        for (int i = 0; i < currPlayer.getNumPlayerTerritories(); i++) {
            if (command.equals(currPlayer.getPlayerTerritory(i).territoryName)) {
                check = true;
                territoryCode=i;
            }
        }
        if (!check) {
            uiWindow.displayString("You do not own this territory. Please enter the name of another territory\n");
            GameLogic.command = uiWindow.getCommand();
            territoryCode=checkHasTerritory();
        }

        return territoryCode;
    }

    public static int diceRoll(){
        Random random =new Random();
        return random.nextInt(5)+1;
    }

    private void placeTroops(int numTroops){
        uiWindow.displayString("Please enter the name of the territory in which you wish to place your troops\n");
        command=uiWindow.getCommand();
        territoryCode = checkHasTerritory();
        uiWindow.displayString("Please enter 'FORTIFY' to add your troops to " + uiWindow.board.getTerritory(territoryCode).territoryName + "\n");
        command=uiWindow.getCommand();
        checkCommand("FORTIFY");
        uiWindow.board.addUnits(territoryCode,numTroops);
        currPlayer.addArmies(-numTroops);
        uiWindow.displayMap();
    }
}
