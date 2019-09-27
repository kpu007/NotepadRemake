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




	    rootWindow.textArea.setText(text);
	    rootWindow.setSize(700, 700);
	    rootWindow.setVisible(true);

    }
}
