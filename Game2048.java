package com.codegym.games.game2048;

import java.util.Arrays;
import com.codegym.engine.cell.*;
import com.codegym.engine.cell.Game;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score = 0;

    @Override
    public void initialize() {
        setScreenSize(4, 4);
        createGame();
        drawScene();
    }

    private void createGame() {
        gameField = new int[4][4];
        createNewNumber();
        createNewNumber();
    }

    private void drawScene() {
        for (int i = 0; i < SIDE; i++) { 
            for (int y = 0; y < SIDE; y++) {
                
                setCellColoredNumber(y, i, gameField[i][y]);
            }
        }
       
    }

    private void createNewNumber() {
        int maxTileValue = getMaxTileValue();
        if (maxTileValue == 2048) {
            win();
        }

        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);
        if (gameField[y][x] != 0) {
            createNewNumber();
        } else {
            int i = getRandomNumber(10);
            int chance;
            if (i == 9) {
                chance = 4;
            } else {
                chance = 2;
                gameField[y][x] = chance;
            }
        }
    }

    private Color getColorByValue(int value) {
        
        if (value == 0) {
            return Color.WHITE;
        } else if (value == 2) {
            return Color.BLUE;
        } else if (value == 4) {
            return Color.RED;
        } else if (value == 8) {
            return Color.GREEN;
        } else if (value == 16) {
            return Color.CYAN;
        } else if (value == 32) {
            return Color.ORANGE;
        } else if (value == 64) {
            return Color.GRAY;
        } else if (value == 128) {
            return Color.PURPLE;
        } else if (value == 256) {
            return Color.BLACK;
        } else if (value == 512) {
            return Color.PINK;
        } else if (value == 1024) {
            return Color.YELLOW;
        } else if (value == 2048) {
            return Color.AQUA;
        } else
            return Color.WHITE;
    }

    private void setCellColoredNumber(int x, int y, int value) {
       
        if (value != 0) {
            setCellValueEx(x, y, getColorByValue(value), Integer.toString(value));
        } else {
            setCellValueEx(x, y, getColorByValue(value), "");
        }
    }

    
    private boolean compressRow(int[] row) {
        int temp = 0;
        int[] rowTemp = row.clone(); 
        boolean hasMoved = false;
        for (int i = 0; i < row.length; i++) {
            for (int j = 0; j < row.length - i - 1; j++) {
                if (row[j] == 0) {
                    temp = row[j];
                    row[j] = row[j + 1];
                    row[j + 1] = temp;
                }
            }
        }
        
        if (!Arrays.equals(row, rowTemp))
            hasMoved = true;
        return hasMoved;
    }

    private boolean mergeRow(int[] row) {
        boolean hasMoved = false;
        for (int i = 0; i < row.length - 1; i++) {
            
            if (row[i] == row[i + 1] && row[i] != 0) {
                
                score += (row[i] + row[i + 1]);
                setScore(score);
                row[i] = 2 * row[i];
                row[i + 1] = 0;
                hasMoved = true;
            }
        }
        return hasMoved;
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();

    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveLeft() {
        int move = 0;
        for (int i = 0; i < SIDE; i++) {
            boolean compressed = compressRow(gameField[i]); 
            boolean merged = mergeRow(gameField[i]); 
            boolean compresses = compressRow(gameField[i]); 
            
            if (compressed || merged || compresses)
                move++;
        }
       
        if (move != 0) {
            createNewNumber();
        }
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void rotateClockwise() {
        for (int i = 0; i < SIDE / 2; i++) {
            for (int j = i; j < SIDE - i - 1; j++) {
                
                int temp = gameField[i][j];
                gameField[i][j] = gameField[SIDE - 1 - j][i];
                gameField[SIDE - 1 - j][i] = gameField[SIDE - 1 - i][SIDE - 1 - j];
                gameField[SIDE - 1 - i][SIDE - 1 - j] = gameField[j][SIDE - 1 - i];
                gameField[j][SIDE - 1 - i] = temp;

            }
        }
    }

    @Override
    public void onKeyPress(Key key) {
        if (isGameStopped) {
            if (key == Key.SPACE) {
                isGameStopped = false;
                score = 0;
                setScore(score);
                createGame();
                drawScene();
            }
        } else if (canUserMove()) {
            if (key == Key.LEFT) {
                moveLeft();
            } else if (key == Key.RIGHT) {
                moveRight();
            } else if (key == Key.UP) {
                moveUp();
            } else if (key == Key.DOWN) {
                moveDown();
                
            } 
            drawScene();
        } else {
            gameOver();
        }
    }

    private int getMaxTileValue() {
        int max = 0;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] > max) {
                    max = gameField[i][j];
                }
            }
        }
        return max;
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "You Won", Color.GREEN, 50);
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "You Lost", Color.RED, 50);
    }

    private boolean canUserMove() {
        
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] == 0) {
                    return true;
                }
            }
        }

        for (int i = 0; i < SIDE - 1; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] == gameField[i + 1][j]) {
                    return true;
                }
            }
        }

        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE - 1; j++) {
                if (gameField[i][j] == gameField[i][j + 1]) {
                    return true;
                }
            }
        }
        return false;
    }
}