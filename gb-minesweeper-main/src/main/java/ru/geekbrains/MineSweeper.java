package ru.geekbrains;

import java.util.Random;
import java.util.Scanner;

public class MineSweeper {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    private static final int MINES = 25;

    private static final int MINE = 1000;
    private static final int EMPTY = 0;
    private static final int CELL_OPEN = 1;
    private static final int CELL_CLOSE = 0;
    private static final int CELL_FLAG = -1;

    public static void main(String[] args) {
        final boolean isWin = play();
        if (isWin) {
            System.out.println("Поздравляю!\nВы выиграли!");
        } else {
            System.out.println("БАБАХ!!!\nВы проиграли!");
        }
    }


    public static boolean play() {
        int[][] board = generateBoard();
        int[][] moves = new int[HEIGHT][WIDTH];
        boolean isPassMove;
        boolean win;
        do {
            isPassMove = move(board, moves);
            win = isWin(moves);
        } while (isPassMove && !win);

        return isPassMove;
    }

    private static boolean isWin(final int[][] moves) {
        int openCell = 0;
        for (int[] lines : moves) {
            for (int cell : lines) {
                if (cell == CELL_OPEN) {
                    openCell++;
                }
            }
        }
        return openCell + MINES == HEIGHT * WIDTH;
    }

    private static boolean move(final int[][] board, final int[][] moves) {
        final Scanner scanner = new Scanner(System.in);
        printBoard(board, moves);
        int row, line;
        boolean flag = false;
        while (true) {
            System.out.print("Ваш ход (строка, столбец, флаг, например А1*): ");
            final String move = scanner.nextLine().toUpperCase();
            if (move.length() == 2 || move.length() == 3) {
                row = move.charAt(0) - 'A';
                line = move.charAt(1) - '0';
                if (move.length() > 2) {
                    flag = move.charAt(2) == '*';
                }
                if (row < WIDTH && row >= 0 && line < HEIGHT && line >= 0 && moves[line][row] == CELL_CLOSE) {
                    break;
                }
                System.out.println(row + " " + line);
            }
            System.out.println("Неправильный ввод");
        }

        if (flag) {
            moves[line][row] = CELL_FLAG;
            return true;
        }
        if (!isMine(board[line][row])) {
            moves[line][row] = CELL_OPEN;
            return true;
        }
        return false;
    }

    private static void printBoard(final int[][] board, final int[][] moves) {
        System.out.print("   ");
        for (char i = 'A'; i < 'A' + WIDTH; i++) {
            System.out.print(" " + i);
        }
        System.out.println();
        for (int i = 0; i < HEIGHT; i++) {
            System.out.printf("%3d", i);
            for (int j = 0; j < WIDTH; j++) {
                if (moves[i][j] == CELL_CLOSE) {
                    System.out.print("[]");
                    continue;
                }
                if (moves[i][j] == CELL_FLAG) {
                    System.out.print(" +");
                    continue;
                }
                String cellColor = getColorCode(board[i][j]);
                System.out.print(cellColor);
                if (board[i][j] == EMPTY) {
                    System.out.print(" .");
                } else if (isMine(board[i][j])) {
                    System.out.print(" *");
                } else {
                    System.out.printf("%2d", board[i][j]);
                }
                System.out.print(ANSI_RESET);
            }
            System.out.println();
        }
    }

    private static String getColorCode(final int i) {
        switch (i) {
            case EMPTY:
                return ANSI_WHITE;
            case MINE:
                return ANSI_PURPLE;
            case 1:
                return ANSI_BLUE;
            case 2:
                return ANSI_GREEN;
            case 3:
                return ANSI_RED;
            case 4:
                return ANSI_CYAN;
            case 5:
                return ANSI_YELLOW;
        }
        return null;
    }

    private static int[][] generateBoard() {
        final int[][] board = fillMines();
        calculateMines(board);
        return board;
    }

    private static int[][] fillMines() {
        int[][] board = new int[HEIGHT][WIDTH];
        int mines = MINES;
        final Random random = new Random();
        while (mines > 0) {
            int x, y;
            do {
                x = random.nextInt(HEIGHT);
                y = random.nextInt(WIDTH);
            } while (isMine(board[x][y]));
            board[x][y] = MINE;
            mines--;
        }
        return board;
    }

    private static void calculateMines(int[][] board) {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (isMine(board[i][j])) {
                    continue;
                }
                board[i][j] = getMinesCountAroundCell(board, i, j);
            }
        }
    }

    private static boolean isMine(int i) {
        return i == MINE;
    }

    private static int getMinesCountAroundCell(int[][] board, int line, int row) {
        int mCount = 0;
        for (int i = line - 1; i <= line + 1; i++) {
            for (int j = row - 1; j <= row + 1; j++) {
                if (i < 0 || i >= HEIGHT || j < 0 || j >= WIDTH) {
                    continue;
                }
                if (isMine(board[i][j])) {
                    mCount++;
                }
            }
        }
        return mCount;
    }
}
