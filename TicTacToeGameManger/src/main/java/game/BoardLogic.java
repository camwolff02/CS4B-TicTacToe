package game;

public class BoardLogic {

    public static final int BOARD_SIZE = 3;

    private String[][] cells;
    private int numMoves;

    public BoardLogic() {
        cells = new String[BOARD_SIZE][BOARD_SIZE];
        reset();
    }

    public void reset() {
        numMoves = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                cells[i][j] = "-";
            }
        }
    }

    public boolean makeMove(String playerId, int row, int col) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return false;
        }
        if (!cells[row][col].equals("-")) {
            return false;
        }
        cells[row][col] = playerId;
        numMoves++;
        return true;
    }

    public boolean isGameOver() {
        return numMoves == BOARD_SIZE * BOARD_SIZE || getWinner() != null;
    }

    public String getWinner() {
        // check rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            String[] row = cells[i];
            if (row[0].equals(row[1]) && row[1].equals(row[2]) && !row[0].equals("-")) {
                return row[0];
            }
        }

        // check columns
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (cells[0][j].equals(cells[1][j]) && cells[1][j].equals(cells[2][j]) && !cells[0][j].equals("-")) {
                return cells[0][j];
            }
        }

        // check diagonals
        if (cells[0][0].equals(cells[1][1]) && cells[1][1].equals(cells[2][2]) && !cells[0][0].equals("-")) {
            return cells[0][0];
        }
        if (cells[0][2].equals(cells[1][1]) && cells[1][1].equals(cells[2][0]) && !cells[0][2].equals("-")) {
            return cells[0][2];
        }

        return null;
    }

    public String[][] getCells() {
        return cells;
    }

}