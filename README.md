# MazeSolver

# CONTROLS:
r - restarts game with new maze of same size as before
b - start breadth-first search
d - start depth first search
m - manual mode (stops all searches)

enter - using the last used algorithm (depth-first by default), auto-move
one tile

arrow keys: (only in manual mode)
up - move up a tile (if allowed)
down - move down a tile (if allowed)
left - move left a tile (if allowed)
right - move right a tile (if allowed)

in the HexMaze, there is no player-controlled option, so just change the input 
HexBot parameter:
true is for depth-first search
false is for breadth-first search


Design ideas: (exactly the same for HexMaze)

# Maze - class encapsulating the full game state, also responsible for 
taking inputs and rendering the game

fields:
cellsize - size of maze "cells"
linesize - size of maze lines
sx - how many cells are on the maze's x-dimension
sy - how many cells are on the maze's y-dimension
maze - list of all the cells in the maze
r - random number source
player - controller for all automatic and player-inputted movements
manual - is the maze taking manual inputs or being solved automatically?
dfs - is the maze being solved with depth-first or breadth-first search?

methods
initMaze() - generates a complete game board using Kruskal's algorithm
We decided to use Posns to represent tiles because they were unambiguous
both in terms of debugging (they have well-defined toString()) and in 
comparison each other (by equality)

to union data we decided to create an Edge class (see below) to represent
edges in the graph, and to smoothly link them with Cell class nodes

makeScene() - draws the world state

onTick() - performs an action for every time unit of the game
We decided this would be most useful for algorithmically solving the maze, 
so that's the sole purpose of this function.

onKeyEvent() - handles player input, nothing special


# Edge - class defining edges between tiles in the maze, used mostly during
initialization for Kruskal's algorithm.

fields:
a - Posn representing one node of the edge
b - Posn representing another node of the edge
weight - weight of the edge determining which are kept during algorithm execution

methods:
updateCell() - if this edge is kept, this method links the respective cells
which it is representing together

sameRep() - determines whether or not the two nodes have the same representative
if they do, do nothing (it is automatically discarded) if they don't, link them 
together and set their representatives



# ICell - interface
We decided to use an interface for cells to take advantage of dynamic dispatch in
the case of cell borders (or the 'lines' in the maze). For cells that are linked to
borders, we want solid lines, while for cells that are linked together (with edges),
we do not want any lines at all.

methods:
edgeV() - draws a vertical line
edgeH() - draws a horizontal line

Border - class
Border class for cells, useful in rendering game state
methods:
edgeV() - see above (draws a solid vertical line)
edgeH() - see above (draws a solid horizontal line)

# Cell - class
class represnting the underlying base units of the maze

fields:
p - position of this cell
top - cell or border to the top of this cell
left - cell or border to the left of this cell
right = cell or border to the right of this cell
bottom - cell or border to the bottom of this cell

neighbors - neighboring cells in list format for easy iteration

methods:
draw() - draws the current cell state in the correct position on a provided WorldScene
link() - links this cell with another cell on the correct side, and adds it to the list 
of neighbors (important for searching algorithms)

edgeV() - see above (does nothing)
edgeH() - see above (does nothing)



# Player - class
this class is for controlling the on-screen "Player" to solve the maze

fields:
position - current player position
visited - locations the player has been
unfinished - locations the player still needs to go
map - a map for backtracking to the start (for displaying the solved path)

methods:
autoMove() - Iterates through the neighbors of the current cell, then sets the position to 
the next cell to be visited. Updates unfinished and map with each cycle. If the maze is 
completely full, teleports the player to the ending square.

draw() - displays the current state of the player and the visited squares. If the player 
is not in manual mode (e.g. it is solving using an algorithm), also display the squares
to be visited next.

reset() - resets the player to default position as well as clear the visited list, unfinished
list, and the backtracking map

move() - moves the player manually by x amount or y amount. If not a legal move, does nothing.

won() - checks if the player has completed the maze.
