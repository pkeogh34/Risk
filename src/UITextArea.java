//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.Serial;

public class UITextArea extends JPanel {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final int CHARACTER_WIDTH = 16;
    private static final int FONT_SIZE = 14;

    JTextArea textArea = new JTextArea(20, CHARACTER_WIDTH);
    JScrollPane scrollPane = new JScrollPane(textArea);

    UITextArea() {
        textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, FONT_SIZE));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    }

    public void addText(String text) {
        textArea.setText(textArea.getText()+"\n"+text);
    }

}