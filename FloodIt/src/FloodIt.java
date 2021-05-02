import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

//Created by Daniel Jung and Aidan Matrician

//Represents a single square of the game area
class Cell {
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;

  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  // constructor for a cell : sets the top left corner cell's flooded value to
  // true.
  Cell(int x, int y, Color color, boolean flooded) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = this.x == 0 && this.y == 0;
    this.left = null;
    this.top = null;
    this.right = null;
    this.bottom = null;
  }

  // draws a single cell
  WorldImage drawCell() {
    return new RectangleImage(20, 20, OutlineMode.SOLID, this.color);

  }

  // EFFECT : floods the neighbors of this cell.
  void floodNeighbors(Color color) {

    if (this.left != null && this.left.flooded) {
      this.left.color = color;

    }
    else if (this.left != null && this.left.color.equals(color)) {
      this.left.flooded = true;
    }

    if (this.right != null && this.right.flooded) {
      this.right.color = color;

    }
    else if (this.right != null && this.right.color.equals(color)) {
      this.right.flooded = true;
    }

    if (this.bottom != null && this.bottom.flooded) {
      this.bottom.color = color;

    }
    else if (this.bottom != null && this.bottom.color.equals(color)) {
      this.bottom.flooded = true;
    }

    if (this.top != null && this.top.flooded) {
      this.top.color = color;

    }
    else if (this.top != null && this.top.color.equals(color)) {
      this.top.flooded = true;
    }

  }

}

class FloodItWorld extends World {
  // All the cells of the game
  ArrayList<ArrayList<Cell>> board;
  // the original board
  ArrayList<ArrayList<Cell>> originalBoard;

  int maxSteps;

  // will start the game at 0
  int currentSteps;

  // list of colors to use, must be at least 6 colors
  ArrayList<Color> loc;

  // size of the game (n by n)
  int size;

  // clicked color
  Color clickedColor;

  // represents the high score
  int highScore;

  FloodItWorld(int size) {

    this.maxSteps = size * 2 - 2;
    this.currentSteps = 0;
    this.loc = new ArrayList<Color>(
        Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.PINK, Color.MAGENTA));
    this.size = size;
    this.board = this.randomBoard(size);

    // this.originalBoard = new ArrayList<ArrayList<Cell>>(this.board);

