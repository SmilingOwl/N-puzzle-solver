import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Solver {
    private ArrayList<Board> boards_to_expand;
    private String search;
    private long time;
    private int num_nodes;

    /**
     * Asks the user for a board and a type of search used to solve the problem and calls the class N_Puzzle_Solver that
     * solves the problem.
     */
    public static void main(String[] args) {
        int[][] board1 = {{1,2,3}, {5,0,6}, {4,7,8}};
        int[][] board2 = {{1,3,6}, {5,2,0}, {4,7,8}};
        int[][] board3 = {{1,6,2}, {5,7,3}, {0,4,8}};
        int[][] board4 = {{5,1,3,4}, {2,0,7,8}, {10,6,11,12}, {9,13,14,15}};

        System.out.println("N-Puzzle Solver\n");
        System.out.println("Choose a number from 1 to 4 representing the board, that will be used to discover the solution\n");

        int[][] initial_board = null;

        switch(Solver.getOption(1, 4)) {
            case 1: initial_board = board1; break;
            case 2: initial_board = board2; break;
            case 3: initial_board = board3; break;
            default: initial_board = board4;
        }

        System.out.println("Choose the algorithm to use from the following: ");
        System.out.println(" 1 - BFS");
        System.out.println(" 2 - Greedy-H1");
        System.out.println(" 3 - Greedy-H2");
        System.out.println(" 4 - A*-H1");
        System.out.println(" 5 - A*-H2");

        String search = null;

        switch(Solver.getOption(1, 5)) {
            case 1: search = "bfs"; break;
            case 2: search = "greedy-h1"; break;
            case 3: search = "greedy-h2"; break;
            case 4: search = "astar-h1"; break;
            default: search = "astar-h2";
        }

        new Solver(initial_board, search);
    }
    /**
     * Builds the solver, putting the initial board in the list of boards to expand and calling the functions to solve
     * the problem.
     */
    public Solver(int[][] initial_board, String search) {
        this.search = search;
        this.boards_to_expand = new ArrayList<Board>();
        Board initial = new Board(initial_board, null, 0, this.search);
        this.boards_to_expand.add(initial);
        this.num_nodes = 0;
        ArrayList<Board> sequence = null;

        if(this.search.equals("bfs"))
            sequence = generate_sequence(solve_bfs());
        else sequence = generate_sequence(solve());
        for(int i = 0; i < sequence.size(); i++) {
            sequence.get(i).print();
        }

        System.out.println("Number of movements: " + sequence.size());
        System.out.println("Number of states visited: " + this.num_nodes);
        System.out.println("Time needed: " + this.time/1000F + " seconds");
    }

    /**
     * Gets user input.
     */
    public static int getOption(int min, int max) {
        int option = 0;
        do {
            try {
                System.out.println("Option:");
                Scanner sc = new Scanner(System.in);
                option = sc.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid Input");
                continue;
            }
            if(option < min || option > max){
                System.out.println("Invalid Option");
            }
        } while (option < min || option > max);
        return option;
    }

    /**
     * Receives a final board and returns the sequence of boards it took to reach it since the initial board.
     */
    private ArrayList<Board> generate_sequence(Board final_board){
        ArrayList<Board> sequence = new ArrayList<>();
        Board board = final_board;

        while(board != null) {
            sequence.add(0, board);
            board = board.get_parent();
        }

        return sequence;
    }
    /**
     * Solves problem using bfs algorithm. Item b) from Activity 2a.
     */
    private Board solve_bfs() {
        long start = System.currentTimeMillis();

        while(!boards_to_expand.isEmpty()) {
            Board parent = boards_to_expand.get(0);
            this.num_nodes++;
            ArrayList<Board> successors = parent.generate_successors();
            for(int i = 0; i < successors.size(); i++) {

                if(successors.get(i).is_final()) {
                    this.time = System.currentTimeMillis() - start;
                    return successors.get(i);
                }

                boards_to_expand.add(successors.get(i));
            }
            boards_to_expand.remove(0);
        }
        this.time = System.currentTimeMillis() - start;
        return null;
    }
    /**
     * Solves problem using a* and greedy algorithms. Item c) from Activity 2a.
     */
    private Board solve() {
        long start = System.currentTimeMillis();
        Board best_solution = null;
        int best_solution_price = 1000;

        while(!boards_to_expand.isEmpty()) {
            Board parent = boards_to_expand.get(0);
            this.num_nodes++;
            ArrayList<Board> successors = parent.generate_successors();

            for(int i = 0; i < successors.size(); i++) {
                if(successors.get(i).is_final()) {
                    this.time = System.currentTimeMillis() - start;
                    if(successors.get(i).get_depth() < best_solution_price) {
                        best_solution = successors.get(i);
                        best_solution_price = successors.get(i).get_depth();
                    }
                } else if(best_solution == null || (best_solution != null
                        && successors.get(i).get_depth() < best_solution_price)){
                    boards_to_expand.add(successors.get(i));
                }

            }
            boards_to_expand.remove(0);
            Collections.sort(boards_to_expand);
        }
        this.time = System.currentTimeMillis() - start;
        return best_solution;
    }
}
