package game.pack.tools;

import java.util.Scanner;

public class HumanPlayer extends Player{
    private String name;

    public HumanPlayer(String name){
        super();
        this.name=name;

    }


    @Override
    public Type getType() {
        return Type.HUMAN;
    }

    @Override
    public String getMove(Object... args) {
        return getMove();
    }

    @Override
    public String getMove() {
        System.out.println("Please select column to drop into:");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @Override
    public String toString() {
        return "Player: " + name;
    }
}
