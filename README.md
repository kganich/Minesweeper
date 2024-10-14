# Minesweeper Game

## Overview
This is a classic Minesweeper game implemented in Java with four difficulty levels. The goal is to uncover all non-mine tiles on the game board while avoiding bombs, using logical reasoning and strategy to flag and reveal tiles.

### Game Features
- **Difficulty Levels:**
  - Easy: 9x9 grid, 6 mines
  - Medium: 16x16 grid, 20 mines
  - Hard: 16x32 grid, 40 mines
  - Extreme: 25x30 grid, 100 mines
- **Lives:** Players have 3 lives. If a mine is uncovered, 1 life is lost. The game ends when all lives are gone.
- **Unlimited Time:** There is no time limit.
- **Scoring System:** Tracks and displays the top 5 scores for each difficulty level.
- **Game Menu:** Allows players to switch difficulties or end the game via the "Game" button.

## User Guide
1. **Starting the Game:**
   - Choose a difficulty level (Easy, Medium, Hard, Extreme) from the start menu. The corresponding game board will appear.
   
2. **Gameplay:**
   - **Left-click** to reveal the content of a cell. If the cell has no surrounding bombs, adjacent cells will automatically be uncovered.
   - **Right-click** to flag a cell if you suspect it contains a mine.
   - Win by flagging all mines and uncovering all non-mine cells. You lose if all 3 lives are depleted after uncovering mines.

3. **Lives:**
   - You have 3 lives. Each time you uncover a bomb, 1 life is lost. The game continues as long as you have lives remaining.
   - Up to two bombs can be uncovered without immediately losing.

4. **Scores:**
   - The "Scores" button shows the top 5 high scores for each difficulty level.

5. **Game Menu:**
   - Use the "Game" button to switch difficulty or start a new game.
   - Press "Close" to end the current game.

## Developer Section

### Code Structure
- The game logic is implemented using object-oriented programming (OOP) principles, with separate classes for managing game state, user interface (UI), and game logic.
- Java Swing is used for the graphical interface, enabling mouse-based interaction for gameplay.

### Javadoc Generation
The project's Javadoc documentation can be generated directly from the source code. To generate the Javadoc:
1. Open the terminal and navigate to the root directory of the project.
2. Run the following command:
   ```bash
   javadoc -d doc -sourcepath src -subpackages your.package.name
   ```
   This command will generate the HTML documentation for all classes and methods.

### Possible Enhancements
- **Timer:** Introduce a timer feature to challenge players to complete the game within a specific time frame.
- **Custom Difficulty:** Allow players to configure custom grid sizes and mine counts.
- **Hint System:** Implement a hint feature for players who need assistance.

## Conclusion
This implementation of Minesweeper combines classic mechanics with new features like lives, difficulty options, and score tracking. The code is designed to be extendable, allowing developers to add new functionality and enhance the gameplay experience.
