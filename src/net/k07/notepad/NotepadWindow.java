package net.k07.notepad;

import javax.swing.*;
import java.awt.*;

public class NotepadWindow extends JFrame {
    public JScrollPane pane;
    public JTextArea textArea;

    public NotepadWindow() {
        super();
        this.setLayout(new BorderLayout());
        this.setTitle("Untitled - NotepadRemake");

        this.textArea = new JTextArea();
        this. pane = new JScrollPane(textArea);
        this.add(pane, BorderLayout.CENTER);
    }



}
