import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import javalib.impworld.*;
import javalib.worldimages.*;
import tester.Tester;

//Examples class for Mazes
class Examples {

  void testMaze(Tester t) {

    Maze world = new Maze(160, 90, new Random(), 4, 2);
    //int sceneSize = 800;
    //world.bigBang(world.simonsize().x, world.simonsize().y, 0.0001);

  }

  Cell c1;

  Cell c2;

  Cell c3;

  Cell c4;

  Cell c5;

  Edge e1;

  Edge e2;

  Edge e3;

  Edge e4;

  Maze game1;

  Maze game2;

  Maze game3;

  Maze m2;

  WorldScene rec1;

  WorldScene rec2;

  WorldScene rec3;

  Border bc1;

  Border bc2;

  Player p1;

  Player p2;

  Player p3;

  void initData() {

    c1 = new Cell(0, 1, this.c2, c2, c2, c2);
    c2 = new Cell(1, 1);
    c3 = new Cell(2, 3);
    c4 = new Cell(1, 2);
    c5 = new Cell(1, 2, c1, c2, c3, c4);
    e1 = new Edge(new Posn(1, 1), new Posn(1, 1), 1);
    e2 = new Edge(new Posn(2, 1), new Posn(3, 1), 4);
    game1 = new Maze(1, 1, new Random(1));
    game2 = new Maze(2, 2, new Random(1));
    game3 = new Maze(20, 20, new Random(1));
    m2 = new Maze(1, 1, new Random(1));
    bc1 = new Border();
    bc2 = new Border();
    p1 = new Player(1, 1);
    p2 = new Player(2, 2);
    p3 = new Player(30, 30);

    rec1 = new WorldScene(1000, 600);
    rec1.placeImageXY(new RectangleImage(1000, 600, OutlineMode.OUTLINE, new Color(0, 0, 0)), 500,
        300);
    rec1.placeImageXY(
        new OverlayImage(new RectangleImage(12, 2, OutlineMode.SOLID, new Color(0, 0, 0)),
            (new RectangleImage(1000, 600, OutlineMode.SOLID, new Color(0, 0, 0)))),
        5, 5);

  }

  //test Cell.link(Cell)
  void testLink(Tester t) {

    initData();

    Border mt = new Border();
    Cell a = new Cell(0, 1, mt, mt, mt, mt);
    Cell b = new Cell(1, 1, mt, mt, mt, mt);

    t.checkExpect(a, new Cell(0, 1, mt, mt, mt, mt));
    t.checkExpect(b, new Cell(1, 1, mt, mt, mt, mt));

    a.link(b);
    b.link(a);

    Cell a2 = new Cell(0, 1, mt, mt, b, mt);
    a2.neighbors.add(b);
    Cell b2 = new Cell(1, 1, mt, a, mt, mt);
    b2.neighbors.add(a);
    t.checkExpect(a, a2);
    t.checkExpect(b, b2);

  }

  //test Maze.makeScene()
  void testMakeScene(Tester t) {

    initData();

    Maze m = new Maze(2, 2, new Random(1), 50, 2);

    //m.bigBang(200, 200);

    WorldScene scene = new WorldScene(250, 250);
    scene.placeImageXY(new RectangleImage(100, 100, OutlineMode.SOLID, new Color(128, 0, 128)), 150,
        150);
    scene.placeImageXY(new RectangleImage(100, 100, OutlineMode.SOLID, Color.GREEN), 50, 50);
    scene.placeImageXY(new RectangleImage(100, 100, OutlineMode.SOLID, Color.BLUE), 50, 50);

    //m.player.draw(scene, 50, 2, 2, true);

    scene.placeImageXY(
        new OverlayImage(
            new OverlayImage(
                new RectangleImage(102, 2, OutlineMode.SOLID, Color.BLACK).movePinhole(0, 50),
                new RectangleImage(0, 0, OutlineMode.SOLID, Color.GRAY).movePinhole(0, -50)),
            new OverlayImage(
                new RectangleImage(2, 102, OutlineMode.SOLID, Color.BLACK).movePinhole(50, 0),
                new RectangleImage(0, 0, OutlineMode.SOLID, Color.GRAY).movePinhole(-50, 0))),
        50, 50);
    scene.placeImageXY(new OverlayImage(
        new OverlayImage(new RectangleImage(0, 0, OutlineMode.SOLID, Color.GRAY).movePinhole(0, 50),
            new RectangleImage(102, 2, OutlineMode.SOLID, Color.BLACK).movePinhole(0, -50)),
        new OverlayImage(
            new RectangleImage(2, 102, OutlineMode.SOLID, Color.BLACK).movePinhole(50, 0),
            new RectangleImage(0, 0, OutlineMode.SOLID, Color.GRAY).movePinhole(-50, 0))),
        50, 150);
    scene.placeImageXY(
        new OverlayImage(new OverlayImage(
            new RectangleImage(102, 2, OutlineMode.SOLID, Color.BLACK).movePinhole(0, 50),
            new RectangleImage(102, 2, OutlineMode.SOLID, Color.BLACK).movePinhole(0, -50)),
            new OverlayImage(
                new RectangleImage(0, 0, OutlineMode.SOLID, Color.GRAY).movePinhole(50, 0),
                new RectangleImage(2, 102, OutlineMode.SOLID, Color.BLACK).movePinhole(-50, 0))),
        150, 50);
    scene.placeImageXY(
        new OverlayImage(new OverlayImage(
            new RectangleImage(102, 2, OutlineMode.SOLID, Color.BLACK).movePinhole(0, 50),
            new RectangleImage(102, 2, OutlineMode.SOLID, Color.BLACK).movePinhole(0, -50)),
            new OverlayImage(
                new RectangleImage(0, 0, OutlineMode.SOLID, Color.GRAY).movePinhole(50, 0),
                new RectangleImage(2, 102, OutlineMode.SOLID, Color.BLACK).movePinhole(-50, 0))),
        150, 150);

    t.checkExpect(m.makeScene(), scene);

  }

