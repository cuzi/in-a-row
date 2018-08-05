package game.pack;

import game.pack.board.Cell;
import game.pack.board.SquareBoard;
import game.pack.exceptions.GameBoardFull;
import game.pack.exceptions.GameBoardWinner;
import game.pack.tools.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class EngineDescriptor implements Interactive{

    private SquareBoard squareBoard;
    private ArrayList<GamePlayer> p = new ArrayList<>();
    private ArrayList<Tuple<GamePlayer,Tuple<Integer,Integer>>> history;
    private HashMap <Tuple<Integer,Integer>,String> tools;
    private int turn;
    private boolean gameOn=false;
    private GameType gameType;
    private int sequenceTarget;
    private HashMap <GamePlayer,Integer> movesCount;

    public ArrayList<Tuple<GamePlayer,Tuple<Integer,Integer>>> getGameHistory(){
        return history;
    }

    public void initEngine(int width, int height, GameType t, Variant v, int target){

        startGame();

        gameType=t;
        squareBoard= new SquareBoard(width, height, v);
        history =  new ArrayList<>();
        tools= new HashMap<>();
        turn=new Random().nextInt(p.size() -1);
        movesCount = new HashMap<>();
        p.forEach(_p-> movesCount.put(_p,0));
        sequenceTarget=target;
    }

    public int getSequence(){
        return sequenceTarget;
    }

    public int getToolsCount(){return tools.size();}

    public int getPlayerCount(){
        return p.size();
    }

    public void addPlayer(GamePlayer player){
        p.add(player);

    }

    public boolean isHumanPlay() {
        return  (p == null || !gameOn || p.get(turn).getClass() == HumanPlayer.class);
    }

    public HashMap <Tuple<Integer,Integer>,String> getBoardTools() {
        return tools;
    }

    public boolean playMove(String col) throws GameBoardWinner, GameBoardFull{

        Tuple<Integer,Integer> pos = squareBoard.placeTool(col);
        if(pos == null){
            return false;
        }
        else {
            history.add(new Tuple(playerTurn(),pos));
            tools.put(pos,playerTurn().getKey());
            movesCount.put(playerTurn(),movesCount.get(playerTurn()) + 1);

            if(winner(pos)){
                gameOn=false;
                throw new GameBoardWinner(playerTurn() + " Is the WINNER !");
            }

            if (squareBoard.full()){
                gameOn=false;
                throw new GameBoardFull("Game over, no winner.");
            }
            nextTurn();
            return true;
        }

    }

    public HashMap <GamePlayer,Integer> getMovesCount(){return movesCount;}

    private void startGame(){
        gameOn=true;
        Player.loadTools();
    }

    public boolean gameActive() {
        return gameOn;
    }

    public int getBoardHeight(){
        return squareBoard.getHeight();
    }

    public int getBoardWidth(){
        return squareBoard.getWidth();
    }

    public boolean winner(Tuple<Integer,Integer> pos){
        return winnerCheck(pos, playerTurn().getKey());
    }

    private boolean winnerCheck(Tuple<Integer,Integer> pos, String tool){
        StringBuilder sb = new StringBuilder(sequenceTarget);
        Stream.generate(() -> tool).limit(sequenceTarget).forEach(c -> sb.append(c));
        String streak = sb.toString();

        return contains(horizontal(pos), streak) ||
                contains(vertical(pos), streak) ||
                contains(slashDiagonal(pos), streak) ||
                contains(backslashDiagonal(pos), streak);

    }

    private String horizontal(Tuple<Integer,Integer> pos) {
        StringBuilder sb = new StringBuilder(squareBoard.getWidth());
        IntStream.range(0, squareBoard.getWidth()).forEach(i -> {
        if (squareBoard.getCell(pos.x, i) == Cell.FULL) {
            sb.append(tools.get(new Tuple<>(pos.x,i)));
        }
        else {
            sb.append("_");
        }

        });

        return sb.toString();
    }

    private String vertical(Tuple<Integer,Integer> pos) {
        StringBuilder sb = new StringBuilder(squareBoard.getWidth());
        IntStream.range(0, squareBoard.getHeight()).forEach(i -> {
            if (squareBoard.getCell(i, pos.y) == Cell.FULL) {
                sb.append(tools.get(new Tuple<>(i, pos.y)));
            }
            else {
                sb.append("_");
            }

        });

        return sb.toString();
    }

    private String slashDiagonal(Tuple<Integer,Integer> pos) {
        StringBuilder sb = new StringBuilder(squareBoard.getHeight());
        IntStream.range(0, squareBoard.getHeight()).forEach(i -> {
            int w = pos.y + pos.x - i;
            if (0 <= w && w < squareBoard.getWidth()) {
                if (squareBoard.getCell(i, w) == Cell.FULL) {
                    sb.append(tools.get(new Tuple<>(i, w)));
                } else {
                    sb.append("_");
                }
            }
        });
        return sb.toString();
    }

    private String backslashDiagonal(Tuple<Integer,Integer> pos) {
        StringBuilder sb = new StringBuilder(squareBoard.getHeight());
        IntStream.range(0, squareBoard.getHeight()).forEach(i -> {
            int w = pos.y - pos.x + i;
            if (0 <= w && w < squareBoard.getWidth()) {
                if (squareBoard.getCell(i, w) == Cell.FULL) {
                    sb.append(tools.get(new Tuple<>(i, w)));
                } else {
                    sb.append("_");
                }
            }
        });
        return sb.toString();
    }

    private static boolean contains(String haystack, String needle) {
        return haystack.indexOf(needle) >= 0;
    }

    private void nextTurn() {
        turn = (turn+1) % p.size();
    }

    public ArrayList<GamePlayer> getPlayers(){return p;}

    public GamePlayer playerTurn(){
        return p.get(turn);
    }

    public void undoLastMove(){
        Tuple<Integer,Integer> pos = history.get(history.size()-1).y;
        history.remove(history.size()-1);
        tools.remove(pos);
        squareBoard.setEmptyCell(pos);
        nextTurn();
    }

}

