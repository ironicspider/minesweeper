package minesweeper;

import processing.core.PApplet;
import processing.core.PImage;

class tile {
    public int x;
    public int y;
    public boolean is_clicked = false;
    public boolean is_mine = false;
    public boolean is_flag = false;
    public boolean is_hovered = false;
    public int adjacent_mines;
    public int mine_frame = 4;
    public boolean is_revealed = false;

    public tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(PApplet app, int x, int y, PImage[] images_tm) { 

        int x_val = x * App.CELLSIZE;
        int y_val = y * App.CELLSIZE;
    
        if (this.IsClicked() == false) {
            app.image(images_tm[1], x_val, y_val + 64);
        } else if (this.IsClicked() == true) {
            this.is_hovered = false;
            if (this.is_mine == false) {
                this.is_revealed = true;
                app.image(images_tm[3], x_val, y_val + 64);
                if (this.adjacent_mines > 0) {
                    if (this.adjacent_mines == 1) {
                        app.fill(0, 0, 255);
                    } else if (this.adjacent_mines == 2) {
                        app.fill(0, 133, 0);
                    } else if (this.adjacent_mines == 3) {
                        app.fill(255, 0, 0);
                    } else if (this.adjacent_mines == 4) {
                        app.fill(0, 0, 132);
                    } else if (this.adjacent_mines == 5) {
                        app.fill(132, 0, 0);
                    } else if (this.adjacent_mines == 6) {
                        app.fill(0, 132, 132);
                    } else if (this.adjacent_mines == 7) {
                        app.fill(132, 0, 132);
                    } else if (this.adjacent_mines == 8) {
                        app.fill(32, 32, 32);
                    }
                    app.textSize(24);
                    app.textAlign(PApplet.CENTER, PApplet.CENTER);
                    app.text(this.adjacent_mines, x_val + 16, y_val + 77);
                }
            } else if (this.is_mine == true) {
                if (mine_frame < 13) {
                    app.image(images_tm[mine_frame], x_val, y_val + 64);
                    mine_frame++;
                } else {
                    app.image(images_tm[13], x_val, y_val + 64);
                }
            }
        }
    
        if (this.has_flag()) {
            this.is_hovered = false;
            app.image(images_tm[14], x_val, y_val + 64);
        }
    
        if (this.is_hovered == true) {
            app.image(images_tm[2], x_val, y_val + 64);
        }
    }

    public void set_reveal_status() {
        this.is_clicked = true;
        this.is_revealed = true;
    }

    public void click(tile[][] board) {
        if (this.is_flag || this.is_clicked) return;

        this.is_clicked = true;
        if (this.adjacent_mines == 0) {
            revealBlankTiles(board);
        }
    }

    public void revealBlankTiles(tile[][] board) {
        if (this.adjacent_mines > 0 || this.is_mine) return;
    
        this.is_clicked = true;
    
        int tileX = this.x;
        int tileY = this.y;
    

        if (tileX - 1 >= 0 && tileY - 1 >= 0) {
            tile neighbor = board[tileX - 1][tileY - 1]; // Top-left tile
            if (!neighbor.is_clicked && !neighbor.has_mine()) {
                neighbor.click(board);
            }
        }
    
        
        if (tileY - 1 >= 0) {
            tile neighbor = board[tileX][tileY - 1];  // Top tile
            if (!neighbor.is_clicked && !neighbor.has_mine()) {
                neighbor.click(board);
            }
        }
    
        
        if (tileX + 1 < App.BOARD_WIDTH && tileY - 1 >= 0) {
            tile neighbor = board[tileX + 1][tileY - 1];  // Top-right tile
            if (!neighbor.is_clicked && !neighbor.has_mine()) {
                neighbor.click(board);
            }
        }
    
        
        if (tileX - 1 >= 0) {
            tile neighbor = board[tileX - 1][tileY];  // Left tile
            if (!neighbor.is_clicked && !neighbor.has_mine()) {
                neighbor.click(board);
            }
        }
    
       
        if (tileX + 1 < App.BOARD_WIDTH) {
            tile neighbor = board[tileX + 1][tileY];   // Right tile
            if (!neighbor.is_clicked && !neighbor.has_mine()) {
                neighbor.click(board);
            }
        }
    
       
        if (tileX - 1 >= 0 && tileY + 1 < App.BOARD_HEIGHT) {
            tile neighbor = board[tileX - 1][tileY + 1];   // Bottom-left tile
            if (!neighbor.is_clicked && !neighbor.has_mine()) {
                neighbor.click(board);
            }
        }
    
       
        if (tileY + 1 < App.BOARD_HEIGHT) {
            tile neighbor = board[tileX][tileY + 1];   // Bottom tile
            if (!neighbor.is_clicked && !neighbor.has_mine()) {
                neighbor.click(board);
            }
        }
    
       
        if (tileX + 1 < App.BOARD_WIDTH && tileY + 1 < App.BOARD_HEIGHT) {
            tile neighbor = board[tileX + 1][tileY + 1];   // Bottom-right tile
            if (!neighbor.is_clicked && !neighbor.has_mine()) {
                neighbor.click(board);
            }
        }
    }
    

    public void setHovered(boolean hovered) {
        this.is_hovered = hovered;
    }

    public void setAdjacentMines(int adjacent_mines) {
        this.adjacent_mines = adjacent_mines;
    }

    public boolean IsClicked() {
        return is_clicked;
    }

    public void flag() {
        this.is_flag = true;
    }

    public void remove_flag() {
        this.is_flag = false;
    }

    public boolean has_flag() {
        return is_flag;
    }

    public void add_mine() {
        this.is_mine = true;
    }

    public boolean has_mine() {
        return is_mine;
    }
}
