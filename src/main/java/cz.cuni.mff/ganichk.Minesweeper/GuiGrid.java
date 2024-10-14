package cz.cuni.mff.ganichk.Minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * In this class we create the interface of the game with all the buttons and menu items, labels of the number of
 * mines, flags, time and lives left.
 */
public class GuiGrid extends JPanel {

    int x;
    int y;
    int mines;
    public LogicGrid logicGrid;
    private JLabel timeL, liveL, minesL, flagsL;
    private int lifeCounter = 3;
    private int flagCounter;
    private int trueMineCount;
    private final ImageIcon[] iconTable = new ImageIcon[10];
    private ImageIcon facingDown;
    private JButton[][] button;
    private Timer timer;
    private int secs;
    private int mints;
    private int seconds;
    private boolean gameOver = false;
    private int invalid;
    private int validateCounter;
    private int facingDownState;
    private int facingDownCount;

    /**
     * Constructor of the class, calls iconTable() and initialize() methods
     * @param x, width of the board
     * @param y, height of the board
     * @param mines, number of mines
     */
    public GuiGrid(int x, int y, int mines) {
        this.x = x;
        this.y = y;
        iconTable();
        initialize(this.x, this.y, mines);
    }

    /**
     * Create an array of all the available images for the game, scales image for facing down buttons
     */
    public void iconTable() {
        iconTable[0] = new ImageIcon((Objects.requireNonNull(getClass().getResource("/0.png"))));
        iconTable[1] = new ImageIcon((Objects.requireNonNull(getClass().getResource("/1.png"))));
        iconTable[2] = new ImageIcon((Objects.requireNonNull(getClass().getResource("/2.png"))));
        iconTable[3] = new ImageIcon((Objects.requireNonNull(getClass().getResource("/3.png"))));
        iconTable[4] = new ImageIcon((Objects.requireNonNull(getClass().getResource("/4.png"))));
        iconTable[5] = new ImageIcon((Objects.requireNonNull(getClass().getResource("/5.png"))));
        iconTable[6] = new ImageIcon((Objects.requireNonNull(getClass().getResource("/6.png"))));
        iconTable[7] = new ImageIcon((Objects.requireNonNull(getClass().getResource("/7.png"))));
        iconTable[8] = new ImageIcon((Objects.requireNonNull(getClass().getResource("/8.png"))));
        iconTable[9] = new ImageIcon((Objects.requireNonNull(getClass().getResource("/bomb.png"))));

        Image img = new ImageIcon((Objects.requireNonNull(getClass().getResource("/facingDown.png")))).getImage();
        facingDown = new ImageIcon(img.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
    }

    /**
     * Initialize creates the board as buttons using gridlayout, adding actionListener and mouseListener to its button, adding two panels,
     * one top of the board for the number of mines and flags and one on the bottom
     * for the time for the beginning of the game and lives left
     * @param x, width of the board
     * @param y, height of the board
     * @param mines, number of mines
     */
    public void initialize(int x, int y, int mines) {
        this.x = x;
        this.y = y;
        this.mines = mines;

        int i, j;
        logicGrid = new LogicGrid(x, y, this.mines);
        trueMineCount = (this.x * this.y) - logicGrid.getIndex();
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        JPanel grid = new JPanel();
        grid.setLayout(new GridLayout(this.x, this.y));
        JPanel status = new JPanel();
        JPanel status2 = new JPanel();
        status.setLayout(new FlowLayout());
        status2.setLayout(new FlowLayout());

        timeL = new JLabel("Time:");
        liveL = new JLabel("Lives:" + lifeCounter);
        minesL = new JLabel("Mines:" + trueMineCount);
        flagsL = new JLabel("Flags" + flagCounter);
        status.add(timeL);
        status.add(liveL);
        status2.add(minesL);
        status2.add(flagsL);
        button = new JButton[this.x][this.y];
        mines = this.x * this.y - (logicGrid.getIndex());

        for (i = 0; i < this.x; i++) {
            for (j = 0; j < this.y; j++) {
                button[i][j] = new JButton();
                button[i][j].setPreferredSize(new Dimension(20, 20));
                button[i][j].addMouseListener(new MyMouseListener());
                button[i][j].addActionListener(new MyActionListener());
                button[i][j].setIcon(facingDown);
                grid.add(button[i][j]);
            }
        }
        main.add(grid, BorderLayout.CENTER);
        main.add(status, BorderLayout.SOUTH);
        main.add(status2, BorderLayout.NORTH);

        timer = new Timer(1000, e -> {
            secs++;
            seconds++;
            if (seconds == 60) {
                mints++;
                seconds = 0;
            }
            timeL.setText("Time:" + mints + "." + seconds);
        });

        add(main);
    }

    /**
     * Set or unsets a flag depended on if the cell in [row][col] is flagged or no
     * @param row, width of the board
     * @param col, height of the board
     */
    public void setUnsetFlag(int row, int col) {
        // checks if a button of the board is not a flag
        if (!button[row][col].getIcon().equals(logicGrid.flag)) {
            for (int i = 0; i < button.length; i++) {
                for (int j = 0; j < button[0].length; j++) {

                    for (ImageIcon imageIcon : iconTable) {
                        if (button[row][col].getIcon().equals(imageIcon))
                        {
                            return;
                        }
                    }
                }
            }
            button[row][col].setIcon(logicGrid.flag);
            flagCounter++;
            mines--;
            if (!logicGrid.valueOfCell(row, col, logicGrid.getMine())) {
                invalid++;
            }
            validCounterAdd(row, col);
            flagsL.setText("Flags" + flagCounter);
            minesL.setText("Mines:" + mines);
            button[row][col].setForeground(Color.RED);

        } else if (button[row][col].getIcon().equals(logicGrid.flag)) {
            button[row][col].setIcon(facingDown);
            flagCounter--;
            if (!logicGrid.valueOfCell(row, col, logicGrid.getMine())) invalid--;
            if (logicGrid.valueOfCell(row, col, logicGrid.getMine())) validateCounter--;
            mines++;
            flagsL.setText("Flags " + flagCounter);
            minesL.setText("Mines:" + mines);
        }
    }

    /**
     * Gets called when we put a flag on a cell to check if it is a mine underneath it.
     * If it is, it adds one to validateCounter(counter for how
     * many flags are correctly placed on top of bombs). If validateCounter is
     * equal to how many mines are in the board, player wins the game and timer stops
     * @param x, rows of the board
     * @param y, columns of the board
     */
    public void validCounterAdd(int x, int y) {

        if (logicGrid.valueOfCell(x, y, logicGrid.getMine())) {
            validateCounter++;

            if (validateCounter == trueMineCount && invalid == 0) {
                timer.stop();
                winEnd();
            }
        }
    }

    /**
     * Reveals the button in the specific coordinates if it is not a mine
     * @param row, row of the board
     * @param col, col of the board
     */
    public void showNeighbors(int row, int col) {
        if (logicGrid.valueOfCell(row, col, ' ')) {
            emptyCell(row, col);
        } else {
            if (logicGrid.isBorder(row, col) && !logicGrid.valueOfCell(row, col, logicGrid.getMine())) {
                button[row][col].setText(logicGrid.numOfNeighbors(row, col));
                String currentPiece = logicGrid.numOfNeighbors(row, col);
                switch (currentPiece) {
                    case "1" -> {
                        button[row][col].setIcon(iconTable[1]);
                        button[row][col].setText("");
                    }
                    case "2" -> {
                        button[row][col].setIcon(iconTable[2]);
                        button[row][col].setText("");
                    }
                    case "3" -> {
                        button[row][col].setIcon(iconTable[3]);
                        button[row][col].setText("");
                    }
                    case "4" -> {
                        button[row][col].setIcon(iconTable[4]);
                        button[row][col].setText("");
                    }
                    case "5" -> {
                        button[row][col].setIcon(iconTable[5]);
                        button[row][col].setText("");
                    }
                    case "6" -> {
                        button[row][col].setIcon(iconTable[6]);
                        button[row][col].setText("");
                    }
                    case "7" -> {
                        button[row][col].setIcon(iconTable[7]);
                        button[row][col].setText("");
                    }
                    case "8" -> {
                        button[row][col].setIcon(iconTable[8]);
                        button[row][col].setText("");
                    }
                }
            }
        }
    }

    /**
     * When called it reveals all the blank buttons and outer layer of hints
     * @param row, row of the board
     * @param col, col of the board
     */
    public void emptyCell(int row, int col) {
        if (!button[row][col].getBackground().equals(new JButton().getBackground())) {
            button[row][col].setIcon(iconTable[0]);
            button[row][col].setEnabled(false);
            return;
        }
        if (logicGrid.valueOfCell(row, col, ' ')) {
            button[row][col].setBackground(new Color(111, 197, 236));
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    if (logicGrid.valueOfCell(i, j, ' ')) {
                        emptyCell(i, j);
                    } else {
                        showNeighbors(i, j);
                    }
                }
            }
        }
    }

    /**
     * We call this method when player opens all the cells without a mine on
     * them or flags all the mines in the board. When called, an InputDialog
     * that is asks as our name, so it can save it in the file of highScores.
     */
    public void winEnd() {
        JFrame f = new JFrame();
        String name = JOptionPane.showInputDialog(f, "Enter Name");
        if (x == 9 && y == 9) {
            mints += mints + secs;
            String dif = "Easy";
            highScores(dif, name, mints);
        }
        if (x == 16 && y == 16) {
            mints += mints + secs;
            String dif = "Medium";
            highScores(dif, name, mints);
        }
        if (y == 32 && x==16) {
            mints += mints + secs;
            String dif = "Hard";
            highScores(dif, name, mints);
        }
        if (y == 30 && x==25) {
            mints += mints + secs;
            String dif = "Extreme";
            highScores(dif, name, mints);
        }
        System.out.print("YOU ARE THE WINNER");
        gameOver = true;
    }

    /**
     * Saves existing scores to an ArrayList<String>. Add its entry split of spaces, to an array of
     * strings, so we can check the difficulty being the same and if highScore needs to be changed.
     * @param dif, difficulty of the game
     * @param name, name of gamer
     * @param score, score of the game
     */
    public void highScores(String dif, String name, int score) {
        System.out.println(dif + " " + " " + name + " " + score);
        String filepath = "src/main/java/cz.cuni.mff/ganichk.Minesweeper/highScores.txt";
        readFile(filepath, dif, name, score);
    }

    /**
     * Reads file with scores, when game is over
     * @param filepath, path to file with scores
     * @param dif, difficult of the game
     * @param name, name of gamer
     * @param score, score of the game
     */
    private void readFile(String filepath, String dif, String name, int score) {
        try {
            FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);
            ArrayList<String> list = new ArrayList<>();
            System.out.println("READING....");
            int i = 0;
            boolean terminate = true;
            while (terminate) {
                String li = br.readLine();
                if (li != null) {
                    list.add(li);
                    System.out.println(list.get(i));
                    i++;
                } else {
                    list.add(dif + ":" + name + ":" + score);
                    terminate = false;
                }
            }
            writeFile(filepath, list);
            gameOver = true;
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rewrite file with new scores
     * @param filepath, path to the file with scores
     * @param list, content of the file
     */
    private void writeFile(String filepath, ArrayList<String> list) {
        try{
            FileWriter fw = new FileWriter(filepath);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String s : list) {
                System.out.println(s);
                bw.write(s);
                bw.write("\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * MouseListener for flagging the cells
     */
    public class MyMouseListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        /**
         * Called when the right click is pressed
         * @param e the event to be processed
         */
        @Override
        public void mousePressed(MouseEvent e) {
            if (!gameOver) {
                timer.start();
                for (int row = 0; row < button.length; row++) {
                    for (int col = 0; col < button[0].length; col++) {
                        ImageIcon buttonIcon = (ImageIcon) button[row][col].getIcon();
                        if (e.getButton() == 3 && e.getSource() == button[row][col]) {
                            if (!buttonIcon.equals(new ImageIcon((Objects.requireNonNull(getClass().getResource("/flagged.png"))))) || buttonIcon.equals(new ImageIcon((Objects.requireNonNull(getClass().getResource("/flagged.png")))))) {
                                setUnsetFlag(row, col);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    /**
     * ActionListener when the left click is pressed on the button.
     * Whenever left click is pressed, check if game is over.
     */
    public class MyActionListener implements ActionListener{

        /**
         * Whenever left click is pressed, check if game is over.
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameOver) {
                for (int i = 0; i < logicGrid.getHeight(); i++) {
                    for (int j = 0; j < logicGrid.getWidth(); j++) {
                        facingDownCount = 0;
                        facingDownState = 0;
                        //how many cells are facing down and have a mine underneath them,
                        // so we can crosscheck it with a sum of cells opened without mine
                        for (JButton[] buttons : button) {
                            for (int y = 0; y < button[0].length; y++) {
                                if (buttons[y].getIcon().equals(facingDown) && logicGrid.valueOfCell(i, j, logicGrid.getMine())) {
                                    facingDownCount++;
                                }
                            }
                        }
                        //how many cells are still not opened
                        for (JButton[] jButtons : button) {
                            for (int x = 0; x < button[0].length; x++) {
                                if (jButtons[x].getIcon().equals(facingDown)) {
                                    facingDownState++;
                                }
                            }
                        }
                        if (e.getSource() == button[i][j] && !button[i][j].getIcon().equals(logicGrid.flag)) {
                            if (logicGrid.valueOfCell(i, j, logicGrid.getMine())) {
                                button[i][j].setBackground(Color.YELLOW);
                                mines--;
                                minesL.setText("Mines:" + mines);
                                lifeCounter--;
                                button[i][j].setIcon(iconTable[9]);
                                validCounterAdd(i, j);
                                if (lifeCounter > 0) {
                                    liveL.setText("Lives:" + lifeCounter);
                                    gameOver = false;
                                } else {
                                    timer.stop();
                                    lifeCounter = 0;
                                    liveL.setText("Lives:" + lifeCounter);
                                    gameOver = true;
                                    //Reveals all mines in the board
                                    for (int x = 0; x < button.length; x++) {
                                        for (int y = 0; y < button[0].length; y++) {
                                            if (logicGrid.valueOfCell(x, y, logicGrid.mine)) {
                                                if (!button[x][y].getIcon().equals(logicGrid.flag)) {
                                                    button[x][y].setIcon(iconTable[9]);
                                                } else if (logicGrid.valueOfCell(x, y, logicGrid.getMine())
                                                        && button[x][y].getIcon().equals(logicGrid.flag)) {
                                                    button[x][y].setIcon(iconTable[9]);
                                                }
                                            }
                                        }
                                    }
                                    button[i][j].setBackground(Color.RED);
                                }
                            } else {
                                showNeighbors(i, j);
                            }
                        }
                    }
                }
                if (facingDownCount == facingDownState) {
                    timer.stop();
                    winEnd();
                }
            }
        }
    }
}
