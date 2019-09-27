package net.k07.notepad;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotepadWindow extends JFrame {
    public JScrollPane pane;
    public JTextArea textArea;
    public File openedFile = null;
    public boolean fileChanged = false;

    public NotepadWindow() {
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setTitle("Untitled - NotepadRemake");

        JButton open = new JButton();
        open.addActionListener(e -> {
            openFile();
        });
        this.add(open, BorderLayout.NORTH);

        JButton close = new JButton();
        close.addActionListener(e -> {
            closeFile();
        });
        this.add(close, BorderLayout.SOUTH);

        this.textArea = new JTextArea();
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                fileChanged = true;
            }
            public void removeUpdate(DocumentEvent e) {
                fileChanged = true;
            }
            public void changedUpdate(DocumentEvent e) {
                fileChanged = true;
            }
        });
        this.pane = new JScrollPane(textArea);
        this.add(pane, BorderLayout.CENTER);
    }

    public void openFile() {
        File file = pickFile();
        if(file == null) {
            return;
        }

        this.openedFile = file;
        this.textArea.setText(getFileContents(file));
        this.setTitle(file.getName() + " - NotepadRemake");

        this.fileChanged = false;
    }

    public File pickFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }

        return null;
    }

    public String getFileContents(File file) {
        String text = "";
        String pathString = file.getAbsolutePath();
        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(pathString));
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(null, "Error when opening file!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        for(String line: lines) {
            text += line + "\n";
        }

        return text;
    }

    public void closeFile() {
        if(fileChanged) {
            int result = JOptionPane.showConfirmDialog(null, "Save changes to " + openedFile.getName() + "?", "Save Changes", JOptionPane.YES_NO_OPTION);

            if(result == 0) {
                saveToFile();
            }
        }

        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public void saveToFile() {
        if(openedFile == null) {
            return;
        }

        String pathString = openedFile.getAbsolutePath();
        ArrayList<String> lines = new ArrayList<String>();
        lines.add(textArea.getText());

        try {
            Files.write(Paths.get(openedFile.getAbsolutePath()), lines, StandardOpenOption.WRITE);
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(null, "Error when writing file!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

}

