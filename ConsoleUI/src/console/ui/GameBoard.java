package console.ui;


import game.pack.Tuple;
import java.util.Formatter;
import java.util.HashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GameBoard {
    private int width;
    private int length;
    private HashMap<Tuple<Integer,Integer>,String> b;

    public GameBoard(int width,int length, HashMap<Tuple<Integer,Integer>,String> b){
        this.width=width;
        this.length=length;
        this.b=b;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Formatter fmt = new Formatter(sb);
        sb.append("  ");
        Stream.generate(() -> "+").limit(width+1).forEach(c -> fmt.format("%-4s",c));

        IntStream.range(0, length).forEach(r -> {
            sb.append(System.lineSeparator());
            fmt.format("%-2c",(char ) ('A'+length-1)-r);
            IntStream.range(0, width).forEach(c -> {
                String cell = b.get(new Tuple(r,c));
                fmt.format("%-4s",cell == null ? "|___" : "|_"+cell+"_");


            });
            sb.append("|");

        });
        sb.append(System.lineSeparator()).append("    ");

        IntStream.range(0, width).forEach(c -> fmt.format("%-4c",(char ) c+'A'));
        sb.append(System.lineSeparator());
        return sb.toString();

        }
    }

