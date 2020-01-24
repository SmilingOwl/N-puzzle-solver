# Solving N-Puzzle using basic AI algorithm

**N-Puzzle** or **sliding puzzle** is a popular puzzle that consists of N tiles where N can be 8, 15, 24 and so on. In our example N = 8. The puzzle is divided into sqrt(N+1) rows and sqrt(N+1) columns. Eg. 15-Puzzle will have 4 rows and 4 columns and an 8-Puzzle will have 3 rows and 3 columns. The puzzle consists of N tiles and one empty space where the tiles can be moved. Start and Goal configurations (also called state) of the puzzle are provided. The puzzle can be solved by moving the tiles one by one in the single empty space and thus achieving the Goal configuration.

![example](https://github.com/SmilingOwl/N-puzzle-solver/example.png)

The tiles in the initial(start) state can be moved in the empty space in a particular order and thus achieve the goal state.

## Uniformed Search Algorithms:
* Breadth-first search (uniform cost search in this case)

## Informed Search ALgorithms:
* A* Search
* Greedy Search 

## Heuristics:
N-Puzzle supports 2 different heuristic functions:
* H1 - Tiles out of place
* H2 - Manhattan distance (city-block distance)

