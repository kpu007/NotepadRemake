package net.k07.notepad;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

	private static NotepadWindow rootWindow = new NotepadWindow();
    public static void main(String[] args) {

    	String text = "";
    	String path = null;
	    JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	        path = chooser.getSelectedFile().getAbsolutePath();
        }

	    List<String> lines = Collections.emptyList();
	    try {
	    	lines = Files.readAllLines(Paths.get(path));
		}
	    catch(IOException e) {
	    	JOptionPane.showMessageDialog(null, "Error when opening file!", "Error", JOptionPane.ERROR_MESSAGE);
	    	System.exit(0);
		}

	    for(String line: lines) {
	    	text += line + "\n";
		}

	    rootWindow.textArea.setText(text);
	    rootWindow.setSize(700, 700);
	    rootWindow.setVisible(true);

    }
}