    this.updateCells();
    this.clickedColor = this.board.get(0).get(0).color;
    this.highScore = 0;

  }

  // convenience constructor
  // this constructor is used for testing but should not be used
  // to play the game
  FloodItWorld(ArrayList<ArrayList<Cell>> board, int size) {
    this.board = board;
    // this.originalBoard = new ArrayList<ArrayList<Cell>>(board);
    this.maxSteps = 25;
    this.currentSteps = 0;
    this.loc = new ArrayList<Color>(
        Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.PINK, Color.MAGENTA));
    this.size = size;
    this.highScore = 0;
  }

  // EFFECT : void function that updates the cell's left, right, top, and bottom
  // cells
  void updateCells() {
    for (int row = 0; row < this.board.size(); row++) {
      for (int col = 0; col < this.board.get(row).size(); col++) {

        // this conditional block checks the rows and assigns top and bottom cells
        // accordingly
        if (row == 0) {
          this.board.get(row).get(col).top = this.board.get(row + 1).get(col);
        }

        else if (row == this.board.size() - 1) {

          this.board.get(row).get(col).bottom = this.board.get(row - 1).get(col);

        }
        else {
          this.board.get(row).get(col).top = this.board.get(row + 1).get(col);
          this.board.get(row).get(col).bottom = this.board.get(row - 1).get(col);
        }

        // this conditional block checks the columns and assigns left and right cells
        // accordingly
        if (col == 0) {
          this.board.get(row).get(col).right = this.board.get(row).get(col + 1);

        }

        else if (col == this.board.size() - 1) {

          this.board.get(row).get(col).left = this.board.get(row).get(col - 1);
        }

        else {
          this.board.get(row).get(col).right = this.board.get(row).get(col + 1);
          this.board.get(row).get(col).left = this.board.get(row).get(col - 1);
        }

      }

    }

  }

  // creates a random board to play on based on the size of the board
  // the top left corner cell has a position of 0, 0.
  ArrayList<ArrayList<Cell>> randomBoard(int size) {
    ArrayList<ArrayList<Cell>> randomBoard = new ArrayList<ArrayList<Cell>>();

    for (int row = 0; row < size; row++) {
      ArrayList<Cell> randomRow = new ArrayList<Cell>();

      for (int col = 0; col < size; col++) {
        Random randomIndex = new Random();

        Cell randomCell = new Cell(row, col, this.loc.get(randomIndex.nextInt(this.loc.size() - 1)),
            false);
        randomRow.add(randomCell);

      }
      randomBoard.add(randomRow);

    }

    return randomBoard;

  }

  // draws the board
  WorldImage drawBoard() {

    WorldImage stackedRow = new EmptyImage();

    for (int row = 0; row < this.board.size(); row++) {
      WorldImage singleRow = new EmptyImage();

      for (int col = 0; col < this.board.get(row).size(); col++) {

        singleRow = new BesideImage(singleRow, this.board.get(row).get(col).drawCell());

      }
      stackedRow = new AboveImage(stackedRow, singleRow);
    }

    return stackedRow;

  }

  // draws the total amount of steps and the current amount of steps the player
  // has
  WorldImage drawSteps() {
    return new TextImage(this.currentSteps + "/" + this.maxSteps, 26, Color.BLACK);

  }

  // updates and draws the High score

  WorldImage updateHighscore() {
    if (this.maxSteps - this.currentSteps > this.highScore) {
      this.highScore = this.maxSteps - this.currentSteps;
    }
    return new TextImage("        Highscore: " + this.highScore, 26, Color.BLACK);

  }

  // creates the WorldScene for the FloodIt game
  public WorldScene makeScene() {
    int width = 500;
    int height = 500;
    WorldScene bkg = new WorldScene(width, height);
    boolean flooded = true;

    for (ArrayList<Cell> arrlist : this.board) {
      for (Cell c : arrlist) {
        flooded = c.flooded && flooded;
      }
    }

    // lose condition
    if (this.currentSteps > this.maxSteps) {

      bkg.placeImageXY(new AboveImage(
          new AboveImage(this.drawBoard(),
              new TextImage("        You Lose: " + this.currentSteps + "/" + this.maxSteps, 26,
                  Color.BLACK)).movePinhole(0, 0),
          new TextImage("        Highscore: " + this.highScore, 26, Color.BLACK)).movePinhole(0, 0),
          this.board.size() * 10, this.board.size() * 10 + 26);

    }
    // win condition
    else if (flooded) {

      bkg.placeImageXY(
          new AboveImage(new AboveImage(this.drawBoard(),
              new TextImage("       You Win: " + this.currentSteps + "/" + this.maxSteps, 26,
                  Color.BLACK)),
              this.updateHighscore()),
          this.board.size() * 10, this.board.size() * 10 + 26);

    }
    // no lose or win yet
    else {

      bkg.placeImageXY(
          new AboveImage(new AboveImage(this.drawBoard(), this.drawSteps()),
              new TextImage("       Highscore: " + this.highScore, 26, Color.BLACK)),
          this.board.size() * 10, this.board.size() * 10 + 26);

    }

    return bkg;

  }

  // EFFECT : handles the mouse clicks for the game and update's the game's
  // settings based on each click
  public void onMouseClicked(Posn posn) {

    int xPos = posn.x / 20;
    int yPos = posn.y / 20;

    if (xPos >= 0 && yPos >= 0 && xPos < this.board.size() && yPos < this.board.size()
        && !this.board.get(yPos).get(xPos).color.equals(this.board.get(0).get(0).color)

    ) {
      this.currentSteps += 1;
      this.clickedColor = this.board.get(yPos).get(xPos).color;
      this.board.get(0).get(0).color = this.clickedColor;

    }

  }

  // EFFECT : handles flooding per tick of the game
  public void onTick() {

    ArrayList<Cell> floodCells = new ArrayList<Cell>();
    for (ArrayList<Cell> arrlist : this.board) {
      for (Cell c : arrlist) {
        if (c.flooded && c.color.equals(this.clickedColor)) {
          floodCells.add(c);
        }
      }
    }
    for (int i = 0; i < floodCells.size(); i++) {
      floodCells.get(i).floodNeighbors(this.clickedColor);
    }

  }

  // EFFECT: updates the board to the original board when the key "r" is pressed.
  public void onKeyEvent(String ke) {
    if (ke.equals("r")) {
      this.board = this.randomBoard(this.size);
      this.updateCells();
      this.currentSteps = 0;
      this.clickedColor = this.board.get(0).get(0).color;
    }
  }
}

