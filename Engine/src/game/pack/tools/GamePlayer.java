package game.pack.tools;

public interface GamePlayer {
    String name = "";

    Type getType();
    String getKey();
    String getMove(Object... args);
}