  //test ICell.edgeV(int, int)
  void testEdgeV(Tester t) {

    initData();
    t.checkExpect(c1.edgeV(1, 2), new RectangleImage(0, 0, OutlineMode.SOLID, Color.GRAY));
    t.checkExpect(c1.edgeV(3, 2), new RectangleImage(0, 0, OutlineMode.SOLID, Color.GRAY));

    t.checkExpect(bc1.edgeV(2, 4), new RectangleImage(4, 8, OutlineMode.SOLID, Color.BLACK));
    WorldImage border2 = new RectangleImage(6, 16, OutlineMode.SOLID, Color.BLACK);
    t.checkExpect(bc2.edgeV(5, 6), border2);

  }

  //test ICell.edgeH(int, int)
  void testEdgeH(Tester t) {

    initData();
    t.checkExpect(c1.edgeH(2, 4), new RectangleImage(0, 0, OutlineMode.SOLID, Color.GRAY));
    t.checkExpect(c1.edgeH(3, 5), new RectangleImage(0, 0, OutlineMode.SOLID, Color.GRAY));

    WorldImage border1 = new RectangleImage(2 * (1 + 2 / 2), 2, OutlineMode.SOLID, Color.BLACK);
    t.checkExpect(bc1.edgeH(1, 2), border1);
    WorldImage border2 = new RectangleImage(2 * (3 + 2 / 2), 2, OutlineMode.SOLID, Color.BLACK);
    t.checkExpect(bc2.edgeH(3, 2), border2);

  }

  //test Edge.updateCell(ArrayList<Cell>, int, int)
  void testUpdateCell(Tester t) {

    initData();

    ArrayList<Cell> board1 = new ArrayList<Cell>();

    Cell cell1 = new Cell(0, 0);
    Cell cell2 = new Cell(0, 1);
    board1.add(cell1);
    board1.add(cell2);

    e1 = new Edge(new Posn(0, 0), new Posn(0, 1), 1);

    e1.updateCell(board1, 1, 2);
    t.checkExpect(cell1.bottom, cell2);
    t.checkExpect(cell1.bottom, cell2);

  }

  // check that cell1 and cell2 are linked in the opposite direction
  void testSameRep(Tester t) {

    initData();
    HashMap<Posn, Posn> rooms = new HashMap<Posn, Posn>();

    for (int i = 0; i < 100; i++) {

      for (int j = 0; j < 100; j++) {

        rooms.put(new Posn(i, j), new Posn(i, j));

      }

    }

    // Put all the data into the hashtable
    rooms.put(new Posn(1, 1), new Posn(1, 1));
    t.checkExpect(e1.sameRep(rooms), true);
    rooms.put(new Posn(2, 1), new Posn(6, 8));
    t.checkExpect(rooms.get(new Posn(2, 1)), new Posn(6, 8));

    rooms.put(new Posn(1, 1), new Posn(2, 2));
    rooms.put(new Posn(2, 2), new Posn(3, 3));
    t.checkExpect(e2.sameRep(rooms), false);

  }

