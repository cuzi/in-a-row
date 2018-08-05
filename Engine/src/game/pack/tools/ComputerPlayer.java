package game.pack.tools;

import java.util.Random;

public class ComputerPlayer extends Player{

    private String name = "Computer";
    public ComputerPlayer(){
        super();
    }

    @Override
    public Type getType() {
        return Type.HUMAN;
    }

    @Override
    public String toString() {
        return "Player: " + name;
    }

    @Override
    public String getMove() {
        return "A";
    }

    public String getMove(Object... args) {
        try{
            System.out.println("Thinking ... \n");
            Thread.sleep(1000);
            int w = (int) args[0];
            Random rnd = new Random();
            int i = rnd.nextInt(w);
            return String.valueOf((char)('A'+i));
        }
    catch (InterruptedException ex){
            return "A";
    }
    }


}


