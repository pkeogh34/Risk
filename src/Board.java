public class Board {

   private Territory[] territories =new Territory[Constants.NUM_COUNTRIES];

    Board() {
        for (int i=0; i<Constants.NUM_COUNTRIES; i++) {
            String territoryName=Constants.COUNTRY_NAMES[i];
            String territoryContinent=Constants.CONTINENT_NAMES[Constants.CONTINENT_IDS[i]];
            int continentCode= Constants.CONTINENT_IDS[i];
            Territory territory = new Territory(territoryName,territoryContinent,continentCode, false,0);
            territories[i]=territory;
        }
    }

    public void addUnits (int country, int player, int addNumUnits) {
        // prerequisite: country must be unoccupied or already occupied by this player
        if (!territories[country].occupied) {
            territories[country].occupied = true;
            territories[country].playerCode = player;
        }
        territories[country].numOccupyingArmies += addNumUnits;
    }

    public boolean isOccupied(int country) {
        return territories[country].occupied;
    }

    public int getOccupier (int country) {
        return territories[country].playerCode;
    }

    public int getNumUnits (int country) {
        return territories[country].numOccupyingArmies;
    }

}
