public class Territory {
    public String territoryName;
    public int territoryCode;
    public String territoryContinent;
    public int continentCode;
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
