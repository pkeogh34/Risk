public class Board {

   private static Territory territories[]=new Territory[Constants.NUM_COUNTRIES];

    Board() {
        for (int i=0; i<Constants.NUM_COUNTRIES; i++) {
            territories[i].setTerritoryContinent(Constants.CONTINENT_NAMES[Constants.CONTINENT_IDS[i]]);
            territories[i].setContinentCode(Constants.CONTINENT_IDS[i]);
            territories[i].occupied=false;
            territories[i].numOccupyingArmies=0;
        }
        return;
    }

    public void addUnits (int country, int player, int addNumUnits) {
        // prerequisite: country must be unoccupied or already occupied by this player
        if (!territories[country].occupied) {
            territories[country].occupied = true;
            territories[country].playerCode = player;
        }
        territories[country].numOccupyingArmies = territories[country].numOccupyingArmies + addNumUnits;
        return;
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
