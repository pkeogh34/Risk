//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

//Changes the outputStream to the text area created in UIWindow class

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

public class TextAreaOutput extends OutputStream {

    private JTextArea textArea;

    public TextAreaOutput( JTextArea ta) {
        textArea=ta;
    }

    public void write(int a) throws IOException {
        textArea.append(String.valueOf((char)a));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
