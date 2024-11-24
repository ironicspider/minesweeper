package minesweeper;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.Random;

public class App extends PApplet {

    public static final int CELLSIZE = 32;
    public static final int TOPBAR = 64;
    public static final int WIDTH = 864;
    public static final int HEIGHT = 640;
    public static final int BOARD_WIDTH = WIDTH / CELLSIZE;
    public static final int BOARD_HEIGHT = 18;
    public static final int FPS = 60;

    public static Random random = new Random();
    public static int mine_count;

    private tile[][] board = new tile[BOARD_WIDTH][BOARD_HEIGHT];
    private PImage[] images_tm = new PImage[15];
    private boolean won = false;
    private boolean lost = false;
    private boolean gameStarted = false;
    private boolean explosion_lost = false;
    private int gameTime;
    private int startTime;
    private int cascade_counter = 0;
    private int currentExplosionIndex = 0;
    private int explosionCounter = 0;
    private int framesBetweenExplosions = 3;
    private ArrayList<tile> mine_locs = new ArrayList<>();

    public static int[][] mineCountColour = new int[][] {
        {0,0,0}, // 0 is not shown
        {0,0,255}, //1
        {0,133,0}, //2
        {255,0,0}, //3
        {0,0,132}, //4
        {132,0,0}, //5
        {0,132,132}, //6
        {132,0,132}, //7
        {32,32,32} //8
        //colours of the numbers to be displayed
};

    public void images_load() {
        images_tm[3] = loadImage("src/main/resources/minesweeper/tile.png");
        images_tm[1] = loadImage("src/main/resources/minesweeper/tile1.png");
        images_tm[2] = loadImage("src/main/resources/minesweeper/tile2.png");
        images_tm[0] = loadImage("src/main/resources/minesweeper/wall0.png");

        images_tm[4] = loadImage("src/main/resources/minesweeper/mine0.png");
        images_tm[5] = loadImage("src/main/resources/minesweeper/mine1.png");
        images_tm[6] = loadImage("src/main/resources/minesweeper/mine2.png");
        images_tm[7] = loadImage("src/main/resources/minesweeper/mine3.png");
        images_tm[8] = loadImage("src/main/resources/minesweeper/mine4.png");
        images_tm[9] = loadImage("src/main/resources/minesweeper/mine5.png");
        images_tm[10] = loadImage("src/main/resources/minesweeper/mine6.png");
        images_tm[11] = loadImage("src/main/resources/minesweeper/mine7.png");
        images_tm[12] = loadImage("src/main/resources/minesweeper/mine8.png");
        images_tm[13] = loadImage("src/main/resources/minesweeper/mine9.png");

        images_tm[14] = loadImage("src/main/resources/minesweeper/flag.png");
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup() {
        frameRate(FPS);
        colorMode(RGB);

        startTime = millis();
        images_load();

        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                board[i][j] = new tile(i, j);
            }
        }

