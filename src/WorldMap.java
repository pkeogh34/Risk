//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class WorldMap extends JComponent{
    public void paintComponent(Graphics g) {
        // Recover Graphics2D
        Graphics2D g2 = (Graphics2D) g;


        //Creates the nodes representing each territory amd connects them with lines
        for ( int i=0; i < Constants.NUM_COUNTRIES; i++ )
        {
            //Creates a node at each coordinate point
            g2.setColor(Color.GRAY);
            int x1 = Constants.getCountryCoord(i,0);
            int y1 = Constants.getCountryCoord(i,1);
            Ellipse2D.Double node = new Ellipse2D.Double(x1, y1, 20, 20);
            if(i<=8){
                g2.setColor(Color.RED);
                g2.fill(node);
            }else if(i>8 && i<=17){
                g2.setColor(Color.BLUE);
                g2.fill(node);
            }else if(i>17&&i<=23){
                g2.setColor(Color.PINK);
                g2.fill(node);
            }else if(i>23&&i<=29){
                g2.setColor(Color.MAGENTA);
                g2.fill(node);
            }else if(i>29&&i<=35){
                g2.setColor(Color.YELLOW);
                g2.fill(node);
            }else if(i>35&&i<=41){
                g2.setColor(Color.GREEN);
                g2.fill(node);
            }
            g2.draw(node);


            //Connects the newly created node to its adjacent nodes
            for (int k : Constants.ADJACENT[i]) {
                g2.setColor(Color.LIGHT_GRAY);
                if(i==8 || i==22){
                    continue;
                }
                int x2 = Constants.getCountryCoord(k,0);
                int y2 = Constants.getCountryCoord(k,1);
                Line2D.Double line = new Line2D.Double(x1+10, y1+10, x2+10, y2+10);
                g2.draw(line);
            }

            g2.setColor(Color.BLACK);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            Font font = new Font("Arial", Font.PLAIN, 12);
            g2.setFont(font);

            g2.drawString(Constants.COUNTRY_NAMES[i], x1-15, y1-2);
            g2.drawString("1", x1+7, y1+30);
        }
        g2.setColor(Color.LIGHT_GRAY);
        Line2D.Double line = new Line2D.Double(0, 65, 15+10, 49+10);
        g2.draw(line);
        Line2D.Double line1 = new Line2D.Double(787+10, 54+10, 1000, 54);
        g2.draw(line1);
    }
}
