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
    private static final JTextArea textArea = new JTextArea(1, 16);
    private static final JTextField textField = new JTextField( 1);
    private static String playerName1;
    private static String playerName2;

    public UIWindow(){
        createAndShowGUI();
        getPlayerNames();
        if(playerName1==null){
            System.out.println("NO");
        }else{
            System.out.print("Yes");
        }
        System.out.println(playerName1);
        System.out.println(playerName2);

        System.out.print("Yes");
    }

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

        WorldMap map = new WorldMap();
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 3;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        pane.add(map, c);

        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setEditable(true);

        ActionListener listener =new AddUIListener();
        textField.addActionListener(listener);

        c.gridx = 3;
        c.gridy = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.ipady = 30;
        pane.add(textField, c);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("User Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(Constants.getFrameWidth(), Constants.getFrameHeight());

        addComponentsToPane(frame.getContentPane());
        frame.setVisible(true);
    }

    private static void getPlayerNames(){
        playerName1=textField.getText();
        playerName2=textField.getText();
    }

    static class AddUIListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
                userCommand=textField.getText();
                System.out.println(userCommand);
        }
    }
}