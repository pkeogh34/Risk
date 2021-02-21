import java.util.ArrayList;
import java.util.Random;


public class Deck 
{
	private static ArrayList<TerritoryCard> theDeck = new ArrayList<TerritoryCard>();

	//not sure if need this yet
	private static ArrayList<TerritoryCard> discardPile = new ArrayList<TerritoryCard>();
	
	public Deck() {
		reset();
	}
	
	public void reset() {
		theDeck.clear();
		discardPile.clear();
		
		for (int i=0; i<42; i++) {
			theDeck.add(new TerritoryCard(i));
		}
	}
	
	public void shuffle() 
	{
		Random rand = new Random();
		
		for (int i = 0; i < 100; i++)
		{
			// Obtain a card between [0 - 41].
			int n = rand.nextInt(42);
			TerritoryCard c = theDeck.remove(n);
			theDeck.add(c);
			
		}
		
		
	}
	
	public TerritoryCard drawCard() {
		TerritoryCard tc = null;

		try{
			tc = theDeck.remove(0);
			//discardPile.add(tc);
		}
		catch(IndexOutOfBoundsException e){
			System.out.println("Error: desk is empty;");
		}

		return tc;
	}
}
