import java.util.ArrayList;

public class Board  implements Comparable<Board>{
    private int[][] board;
    private Board parent;
    private int depth; //distance to initial board
    private int distance_to_final;
    private String search; //bfs; greedy-h1; greedy-h2; astar-h1; astar-h2

    /**
     * Builds the board.
     */
    public Board(int[][] board, Board parent, int depth, String search) {
        this.board = board;
        this.parent = parent;
        this.depth = depth;
        this.search = search;
        if(!this.search.equals("bfs"))
            this.calculate_distance_to_final();
    }
    /**
     * Returns the board.
     */
    public int[][] get_board() {
        return this.board;
    }

    /**
     * Returns the board's parent.
     */
    public Board get_parent() {
        return this.parent;
    }

    /**
     * Returns the board's depth.
     */
    public int get_depth() {
        return this.depth;
    }

    /**
     * Returns the board's distance to the final state.
     */
    public int get_distance_to_final() {
        return this.distance_to_final;
    }

    /**
     * Checks if board is final.
     */
    public boolean is_final() {
        boolean is_final = true;
        for(int i = 0; i < this.board.length; i++) {
            for(int j = 0; j < this.board[i].length; j++) {
                if((this.board[i][j] != 0 && this.board[i][j] != i*this.board.length + j + 1)
                        || (this.board[i][j] == 0 && (i != this.board.length-1 || j != this.board[0].length-1))) {
                    is_final = false;
                    break;
                }
            }
            if(!is_final)
                break;
        }
        return is_final;
    }

    /**
     * Checks if the board is the same as the one received as argument.
     */
    public boolean compare_board(Board b) {
        int[][] board_to_compare = b.get_board();
        for(int i = 0; i < this.board.length; i++) {
            for(int j = 0; j < this.board[i].length; j++) {
                if(board[i][j] != board_to_compare[i][j])
                    return false;
            }
        }
        return true;
    }
    /**
     * Returns copy of the board.
     */
    public int[][] copy_board() {
        int x = this.board.length;
        int y = this.board[0].length;
        int[][] new_array = new int[x][y];
        for(int i = 0; i < this.board.length; i++) {
            for(int j = 0; j < this.board[i].length; j++) {
                new_array[i][j] = board[i][j];
            }
        }
        return new_array;
    }

    /**
     * Prints board.
     */
    public void print() {
        for(int i = 0; i < this.board[0].length; i++) {
            System.out.print("----");
        }
        System.out.print("-\n");
        for(int i = 0; i < this.board.length; i++) {
            for(int j = 0; j < this.board[i].length; j++) {
                if(this.board[i][j] < 10)
                    System.out.print("| " + this.board[i][j] + " ");
                else
                    System.out.print("| " + this.board[i][j]);
                if(j == this.board[i].length - 1)
                    System.out.print("|");
            }
            System.out.println();
        }
        for(int i = 0; i < this.board[0].length; i++) {
            System.out.print("----");
        }
        System.out.print("-\n");
    }

    /**
     * Generates the successors to the board using the operators to move the element 0 in four directions: up, down,
     * left and right.
     */
    public ArrayList<Board> generate_successors() {
        ArrayList<Board> successors = new ArrayList<Board>();
        int pos_zero = this.find_zero();
        int y_zero = pos_zero / this.board.length;
        int x_zero = pos_zero % this.board.length;

        //move up
        if(y_zero != 0) {
            int[][] b = this.copy_board();
            b[y_zero][x_zero] = b[y_zero - 1][x_zero];
            b[y_zero-1][x_zero] = 0;
            Board new_board_up = new Board(b, this, this.depth+1, this.search);
            if(parent == null || (parent != null && !new_board_up.compare_board(parent)))
                successors.add(new_board_up);
        }
        //move down
        if(y_zero != this.board.length-1) {
            int[][] b = this.copy_board();
            b[y_zero][x_zero] = b[y_zero + 1][x_zero];
            b[y_zero + 1][x_zero] = 0;
            Board new_board_down = new Board(b, this, this.depth+1, this.search);
            if(parent == null || (parent != null && !new_board_down.compare_board(parent)))
                successors.add(new_board_down);
        }
        //move left
        if(x_zero != 0) {
            int[][] b = this.copy_board();
            b[y_zero][x_zero] = b[y_zero][x_zero - 1];
            b[y_zero][x_zero - 1] = 0;
            Board new_board_left = new Board(b, this, this.depth+1, this.search);
            if(parent == null || (parent != null && !new_board_left.compare_board(parent)))
                successors.add(new_board_left);
        }
        //move right
        if(x_zero != this.board[0].length-1) {
            int[][] b = this.copy_board();
            b[y_zero][x_zero] = b[y_zero][x_zero + 1];
            b[y_zero][x_zero + 1] = 0;
            Board new_board_right = new Board(b, this, this.depth+1, this.search);
            if(parent == null || (parent != null && !new_board_right.compare_board(parent)))
                successors.add(new_board_right);
        }

        return successors;
    }

    /**
     * Finds the position of the element 0 in the board.
     */
    private int find_zero() {
        int pos = 0;
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if(this.board[i][j] == 0)
                    pos = i * this.board.length + j;
            }
        }
        return pos;
    }

    /**
     * Calculates distance to the final board using the following heuristics, according to the argument search of the class.
     * h1 - number of elements out of place.
     * h2 - manhattan distances between elements out of place and their right place.
     */
    private void calculate_distance_to_final() {
        if(this.search.equals("greedy-h1") || this.search.equals("astar-h1")) {
            this.distance_to_final = 0;
            for(int i = 0; i < this.board.length; i++) {
                for(int j = 0; j < this.board[i].length; j++) {
                    if((this.board[i][j] != 0 && this.board[i][j] != i*this.board.length + j + 1)
                            || (this.board[i][j] == 0 && (i != this.board.length-1 || j != this.board[0].length-1))){
                        this.distance_to_final++;
                    }
                }
            }
        } else if(this.search.equals("greedy-h2") || this.search.equals("astar-h2")) {
            this.distance_to_final = 0;
            for(int i = 0; i < this.board.length; i++) {
                for(int j = 0; j < this.board[i].length; j++) {
                    if(this.board[i][j] == 0) {
                        this.distance_to_final += this.board.length - 1 - i + this.board[i].length - 1 - j;
                    } else if(this.board[i][j] != i*this.board.length + j + 1){
                        int expected_i = (this.board[i][j]-1) / this.board.length;
                        int expected_j = (this.board[i][j]-1) % this.board.length;
                        this.distance_to_final += java.lang.Math.abs(expected_i - i) + java.lang.Math.abs(expected_j - j);
                    }
                }
            }
        }
    }

    /**
     * Function that compares two boards using the heuristics defined and the type of search.
     */
    @Override
    public int compareTo(Board b) {
        if(this.search.equals("astar-h1") || this.search.equals("astar-h2"))
            return (this.depth+this.distance_to_final)-(b.get_depth()+b.get_distance_to_final());
        else if(this.search.equals("greedy"))
            return this.distance_to_final-b.get_distance_to_final();
        return 0;
    }
}
