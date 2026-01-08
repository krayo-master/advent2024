package org.example.day6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6 {

    /**
     * Main entry point. Loads the board from day6input.txt and prints:
     * 1. Guard's visited positions (Part 1)
     * 2. Number of obstruction positions causing loops (Part 2)
     */
    public static void main(String[] args) throws IOException {
        List<String> input;
        try (InputStream is = Day6.class.getClassLoader().getResourceAsStream("day6input.txt")) {

            if (is == null) {
                throw new IllegalStateException("day6input.txt not found");
            }

            input = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .toList();
        }

        System.out.println("The Guard will visit " + countDistinctPositions(input) + " positions.");
        System.out.println("The Guard can get stuck in " + countAllPossibleLoops(input) + " loops.");
    }

    /**
     * Counts board positions where placing a new obstruction ('O')
     * would make the guard get stuck in a loop (excluding walls and starting position).
     *
     * @param input board as list of strings
     * @return number of looping obstruction positions
     */
    public static long countAllPossibleLoops(List<String> input) {
        Board boardData = buildBoard(input);
        char[][] board = boardData.boardArray();
        State start = boardData.arrowState();

        int rows = board.length;
        int cols = board[0].length;
        long loopPositions = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == '#' || (i == start.row() && j == start.col())) {
                    continue;
                }

                board[i][j] = 'O'; // add obstruction

                if (simulateWalk(board, start, true)) {
                    loopPositions++;
                }

                board[i][j] = '.'; // restore
            }
        }

        return loopPositions;
    }

    /**
     * Counts distinct positions the guard visits, marking each with 'X'.
     *
     * @param input board as list of strings
     * @return number of visited positions
     */
    public static long countDistinctPositions(List<String> input) {
        Board boardData = buildBoard(input);
        char[][] board = boardData.boardArray();

        simulateWalk(board, boardData.arrowState(), false);

        // count marked positions
        long count = 0;
        for (char[] row : board) {
            for (char c : row) {
                if (c == 'X') {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Simulates the guard's walk on the board.
     *
     * @param board      2D board array
     * @param start      guard's starting state
     * @param detectLoop if true, returns true when a loop is detected instead of marking positions
     * @return true if a loop is detected and detectLoop is true
     */
    static boolean simulateWalk(char[][] board, State start, boolean detectLoop) {
        int rows = board.length;
        int cols = board[0].length;

        int row = start.row();
        int col = start.col();
        char direction = start.direction();

        Set<State> visited = detectLoop ? new HashSet<>() : null;

        while (true) {
            int nextRow = row;
            int nextCol = col;

            switch (direction) {
                case '>' -> nextCol++;
                case '<' -> nextCol--;
                case '^' -> nextRow--;
                case 'v' -> nextRow++;
            }

            // leaving board → end simulation
            if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols) {
                return false; // no loop
            }

            // obstacle → turn right
            if (board[nextRow][nextCol] == '#' || (detectLoop && board[nextRow][nextCol] == 'O')) {
                direction = switch (direction) {
                    case '>' -> 'v';
                    case 'v' -> '<';
                    case '<' -> '^';
                    case '^' -> '>';
                    default -> throw new IllegalStateException("Unexpected direction: " + direction);
                };
                continue;
            }

            // move
            row = nextRow;
            col = nextCol;

            State current = new State(row, col, direction);

            if (detectLoop) {
                if (!visited.add(current)) {
                    return true; // loop detected
                }
            } else {
                board[row][col] = 'X'; // mark visited
            }
        }
    }

    /**
     * Builds a 2D board array and finds the guard's starting state.
     *
     * @param input board as list of strings
     * @return {@link Board} object with array and starting State
     */
    static Board buildBoard(List<String> input) {
        int rows = input.size();
        int cols = input.getFirst().length();

        char[][] board = new char[rows][cols];
        State arrow = null;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char c = input.get(i).charAt(j);
                board[i][j] = c;

                // Assumes exactly one guard arrow in input
                if (c == '^' || c == '>' || c == 'v' || c == '<') {
                    arrow = new State(i, j, c);
                }
            }
        }

        if (arrow == null) {
            throw new IllegalStateException("Arrow not found");
        }

        return new Board(board, arrow);
    }
}
