# Minesweeper Game

A Java-based implementation of the classic Minesweeper game, developed using the Processing library and Gradle for dependency management. This project is a university assignment for **INFO1113 / COMP9003**.

## Table of Contents
- [Description](#description)
- [Gameplay](#gameplay)
- [Features](#features)
- [Setup](#setup)
- [Controls](#controls)

## Description

This project recreates the classic Minesweeper game, featuring a customizable board size and random mine placement. Players must uncover tiles, flag potential mines, and avoid triggering explosions. The game ends with either a victory for revealing all non-mine tiles or a loss when a mine is clicked.

## Gameplay

### Board
- **Grid Size**: 18x27 tiles.
- **Mines**: Default 100 mines, configurable via command-line arguments.

### Rules
- Left-click a tile to reveal it.
- Right-click a tile to flag or unflag it.
- If a mine is clicked, it explodes along with all others, ending the game with a "You Lost!" message.
- If all non-mine tiles are revealed, the game ends with a "You Win!" message.

### Animation
- Exploding mines are animated in sequence, with each mine starting 3 frames after the previous one.

## Features
- Randomized mine placement.
- Numbered tiles showing adjacent mines with specific colors.
- Cascading reveal of blank tiles.
- Timer to track game duration.
- Fully interactive and replayable.

## Setup

### Prerequisites
- Java 8 or higher.
- Gradle installed.
- Clone the repository:
  ```bash
  git clone https://github.com/<your-username>/<repository-name>.git
  ```

### Run the Game
1. Navigate to the project directory:
   ```bash
   cd <repository-name>
   ```
2. Build and run the game:
   ```bash
   gradle run --args="100"
   ```
   Replace `100` with the desired number of mines.

### Testing
Run unit tests:
```bash
gradle test
```

## Controls
- **Left Mouse Click**: Reveal a tile.
- **Right Mouse Click**: Flag or unflag a tile.
- **'r' Key**: Restart the game.
