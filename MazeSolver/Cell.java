import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import javalib.impworld.WorldScene;
import javalib.worldimages.*;

class EdgeComparator implements Comparator<Edge> {

  public int compare(Edge e1, Edge e2) {

    return e1.weight > e2.weight ? 1 : (e1.weight < e2.weight) ? -1 : 0;

  }

}

//represents the edge of the maze
class Edge {

  Posn a;

  Posn b;

  int weight;

  Edge(Posn a, Posn b, int w) {

    this.a = a;
    this.b = b;
    this.weight = w;

  }

  //updates the cells by getting them from the board and linking them  
  void updateCell(ArrayList<Cell> board, int sx, int sy) {

    Cell a = board.get(this.a.x * sy + this.a.y);
    Cell b = board.get(this.b.x * sy + this.b.y);
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

interface ICell {

  WorldImage edgeV(int size, int line);

  WorldImage edgeH(int size, int line);

}

class Border implements ICell {

  //draw border between cells (horizontally)
  public WorldImage edgeH(int size, int line) {

    return new RectangleImage(2 * size + line, line, OutlineMode.SOLID, Color.BLACK);

  }

  //draw border between cells (vertically)  
  public WorldImage edgeV(int size, int line) {

    return new RectangleImage(line, 2 * size + line, OutlineMode.SOLID, Color.BLACK);

  }

}

//A cell in the game
class Cell implements ICell {

  Posn p;

  ICell top;

  ICell left;

  ICell right;

  ICell bottom;

  ArrayList<Cell> neighbors = new ArrayList<Cell>();

  //constructors
  Cell(int x, int y) {

    this.p = new Posn(x, y);
    Border b = new Border();
    this.top = b;
    this.left = b;
    this.right = b;
    this.bottom = b;

  }

  Cell(int x, int y, ICell t, ICell l, ICell r, ICell b) {

    this.p = new Posn(x, y);
    this.top = t;
    this.left = l;
    this.right = r;
    this.bottom = b;

  }

  //draw this cell onto the given worldscene
  //EFFECT: worldscene is mutated to have this Cell rendered on it.
  void draw(int sx, int sy, WorldScene scene, int size, int line) {

    WorldImage rect = new OverlayImage(
        new OverlayImage(this.top.edgeH(size, line).movePinhole(0, size),
            this.bottom.edgeH(size, line).movePinhole(0, -size)),
        new OverlayImage(this.left.edgeV(size, line).movePinhole(size, 0),
            this.right.edgeV(size, line).movePinhole(-size, 0)));

    scene.placeImageXY(rect, 2 * this.p.x * size + size, 2 * this.p.y * size + size);

  }

  //don't draw anything (horizontally)
  public WorldImage edgeH(int size, int line) {

    return new RectangleImage(0, 0, OutlineMode.SOLID, Color.GRAY);

  }

  //don't draw anything (but vertically)
  public WorldImage edgeV(int size, int line) {

    return new RectangleImage(0, 0, OutlineMode.SOLID, Color.GRAY);

  }

  //links the cells together
  void link(Cell c) {

    if (c.p.x > this.p.x) {

      this.right = c;

    }

    else if (c.p.x < this.p.x) {

      this.left = c;

    }

    else if (c.p.y > this.p.y) {

      this.bottom = c;

    }

    else if (c.p.y < this.p.y) {

      this.top = c;

    }

    if (!this.neighbors.contains(c)) {

      this.neighbors.add(c);

    }

  }

}