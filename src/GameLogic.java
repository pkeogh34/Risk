import java.util.Random;

public class GameLogic {
    public static void main(String[] args) {
    }

    private static void diceRoll(){
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
}
