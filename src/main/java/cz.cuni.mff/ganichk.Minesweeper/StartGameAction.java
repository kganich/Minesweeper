package cz.cuni.mff.ganichk.Minesweeper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Called in the Menu, hiding the menu frame and making visible the selected frame of the game
 */
public class StartGameAction implements ActionListener {
    private final JFrame menu;
    private final JFrame game;

    /**
     * Constructor of the class. Takes the main and the game.
     * @param menu, frame of startMenu
     * @param game, selected frame of Game
     */
    public StartGameAction(JFrame menu, JFrame game) {
        this.game = game;
        this.menu = menu;
    }

    /**
     * When mouse is clicked menu is not visible and game becomes visible
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.menu.setVisible(false);
        this.game.setVisible(true);
    }
}
