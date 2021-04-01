//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import java.util.ArrayList;
import java.util.*;


public class Deck 
{
	private final ArrayList<TerritoryCard> cardPile = new ArrayList<>();
	
	public Deck() {
		reset();
		Collections.shuffle(cardPile);
	}
	
	public void reset() {
		cardPile.clear();
		for (int i=0; i<44; i++) {
			cardPile.add(new TerritoryCard(i));
		}
	}

	public TerritoryCard drawCard() {
		TerritoryCard territoryCard=cardPile.get(0);
		cardPile.remove(0);

		return territoryCard;
	}

	public ArrayList<TerritoryCard> getCardPile(){
		return cardPile;
	}

	static class TerritoryCard
	{
		private final int territoryCode;
		private final String type;

		public TerritoryCard (int territoryCode) {
			this.territoryCode = territoryCode;
			this.type = Constants.CARD_UNIT_TYPE[territoryCode];
		}

		public int getTerritoryCode(){
			return territoryCode;
		}

		public String getType(){
			return type;
		}

		// custom String representation for the card
		public String toString()
		{
			String str = switch (type) {
				case "I" -> "INFANTRY";
				case "C" -> "CAVALRY";
				case "A" -> "ARTILLERY";
				case "W" -> "WILD CARD\n";
				default -> null;
			};

			assert str != null;
			if(str.equals("WILD CARD\n")){
				return str;
			}

			return (Constants.COUNTRY_NAMES[territoryCode] + " ("+ str + ")\n");
		}
	}


}
