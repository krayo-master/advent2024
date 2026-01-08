package org.example.day6;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Day6Test {

    @Test
    void testAoCExamplePart1() {
        List<String> board = List.of(
                "....#.....",
                ".........#",
                "..........",
                "..#.......",
                ".......#..",
                "..........",
                ".#..^.....",
                "........#.",
                "#.........",
                "......#..."
        );

        long visited = Day6.countDistinctPositions(board);
        assertEquals(41, visited, "Part 1: AoC example visited positions");
    }

    @Test
    void testAoCExamplePart2() {
        List<String> board = List.of(
                "....#.....",
                ".........#",
                "..........",
                "..#.......",
                ".......#..",
                "..........",
                ".#..^.....",
                "........#.",
                "#.........",
                "......#..."
        );

        long loops = Day6.countAllPossibleLoops(board);
        assertEquals(6, loops, "Part 2: AoC example should have 6 possible loops");
    }

    @Test
    void testSmallBoardPart2NoLoop() {
        List<String> board = List.of(
                "....",
                ".>..",
                ".#..",
                "...."
        );

        long loops = Day6.countAllPossibleLoops(board);
        assertEquals(0, loops, "Part 2: Small board has no possible loops");
    }

    @Test
    void testMissingArrowThrowsException() {
        List<String> board = List.of(
                "....",
                ".#..",
                "....",
                "...."
        );

        assertThrows(IllegalStateException.class, () -> Day6.countDistinctPositions(board),
                "Should throw IllegalStateException if arrow is missing");
    }

    @Test
    void testInvalidDirectionThrowsException() {
        List<String> board = List.of(
                "....",
                ".Z..", // Z is invalid
                ".#..",
                "...."
        );

        assertThrows(IllegalStateException.class, () -> Day6.countDistinctPositions(board),
                "Should throw IllegalStateException if guard has invalid direction");
    }
}
