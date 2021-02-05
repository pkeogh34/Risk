import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class UIWindow{
    final static boolean RIGHT_TO_LEFT = false;
    private static String userCommand = new String();
    private static JTextArea textArea = new JTextArea(1, 20);
    private static JTextField textField = new JTextField( 10);

    public UIWindow(){
        createAndShowGUI();
    }

    private static void addComponentsToPane(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setText("Welcome to Risk");
        JScrollPane scrollPane = new JScrollPane(textArea);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 2;
        c.gridwidth = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        pane.add(scrollPane,c);


        JLabel map = new JLabel("");
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 3;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        pane.add(map, c);

        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setEditable(true);
        textField.setText("User input area");

        ActionListener listener =new AddUIListener();
        textField.addActionListener(listener);

        c.gridx = 3;
        c.gridy = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.ipady = 50;
        pane.add(textField, c);
    }

    private static void printTextField(String text) {
        textArea.setText(text);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("User Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);

        addComponentsToPane(frame.getContentPane());
        frame.setVisible(true);
    }

    static class AddUIListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            userCommand+=("\n" + textField.getText());
            printTextField(userCommand);
        }
    }

}