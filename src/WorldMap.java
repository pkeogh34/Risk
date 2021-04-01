//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.Serial;

class WorldMap extends JPanel {

    @Serial
    private static final long serialVersionUID = 1L;
    private final GameData gameData;
    private static final int COUNTRY_RADIUS = 12;   // must be even
    private static final int NAME_OFFSET_X = 3;
    private static final int NAME_OFFSET_Y = 13;
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final int ADJACENT_LINE = 1;
    private static final Color ADJACENT_COLOR = Color.GRAY;
    private static final int PLAYER_RADIUS = 8;

    WorldMap (GameData gameData) {
        this.gameData = gameData;
        setBackground(new Color(255,255,255));
    }

    public void paintComponent(Graphics g) {
        int xPos, yPos, xPosBegin, yPosBegin, xPosEnd, yPosEnd;
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // Display adjacent lines
        g2.setStroke(new BasicStroke(ADJACENT_LINE));
        g2.setColor(ADJACENT_COLOR);
        for (int i=0; i<Constants.NUM_COUNTRIES; i++) {
            xPosBegin = Constants.getCountryCoord(i,0);
            yPosBegin = Constants.getCountryCoord(i,1);
            for (int j=0; j<Constants.ADJACENT[i].length; j++) {
                xPosEnd = Constants.getCountryCoord(Constants.ADJACENT[i][j],0);
                yPosEnd = Constants.getCountryCoord(Constants.ADJACENT[i][j],1);
                if (xPosBegin < xPosEnd) {
                    if (Math.abs(xPosEnd-xPosBegin)<Constants.getFrameWidth()/2) {
                        g2.drawLine(xPosBegin, yPosBegin, xPosEnd, yPosEnd);
                    } else {
                        g2.drawLine(0,yPosBegin,xPosBegin,yPosBegin);
                        g2.drawLine(Constants.getFrameWidth()-1,yPosEnd,xPosEnd,yPosEnd);
                    }
                }
            }
        }
        // Display countries
        for (int i=0; i<Constants.NUM_COUNTRIES; i++) {
            g2.setColor(Constants.CONTINENT_COLORS[Constants.CONTINENT_IDS[i]]);
            createCircle(g2, i, COUNTRY_RADIUS);
            xPos = Constants.getCountryCoord(i,0) - Constants.COUNTRY_NAMES[i].length()*NAME_OFFSET_X;
            yPos = Constants.getCountryCoord(i,1) - NAME_OFFSET_Y;
            g2.drawString(Constants.COUNTRY_NAMES[i],xPos,yPos);
        }
        // Display players units
        for (int i=0; i<Constants.NUM_COUNTRIES; i++) {
            if (gameData.isOccupied(i)) {
                g2.setColor(Constants.getPlayerColors(gameData.getOccupier(i)));
                createCircle(g2, i, PLAYER_RADIUS);
                xPos = Constants.getCountryCoord(i,0) - NAME_OFFSET_X;
                yPos = Constants.getCountryCoord(i,1) + 2*PLAYER_RADIUS + NAME_OFFSET_Y;
                g2.drawString(String.valueOf(gameData.getNumUnits(i)),xPos,yPos);
            }
        }
    }

    private void createCircle(Graphics2D g2, int i, int radius) {
        int xPos;
        int yPos;
        xPos = Constants.getCountryCoord(i,0) - radius;
        yPos = Constants.getCountryCoord(i,1) - radius;
        Ellipse2D.Double ellipse = new Ellipse2D.Double(xPos,yPos,2* radius,2* radius);
        g2.fill(ellipse);
        g2.setColor(TEXT_COLOR);
    }

    public void refresh() {
        revalidate();
        repaint();
    }
}

