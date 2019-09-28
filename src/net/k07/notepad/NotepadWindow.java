package net.k07.notepad;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    /**
     * Sets up the window and all of its components + action listeners
     */
    public NotepadWindow() {
        super();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setTitle("Untitled - NotepadRemake");
        this.addWindowListener(new NotepadWindowAdapter());

        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        JMenu fileMenu = new JMenu("File");

        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(e -> {
            newFile();
        });
        fileMenu.add(newItem);

        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(e -> {
            boolean canClose = promptToSaveFile();
            if(canClose) {
                openFile(pickFile());
            }
        });
        fileMenu.add(open);

        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(e -> {
            saveToFile();
        });
        fileMenu.add(save);

        JMenuItem close = new JMenuItem("Close");
        close.addActionListener(e -> {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        fileMenu.add(close);

        menuBar.add(fileMenu);
        this.add(menuBar, BorderLayout.NORTH);

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

    /**
     * Brings up the dialog for creating a new file. Does not overwrite existing files
     */
    public void newFile() {
        JFileChooser chooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showDialog(null, "Create");
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            Path filePath = Paths.get(file.getAbsolutePath());

            try {
                if(Files.exists(filePath)) {
                    JOptionPane.showMessageDialog(this, "File already exists! Use \"Open\" to open", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else {
                    Files.write(filePath, "".getBytes());
                }
            }
            catch(IOException e) {
                System.out.println("Couldn't create file!");
            }

            openFile(file);
        }
    }

    /**
     * Given a file, open it. Set our window's file to it, and make it so that we are viewing its text in our text area.
     *
     * @param file the file to open
     */
    public void openFile(File file) {
        if(file == null) {
            return;
        }

        this.openedFile = file;
        this.textArea.setText(getFileContents(file));
        this.setTitle(file.getName() + " - NotepadRemake");

        this.fileChanged = false;
    }

    /**
     * Open a file chooser dialog and get its selected file.
     * @return the file selected in the dialog, null if nothing was picked
     */
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

    /**
     * Read the file contents of the file into a string.
     * Done by using Files.readAllLines() and then manually combining them into one string using newlines.
     *
     * @param file the file to read from
     * @return the string containing the file's contents
     */
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

    /**
     * Method called when closing the file.
     * Prompts the user to save the file if unsaved changes have been made.
     *
     * @return true if the file close is successful, false if it was cancelled by the "Cancel" button
     */
    public boolean promptToSaveFile() {
        if(fileChanged) {
            int result = JOptionPane.showConfirmDialog(null, "Save changes to " + openedFile.getName() + "?", "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION);

            if(result == 0) {
                saveToFile();
            }
            else if(result == 2) {
                return false;
            }
        }
        return true;
    }

    /**
     * Save the data to the file, and set our unsaved changes flag to false.
     */
    public void saveToFile() {
        if(openedFile == null) {
            return;
        }

        String pathString = openedFile.getAbsolutePath();
        ArrayList<String> lines = new ArrayList<String>();
        lines.add(textArea.getText());

        try {
            Files.write(Paths.get(openedFile.getAbsolutePath()), lines, StandardOpenOption.WRITE);
            fileChanged = false;
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(null, "Error when writing file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Window listener class to prompt for unsaved changes whenever the file is closing before actually closing it.
     * Allows for cancellation if needed.
     */
    class NotepadWindowAdapter extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            boolean canClose = promptToSaveFile();
            if(canClose) {
                JFrame frame = (JFrame)e.getSource();
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            }
        }
    }
}