  //test Cell.draw()
  void testDraw(Tester t) {

    WorldScene scene1 = new WorldScene(100, 100);
    WorldScene scene2 = new WorldScene(100, 100);

    Cell c1 = new Cell(0, 0);

    t.checkExpect(scene1, scene2);
    c1.draw(2, 2, scene1, 50, 2);
    scene2.placeImageXY(
        new OverlayImage(
            new OverlayImage(
                new RectangleImage(102, 2, OutlineMode.SOLID, Color.BLACK).movePinhole(0, 50),
                new RectangleImage(102, 2, OutlineMode.SOLID, Color.BLACK).movePinhole(0, -50)),
            new OverlayImage(
                new RectangleImage(2, 102, OutlineMode.SOLID, Color.BLACK).movePinhole(50, 0),
                new RectangleImage(2, 102, OutlineMode.SOLID, Color.BLACK).movePinhole(-50, 0))),
        50, 50);
    t.checkExpect(scene1, scene2);

  }

  //test Maze.initMaze()
  void testInitMaze(Tester t) {

    Maze m1 = new Maze(1, 1, new Random(1));
    t.checkExpect(m1.initMaze(), new ArrayList<Cell>(Arrays.asList(new Cell(0, 0))));

    t.checkConstructorException(new IllegalArgumentException("Size must be greater than 0!"),
        "Maze", 0, 1);
    t.checkConstructorException(new IllegalArgumentException("Size must be greater than 0!"),
        "Maze", 1, 0);
    t.checkConstructorException(new IllegalArgumentException("Size must be greater than 0!"),
        "Maze", 0, -1);

    t.checkConstructorException(new IllegalArgumentException("Cellsize must be greater than 0!"),
        "Maze", 1, 1, new Random(), 0, 1);
    t.checkConstructorException(new IllegalArgumentException("Cellsize must be greater than 0!"),
        "Maze", 1, 1, new Random(), -1, 1);
    t.checkConstructorException(new IllegalArgumentException("Linesize must be greater than 0!"),
        "Maze", 1, 1, new Random(), 1, 0);
    t.checkConstructorException(new IllegalArgumentException("Linesize must be greater than 0!"),
        "Maze", 1, 1, new Random(), 1, -1);

  }

  //test EdgeComparator.compare(Edge, Edge)
  void testEdgeComparator(Tester t) {

    Edge e1 = new Edge(new Posn(0, 0), new Posn(1, 1), 5);
    Edge e2 = new Edge(new Posn(0, 0), new Posn(1, 1), 5);
    Edge e3 = new Edge(new Posn(0, 0), new Posn(1, 1), -5);
    Edge e4 = new Edge(new Posn(0, 0), new Posn(1, 1), 10);

    EdgeComparator e = new EdgeComparator();
    t.checkExpect(e.compare(e1, e2), 0);
    t.checkExpect(e.compare(e1, e3), 1);
    t.checkExpect(e.compare(e1, e4), -1);

  }

  //test Maze.simonsize()
  void testSimonSize(Tester t) {

    this.initData();

    t.checkExpect(game1.simonsize(), new Posn(24, 24));
    t.checkExpect(game2.simonsize(), new Posn(40, 40));
    t.checkExpect(game3.simonsize(), new Posn(328, 328));

  }

  //test Maze.onTick()
  void testOnTick(Tester t) {

    this.initData();
    Maze m = new Maze(2, 2, new Random(1));

    m.dfs = true;
    m.manual = false;

    //starting pos
    t.checkExpect(m.player.position, new Posn(0, 0));

    m.onTick();
    //new pos from automove
    t.checkExpect(m.player.position, new Posn(1, 0));

    m.manual = true;
    m.onTick(); //no move because manual on
    t.checkExpect(m.player.position, new Posn(1, 0));

    m.manual = false;
    m.onTick();
    //new pos from automove
    t.checkExpect(m.player.position, new Posn(0, 1));
    m.onTick();
    //new pos from automove
    t.checkExpect(m.player.position, new Posn(1, 1)); //won

    m.onTick();
    //no new pos because maze completed
    t.checkExpect(m.player.position, new Posn(1, 1));

  }

  //test Player.won(int, int)
  void testWon(Tester t) {

    this.initData();

    t.checkExpect(p1.won(1, 1), false);
    t.checkExpect(p2.won(1, 1), false);

    t.checkExpect(p3.won(30, 30), false);

    //usually p3 will have nodes linked all the way to the start,
    //but this is a shortcut to fulfill the win condition
    p3.map.put(new Posn(29, 29), new Posn(29, 28));

    t.checkExpect(p3.won(30, 30), true);

  }

