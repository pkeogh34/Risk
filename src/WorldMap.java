//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

class WorldMap extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int COUNTRY_RADIUS = 12;   // must be even
    private static final int NAME_OFFSET_X = 3;
    private static final int NAME_OFFSET_Y = 13;
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final int ADJACENT_LINE = 1;
    private static final Color ADJACENT_COLOR = Color.LIGHT_GRAY;
    public static final Color[] CONTINENT_COLORS = {Color.BLACK, Color.CYAN, Color.DARK_GRAY, Color.PINK, Color.ORANGE, Color.GRAY};
    private static final int PLAYER_RADIUS = 8;
   
    private Board board;

    WorldMap (Board inBoard) {
        board = inBoard;
        setBackground(Color.WHITE);
        return;
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
            g2.setColor(CONTINENT_COLORS[Constants.CONTINENT_IDS[i]]);
            xPos = Constants.getCountryCoord(i,0) - COUNTRY_RADIUS;
            yPos = Constants.getCountryCoord(i,1) - COUNTRY_RADIUS;
            Ellipse2D.Double ellipse = new Ellipse2D.Double(xPos,yPos,2*COUNTRY_RADIUS,2*COUNTRY_RADIUS);
            g2.fill(ellipse);
            g2.setColor(TEXT_COLOR);
            xPos = Constants.getCountryCoord(i,0) - Constants.COUNTRY_NAMES[i].length()*NAME_OFFSET_X;
            yPos = Constants.getCountryCoord(i,1) - NAME_OFFSET_Y;
            g2.drawString(Constants.COUNTRY_NAMES[i],xPos,yPos);
        }
        // Display players units
        for (int i=0; i<Constants.NUM_COUNTRIES; i++) {
            if (board.isOccupied(i)) {
                g2.setColor(Constants.getPlayerColors(board.getOccupier(i)));
                xPos = Constants.getCountryCoord(i,0) - PLAYER_RADIUS;
                yPos = Constants.getCountryCoord(i,1) - PLAYER_RADIUS;
                Ellipse2D.Double ellipse = new Ellipse2D.Double(xPos,yPos,2*PLAYER_RADIUS,2*PLAYER_RADIUS);
                g2.fill(ellipse);
                g2.setColor(TEXT_COLOR);
                xPos = Constants.getCountryCoord(i,0) - NAME_OFFSET_X;
                yPos = Constants.getCountryCoord(i,1) + 2*PLAYER_RADIUS + NAME_OFFSET_Y;
                g2.drawString(String.valueOf(board.getNumUnits(i)),xPos,yPos);
            }
        }
        return;
    }

    public void refresh() {
        revalidate();
        repaint();
        return;
    }
}

