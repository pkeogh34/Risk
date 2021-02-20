import java.util.Random;

public class GameLogic {
    private Board board;
    private UIWindow uiWindow;
    private Player[] players;
    private static Deck deck;

    public GameLogic(Board board, UIWindow uiWindow, Player[] players){
        this.board=board;
        this.uiWindow=uiWindow;
        this.players=players;
        this.deck= new Deck();
        deck.shuffle();

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

    public void exampleOfDrawCard()
    {
        players[0].drawCard(deck);
        System.out.println(players[0].showCards());
    }
}
