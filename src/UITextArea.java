import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class UITextArea extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int CHARACTER_WIDTH = 14;
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
        add(scrollPane);
        return;
    }

    public void addText(String text) {
        textArea.setText(textArea.getText()+"\n"+text);
    }

}