// represents the class for examples and function testing
class ExamplesFloodIt {

  ExamplesFloodIt() {

  }

  Cell c11 = new Cell(0, 0, Color.BLUE, true);
  Cell c12 = new Cell(0, 1, Color.RED, false);
  Cell c13 = new Cell(0, 2, Color.GREEN, false);
  ArrayList<Cell> rowOne = new ArrayList<Cell>(Arrays.asList(this.c11, this.c12, this.c13));

  Cell c21 = new Cell(1, 0, Color.RED, false);
  Cell c22 = new Cell(1, 1, Color.YELLOW, false);
  Cell c23 = new Cell(1, 2, Color.GREEN, false);
  ArrayList<Cell> rowTwo = new ArrayList<Cell>(Arrays.asList(this.c21, this.c22, this.c23));

  Cell c31 = new Cell(2, 0, Color.DARK_GRAY, false);
  Cell c32 = new Cell(2, 1, Color.GREEN, false);
  Cell c33 = new Cell(2, 2, Color.BLACK, false);
  ArrayList<Cell> rowThree = new ArrayList<Cell>(Arrays.asList(this.c31, this.c32, this.c33));

  // example of a 3 by 3 board
  ArrayList<ArrayList<Cell>> exampleOne = new ArrayList<ArrayList<Cell>>(
      Arrays.asList(this.rowOne, this.rowTwo, this.rowThree));

  // example of a 3 by 3 game
  FloodItWorld f1 = new FloodItWorld(this.exampleOne, 3);

  void initF1() {

    c11 = new Cell(0, 0, Color.BLUE, true);
    c12 = new Cell(0, 1, Color.RED, false);
    c13 = new Cell(0, 2, Color.GREEN, false);
    rowOne = new ArrayList<Cell>(Arrays.asList(this.c11, this.c12, this.c13));

    c21 = new Cell(1, 0, Color.RED, false);
    c22 = new Cell(1, 1, Color.YELLOW, false);
    c23 = new Cell(1, 2, Color.GREEN, false);
    rowTwo = new ArrayList<Cell>(Arrays.asList(this.c21, this.c22, this.c23));

    c31 = new Cell(2, 0, Color.DARK_GRAY, false);
    c32 = new Cell(2, 1, Color.GREEN, false);
    c33 = new Cell(2, 2, Color.BLACK, false);
    rowThree = new ArrayList<Cell>(Arrays.asList(this.c31, this.c32, this.c33));

    // example of a 3 by 3 board
    exampleOne = new ArrayList<ArrayList<Cell>>(
        Arrays.asList(this.rowOne, this.rowTwo, this.rowThree));

    // example of a 3 by 3 game
    f1 = new FloodItWorld(this.exampleOne, 3);

  }

  ArrayList<Cell> oneByOneRow = new ArrayList<Cell>(Arrays.asList(this.c11));

  // example of a 1 by 1 board
  ArrayList<ArrayList<Cell>> exampleTwo = new ArrayList<ArrayList<Cell>>(
      Arrays.asList(this.oneByOneRow));

  // example of a 1 by 1 game
  FloodItWorld f2 = new FloodItWorld(this.exampleTwo, 1);

  // example of a 10 by 10 random game
  FloodItWorld f3 = new FloodItWorld(10);

  void testBigBang(Tester t) {
    World w = f3;

    int worldWidth = 500;
    int worldHeight = 500;
    double tickRate = 0.1;
    w.bigBang(worldWidth, worldHeight, tickRate);
  }

  void testUpdateCells(Tester t) {
    this.initF1();
    this.f1.updateCells();
    // bottom left corner
    t.checkExpect(this.c11.top, this.c21);
    t.checkExpect(this.c11.left, null);
    t.checkExpect(this.c11.right, this.c12);
    t.checkExpect(this.c11.bottom, null);
    // bottom right corner
    t.checkExpect(this.c13.top, this.c23);
    t.checkExpect(this.c13.left, this.c12);
    t.checkExpect(this.c13.right, null);
    t.checkExpect(this.c13.bottom, null);

    // top right corner
    t.checkExpect(this.c33.top, null);
    t.checkExpect(this.c33.right, null);
    t.checkExpect(this.c33.left, this.c32);
    t.checkExpect(this.c33.bottom, this.c23);

    // top left corner
    t.checkExpect(this.c31.top, null);
    t.checkExpect(this.c31.right, this.c32);
    t.checkExpect(this.c31.left, null);
    t.checkExpect(this.c31.bottom, this.c21);

    // top middle
    t.checkExpect(this.c32.top, null);
    t.checkExpect(this.c32.right, this.c33);
    t.checkExpect(this.c32.left, this.c31);
    t.checkExpect(this.c32.bottom, this.c22);

    // middle
    t.checkExpect(this.c22.top, this.c32);
    t.checkExpect(this.c22.right, this.c23);
    t.checkExpect(this.c22.bottom, this.c12);

  }

