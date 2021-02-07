//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import javax.swing.*;

public class UIWindow{
    final static boolean RIGHT_TO_LEFT = false;
    private static String userCommand = new String("");
    private static WorldMap map = new WorldMap();
    private static final JTextArea textArea = new JTextArea(1, 16);
    private static final JTextField textField = new JTextField( 1);
    private static String playerName1;
    private static String playerName2;

    private static void addComponentsToPane(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        textArea.setFont(new Font("Calibri", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 2;
        c.gridwidth = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        pane.add(scrollPane,c);

        PrintStream outStream = new PrintStream( new TextAreaOutput(textArea));
        System.setOut(outStream);

        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 3;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        pane.add(map, c);

        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setEditable(true);
        System.out.print("Enter names for the players in this format: \"1,name\" or \"2,othername\"\n");

        ActionListener listener =new AddUIListener();
        textField.addActionListener(listener);

        c.gridx = 3;
        c.gridy = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.ipady = 30;
        pane.add(textField, c);
    }

    private static void setPlayerName(int i, String name) {
        if (i==1) {
            playerName1 = name;
            textArea.append("Player 1 set to: "+name+"\n");
        }
        else if (i ==2 ) {
            playerName2 = name;
            textArea.append("Player 2 set to: "+name+"\n");
        }
        else {
            textArea.append("error: invalid player number\n");
        }
    }

    public static void createAndShowUI() {
        JFrame frame = new JFrame("User Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(Constants.getFrameWidth(), Constants.getFrameHeight());

        addComponentsToPane(frame.getContentPane());
        frame.setVisible(true);
    }

    static class AddUIListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            userCommand=textField.getText();
            System.out.println(userCommand + "\n");
            textField.setText(""); //reset the input
            String bits[] =  userCommand.split(","); // ONLY ACCEPTS commands like this: "1,name" or "2,othername", anything else not work
            try {
                setPlayerName(Integer.parseInt(bits[0]), bits[1]);
            } catch (Exception e1) {
                System.out.println("error: not a valid command\n");
            }
        }
    }

}