import java.nio.charset.StandardCharsets;
import java.util.Random;

public class GameLogic {
    Initialisation I;
    private UIWindow uiWindow;
    private Player[] players;
    private int[] playerOrder;
    private int numPlayers=6;
    private Player currPlayer;
    private int territoryCode;
    public static String command; //Maybe change to enum when all commands are known (will require changing return type of getCommand() function also)



    public GameLogic(){
        I=new Initialisation();
        I.initialisation();
        uiWindow=I.getUiWindow();
        players=I.getPlayers();
        playerOrder=I.getPlayerOrder();

        currPlayer=players[playerOrder[0]];
    }

    public void game(){
        for(int i=0;numPlayers>1;i++){
            uiWindow.displayString("" + currPlayer.getPlayerName() +", it is your turn\n");
            if(i<=1){
               turnPlayer();
            }else{
                turnNeutral();
            }
            if(i==numPlayers){
                i=0;
            }
        }
        uiWindow.displayString("" + currPlayer.getPlayerName() +" has one the game!");
    }

    public void turnPlayer(){
        uiWindow.displayString("Please enter the name of the territory in which you wish to place your troops: ");
        command=uiWindow.getCommand();

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

    public static int diceRoll(){
        Random random =new Random();
        return random.nextInt(5)+1;
    }
}
