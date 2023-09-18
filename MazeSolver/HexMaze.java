import javalib.impworld.*;

import java.awt.Color;
import javalib.worldimages.*;
import tester.Tester;

import java.util.*;

class HexBot {

  Posn position;

  ArrayList<Posn> visited;

  ArrayList<Posn> unfinished;

  HashMap<Posn, Posn> map;

  boolean dfs;

  HexBot(boolean dfs) {

    this.position = new Posn(0, 0);
    this.visited = new ArrayList<Posn>();
    this.unfinished = new ArrayList<Posn>();
    this.dfs = dfs;
    this.map = new HashMap<Posn, Posn>();

  }

  void autoMove(ArrayList<HexCell> board, int sx, int sy) {

    if (this.map.size() == 0) {

      for (int i = 0; i < sx; i++) {

        for (int j = 0; j < sy; j++) {

          this.map.put(new Posn(i, j), new Posn(i, j));

        }

      }

    }

    HexCell current = board.get(this.position.x * sy + this.position.y);

    for (HexCell c : current.neighbors) {

      Posn pos = c.p;

      if (!this.visited.contains(pos) && !this.unfinished.contains(pos)) {

        this.map.put(pos, this.position);
        unfinished.add(pos);

      }

    }

    if (unfinished.size() > 0) {

      this.visited.add(this.position);
      this.position = this.dfs ? unfinished.remove(unfinished.size() - 1) : unfinished.remove(0);

    }

  }

  void draw(WorldScene scene, int cellsize, int sx, int sy) {

    int modx = (int) (Math.sin(Math.toRadians(30)) * cellsize);

    int root3 = (int) (Math.sqrt(3) * cellsize / 2);

    for (Posn p : this.visited) {

      int pady = p.x % 2 == 1 ? cellsize + root3 : cellsize;

      scene.placeImageXY(new HexagonImage(cellsize, OutlineMode.SOLID, Color.CYAN),
          p.x * (2 * cellsize - modx) + cellsize, p.y * (2 * root3) + pady);

    }

    for (Posn p : this.unfinished) {

      int pady = p.x % 2 == 1 ? cellsize + root3 : cellsize;

      scene.placeImageXY(new HexagonImage(cellsize, OutlineMode.SOLID, Color.ORANGE),
          p.x * (2 * cellsize - modx) + cellsize, p.y * (2 * root3) + pady);

    }

    int pady = position.x % 2 == 1 ? cellsize + root3 : cellsize;

    scene.placeImageXY(new HexagonImage(cellsize, OutlineMode.SOLID, Color.BLUE),
        this.position.x * (2 * cellsize - modx) + cellsize, this.position.y * (2 * root3) + pady);

    Posn current = this.position;

    if (this.won(sx, sy)) {

      scene.placeImageXY(new HexagonImage(cellsize, OutlineMode.SOLID, Color.BLUE), cellsize,
          cellsize);

      while (!this.map.get(current).equals(current)) {

        pady = current.x % 2 == 1 ? cellsize + root3 : cellsize;

        scene.placeImageXY(new HexagonImage(cellsize, OutlineMode.SOLID, Color.BLUE),
            current.x * (2 * cellsize - modx) + cellsize, current.y * (2 * root3) + pady);
        current = this.map.get(current);

      }

    }

  }

  void reset(int sx, int sy) {

    this.position = new Posn(0, 0);
    this.visited = new ArrayList<Posn>();
    this.unfinished = new ArrayList<Posn>();
    this.map = new HashMap<Posn, Posn>();

    for (int i = 0; i < sx; i++) {

      for (int j = 0; j < sy; j++) {

        this.map.put(new Posn(i, j), new Posn(i, j));

      }

    }

  }

  boolean won(int sx, int sy) {

    return this.position.x == sx - 1 && this.position.y == sy - 1;

  }

}

class HexEdgeComparator implements Comparator<HexEdge> {

