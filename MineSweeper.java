import java.util.Scanner;

public class MineSweeper {
    private int[][] board;
    private boolean[][] revealed;
    private int rows;
    private int cols;
    private int numMines;
    private boolean gameOver;

    public MineSweeper(int rows, int cols, int numMines) {
        this.rows = rows;
        this.cols = cols;
        this.numMines = numMines;
        this.board = new int[rows][cols];
        this.revealed = new boolean[rows][cols];
        this.gameOver = false;
    }

    public void initialize() {
        placeMines();
        calculateNumbers();
    }

    private void placeMines() {
        int count = 0;
        while (count < numMines) {
            int row = (int) (Math.random() * rows);
            int col = (int) (Math.random() * cols);
            if (board[row][col] != -1) {
                board[row][col] = -1;
                count++;
            }
        }
    }

    private void calculateNumbers() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == -1) {
                    continue;
                }
                int count = 0;
                if (i > 0 && j > 0 && board[i - 1][j - 1] == -1) count++;
                if (i > 0 && board[i - 1][j] == -1) count++;
                if (i > 0 && j < cols - 1 && board[i - 1][j + 1] == -1) count++;
                if (j > 0 && board[i][j - 1] == -1) count++;
                if (j < cols - 1 && board[i][j + 1] == -1) count++;
                if (i < rows - 1 && j > 0 && board[i + 1][j - 1] == -1) count++;
                if (i < rows - 1 && board[i + 1][j] == -1) count++;
                if (i < rows - 1 && j < cols - 1 && board[i + 1][j + 1] == -1) count++;
                board[i][j] = count;
            }
        }
    }

    public void playGame() {
        Scanner scanner = new Scanner(System.in);

        while (!gameOver) {
            printBoard();
            System.out.print("Select a row (0-" + (rows - 1) + "): ");
            int row = scanner.nextInt();
            System.out.print("Select a column (0-" + (cols - 1) + "): ");
            int col = scanner.nextInt();

            if (isValidMove(row, col)) {
                if (board[row][col] == -1) {
                    gameOver = true;
                    System.out.println("Game Over! You stepped on a mine.");
                } else {
                    revealCell(row, col);
                    if (isGameWon()) {
                        gameOver = true;
                        System.out.println("Congratulations! You won the game.");
                    }
                }
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }

        scanner.close();
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols && !revealed[row][col];
    }

    private void revealCell(int row, int col) {
        if (!revealed[row][col]) {
            revealed[row][col] = true;
            if (board[row][col] == 0) {
                revealNeighbors(row, col);
            }
        }
    }

    private void revealNeighbors(int row, int col) {
        if (row > 0) {
            revealCell(row - 1, col); // above
            if (col > 0) revealCell(row - 1, col - 1); // above left
            if (col < cols - 1) revealCell(row - 1, col + 1); // above right
        }
        if (row < rows - 1) {
            revealCell(row + 1, col); // below
            if (col > 0) revealCell(row + 1, col - 1); // below left
            if (col < cols - 1) revealCell(row + 1, col + 1); // below right
        }
        if (col > 0) revealCell(row, col - 1); // left
        if (col < cols - 1) revealCell(row, col + 1); // right
    }

    private boolean isGameWon() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!revealed[i][j] && board[i][j] != -1) {
                    return false;
                }
            }
        }
        return true;
    }

    private void printBoard() {
        System.out.println();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (revealed[i][j]) {
                    if (board[i][j] == -1) {
                        System.out.print("X ");
                    } else {
                        System.out.print(board[i][j] + " ");
                    }
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of rows: ");
        int rows = scanner.nextInt();
        System.out.print("Enter the number of columns: ");
        int cols = scanner.nextInt();
        int numMines = (rows * cols) / 4;

        MineSweeper game = new MineSweeper(rows, cols, numMines);
        game.initialize();
        game.playGame();

        scanner.close();
    }
}
