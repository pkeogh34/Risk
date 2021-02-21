public class Board {

   private Territory[] territories =new Territory[Constants.NUM_COUNTRIES];

    Board() {
        for (int i=0; i<Constants.NUM_COUNTRIES; i++) {
            String territoryName=Constants.COUNTRY_NAMES[i];
            String territoryContinent=Constants.CONTINENT_NAMES[Constants.CONTINENT_IDS[i]];
            int continentCode= Constants.CONTINENT_IDS[i];
            Territory territory = new Territory(territoryName,i, territoryContinent,continentCode, false,0);
            territories[i]=territory;
        }
    }

    public void addUnits (int territory, int addNumUnits) {
        // prerequisite: country must be unoccupied or already occupied by this player
        if (!territories[territory].occupied) {
            territories[territory].occupied = true;;
        }
        territories[territory].numOccupyingArmies += addNumUnits;
    }

    //Create just one getter and add getters in Territory class?
    public boolean isOccupied(int territory) {
        return territories[territory].occupied;
    }

    public int getOccupier(int territory){
        return territories[territory].playerCode;
    }

    public void setOccupier(int territory, int player){
        territories[territory].playerCode=player;
    }

    public int getNumUnits(int territory) {
        return territories[territory].numOccupyingArmies;
    }

    public Territory getTerritory(int territory){
        return territories[territory];
    }

}