  public int compare(HexEdge e1, HexEdge e2) {

    return e1.weight > e2.weight ? 1 : (e1.weight < e2.weight) ? -1 : 0;

  }

}

class HexEdge {

  Posn a;

  Posn b;

  int weight;

  HexEdge(Posn a, Posn b, int w) {

    this.a = a;
    this.b = b;
    this.weight = w;

  }

  //updates the cells by getting them from the board and linking them  
  void updateCell(ArrayList<HexCell> board, int sx, int sy) {

    HexCell a = board.get(this.a.x * sy + this.a.y);
    HexCell b = board.get(this.b.x * sy + this.b.y);
    a.link(b);
    b.link(a);

  }

  //checks if its the same representative 
  boolean sameRep(HashMap<Posn, Posn> map) {

    Posn arep = map.get(this.a);
    Posn arep2 = this.a;

    while (!arep2.equals(arep)) {

      arep2 = arep;
      arep = map.get(arep);

    }

    Posn brep = map.get(this.b);
    Posn brep2 = this.b;

    while (!brep2.equals(brep)) {

      brep2 = brep;
      brep = map.get(brep);

    }

    if (arep.equals(brep)) {

      return true;

    } else {

      map.put(brep, arep);
      map.put(this.a, arep);
      map.put(this.b, arep);
      return false;

    }

  }

}

interface IHexCell {

  public WorldImage edge(int x, int y);

  public WorldImage edge(int x, int y, Color color);

}

class HexBorder implements IHexCell {

  public WorldImage edge(int x, int y) {

    return new LineImage(new Posn(x, y), Color.BLACK);

  }

  public WorldImage edge(int x, int y, Color c) {

    return new LineImage(new Posn(x, y), c);

  }

}

class HexCell implements IHexCell {

  Posn p;

  IHexCell tleft;

  IHexCell tmid;

  IHexCell tright;

  IHexCell bleft;

  IHexCell bmid;

  IHexCell bright;

  ArrayList<HexCell> neighbors = new ArrayList<HexCell>();

  HexCell(int x, int y) {

    this.p = new Posn(x, y);
    HexBorder b = new HexBorder();
    this.tleft = b;
    this.tmid = b;
    this.tright = b;
    this.bleft = b;
    this.bmid = b;
    this.bright = b;

  }

  public void draw(int sx, int sy, WorldScene scene, int size) {

    int modx = (int) (Math.sin(Math.toRadians(30)) * size);
    int mody = (int) (Math.cos(Math.toRadians(30)) * size);

    int consty = (int) (Math.sqrt(3) * size / 2);

    WorldImage lines = new OverlayImage(
        new OverlayImage(
            new OverlayImage(this.tright.edge(modx, mody).movePinhole(-size + modx / 2, consty / 2),
                this.tleft.edge(modx, -mody).movePinhole(size - modx / 2, consty / 2)),
            this.tmid.edge(size, 0).movePinhole(0, consty)),
        new OverlayImage(
            new OverlayImage(
                this.bright.edge(modx, -mody).movePinhole(-size + modx / 2, -consty / 2),
                this.bleft.edge(-modx, -mody).movePinhole(size - modx / 2, -consty / 2)),
            this.bmid.edge(size, 0).movePinhole(0, -consty)));

    int scenex = this.p.x * (2 * size - modx) + size;
    int sceney = (int) (this.p.y * 2 * consty + size);

    if (this.p.x % 2 == 1) {

      sceney = sceney + consty;

    }

    scene.placeImageXY(lines, scenex, sceney);

  }

  public WorldImage edge(int x, int y) {

    return new RectangleImage(0, 0, OutlineMode.SOLID, Color.GRAY);

  }

  public WorldImage edge(int x, int y, Color c) {

    return new RectangleImage(0, 0, OutlineMode.SOLID, Color.GRAY);

  }

