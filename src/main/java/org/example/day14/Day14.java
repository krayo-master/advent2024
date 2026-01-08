package org.example.day14;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day14 {

    public static void main(String[] args) throws Exception {
        try (InputStream is = Day14.class.getClassLoader().getResourceAsStream("day14input.txt")) {
            if (is == null) throw new IllegalStateException("day14input.txt not found");

            List<Robot> robots = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .map(line -> {
                        String[] parts = line.split(" ");
                        String[] p = parts[0].substring(2).split(",");
                        String[] v = parts[1].substring(2).split(",");
                        return new Robot(
                                Integer.parseInt(p[0]),
                                Integer.parseInt(p[1]),
                                Integer.parseInt(v[0]),
                                Integer.parseInt(v[1])
                        );
                    }).toList();

            int WIDTH = 101;
            int HEIGHT = 103;
            int SECONDS = 100;

            // Part 1
            List<Robot> moved = moveAll(robots, WIDTH, HEIGHT, SECONDS);
            int safetyFactor = calculateSafetyFactor(moved, WIDTH, HEIGHT);
            System.out.println("Safety factor after " + SECONDS + " seconds: " + safetyFactor);

            // Part 2
            int treeSecond = findChristmasTree(robots, WIDTH, HEIGHT, 10000);
            System.out.println("Christmas tree appears at second " + treeSecond);
        }
    }

    static int calculateSafetyFactor(List<Robot> robots, int width, int height) {
        int midX = width / 2;
        int midY = height / 2;

        int q1 = 0, q2 = 0, q3 = 0, q4 = 0;
        for (Robot r : robots) {
            if (r.x() == midX || r.y() == midY) {
                continue;
            }
            if (r.x() > midX && r.y() > midY) {
                q1++;
            } else if (r.x() < midX && r.y() > midY) {
                q2++;
            } else if (r.x() < midX) {
                q3++;
            } else q4++;
        }
        System.out.printf("Quadrant counts: Q1=%d Q2=%d Q3=%d Q4=%d%n", q1, q2, q3, q4);
        return q1 * q2 * q3 * q4;
    }

    static List<Robot> moveAll(List<Robot> robots, int width, int height, int seconds) {
        return robots.stream().map(r -> r.move(width, height, seconds)).toList();
    }

    static int findChristmasTree(List<Robot> robots, int width, int height, int maxSeconds) {
        List<Robot> current = robots;
        for (int t = 1; t <= maxSeconds; t++) {
            current = moveAll(current, width, height, 1);

            // Build a set of positions
            Set<String> positions = new HashSet<>();
            for (Robot r : current) {
                positions.add(r.getCurrentPosition());
            }

            if (isChristmasTreePattern(positions, width, height)) {
                printRobots(positions, width, height);
                return t;
            }
        }
        return -1;
    }

    static boolean isChristmasTreePattern(Set<String> positions, int width, int height) {
        boolean[][] board = new boolean[height][width];
        for (String pos : positions) {
            String[] split = pos.split(",");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            board[y][x] = true;
        }

        int density = 0;
        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {
                if (!board[y][x]) {
                    continue;
                }
                if ((y > 0 && board[y - 1][x]) || (y < height - 1 && board[y + 1][x]) ||
                        (x > 0 && board[y][x - 1]) || (x < width - 1 && board[y][x + 1]) ||
                        (x < width - 1 && y < height - 1 && board[y + 1][x + 1]) ||
                        (x > 0 && y < height - 1 && board[y + 1][x - 1]) ||
                        (x < width - 1 && y > 0 && board[y - 1][x + 1]) ||
                        (x > 0 && y > 0 && board[y - 1][x - 1])) {
                    density++;
                }
            }
        }

        return density >= 0.6 * positions.size();
    }

    static void printRobots(Set<String> positions, int width, int height) {
        char[][] grid = new char[height][width];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                grid[y][x] = ' ';

        for (String pos : positions) {
            String[] split = pos.split(",");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            grid[y][x] = 'X';
        }

        for (int y = 0; y < height; y++) {
            System.out.println(new String(grid[y]));
        }
    }
}