  //test Player.move(ArrayList<Cell>, int, int, int)
  void testMove(Tester t) {

    ArrayList<Cell> b = new ArrayList<Cell>();

    for (int i = 0; i < 4; i++) {

      for (int j = 0; j < 4; j++) {

        b.add(new Cell(i, j));

      }

    }

    b.get(0).link(b.get(1));
    b.get(1).link(b.get(0));
    b.get(1).link(b.get(2));
    b.get(2).link(b.get(1));

    b.get(6).link(b.get(2));
    b.get(2).link(b.get(6));

    Player p = new Player(4, 4);

    t.checkExpect(p.position, new Posn(0, 0));
    //invalid y move -> no change
    p.move(b, 0, -1, 4);
    t.checkExpect(p.position, new Posn(0, 0));

    p.move(b, 0, 1, 4);
    t.checkExpect(p.position, new Posn(0, 1));
    //invalid x move -> no change
    p.move(b, 1, 0, 4);
    t.checkExpect(p.position, new Posn(0, 1));

    p.move(b, 0, 1, 4);
    t.checkExpect(p.position, new Posn(0, 2));
    //invalid x move -> no change
    p.move(b, 0, 1, 4);
    t.checkExpect(p.position, new Posn(0, 2));

    //valid x move
    p.move(b, 1, 0, 4);
    t.checkExpect(p.position, new Posn(1, 2));

    t.checkException(new IllegalArgumentException("dx and dy cannot both be nonzero!"), p, "move",
        b, 1, 1, 4);
    t.checkException(new IllegalArgumentException("dx and dy cannot both be nonzero!"), p, "move",
        b, -1, -1, 4);
    t.checkException(new IllegalArgumentException("dx and dy cannot both be nonzero!"), p, "move",
        b, -1, 1, 4);

    t.checkException(new IllegalArgumentException("dx cannot be greater than 1 or less than -1!"),
        p, "move", b, -2, 0, 4);
    t.checkException(new IllegalArgumentException("dx cannot be greater than 1 or less than -1!"),
        p, "move", b, 2, 0, 4);
    t.checkException(new IllegalArgumentException("dy cannot be greater than 1 or less than -1!"),
        p, "move", b, 0, 2, 4);
    t.checkException(new IllegalArgumentException("dy cannot be greater than 1 or less than -1!"),
        p, "move", b, 0, -2, 4);

  }

  void testAutoMove(Tester t) {

    //depth-first
    Maze m = new Maze(3, 3, new Random(1));
    t.checkExpect(m.player.position, new Posn(0, 0));
    m.player.autoMove(m.maze, 3, 3, true);
    t.checkExpect(m.player.position, new Posn(0, 1));
    m.player.autoMove(m.maze, 3, 3, true);
    t.checkExpect(m.player.position, new Posn(1, 1));
    m.player.autoMove(m.maze, 3, 3, true);
    t.checkExpect(m.player.position, new Posn(1, 2));
    m.player.autoMove(m.maze, 3, 3, true);
    t.checkExpect(m.player.position, new Posn(2, 2));
    m.player.autoMove(m.maze, 3, 3, true);
    t.checkExpect(m.player.position, new Posn(0, 2));
    m.player.autoMove(m.maze, 3, 3, true);
    t.checkExpect(m.player.position, new Posn(1, 0));
    m.player.autoMove(m.maze, 3, 3, true);
    t.checkExpect(m.player.position, new Posn(2, 0));

    //breadth-first
    m = new Maze(3, 3, new Random(1));
    t.checkExpect(m.player.position, new Posn(0, 0));
    m.player.autoMove(m.maze, 3, 3, false);
    t.checkExpect(m.player.position, new Posn(0, 1));
    m.player.autoMove(m.maze, 3, 3, false);
    t.checkExpect(m.player.position, new Posn(1, 1));
    m.player.autoMove(m.maze, 3, 3, false);
    t.checkExpect(m.player.position, new Posn(1, 0));
    m.player.autoMove(m.maze, 3, 3, false);
    t.checkExpect(m.player.position, new Posn(1, 2));
    m.player.autoMove(m.maze, 3, 3, false);
    t.checkExpect(m.player.position, new Posn(2, 0));
    m.player.autoMove(m.maze, 3, 3, false);
    t.checkExpect(m.player.position, new Posn(0, 2));
    m.player.autoMove(m.maze, 3, 3, false);
    t.checkExpect(m.player.position, new Posn(2, 2));

  }