  //links the cells together
  void link(HexCell c) {

    if (this.p.x % 2 == 1) {

      if (c.p.y < this.p.y && c.p.x == this.p.x) {

        this.tmid = c;

      }

      else if (c.p.y > this.p.y) {

        if (c.p.x < this.p.x) {

          this.bleft = c;

        } else if (c.p.x > this.p.x) {

          this.bright = c;

        } else if (c.p.x == this.p.x) {

          this.bmid = c;

        }

      } else {

        if (c.p.x < this.p.x) {

          this.tleft = c;

        } else if (c.p.x > this.p.x) {

          this.tright = c;

        }

      }

    } else {

      if (c.p.y < this.p.y) {

        if (c.p.x < this.p.x) {

          this.tleft = c;

        } else if (c.p.x > this.p.x) {

          this.tright = c;

        }

        if (c.p.x == this.p.x) {

          this.tmid = c;

        }

      }

      else if (c.p.y > this.p.y) {

        if (c.p.x == this.p.x) {

          this.bmid = c;

        }

      } else {

        if (c.p.x < this.p.x) {

          this.bleft = c;

        } else if (c.p.x > this.p.x) {

          this.bright = c;

        }

      }

    }

    if (!this.neighbors.contains(c)) {

      this.neighbors.add(c);

    }

  }

}

//represents the Maze class
class HexMaze extends World {

  int cellsize;

  int sx;

  int sy;

  ArrayList<HexCell> maze;

  Random r;

  HexBot player;

  boolean auto;

  int tick = 0;

  //constructors for maze
  HexMaze(int sizex, int sizey) {

    this(sizex, sizey, new Random(), 5, new HexBot(true));

  }

  HexMaze(int sizex, int sizey, Random r) {

    this(sizex, sizey, r, 5, new HexBot(true));

  }

  HexMaze(int sizex, int sizey, Random r, HexBot p) {

    this(sizex, sizey, r, 5, p);

  }

  HexMaze(int sizex, int sizey, Random r, int cellsize) {

    this(sizex, sizey, r, cellsize, new HexBot(true));

  }

  HexMaze(int sizex, int sizey, Random r, int cellsize, HexBot player) {

    //maze size can't be less than 0
    if (sizex <= 0 || sizey <= 0) {

      throw new IllegalArgumentException("Size must be greater than 0!");

    }

    if (cellsize <= 0) {

      throw new IllegalArgumentException("Cellsize must be greater than 0!");

    }

    this.sx = sizex;
    this.sy = sizey;
    this.r = r;
    this.cellsize = cellsize;

    this.player = player;

    //long startTime = System.nanoTime();
    this.maze = this.initMaze();
    /*
    System.out.println("init finished in "
      + Long.toString((System.nanoTime() - startTime) / 1000000) + " milliseconds!");*/

  }

  //generate a complete game board
  ArrayList<HexCell> initMaze() {

    ArrayList<HexCell> board = new ArrayList<HexCell>();
    ArrayList<HexEdge> edges = new ArrayList<HexEdge>();
    HashMap<Posn, Posn> map = new HashMap<Posn, Posn>();

    for (int x = 0; x < this.sx; x++) {

      for (int y = 0; y < this.sy; y++) {

        board.add(new HexCell(x, y));
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

          if (x >= 1) {

            b = new Posn(x - 1, x % 2 == 1 ? y : y - 1);
            HexEdge e = new HexEdge(a, b, r.nextInt());
            edges.add(e);

          }

          if (x < this.sx - 1) {

            b = new Posn(x + 1, x % 2 == 1 ? y : y - 1);
            HexEdge e = new HexEdge(a, b, r.nextInt());
            edges.add(e);

          }

          b = new Posn(x, x % 2 == 1 ? y : y - 1);
          HexEdge e = new HexEdge(a, b, r.nextInt());
          edges.add(e);

        }

        if (y < this.sy - 1) {

          if (x >= 1) {

            b = new Posn(x - 1, x % 2 == 1 ? y + 1 : y);
            HexEdge e = new HexEdge(a, b, r.nextInt());
            edges.add(e);

          }

          if (x < this.sx - 1) {

            b = new Posn(x + 1, x % 2 == 1 ? y + 1 : y);
            HexEdge e = new HexEdge(a, b, r.nextInt());
            edges.add(e);

          }

          b = new Posn(x, x % 2 == 1 ? y + 1 : y);
          HexEdge e = new HexEdge(a, b, r.nextInt());
          edges.add(e);

        }

      }

    }

