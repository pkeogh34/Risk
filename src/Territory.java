//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

public class Territory {
    public final String territoryName;
    public final int territoryCode;
    public final String territoryContinent;
    public final int continentCode;
    public boolean occupied;
    public int numOccupyingArmies;
    public int playerCode;

    public Territory(String territoryName,int territoryCode, String territoryContinent, int continentCode, boolean occupied,int numOccupyingArmies){
        this.territoryName=territoryName;
        this.territoryCode=territoryCode;
        this.territoryContinent=territoryContinent;
        this.continentCode=continentCode;
        this.occupied=occupied;
        this.numOccupyingArmies=numOccupyingArmies;
    }
}
