public class Territory {
    private static String territoryContinent;
    private static int continentCode;
    public static boolean occupied;
    public static String occupyingPlayer;
    public static int playerCode;
    public static int numOccupyingArmies;


    public static int getContinentCode() {
        return continentCode;
    }

    public static void setContinentCode(int continentCode) {
        Territory.continentCode = continentCode;
    }

    public static String getTerritoryContinent() {
        return territoryContinent;
    }

    public static void setTerritoryContinent(String territoryContinent) {
        Territory.territoryContinent = territoryContinent;
    }
}