        mine_locations();
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (!board[i][j].has_mine()) {
                    adjacent_mines(i, j);
                }
          }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!won && !lost) {
            int tileX = mouseX / CELLSIZE;
            int tileY = (mouseY - TOPBAR) / CELLSIZE;

            if (tileX >= 0 && tileX < BOARD_WIDTH && tileY >= 0 && tileY < BOARD_HEIGHT) {
                if (mouseButton == LEFT) {
                    if (!board[tileX][tileY].has_flag() && !board[tileX][tileY].IsClicked()) {
                        board[tileX][tileY].click(board);
                        if (!gameStarted) {
                            gameStarted = true;
                            startTime = millis();
                        }
                        if (board[tileX][tileY].has_mine()) {
                            lost = true;
                            explosion_lost = true;
                            currentExplosionIndex = 0;
                            explosionCounter = 0;
                        } else {
                            win_con();
                        }
                    }
                } else if (mouseButton == RIGHT) {
                    if (!board[tileX][tileY].IsClicked()) {
                        if (!board[tileX][tileY].has_flag()) {
                            board[tileX][tileY].flag();
                        } else {
                            board[tileX][tileY].remove_flag();
                        }
                 }
        }
             }
    }
    }

    @Override
    public void draw() {
        background(255, 255, 255);

        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                board[i][j].setHovered(false);
            }
        }

        int hoverTileX = mouseX / CELLSIZE;
        int hoverTileY = (mouseY - TOPBAR) / CELLSIZE;

        if (hoverTileX >= 0 && hoverTileX < BOARD_WIDTH && hoverTileY >= 0 && hoverTileY < BOARD_HEIGHT) {
            board[hoverTileX][hoverTileY].setHovered(true);
        }

        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                board[i][j].draw(this, i, j, images_tm);
            }
        }

        if (gameStarted && !won && !lost) {
            gameTime = (millis() - startTime) / 1000;
        }

        fill(0, 0, 0);
        textSize(30);
        textAlign(RIGHT, TOP);
        text("Time: " + gameTime + "s", WIDTH - 10, TOPBAR / 2 - 18);

        if (explosion_lost && lost) {
            if (cascade_counter % 3 == 0) {
                explode_cascade_mine();
            }
            cascade_counter++;
        }

        if (lost) {
            fill(0, 0, 0);
            textSize(30);
            textAlign(CENTER, TOP);
            text("You Lost!", WIDTH / 2, TOPBAR / 2 - 17);
        }

        if (won) {
            fill(0, 0, 0);
            textSize(30);
            textAlign(CENTER, TOP);
            text("You Won!", WIDTH / 2, TOPBAR / 2 - 17);
        }
    }

    public void mine_locations() {
        int num_mines = mine_count;
        int control_var = 0;
        while (control_var < num_mines) {
            int r1 = random.nextInt(BOARD_WIDTH);
            int r2 = random.nextInt(BOARD_HEIGHT);
            if (!board[r1][r2].has_mine()) {
                board[r1][r2].add_mine();
                mine_locs.add(board[r1][r2]);
                control_var++;
            }
        }
    }

    public void explode_cascade_mine() {
        if (currentExplosionIndex < mine_locs.size()) {
            mine_locs.get(currentExplosionIndex).set_reveal_status();
            currentExplosionIndex++;
        }
    }

    public void adjacent_mines(int i, int j){
        int tileX = i;
        int tileY = j;
        int counter = 0;
        
            if (!board[tileX][tileY].has_mine()) {
                if (tileX - 1 >= 0 && tileX - 1 < BOARD_WIDTH && tileY - 1 >= 0 && tileY - 1 < BOARD_HEIGHT) {
                    if (board[tileX-1][tileY-1].has_mine()) {
                        counter++; // top left tile
                    }
                }
                if (tileY - 1 >= 0 && tileY - 1 < BOARD_HEIGHT) {
                    if (board[tileX][tileY-1].has_mine()) {
                        counter++; // top tile
                    }
                }
                if (tileX + 1 >= 0 && tileX + 1 < BOARD_WIDTH && tileY - 1 >= 0 && tileY - 1 < BOARD_HEIGHT) {
                    if (board[tileX+1][tileY-1].has_mine()) {
                        counter++; // top right tile
                    }
                }
                if (tileX - 1 >= 0 && tileX - 1 < BOARD_WIDTH) {
                    if (board[tileX-1][tileY].has_mine()) {
                        counter++; // left tile
                    }
                }
                if (tileX + 1 >= 0 && tileX + 1 < BOARD_WIDTH) {
                    if (board[tileX+1][tileY].has_mine()) {
                        counter++; // right tile
                    }
                }
                if (tileX - 1 >= 0 && tileX - 1 < BOARD_WIDTH && tileY + 1 >= 0 && tileY + 1 < BOARD_HEIGHT) {
                    if (board[tileX-1][tileY+1].has_mine()) {
                        counter++; // bottom left tile
                    }
                }
                if (tileY + 1 >= 0 && tileY + 1 < BOARD_HEIGHT) {
                    if (board[tileX][tileY+1].has_mine()) {
                        counter++; // bottom tile
                    }
                }
                if (tileX + 1 >= 0 && tileX + 1 < BOARD_WIDTH && tileY + 1 >= 0 && tileY + 1 < BOARD_HEIGHT) {
                    if (board[tileX+1][tileY+1].has_mine()) {
                        counter++; // bottom right tile
                    }
                }
            }
        
            board[i][j].setAdjacentMines(counter);
        }

    @Override
    public void keyPressed() {
        if ((key == 'r' && lost) || (key == 'r' && won)) {
            restart();
        }
    }
    

    public void restart() {
        won = false;
        lost = false;
        gameStarted = false;
        startTime = 0;
        gameTime = 0;
        mine_locs.clear();
        currentExplosionIndex = 0;
        cascade_counter = 0;

        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                board[i][j] = new tile(i, j);
            }
        }
        mine_locations();
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (!board[i][j].is_mine) {
                    adjacent_mines(i, j);
                }
            }
        }
    }

    public void win_con() {
        int counter = 0;
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (board[i][j].IsClicked() && !board[i][j].has_mine()) {
                    counter++;
                }
            }
        }
        if (counter == ((BOARD_HEIGHT * BOARD_WIDTH) - mine_count)) {
            won = true;
        } else {
            won = false;
        }
    }

    public static void main(String[] args) {
        PApplet.main("minesweeper.App", args);

        if (args.length > 0) {
            try {
                mine_count = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                mine_count = 100;
            }
        } else {
            mine_count = 100;
        }
    }
}

    