  boolean testDrawCell(Tester t) {
    this.initF1();

    return t.checkExpect(this.c11.drawCell(),
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE))
        && t.checkExpect(this.c12.drawCell(),
            new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED))
        && t.checkExpect(this.c13.drawCell(),
            new RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN))
        && t.checkExpect(this.c21.drawCell(),
            new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED))
        && t.checkExpect(this.c22.drawCell(),
            new RectangleImage(20, 20, OutlineMode.SOLID, Color.YELLOW))
        && t.checkExpect(this.c23.drawCell(),
            new RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN));

  }

  // fix this one
  boolean testDrawBoard(Tester t) {
    this.initF1();
    return t.checkExpect(this.f1.drawBoard(),
        new AboveImage(
            new AboveImage(
                new AboveImage(new EmptyImage(),
                    new BesideImage(
                        new BesideImage(
                            new BesideImage(new EmptyImage(),
                                new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE)),
                            new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED)),
                        new RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN))),

                new BesideImage(
                    new BesideImage(
                        new BesideImage(new EmptyImage(),
                            new RectangleImage(20, 20, OutlineMode.SOLID, Color.RED)),
                        new RectangleImage(20, 20, OutlineMode.SOLID, Color.YELLOW)),
                    new RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN))),

            new BesideImage(
                new BesideImage(
                    new BesideImage(new EmptyImage(),
                        new RectangleImage(20, 20, OutlineMode.SOLID, Color.DARK_GRAY)),
                    new RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN)),
                new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLACK))))

        && t.checkExpect(this.f2.drawBoard(),
            new AboveImage(new EmptyImage(), new BesideImage(new EmptyImage(),
                new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE))));

  }

  boolean testDrawSteps(Tester t) {
    this.initF1();
    return t.checkExpect(this.f1.drawSteps(),
        new TextImage(this.f1.currentSteps + "/" + this.f1.maxSteps, 26, Color.BLACK))
        && t.checkExpect(this.f2.drawSteps(),
            new TextImage(this.f2.currentSteps + "/" + this.f2.maxSteps, 26, Color.BLACK));
  }

  boolean testUpdateHighscore(Tester t) {
    this.initF1();
    return t.checkExpect(this.f1.updateHighscore(),
        new TextImage("        Highscore: " + this.f1.highScore, 26, Color.BLACK))
        && t.checkExpect(this.f2.updateHighscore(),
            new TextImage("        Highscore: " + this.f2.highScore, 26, Color.BLACK));
  }

  boolean testRandomBoard(Tester t) {
    return t.checkExpect(this.f1.randomBoard(0).size(), 0)
        && t.checkExpect(this.f1.randomBoard(1).size(), 1)
        && t.checkExpect(this.f1.randomBoard(3).size(), 3)
        && t.checkExpect(this.f2.randomBoard(5).size(), 5)
        && t.checkExpect(this.f2.randomBoard(7).size(), 7);
  }

  void testMakeScene(Tester t) {
    this.initF1();

    WorldScene bkg = new WorldScene(500, 500);
    WorldScene bkg2 = new WorldScene(500, 500);

    bkg.placeImageXY(
        new AboveImage(new AboveImage(this.f1.drawBoard(), this.f1.drawSteps()),
            new TextImage("       Highscore: " + this.f1.highScore, 26, Color.BLACK)),
        this.f1.board.size() * 10, this.f1.board.size() * 10 + 26);

    // bkg.placeImageXY(new AboveImage(this.f1.drawBoard(), this.f1.drawSteps()),
    // 250, 250);

    // fix this one
    t.checkExpect(this.f1.makeScene(), bkg);

    bkg2.placeImageXY(
        new AboveImage(
            new AboveImage(this.f2.drawBoard(),
                new TextImage("       You Win: 0/25", 26, Color.BLACK)),
            new TextImage("        Highscore: 25", 26, Color.BLACK)),
        this.f2.board.size() * 10, this.f2.board.size() * 10 + 26);
    // bkg2.placeImageXY(new AboveImage(this.f2.drawBoard(), this.f2.drawSteps()),
    // 250, 250);

    // fix this one
    t.checkExpect(this.f2.makeScene(), bkg2);

  }

  void testFloodNeighbors(Tester t) {

    this.initF1();
    this.f1.updateCells();

    this.c22.flooded = true;
    this.c12.flooded = true;
    this.c32.flooded = true;
    this.c23.flooded = true;
    this.c21.flooded = true;

    this.c22.floodNeighbors(Color.YELLOW);

    // color changing check

    t.checkExpect(this.c22.color, Color.YELLOW);
    t.checkExpect(this.c12.color, Color.YELLOW);
    t.checkExpect(this.c32.color, Color.YELLOW);
    t.checkExpect(this.c23.color, Color.YELLOW);
    t.checkExpect(this.c21.color, Color.YELLOW);

    // flooded status and color changing check
    this.c12.floodNeighbors(Color.BLUE);
    t.checkExpect(this.c22.color, Color.BLUE);
    t.checkExpect(this.c11.color, Color.BLUE);
    t.checkExpect(this.c11.flooded, true);

    // checks color changing and flood status changing
    this.c12.floodNeighbors(Color.GREEN);
    t.checkExpect(this.c22.color, Color.GREEN);
    t.checkExpect(this.c11.color, Color.GREEN);
    t.checkExpect(this.c13.flooded, true);

  }

  void TestOnKeyEvent(Tester t) {

    this.initF1();
    this.f1.currentSteps = 5;
    this.f1.onKeyEvent("q");

    t.checkExpect(this.f1.currentSteps, 5);

    this.f1.onKeyEvent("r");
    t.checkExpect(this.f1.board.size(), 3);
    t.checkExpect(this.c11.right, this.c12);
    t.checkExpect(this.c12.right, this.c13);
    t.checkExpect(this.c13.right, null);
    t.checkExpect(this.c11.left, null);
    t.checkExpect(this.c12.left, this.c11);
    t.checkExpect(this.c13.left, this.c12);

    t.checkExpect(this.c11.top, null);
    t.checkExpect(this.c12.top, null);
    t.checkExpect(this.c13.top, null);
    t.checkExpect(this.c11.bottom, this.c21);
    t.checkExpect(this.c12.bottom, this.c22);
    t.checkExpect(this.c13.bottom, this.c23);

    t.checkExpect(this.f1.currentSteps, 0);
    t.checkExpect(this.f1.clickedColor, this.f1.board.get(0).get(0).color);

  }

  void testOnMouseClicked(Tester t) {

    this.initF1();
    this.f1.onMouseClicked(new Posn(20, 20));
    t.checkExpect(this.f1.currentSteps, 1);
    t.checkExpect(this.f1.clickedColor, Color.YELLOW);

    // do nothing cus it clicks the 0,0 color
    this.f1.onMouseClicked(new Posn(0, 0));
    t.checkExpect(this.f1.currentSteps, 1);
    t.checkExpect(this.f1.clickedColor, Color.YELLOW);

    this.f1.onMouseClicked(new Posn(30, 0));
    t.checkExpect(this.f1.currentSteps, 2);
    t.checkExpect(this.f1.clickedColor, Color.RED);

    this.f1.onMouseClicked(new Posn(30, 40));
    t.checkExpect(this.f1.currentSteps, 3);
    t.checkExpect(this.f1.clickedColor, Color.GREEN);

    this.f1.onMouseClicked(new Posn(45, 40));
    t.checkExpect(this.f1.currentSteps, 4);
    t.checkExpect(this.f1.clickedColor, Color.BLACK);

    // checks the out of bounds
    // nothing should change
    this.f1.onMouseClicked(new Posn(0, 60));
    t.checkExpect(this.f1.currentSteps, 4);
    t.checkExpect(this.f1.clickedColor, Color.BLACK);

    this.f1.onMouseClicked(new Posn(60, 0));
    t.checkExpect(this.f1.currentSteps, 4);
    t.checkExpect(this.f1.clickedColor, Color.BLACK);

    this.f1.onMouseClicked(new Posn(0, -10));
    t.checkExpect(this.f1.currentSteps, 4);
    t.checkExpect(this.f1.clickedColor, Color.BLACK);
    this.f1.onMouseClicked(new Posn(-10, 0));
    t.checkExpect(this.f1.currentSteps, 4);
    t.checkExpect(this.f1.clickedColor, Color.BLACK);
  }

  void testOnTick(Tester t) {
    this.initF1();
    this.f1.updateCells();

    t.checkExpect(this.f1.board.get(0).get(0).color, Color.BLUE);
    t.checkExpect(this.f1.board.get(0).get(1).color, Color.RED);
    t.checkExpect(this.f1.board.get(1).get(0).color, Color.RED);

    this.f1.onTick();
    t.checkExpect(this.f1.board.get(0).get(0).color, Color.BLUE);
    t.checkExpect(this.f1.board.get(0).get(1).color, Color.RED);
    t.checkExpect(this.f1.board.get(1).get(0).color, Color.RED);

    this.f1.onMouseClicked(new Posn(20, 0));
    this.f1.onTick();
    t.checkExpect(this.f1.board.get(0).get(0).color, Color.RED);
    t.checkExpect(this.f1.board.get(0).get(1).color, Color.RED);
    t.checkExpect(this.f1.board.get(1).get(0).color, Color.RED);
    //////

    this.f1.onMouseClicked(new Posn(40, 0));
    this.f1.onTick();
    t.checkExpect(this.f1.board.get(0).get(0).color, Color.GREEN);
    t.checkExpect(this.f1.board.get(0).get(1).color, Color.GREEN);
    t.checkExpect(this.f1.board.get(1).get(0).color, Color.GREEN);
    t.checkExpect(this.f1.board.get(0).get(2).color, Color.GREEN);

    // this.f1.onMouseClicked(new Posn(42, 25));
    this.f1.onTick();
    t.checkExpect(this.f1.board.get(0).get(0).color, Color.GREEN);
    t.checkExpect(this.f1.board.get(0).get(1).color, Color.GREEN);
    t.checkExpect(this.f1.board.get(1).get(0).color, Color.GREEN);
    t.checkExpect(this.f1.board.get(0).get(2).color, Color.GREEN);
    t.checkExpect(this.f1.board.get(1).get(2).color, Color.GREEN);
    t.checkExpect(this.f1.board.get(1).get(1).color, Color.YELLOW);
    t.checkExpect(this.f1.board.get(2).get(0).color, Color.DARK_GRAY);
    t.checkExpect(this.f1.board.get(2).get(1).color, Color.GREEN);
    t.checkExpect(this.f1.board.get(2).get(2).color, Color.BLACK);

    // t.checkExpect(this.f1.board.get(1).get(0).color, Color.GREEN);

    this.f1.onMouseClicked(new Posn(20, 20));
    this.f1.onTick();

    t.checkExpect(this.f1.board.get(0).get(0).color, Color.YELLOW);
    t.checkExpect(this.f1.board.get(0).get(1).color, Color.YELLOW);
    t.checkExpect(this.f1.board.get(1).get(0).color, Color.YELLOW);
    t.checkExpect(this.f1.board.get(0).get(2).color, Color.GREEN);
    t.checkExpect(this.f1.board.get(1).get(2).color, Color.GREEN);
    t.checkExpect(this.f1.board.get(1).get(1).color, Color.YELLOW);
    t.checkExpect(this.f1.board.get(2).get(0).color, Color.DARK_GRAY);
    t.checkExpect(this.f1.board.get(2).get(1).color, Color.GREEN);
    t.checkExpect(this.f1.board.get(2).get(2).color, Color.BLACK);

    this.f1.onTick();

    t.checkExpect(this.f1.board.get(0).get(0).color, Color.YELLOW);
    t.checkExpect(this.f1.board.get(0).get(1).color, Color.YELLOW);
    t.checkExpect(this.f1.board.get(1).get(0).color, Color.YELLOW);
    t.checkExpect(this.f1.board.get(0).get(2).color, Color.YELLOW);
    t.checkExpect(this.f1.board.get(1).get(2).color, Color.GREEN);
    t.checkExpect(this.f1.board.get(1).get(1).color, Color.YELLOW);
    t.checkExpect(this.f1.board.get(2).get(0).color, Color.DARK_GRAY);
    t.checkExpect(this.f1.board.get(2).get(1).color, Color.GREEN);
    t.checkExpect(this.f1.board.get(2).get(2).color, Color.BLACK);

  }
}
