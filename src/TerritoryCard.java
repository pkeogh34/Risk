public class TerritoryCard
{
	private Constants.UnitType type;
	private String countryName;
	//TODO later: add continent
	
	
	public TerritoryCard (int country_index) {
		this.type = Constants.CARD_UNIT_TYPE[country_index];
		this.countryName = Constants.COUNTRY_NAMES[country_index];
	}
	
	// custom String representation for our card
	public String toString() 
	{
		
		String s = null;
		switch (type)
		{
		case INFANTRY:
		s = "INFANTRY";
		break;
		case CAVALRY:
			s = "CAVALRY";
			break;
		case ARTILLERY:
			s = "ARTILLERY";
			break;
		}
		return (countryName + "("+ s + ")\n");
	}
	
}

