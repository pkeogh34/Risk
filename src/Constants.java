//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import java.awt.*;

public class Constants {
    public static final int NUM_PLAYERS = 2;
    public static final int NUM_NEUTRALS = 4;
    public static final int NUM_PLAYERS_PLUS_NEUTRALS = NUM_PLAYERS + NUM_NEUTRALS;
    public static final int NUM_COUNTRIES = 42;
    public static final int INIT_COUNTRIES_PLAYER = 9;
    public static final int INIT_COUNTRIES_NEUTRAL = 6;
    public static final int INIT_UNITS_PLAYER = 36;
    public static final int INIT_UNITS_NEUTRAL = 24;
    private static final Color[] PLAYER_COLORS = {new Color(255, 20, 0),Color.BLUE,new Color(140, 229, 181),new Color(157, 172, 174),new Color(235, 161, 0),Color.WHITE};
    public static final String[] PLAYER_COLOR_NAME = {"RED","BLUE","GREEN","GREY","ORANGE","WHITE"};
	//                                                        N.America                            Europe                              Asia                         Australia                         S.America                      Africa
    public static final Color[] CONTINENT_COLORS = {new Color(195,133,83), new Color(75, 203, 215), new Color(255,127,90), new Color(224, 172, 213), new Color(100,0,150), new Color(255,223,0)};
    public static final String[] COUNTRY_NAMES = {
    		//0
            "Ontario", "Quebec", "NW Territory", "Alberta", "Greenland", "E United States", "W United States", "Central America", "Alaska",
            //9
			"Great Britain", "W Europe", "S Europe", "Ukraine", "N Europe", "Iceland", "Scandinavia",
            //16
			"Afghanistan", "India", "Middle East", "Japan", "Ural", "Yakutsk", "Kamchatka", "Siam", "Irkutsk", "Siberia", "Mongolia", "China",
            //28
			"E Australia", "New Guinea", "W Australia", "Indonesia",
			//32
            "Venezuela", "Peru", "Brazil", "Argentina",
			//36
            "Congo", "N Africa", "S Africa", "Egypt", "E Africa", "Madagascar"};  // for reference
    public static final int[][] ADJACENT = {
            {4, 1, 5, 6, 3, 2},    // 0
            {4, 5, 0},
            {4, 0, 3, 8},
            {2, 0, 6, 8},
            {14, 1, 0, 2},
            {0, 1, 7, 6},
            {3, 0, 5, 7},
            {6, 5, 32},
            {2, 3, 22},
            {14, 15, 13, 10},
            {9, 13, 11, 37},     // 10
            {13, 12, 18, 37, 39, 10},
            {20, 16, 18, 11, 13, 15},
            {15, 12, 11, 10, 9},
            {15, 9, 4},
            {9, 12, 13, 14},
            {20, 27, 17, 18, 12},
			{16, 27, 23, 18},
            {12, 16, 17, 40, 39, 11},
            {26, 22},
            {25, 27, 16, 12},    // 20
            {22, 24, 25},
            {8, 19, 26, 24, 21},
            {27, 31, 17},
            {21, 22, 26, 25},
            {21, 24, 26, 27, 20},
            {24, 22, 19, 27, 25},
            {26, 23, 17, 16, 20, 25},
            {29, 30},
            {28, 30, 31},
            {29, 28, 31},      // 30
            {23, 29, 30},
            {7, 34, 33},
            {32, 34, 35},
            {32, 37, 35, 33},
            {33, 34},
            {37, 40, 38},
            {10, 11, 39, 40, 36, 34},
            {36, 40, 41},
            {11, 18, 40, 37},
            {39, 18, 41, 38, 36, 37},  //40
            {38, 40}
    };
    public static final int NUM_CONTINENTS = 6;
    public static final String[] CONTINENT_NAMES = {"N America", "Europe", "Asia", "Australia", "S America", "Africa"};  // for reference
    public static final int[] CONTINENT_IDS = {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5};
    public static final int[][] CONTINENT_VALUES = {{9, 7, 12, 4, 4, 6},{5, 5, 7, 2, 2, 3}};
    private static final int FRAME_WIDTH = 1130;    // must be even
    private static final int FRAME_HEIGHT = 660;
    private static final int[][] COUNTRY_COORD = {
            {191,150},     // 0
            {255,161},
            {146,86},
            {123,144},
            {314,61},
            {205,235},
            {135,219},
            {140,299},
            {45,89},
            {390,199},
            {398,280},      // 10
            {465,292},
            {547,180},
            {460,200},
            {373,127},
            {463,122},
            {628,227},
            {679,332},
            {572,338},
            {861,213},
            {645,152},      // 20
            {763,70},
            {827,94},
            {751,360},
            {750,140},
            {695,108},
            {760,216},
            {735,277},
            {889,537},
            {850,429},
            {813,526},       // 30
            {771,454},
            {213,342},
            {180,405},
            {289,415},
            {233,533},
            {496,462},
            {440,383},
            {510,532},
            {499,354},
            {547,432},        // 40
            {576,515}
    };
	
	public static final String[] CARD_UNIT_TYPE = {
			"C",
			"A",
			"A",
			"I",
			"C",
			"A",
			"I",
			"C",
			"I",
			"C",		// 10
			"I",
			"C",
			"A",
			"C",
			"I",
			"A",
			"I",
			"I",
			"A",
			"I",		// 20
			"C",
			"C",
			"C",
			"A",
			"I",
			"A",
			"A",
			"C",
			"I",
			"C",		// 30
			"A",
			"C",
			"A",
			"C",
			"A",
			"I",
			"C",
			"I",
			"A",
			"I",		// 40
			"A",
			"I",
			"W",
			"W"
	};

	public static final String[] VALID_COMBINATIONS = {
			"III",
			"CCC",
			"AAA", //3

			"IIW",
			"IWI",
			"WII",

			"CCW",
			"CWC",
			"WCC",

			"AAW",
			"AWA",
			"WAA", //12

			"ICA",
			"IAC",
			"CIA",
			"CAI",
			"AIC",
			"ACI",

			"WCA",
			"IWA",
			"ICW",

			"WAC",
			"IWC",
			"IAW",

			"WIA",
			"CWA",
			"CIW",

			"WAI",
			"CWI",
			"CAW",

			"WIC",
			"AWC",
			"AIW",

			"WCI",
			"AWI",
			"ACW",
	};

	public static int[] NUM_ARMIES_FOR_SET = {4, 6, 8, 10, 12, 15};

    public static int getCountryCoord(int x, int y) {
        return COUNTRY_COORD[x][y];
    }
    public static int getFrameWidth() {
        return FRAME_WIDTH;
    }
    public static int getFrameHeight() {
        return FRAME_HEIGHT;
    }
    public static Color getPlayerColors(int x) {
        return PLAYER_COLORS[x];
    }

    private Constants() {
        //this prevents even the native class from calling this constructor
        throw new AssertionError();
    }
}