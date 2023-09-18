import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import javalib.impworld.*;
import javalib.worldimages.*;

class Player {

  Posn position;

  ArrayList<Posn> visited;

  ArrayList<Posn> unfinished;

  HashMap<Posn, Posn> map;

  Player(int sx, int sy) {

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

  //moves player to new position either by depth-first or breadth-first search
  void autoMove(ArrayList<Cell> board, int sx, int sy, boolean dfs) {

    Cell current = board.get(this.position.x * sy + this.position.y);

    for (Cell c : current.neighbors) {

      Posn pos = c.p;

      if (!this.visited.contains(pos) && !this.unfinished.contains(pos)) {

        this.map.put(pos, this.position);
        unfinished.add(pos);

      }

    }

    if (unfinished.size() > 0) {

      this.visited.add(this.position);
      this.position = dfs ? unfinished.remove(unfinished.size() - 1) : unfinished.remove(0);

    } else {

      this.visited.add(this.position);
      this.position = new Posn(sx - 1, sy - 1);

    }

  }

  //draws player and related tiles on the given scene
  void draw(WorldScene scene, int cellsize, int sx, int sy, boolean manual) {

    for (Posn p : this.visited) {

      scene.placeImageXY(
          new RectangleImage(2 * cellsize, 2 * cellsize, OutlineMode.SOLID, Color.LIGHT_GRAY),
          p.x * 2 * cellsize + cellsize, p.y * 2 * cellsize + cellsize);

    }

    if (!manual) {

      for (Posn p : this.unfinished) {

        scene.placeImageXY(
            new RectangleImage(2 * cellsize, 2 * cellsize, OutlineMode.SOLID, Color.ORANGE),
            p.x * 2 * cellsize + cellsize, p.y * 2 * cellsize + cellsize);

      }

    }

    Posn end = new Posn(sx - 1, sy - 1);

    if (!this.map.get(end).equals(end)) {

      scene.placeImageXY(
          new RectangleImage(2 * cellsize, 2 * cellsize, OutlineMode.SOLID, Color.MAGENTA),
          cellsize, cellsize);

      while (!this.map.get(end).equals(end)) {

        scene.placeImageXY(
            new RectangleImage(2 * cellsize, 2 * cellsize, OutlineMode.SOLID, Color.MAGENTA),
            end.x * 2 * cellsize + cellsize, end.y * 2 * cellsize + cellsize);
        end = this.map.get(end);

      }

    } else {

      scene.placeImageXY(
          new RectangleImage(2 * cellsize, 2 * cellsize, OutlineMode.SOLID, Color.GREEN), cellsize,
          cellsize);

    }

    scene.placeImageXY(
        new RectangleImage(2 * cellsize, 2 * cellsize, OutlineMode.SOLID, Color.BLUE),
        position.x * 2 * cellsize + cellsize, position.y * 2 * cellsize + cellsize);

  }

  //EFFECT: resets player to normal state
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

  //EFFECT: mutates position (if valid) to be up by one cell
  void move(ArrayList<Cell> board, int dx, int dy, int sy) {

    if (dx != 0 && dy != 0) {

      throw new IllegalArgumentException("dx and dy cannot both be nonzero!");

    }

    if (dx > 1 || dx < -1) {

      throw new IllegalArgumentException("dx cannot be greater than 1 or less than -1!");

    }

    if (dy > 1 || dy < -1) {

      throw new IllegalArgumentException("dy cannot be greater than 1 or less than -1!");

    }

    Cell current = board.get(this.position.x * sy + this.position.y);

    ArrayList<Posn> valid = new ArrayList<Posn>();

    for (Cell c : current.neighbors) {

      Posn pos = c.p;

      valid.add(pos);

      if (!this.visited.contains(pos) && !this.unfinished.contains(pos)) {

        this.map.put(pos, this.position);
        unfinished.add(pos);

      }

    }

    Posn move = new Posn(this.position.x + dx, this.position.y + dy);

    if (valid.contains(move)) {

      if (!this.visited.contains(position)) {

        this.visited.add(position);

      }

      if (this.unfinished.contains(move)) {

        unfinished.remove(move);

      }

      this.position = move;

    }

  }

  boolean won(int sx, int sy) {

    return !this.map.get(new Posn(sx - 1, sy - 1)).equals(new Posn(sx - 1, sy - 1));

  }

}
