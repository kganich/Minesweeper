package cz.cuni.mff.ganichk.Minesweeper;

/**
 * Main class of the application
 * @author Karina Ganich
 */
public class Main {
    /**
     * Main method of the program. Calls Menu to start application
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new Menu().setVisible(true));
    }
}