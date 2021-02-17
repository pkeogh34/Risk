

enum PlayerColor
{
	RED, YELLOW, BLUE, GREEN, ORANGE, PURPLE
	
}
public class Players
{

	PlayerColor playerColor;
	
	public Players(PlayerColor c) {
		// TODO Auto-generated constructor stub
	}

	public void PlayerColor( PlayerColor playerColor)
	{
		this.playerColor = playerColor;
	}
	
	public void PlayerCall()
	{
		switch(playerColor)
		{
		case RED:
			System.out.println("Player1 is RED.");
			break;
			
		case YELLOW:
			System.out.println("Player2 is YELLOW.");
			break;
			
		case BLUE:
			System.out.println("Player3 is BLUE.");
			break;
			
		case GREEN:
			System.out.println("Plsyer4 is GREEN.");
			break;
		case ORANGE:
			System.out.println("Player5 is GRAY");
			break;
			
		case PURPLE:
			System.out.println("Plsyer6 is PURPLE.");
			break;
		}
	}
	
}