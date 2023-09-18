import javalib.impworld.*;

import java.awt.Color;
import javalib.worldimages.*;

import java.util.*;

//represents the Maze class
class Maze extends World {

  int cellsize;

  int linesize;

  int sx;

  int sy;

  ArrayList<Cell> maze;

  Random r;

  Player player;

  boolean manual;

  boolean dfs;

  //constructors for maze
  Maze(int sizex, int sizey) {

    this(sizex, sizey, new Random(), 8, 2, new Player(sizex, sizey));

  }

  Maze(int sizex, int sizey, Random r) {

    this(sizex, sizey, r, 8, 2, new Player(sizex, sizey));

  }

  Maze(int sizex, int sizey, Random r, Player p) {

    this(sizex, sizey, r, 8, 2, p);

  }

  Maze(int sizex, int sizey, Random r, int cellsize, int linesize) {

    this(sizex, sizey, r, cellsize, linesize, new Player(sizex, sizey));

  }

  Maze(int sizex, int sizey, Random r, int cellsize, int linesize, Player player) {

    //maze size can't be less than 0
    if (sizex <= 0 || sizey <= 0) {

      throw new IllegalArgumentException("Size must be greater than 0!");

    }

    if (cellsize <= 0) {

      throw new IllegalArgumentException("Cellsize must be greater than 0!");

    }

    if (linesize <= 0) {

      throw new IllegalArgumentException("Linesize must be greater than 0!");

    }

    this.sx = sizex;
    this.sy = sizey;
    this.r = r;
    this.cellsize = cellsize;
    this.linesize = linesize;

    this.manual = true;
    this.player = player;

    //long startTime = System.nanoTime();
    this.maze = this.initMaze();
    /*
    System.out.println("init finished in "
        + Long.toString((System.nanoTime() - startTime) / 1000000) + " milliseconds!");*/

  }

  //generate a complete game board
  ArrayList<Cell> initMaze() {

    ArrayList<Cell> board = new ArrayList<Cell>();
    ArrayList<Edge> edges = new ArrayList<Edge>();
    HashMap<Posn, Posn> map = new HashMap<Posn, Posn>();

    for (int x = 0; x < this.sx; x++) {

      for (int y = 0; y < this.sy; y++) {

        board.add(new Cell(x, y));
        map.put(new Posn(x, y), new Posn(x, y));

      }

    }

    for (int x = 0; x < this.sx; x++) {

      for (int y = 0; y < this.sy; y++) {

        //Cell a = board.get(x * this.sx + y);
        //Cell b;
        Posn a = new Posn(x, y);
        Posn b;

        if (y >= 1) {

          b = new Posn(x, y - 1);
          Edge e = new Edge(a, b, r.nextInt());
          edges.add(e);

        }

        if (y < this.sy - 1) {

          b = new Posn(x, y + 1);
          Edge e = new Edge(a, b, r.nextInt());
          edges.add(e);

        }

        if (x >= 1) {

          b = new Posn(x - 1, y);
          Edge e = new Edge(a, b, r.nextInt());
          edges.add(e);

        }

        if (x < this.sx - 1) {

          b = new Posn(x + 1, y);
          Edge e = new Edge(a, b, r.nextInt());
          edges.add(e);

        }

      }

    }

    edges.sort(new EdgeComparator());

    while (edges.size() > 0) {

      Edge e = edges.remove(0);

      if (!e.sameRep(map)) {

        e.updateCell(board, this.sx, this.sy);

      }

    }

    return board;

  }

  //renders the game state visually
  public WorldScene makeScene() {

    WorldScene scene = new WorldScene(this.sx * cellsize * 2 + cellsize,
        this.sy * cellsize * 2 + cellsize);
    scene.placeImageXY(
        new RectangleImage(cellsize * 2, cellsize * 2, OutlineMode.SOLID, new Color(128, 0, 128)),
        this.sx * cellsize * 2 - cellsize, this.sy * cellsize * 2 - cellsize);

    this.player.draw(scene, cellsize, this.sx, this.sy, this.manual);

    for (int i = 0; i < maze.size(); i++) {

      this.maze.get(i).draw(this.sx, this.sy, scene, cellsize, linesize);

    }

    return scene;

  }

  //handler for tick events in the game, responsible for animation
  public void onTick() {

    if (!this.manual && !player.won(sx, sy)) {

      this.player.autoMove(this.maze, this.sx, this.sy, this.dfs);

    }

  }

  //handler for keypresses
  public void onKeyEvent(String evt) {

    if (evt.toLowerCase().equals("r")) {

      this.maze = this.initMaze();
      this.player.reset(this.sx, this.sy);
      this.manual = true;

    }

    if (evt.toLowerCase().equals("b")) {

      this.manual = false;
      this.dfs = false;

    }

    if (evt.toLowerCase().equals("d")) {

      this.manual = false;
      this.dfs = true;

    }

    if (evt.toLowerCase().equals("m")) {

      this.manual = true;

    }

    if (evt.toLowerCase().equals("enter")) {

      player.autoMove(this.maze, sx, sy, dfs);

    }

    if (this.manual) {

      if (evt.toLowerCase().equals("up")) {

        this.player.move(this.maze, 0, -1, this.sy);

      }

      if (evt.toLowerCase().equals("down")) {

        this.player.move(this.maze, 0, 1, this.sy);

      }

      if (evt.toLowerCase().equals("left")) {

        this.player.move(this.maze, -1, 0, this.sy);

      }

      if (evt.toLowerCase().equals("right")) {

        this.player.move(this.maze, 1, 0, this.sy);

      }

    }

  }

  //method for optimal resolution in simonworld
  public Posn simonsize() {

    return new Posn(this.sx * cellsize * 2 + cellsize, this.sy * cellsize * 2 + cellsize);

  }

}