package org.example.day14;

record Robot(int x, int y, int vx, int vy) {
    Robot move(int width, int height, int seconds) {
        int newX = Math.floorMod(x + vx * seconds, width);
        int newY = Math.floorMod(y + vy * seconds, height);
        return new Robot(newX, newY, vx, vy);
    }

    String getCurrentPosition() {
        return x + "," + y;
    }
}
