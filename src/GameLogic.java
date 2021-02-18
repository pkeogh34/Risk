import java.nio.charset.StandardCharsets;
import java.util.Random;

public class GameLogic {
    private Board board;
    private UIWindow uiWindow;
    private Player[] players;
    private int numPlayers=6;
    private Player currPlayer;
    private String command; //Maybe change to enum when all commands are known (will require changing return type of getCommand() function also)
    private boolean gameOver = false;

    public GameLogic(Board board, UIWindow uiWindow, Player[] players){
        this.board=board;
        this.uiWindow=uiWindow;
        this.players=players;
    }

    public void game(){
        firstPlayer();
        for(int i=0;!gameOver;i++){
            uiWindow.displayString("" + currPlayer.getPlayerName() +", it is your turn\n");
            gameOver=turn();
            if(i==numPlayers){
                i=0;
            }
            if(gameOver){
                i--;
            }
        }
        uiWindow.displayString("" + currPlayer.getPlayerName() +" has one the game!");
    }

    public void firstPlayer(){
        int roll1=0, roll2=0;
        boolean equal=true;

        while(equal){
            uiWindow.displayString("" + players[0].getPlayerName() + " please enter 'ROLL' to roll the dice\n");
            command=uiWindow.getCommand();
            checkCommand("ROLL");
            roll1=diceRoll();
            uiWindow.displayString("" + players[0].getPlayerName() + " rolled " + roll1 + "\n");

            uiWindow.displayString("" + players[1].getPlayerName() + " please enter 'ROLL' to roll the dice\n");
            command=uiWindow.getCommand();
            checkCommand("ROLL");
            roll2=diceRoll();
            uiWindow.displayString("" + players[1].getPlayerName() + " rolled " + roll2 +"\n");

            if(roll1!=roll2){
                equal=false;
            }

            if(equal){
                uiWindow.displayString("Players rolled the same number. Please roll again.\n");
            }
        }

        if(roll1<roll2){
            uiWindow.displayString("" + players[1].getPlayerName() + " will go first\n");
            Player temp;
            temp=players[0];
            players[0]=players[1];
            players[1]=temp;
        }else{
            uiWindow.displayString("" + players[0].getPlayerName() + " will go first\n");
        }

        currPlayer=players[0];
    }

    public boolean turn(){
        return true;
    }

    public boolean checkCommand(String correctInput){
        if(!command.equals(correctInput)){
            uiWindow.displayString("You must enter '" + correctInput + "'. Please enter your command again\n");
            command=uiWindow.getCommand();
            checkCommand(correctInput);
        }
        return true;
    }

    public int diceRoll(){
        Random random =new Random();
        return random.nextInt(5)+1;
    }
}
