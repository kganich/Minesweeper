package cz.cuni.mff.ganichk.Minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Grid of the Game, having the utilities of the game
 */
public class MineGrid extends JFrame implements ActionListener {
    private Timer closeGame = new Timer(10,null);
    private JPanel gridPanel;
    JFrame startMenu;

    /**
     * Constructor of the class, calls initCompo() to initialize components
     * @param rows, total rows of the board
     * @param columns, total columns of the board
     * @param mines, total count of mines
     */
    public MineGrid(int rows, int columns, int mines) {
        initCompo(rows, columns, mines);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Initialize the main panel of the game
     * @param x, rows of the board
     * @param y, columns of the board
     * @param mines, count of mines
     */
    public void initCompo(int x, int y, int mines) {
        setLayout(new BorderLayout());
        JMenuBar menu = new JMenuBar();
        gridPanel = new JPanel();
        GuiGrid grid = new GuiGrid(x, y, mines);
        gridPanel.add(grid, BorderLayout.NORTH);
        JMenu game = new JMenu("Game");

        JButton scores = new JButton("Scores");
        scores.addActionListener(this);
        scores.setActionCommand("Scores");
        JMenuItem newMenu = new JMenuItem("Settings");
        newMenu.addActionListener(this);
        newMenu.setActionCommand("Settings");
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        exit.setActionCommand("exit");

        add(menu, BorderLayout.NORTH);

        newMenu.setToolTipText("Create new Default Game");
        menu.add(game);
        menu.add(scores);
        game.add(newMenu);
        game.add(exit);

        add(gridPanel, BorderLayout.CENTER);

        add(new JPanel(), BorderLayout.EAST);
        add(new JPanel(), BorderLayout.WEST);
        pack();
        setLocationRelativeTo(null);
    }


    /**
     * Action performed when a button is pressed.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Settings" -> {
                dispose();
                removeAll();
                new Menu().setVisible(true);
            }
            case "exit" -> doClose();
            case "Scores" -> {
                try {
                    Map<String, ArrayList<String>> topPlayers = readFile("src/main/java/cz.cuni.mff/ganichk.Minesweeper/highScores.txt");

                    JDialog popupMenu = new JDialog(startMenu);
                    JPanel flow = new JPanel(new FlowLayout());
                    popupMenu.setSize(new Dimension(500, 200));
                    popupMenu.setLayout(new BorderLayout());
                    popupMenu.add(flow, BorderLayout.CENTER);
                    popupMenu.setLocationRelativeTo(startMenu);

                    JButton close = new JButton("Close");
                    JLabel title = new JLabel("Fastest Sweeps");
                    title.setFont(new Font("Ariel", Font.PLAIN, 22));
                    title.setHorizontalAlignment(SwingConstants.CENTER);
                    popupMenu.add(title, BorderLayout.NORTH);

                    close.addActionListener(new ActionListener() {
                        Timer timer = new Timer(50, null);

                        @Override
                        public void actionPerformed(ActionEvent e) {

                            timer = new Timer(1, new ActionListener() {
                                float opacity = 1;

                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    opacity -= 0.1;
                                    if (opacity < 0) {
                                        timer.stop();
                                        popupMenu.setVisible(false);
                                    } else {
                                        popupMenu.setSize(new Dimension(popupMenu.getWidth(),
                                                (int) (popupMenu.getHeight() * opacity)));
                                    }

                                }
                            });
                            timer.start();
                        }

                    });

                    for (Map.Entry<String, ArrayList<String>> entry : topPlayers.entrySet()) {
                        StringBuilder sb = new StringBuilder(entry.getKey() + ":" + "\n");
                        for (String score : entry.getValue()) {
                            String[] parts = score.split(", ");
                            sb.append(parts[0]).append("(").append(parts[1]).append("s)\n");
                        }
                        sb.append("\n");
                        flow.add(new JLabel(sb.toString()));
                    }

                    popupMenu.add(close, BorderLayout.SOUTH);
                    popupMenu.setVisible(true);

                } catch (HeadlessException ex) {
                    ex.getMessage();
                }
            }
        }
    }

    /**
     * Read file with scores
     * @param filePath, path to file with results of games
     * @return top five plays of each difficulty
     */
    public Map<String, ArrayList<String>> readFile(String filePath) {
        Map<String, ArrayList<String>> difficultyCategories = new HashMap<>();
        difficultyCategories.put("Easy", new ArrayList<>());
        difficultyCategories.put("Medium", new ArrayList<>());
        difficultyCategories.put("Hard", new ArrayList<>());
        difficultyCategories.put("Extreme", new ArrayList<>());

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                String difficulty = parts[0];
                if (difficultyCategories.containsKey(difficulty)) {
                    if (parts[1].length() > 0) difficultyCategories.get(difficulty).add(parts[1] + ", " + parts[2]);
                    else difficultyCategories.get(difficulty).add("anon, " + parts[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (ArrayList<String> category : difficultyCategories.values()) {
            category.sort((s1, s2) -> {
                int score1 = Integer.parseInt(s1.split(", ")[1]);
                int score2 = Integer.parseInt(s2.split(", ")[1]);
                return Integer.compare(score1, score2);
            });
        }

        Map<String, ArrayList<String>> topFiveByDifficulty = new HashMap<>();
        for (Map.Entry<String, ArrayList<String>> entry : difficultyCategories.entrySet()) {
            ArrayList<String> topFive = new ArrayList<>(entry.getValue().subList(0, Math.min(entry.getValue().size(), 5)));
            topFiveByDifficulty.put(entry.getKey(), topFive);
        }

        return topFiveByDifficulty;
    }

    /**
     * Close the game
     */
    private void doClose() {
        try {
            closeGame = new Timer(10, new ActionListener() {
                float opacity = 1;

                @Override
                public void actionPerformed(ActionEvent e) {
                    opacity -= 0.1;
                    if (opacity < 0) {
                        closeGame.stop();
                        dispose();
                        removeAll();
                        System.exit(0);
                    } else {
                        gridPanel.setSize(
                                new Dimension(gridPanel.getWidth(), (int) (gridPanel.getHeight() * opacity)));
                    }
                }
            });
            closeGame.start();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
