//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import java.awt.event.ActionEvent;
import java.util.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import javax.swing.JTextField;


public class UserInputArea extends JPanel  {

    private static final long serialVersionUID = 1L;
    private static final int FONT_SIZE = 14;

    private JTextField commandField = new JTextField();
    private LinkedList<String> commandBuffer = new LinkedList<String>();

    UserInputArea () {
        class AddActionListener implements ActionListener {
            public void actionPerformed(ActionEvent event)	{
                synchronized (commandBuffer) {
                    commandBuffer.add(commandField.getText());
                    commandField.setText("");
                    commandBuffer.notify();
                }
                return;
            }
        }
        ActionListener listener = new AddActionListener();
        commandField.addActionListener(listener);
        commandField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE));
        setLayout(new BorderLayout());
        add(commandField, BorderLayout.CENTER);
        return;
    }

    public String getCommand() {
        String command;
        synchronized (commandBuffer) {
            while (commandBuffer.isEmpty()) {
                try {
                    commandBuffer.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            command = commandBuffer.pop();
        }
        return command;
    }
}