    edges.sort(new HexEdgeComparator());

    while (edges.size() > 0) {

      HexEdge e = edges.remove(0);

      if (!e.sameRep(map)) {

        e.updateCell(board, this.sx, this.sy);

      }

    }

    return board;

  }

  //renders the game state visually
  public WorldScene makeScene() {

    int modx = (int) (Math.sin(Math.toRadians(30)) * cellsize);

    int root3 = (int) (Math.sqrt(3) * cellsize);

    WorldScene scene = new WorldScene(this.sx * (2 * this.cellsize - modx) + modx, this.sy * root3);

    this.player.draw(scene, cellsize, this.sx, this.sy);

    for (int i = 0; i < maze.size(); i++) {

      this.maze.get(i).draw(this.sx, this.sy, scene, cellsize);

    }

    return scene;

  }

  //handler for tick events in the game, responsible for animation
  public void onTick() {

    if (!this.player.won(this.sx, this.sy)) {

      this.player.autoMove(this.maze, this.sx, this.sy);

    }

  }

  public Posn simonsize() {

    int modx = (int) (Math.sin(Math.toRadians(30)) * cellsize);

    int root3 = (int) (Math.sqrt(3) * cellsize);

    return new Posn(this.sx * (2 * this.cellsize - modx) + modx, this.sy * root3);

  }

}

class ExamplesHexMaze {

  void testMaze(Tester t) {

    // HexMaze world = new HexMaze(30, 30, new Random(), 10, new HexBot(false));
    //int sceneSize = 800;
    // world.bigBang(world.simonsize().x, world.simonsize().y, 0.0001);

  }

  HexBot hexbot;

  HexMaze hexmaze;

  void init() {

    this.hexbot = new HexBot(true);
    this.hexmaze = new HexMaze(10, 10);

  }

  void testReset(Tester t) {

    init();
    hexbot.reset(2, 2);
    t.checkExpect(hexbot.position, new Posn(0, 0));
    t.checkExpect(hexbot.visited, new ArrayList<Posn>());
    t.checkExpect(hexbot.unfinished, new ArrayList<Posn>());
    t.checkExpect(hexbot.map.size(), 4);
    t.checkExpect(hexbot.dfs, true);

  }

  void testMakeScene(Tester t) {

    init();
    WorldScene scene = hexmaze.makeScene();
    t.checkExpect(scene.width,
        (int) (Math.sin(Math.toRadians(30)) * hexmaze.cellsize * 2 * hexmaze.sx + hexmaze.cellsize
            + 27));
    t.checkExpect(scene.height, 80);

  }

  void testSimonSays(Tester t) {

    HexMaze maze = new HexMaze(5, 5, new Random(), 10, new HexBot(true));
    t.checkExpect(maze.simonsize(), new Posn(84, 85));

  }

  void testWon(Tester t) {

    init();
    hexbot.position = new Posn(3, 4);
    t.checkExpect(hexbot.won(4, 5), true);
    t.checkExpect(hexbot.won(5, 4), false);

  }

  void testOnTick(Tester t) {

    HexMaze hexMaze = new HexMaze(5, 5);
    hexMaze.onTick();
    t.checkExpect(hexMaze.player.position.x != 0 || hexMaze.player.position.y != 0, true);

  }

}
