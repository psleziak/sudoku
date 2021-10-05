/**
 * Class that solves the Asterisk Sudoku.
 * Prints the number of solutions of a Sudoku if there are multiple. If there is only a single solution, prints this solution instead.
 *
 * by <<TODO YOUR NAME AND ID HERE>>
 * and <<TODO YOUR PARTNERS NAME AND ID HERE>>
 * as group <<TODO GROUP NUMBER HERE>>
 */

import java.util.Arrays;

class SudokuSolver {

    int SUDOKU_SIZE = 9;          // Size of the grid.
    int SUDOKU_MIN_NUMBER = 1;    // Minimum digit to be filled in.
    int SUDOKU_MAX_NUMBER = 9;    // Maximum digit to be filled in.
    int SUDOKU_BOX_DIMENSION = 3; // Dimension of the boxes (sub-grids that should contain all digits).

    int[][] grid = new int[][] {  // The puzzle grid; 0 represents empty.
        { 0, 9, 0,   7, 3, 0,    4, 0, 0 },    // One solution.
        { 0, 0, 0,   0, 0, 0,    5, 0, 0 },
        { 3, 0, 0,   0, 0, 6,    0, 0, 0 },

        { 0, 0, 0,   0, 0, 2,    6, 4, 0 },
        { 0, 0, 0,   6, 5, 1,    0, 0, 0 },
        { 0, 0, 6,   9, 0, 7,    0, 0, 0 },

        { 5, 8, 0,   0, 0, 0,    0, 0, 0 },
        { 9, 0, 0,   0, 0, 3,    0, 2, 5 },
        { 6, 0, 3,   0, 0, 0,    8, 0, 0 },
      
    };

    int[][] asterisk = new int[][] { 
        {2,2},
        {1,4},
        {2,6},
        {4,4},
        {4,1},
        {4,7},
        {6,2},
        {7,4},
        {6,6} };

    int rempty = 0;
    int cempty = 0;

    int solutionCounter = 0; // Solution counter

    int[][] firstSolvedGrid = new int[SUDOKU_SIZE][SUDOKU_SIZE];

    // Is there a conflict when we fill in d at position (r, c)?
    boolean givesConflict(int r, int c, int d) {
        
        return rowConflict(r, d) || columnConflict(c, d) || boxConflict(r, c, d) || asteriskConflict(r, c, d);
    }

    // Is there a conflict when we fill in d in row r?
    boolean rowConflict(int r, int d) {
        for(int column = 0; column < SUDOKU_SIZE; column ++){
           if(grid[r][column] == d){
               return true;
           }
        }
        return false;
        
    }

    // Is there a conflict in column c when we fill in d?
    boolean columnConflict(int c, int d) {
        for(int row = 0; row < SUDOKU_SIZE; row ++){
            if(grid[row][c] == d){
                return true;
            }
        }
        return false;
    }

    // Is there a conflict in the box at (r, c) when we fill in d?
    boolean boxConflict(int r, int c, int d) {
        int boxRow = r - r%3;
        int boxColumn = c - c%3;

        for(int row = boxRow; row < boxRow+3; row ++) {
            for(int column = boxColumn; column < boxColumn+3; column ++) {
                if(grid[row][column] == d){
                    return true;
                }

            }
        }
        return false;
    }
	
	// Is there a conflict in the asterisk when we fill in d?
    boolean asteriskConflict(int r, int c, int d) {
        int[] position = new int [] {r,c};
         
        for(int[] pos:asterisk ){
            if(Arrays.equals(pos,position)){
                for(int[] elm:asterisk ){
                    if(grid[elm[0]][elm[1]] == d){
                       return true;
                    }
                }
            }
        }
        return false;
    }
	
	// Finds the next empty square (in "reading order").
    int[] findEmptySquare() {
        for(int row = rempty; row < SUDOKU_SIZE; row ++ ) {
            for (int column = cempty; column < SUDOKU_SIZE; column ++) {
                if(grid[row][column] == 0) {
                    rempty = row;
                    cempty = column;
                    return new int[]{row,column};
                }
            }

            cempty = 0;
        }      

        rempty = 0;

        return null;
    }

    void copyGrid(int[][] source, int[][] target) {
        for (int row = 0; row < SUDOKU_SIZE; row++) {
            for (int column = 0; column < SUDOKU_SIZE; column++) {
                target[row][column] = source[row][column];
            }
        }
    }

    // Find all solutions for the grid, and stores the final solution.
    boolean solved;
    void solve() {
        int [] emptyPosition = findEmptySquare();
        
        if (emptyPosition != null) {
            int row = emptyPosition[0];
            int column = emptyPosition[1];
            
            for (int d = SUDOKU_MIN_NUMBER; d <= SUDOKU_MAX_NUMBER; d++) {
                if(!givesConflict(row, column, d)) {
                    grid[row][column] = d;
                    solved = true;
                    solve();
                    if(solved){
                        return;
                    } else {
                        grid[row][column] = 0;
                        rempty = row;
                        cempty = column;
                    }
                }
            }
            solved = false;
        } else if (solved) {
            solutionCounter++;

            if (solutionCounter == 1) {
                copyGrid(grid, firstSolvedGrid);
            }
            
            solved = false;
        }
    }
        
    
    // Print the sudoku grid.
    void print() {

        for (int j = 0; j < SUDOKU_SIZE; j++) {

            if ( j%3 == 0 ) {
                System.out.print("+-----------------+"); 
                System.out.println();
            } 

            for(int i = 0; i < SUDOKU_SIZE; i++) {

                if ( i%3 == 0 ) {
                    System.out.print("|"); 
                }

                if((j == 4 && (i == 1 || i == 4 || i == 7)) || (( i == 4 )&& (j == 1 || j == 4 || j == 7))){
                    System.out.print(">"+grid[j][i]+"<"); 
                } else if (( j == 2 || j == 6) && i ==1){
                    System.out.print(" "+grid[j][i]+ ">"); 
                } else if (( j == 2 || j == 6) && i ==7) {
                    System.out.print("<"+grid[j][i]+ " "); 
                } else if (i == 1 || i == 4 || i == 7) {
                    System.out.print(" "+grid[j][i]+" "); 
                } else {
                    System.out.print(grid[j][i]); 
                }
            }

            System.out.print("|"); 
            System.out.println();
        }

        System.out.println("+-----------------+"); 
    }


    // Run the actual solver.
    void solveIt() {
        solve();

        if (solutionCounter == 1) {
            copyGrid(firstSolvedGrid, grid);
            print();
        } else if (solutionCounter > 1) {
            System.out.println(solutionCounter); 
        }       
        
    }

    public static void main(String[] args) {
        (new SudokuSolver()).solveIt();
    }
}

