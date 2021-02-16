public class Territory {
    private static String territoryContinent;
    private static String territoryName;
    private static int continentCode;
    public static boolean occupied;
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

    public static String getTerritoryName() {
        return territoryName;
    }

    public static void setTerritoryName(String territoryName) {
        Territory.territoryName = territoryName;
    }
}
