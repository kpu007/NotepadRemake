package net.k07.notepad;

public class Main {

	private static NotepadWindow rootWindow = new NotepadWindow();

    public static void main(String[] args) {
	    rootWindow.setSize(700, 700);
	    rootWindow.setVisible(true);
    }
}
