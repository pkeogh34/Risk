import java.util.Random;

public class GameLogic {
    private Board board;
    private UIWindow uiWindow;
    private Player[] players;
    private boolean gameOver = false;
    private Player winningPlayer;

    public GameLogic(Board board, UIWindow uiWindow, Player[] players){
        this.board=board;
        this.uiWindow=uiWindow;
        this.players=players;
    }

    public static void turn(){

    }

    public static void diceRoll(){
        boolean notEqual=false;
        int rollPlayer1, rollPlayer2;
        Random roll =new Random();

        while(!notEqual){
           rollPlayer1 = roll.nextInt(6);
           rollPlayer2 = roll.nextInt(6);
           if(rollPlayer1!=rollPlayer2){
               notEqual=true;
           }
           if(notEqual){
               if(rollPlayer1>rollPlayer2){

               }
           }
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Player getWinningPlayer() {
        return winningPlayer;
    }
}
