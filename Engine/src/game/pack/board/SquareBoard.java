package game.pack.board;


import game.pack.Tuple;
import game.pack.Variant;

import java.util.Arrays;

public class SquareBoard {
    private int width;
    private int height;
    private int toolsCount=0;
    private Cell [][] board;
    private Variant variant;

    public SquareBoard(int width, int height, Variant variant){
        this.width=width;
        this.height=height;
        this.variant=variant;

        board = new Cell[height][];
        for (int h = 0; h < height; h++) {
            Arrays.fill(board[h] = new Cell[width], Cell.EMPTY);
        }
    }

    public void setEmptyCell(Tuple<Integer,Integer> pos){
        board[pos.x][pos.y]=Cell.EMPTY;
    }

    public boolean full(){
        return width*height == toolsCount;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Cell getCell(int row, int col){
        return board[row][col];
    }

    public Tuple<Integer,Integer> placeTool(String col){

        try {
            if (col.length() > 1){
                throw new NumberFormatException();
            }

            int c=col.toUpperCase().charAt(0)-'A';
            int i=-1;

            while (++i < board.length && board[i][c] == Cell.EMPTY ){}

            i--;
            if (0<=i && i<board[0].length){
                board[i][c]= Cell.FULL;
                toolsCount++;
                return new Tuple(i,c);
            }
            else {

                System.out.println("Column " + col.toUpperCase().charAt(0)
                        + " is full, Please choose a new column");
                return null;
            }
        }
        catch (IndexOutOfBoundsException | NumberFormatException ex) {
            System.out.println("Column not exit, Please choose new Column [A-" + (char) ('A'+board[0].length-1) + "]");
            return null;
        }
    }
}
