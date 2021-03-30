//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import java.awt.*;
import javax.swing.*;

public class UIWindow {

    final static boolean RIGHT_TO_LEFT = false;
    public final Board board;
    private final JFrame window = new JFrame();
    private final WorldMap map;
    private final UITextArea uiTextArea = new UITextArea();
    private final UserInputArea inputArea = new UserInputArea();

    UIWindow (Board board) {
        this.board=board;
        if (RIGHT_TO_LEFT) {
            window.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        map = new WorldMap(board);
        window.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        window.setSize(1130,660);
        window.setTitle("Risk");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setVisible(true);

        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 2;
        c.gridwidth = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        window.add(uiTextArea.scrollPane,c);

        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 3;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        window.add(map, c);

        c.gridx = 3;
        c.gridy = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.ipady = 20;
        window.add(inputArea, c);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(inputArea);
    }

    public String getCommand () {
        return inputArea.getCommand();
    }

    public void displayMap () {
        map.refresh();
    }

    public void displayString (String string) {
        uiTextArea.addText(string);
    }

}