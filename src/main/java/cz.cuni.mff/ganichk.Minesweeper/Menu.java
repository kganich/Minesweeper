package cz.cuni.mff.ganichk.Minesweeper;

import javax.swing.*;
import java.awt.*;

/**
 * Main Menu of the program.
 * Shows the difficulties of the game as buttons for the player to choose.
 */
public class Menu extends JFrame {

    /**
     * Constructor of the class. Calls initComponents to create GUI of menu
     */
    public Menu() {
        initComponents();
        setLocationRelativeTo(null);
    }

    /**
     * Shows menu with possible difficulties of the game
     */
    private void initComponents(){
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel southPanel = new JPanel(new BorderLayout());
        JPanel subSouthPanel = new JPanel(new FlowLayout());
        JPanel defaultValuesPanel = new JPanel(new FlowLayout());
        JPanel input = new JPanel(new FlowLayout());
        JLabel startLabel = new JLabel("Start Menu");
        JLabel defaultLabel = new JLabel("Default Game Values");

        JButton buttonEasy = new JButton(" 9x9 6 Mines");
        buttonEasy.addActionListener(new StartGameAction(this, new MineGrid(9, 9,6)));

        JButton buttonMedium = new JButton(" 16x16 20 Mines");
        buttonMedium.addActionListener(new StartGameAction(this, new MineGrid(16, 16,20)));

        JButton buttonHard = new JButton(" 16x32 40 Mines");
        buttonHard.addActionListener(new StartGameAction(this, new MineGrid(16, 32,40)));

        JButton buttonExtreme = new JButton(" 25x30 100 Mines");
        buttonExtreme.addActionListener(new StartGameAction(this, new MineGrid(25, 30,100)));

        topPanel.add(input, BorderLayout.NORTH);
        input.add(startLabel, RIGHT_ALIGNMENT);
        topPanel.add(southPanel, BorderLayout.SOUTH);
        southPanel.add(defaultValuesPanel, BorderLayout.SOUTH);
        defaultValuesPanel.add(buttonEasy);
        defaultValuesPanel.add(buttonMedium);
        defaultValuesPanel.add(buttonHard);
        defaultValuesPanel.add(buttonExtreme);
        southPanel.add(subSouthPanel,BorderLayout.NORTH);
        subSouthPanel.add(defaultLabel);
        add(topPanel);
        setSize(5000, 5000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        pack();
    }
}
