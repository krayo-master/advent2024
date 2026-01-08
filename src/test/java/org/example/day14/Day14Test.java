package org.example.day14;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day14Test {

    @Test
    void testExamplePart1() {
        List<Robot> robots = List.of(
                new Robot(0, 4, 3, -3),
                new Robot(6, 3, -1, -3),
                new Robot(10, 3, -1, 2),
                new Robot(2, 0, 2, -1),
                new Robot(0, 0, 1, 3),
                new Robot(3, 0, -2, -2),
                new Robot(7, 6, -1, -3),
                new Robot(3, 0, -1, -2),
                new Robot(9, 3, 2, 3),
                new Robot(7, 3, -1, 2),
                new Robot(2, 4, 2, -3),
                new Robot(9, 5, -3, -3)
        );

        int width = 11;
        int height = 7;
        int seconds = 100;

        List<Robot> moved = Day14.moveAll(robots, width, height, seconds);
        int safetyFactor = Day14.calculateSafetyFactor(moved, width, height);

        assertEquals(12, safetyFactor, "Safety factor on this AoC example board after 100 seconds");
    }

    @Test
    void testEdgeCaseRobots() {
        List<Robot> robots = List.of(
                new Robot(0, 0, 1, 0),   // moves right
                new Robot(4, 0, 0, 0),   // stationary
                new Robot(0, 4, -1, -1)  // moves left/up
        );

        int width = 5;
        int height = 5;
        int seconds = 1;

        List<Robot> moved = Day14.moveAll(robots, width, height, seconds);
        int safetyFactor = Day14.calculateSafetyFactor(moved, width, height);

        assertEquals(0, safetyFactor, "Safety factor on edge-case small board");
    }
}