  void testDrawPlayer(Tester t) {

    WorldScene scene1 = new WorldScene(100, 100);

    Player p1 = new Player(1, 2);
    p1.draw(scene1, 10, 1, 2, true);

    WorldScene scene2 = new WorldScene(100, 100);
    scene2.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN), 10, 10);
    scene2.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE), 10, 10);

    t.checkExpect(scene1, scene2);

    scene1 = new WorldScene(100, 100);
    scene2 = new WorldScene(100, 100);
    p1.position = new Posn(0, 1);
    p1.draw(scene1, 10, 1, 2, true);
    scene2.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.GREEN), 10, 10);
    scene2.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.BLUE), 10, 30);

    t.checkExpect(scene1, scene2);

  }

  void testonKeyEvent(Tester t) {

    this.initData();

    WorldScene scene3 = new WorldScene(24, 24);
    WorldScene scene4 = new WorldScene(24, 24);

    this.game1.onKeyEvent("d");
    this.game1.onKeyEvent("r");
    t.checkExpect(this.game1.initMaze(), new ArrayList<Cell>(Arrays.asList(new Cell(0, 0))));

    scene3.placeImageXY(new RectangleImage(16, 16, OutlineMode.SOLID, new Color(128, 0, 128)), 8,
        8);
    scene3.placeImageXY(new RectangleImage(16, 16, OutlineMode.SOLID, Color.GREEN), 8, 8);
    scene3.placeImageXY(new RectangleImage(16, 16, OutlineMode.SOLID, Color.BLUE), 8, 8);
    scene3.placeImageXY(
        new OverlayImage(
            new OverlayImage(
                new RectangleImage(18, 2, OutlineMode.SOLID, Color.BLACK).movePinhole(0, 8),
                new RectangleImage(18, 2, OutlineMode.SOLID, Color.BLACK).movePinhole(0, -8)),
            new OverlayImage(
                new RectangleImage(2, 18, OutlineMode.SOLID, Color.BLACK).movePinhole(8, 0),
                new RectangleImage(2, 18, OutlineMode.SOLID, Color.BLACK).movePinhole(-8, 0))),
        8, 8);

    t.checkExpect(this.game1.makeScene(), scene3);

    // this.w1.onMouseClicked(new Posn(70, 20));

    this.game1.onKeyEvent("r");

    t.checkExpect(game1.manual, true);

    this.m2.onKeyEvent("d");
    t.checkExpect(m2.manual, false);
    t.checkExpect(m2.dfs, true);
    this.m2.onKeyEvent("r");
    this.m2.onKeyEvent("b");
    t.checkExpect(m2.dfs, false);
    t.checkExpect(m2.manual, false);
    this.m2.onKeyEvent("left");
    t.checkExpect(m2.manual, false);

    scene4.placeImageXY(new RectangleImage(16, 16, OutlineMode.SOLID, new Color(128, 0, 128)), 8,
        8);
    scene4.placeImageXY(new RectangleImage(16, 16, OutlineMode.SOLID, Color.GREEN), 8, 8);
    scene4.placeImageXY(new RectangleImage(16, 16, OutlineMode.SOLID, Color.BLUE), 8, 8);
    scene4.placeImageXY(
        new OverlayImage(
            new OverlayImage(
                new RectangleImage(18, 2, OutlineMode.SOLID, Color.BLACK).movePinhole(0, 8),
                new RectangleImage(18, 2, OutlineMode.SOLID, Color.BLACK).movePinhole(0, -8)),
            new OverlayImage(
                new RectangleImage(2, 18, OutlineMode.SOLID, Color.BLACK).movePinhole(8, 0),
                new RectangleImage(2, 18, OutlineMode.SOLID, Color.BLACK).movePinhole(-8, 0))),
        8, 8);

    t.checkExpect(this.m2.makeScene(), scene4);

  }

  void testReset(Tester t) {

    Player p = new Player(3, 3);
    p.position = new Posn(1, 2);
    p.visited.add(new Posn(0, 0));
    p.unfinished.add(new Posn(2, 2));
    p.map.put(new Posn(1, 1), new Posn(2, 2));

    p.reset(2, 2);

    t.checkExpect(p.position, new Posn(0, 0));
    t.checkExpect(p.visited, new ArrayList<Posn>());
    t.checkExpect(p.unfinished, new ArrayList<Posn>());
    t.checkExpect(p.map.size(), 4);
    t.checkExpect(p.map.get(new Posn(1, 1)), new Posn(1, 1));

  }

}
