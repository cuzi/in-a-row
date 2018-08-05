package game.pack.tools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class Player implements GamePlayer {
    static Set<String> BoardTool;

    private String tool_id;

    static {
        loadTools();
    }

    public Player(){
        tool_id=getRamomTool();
    }

    public static void loadTools(){
         BoardTool= new HashSet<>(Arrays.asList("X", "O","#","@"));
    }

    static String getRamomTool(){
        Random rnd = new Random();
        int i = rnd.nextInt(BoardTool.size());
        Object e = BoardTool.toArray()[i];
        BoardTool.remove(e);
        return (String) e;
    }

    public String getKey(){return tool_id;}

    public abstract String getMove();

}
