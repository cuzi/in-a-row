package console.initiator;

import console.menu.Menu;
import console.menu.Selection;
import game.pack.EngineDescriptor;
import game.pack.Interactive;
import game.pack.Variant;
import game.pack.config.GameDescriptor;
import game.pack.exceptions.GameBoardFull;
import game.pack.exceptions.GameBoardWinner;
import game.pack.exceptions.XmlConfigurationError;
import game.pack.exceptions.XmlConfigurationMissmatch;
import game.pack.tools.*;
import console.ui.GameBoard;
import console.ui.Timer;

import javax.naming.ConfigurationException;
import java.io.File;
import java.util.Scanner;

public class App {

    private Menu m = new Menu();
    private GameBoard b;
    private Interactive ge = new EngineDescriptor();
    private GamePlayer current;
    private Timer t;
    private GameDescriptor gd;

    public void run(){
        while (true){

            switch (m.showMenu(ge.isHumanPlay())){
                case UPLOAD:
                    uploadConfiguration();
                    break;
                case START:
                    startGame();
                    break;
                case EXIT:
                    System.exit(0);
                    break;
                case PLAY:
                    play();
                    break;
                case HISTORY:
                    showHistory();
                    break;
                case DETAILS:
                    showStatistics();
                    break;
                case UNDO:
                    undoLastMove();
                    break;
            }
        }

    }

    private void uploadConfiguration(){
        System.out.println("Please enter config file full path [.xml]:");

        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        File f = new File(fileName);

        try{
            GameDescriptor gd = Interactive.uploadXmlConfig(f);
            this.gd=gd;
            Selection.START.show();
            System.out.println("Configuration uploaded successfully");
        }
        catch (XmlConfigurationError | XmlConfigurationMissmatch ex){
            System.out.println(ex.getMessage());
        }
    }


    private void setPlayer(){
        current=ge.playerTurn();
    }

    private void showHistory(){
        System.out.println("Listing Moves history:");
        ge.getGameHistory().forEach(p-> System.out.println(p.x + " -> Column " + ((char) (p.y.y + 'A'))));
    }

    private void showStatistics(){

        System.out.println(b);
        System.out.println("Game status: " + ((ge.gameActive()) ? "ON":"OFF"));
        System.out.println("Required sequence for winning: " + ge.getSequence());
        ge.getPlayers().forEach(p-> System.out.println(p + " Your tool is " + p.getKey()));
        if(t != null){
            ge.getMovesCount().entrySet().forEach(m -> System.out.println(m.getKey() + " Moves: " + m.getValue()));
            t.show(); }

    }

    public void createPlayers(){
        while (ge.getPlayerCount() < 2){
            System.out.println("Please choose player type [Computer / Human]:");
            Scanner scanner = new Scanner(System.in);
            String type = scanner.nextLine();
            try{
                switch (Type.valueOf(type.toUpperCase())){
                    case HUMAN:
                        System.out.println("Please enter player name:");
                        ge.addPlayer(new HumanPlayer(scanner.nextLine()));
                        break;
                    case COMPUTER:
                        ge.addPlayer(new ComputerPlayer());
                        break;
                }
            }
            catch (IllegalArgumentException ex){
                System.out.println("Wrong selection, please choose again");
            }


        }
    }

    private void undoLastMove(){
        ge.undoLastMove();
        System.out.println(b);
        if(ge.getToolsCount() == 0){
            Selection.UNDO.hide();
            Selection.HISTORY.hide();
        }
        setPlayer();
        System.out.println("Hey " + current + ", its your turn ! \n");

    }

    private void startGame(){
        Selection.TITLE.hide();
        Selection.START.hide();
        Selection.UPLOAD.hide();
        Selection.PLAY.show();

        createPlayers();

        ge.initEngine(gd.getGame().getBoard().getColumns().intValue(), gd.getGame().getBoard().getRows(),
                GameType.valueOf(gd.getGameType().toUpperCase()), Variant.valueOf(gd.getGame().getVariant().toUpperCase()),
                gd.getGame().getTarget().intValue());

        t= new Timer();
        b= new GameBoard(ge.getBoardWidth(),ge.getBoardHeight(), ge.getBoardTools());

        setPlayer();
        Selection.DETAILS.show();
        System.out.println(b);
        System.out.println("Ready ? Game Started !");
        ge.getPlayers().forEach(p-> System.out.println(p + " Your tool is: " + p.getKey()));
        System.out.println("\nHey " + current + ", its your turn !");
    }

    private void play(){

        try{
            String col;
            do {
                 col = current.getMove(ge.getBoardWidth());
            } while (!ge.playMove(col));

            System.out.println(b);
            if (!Selection.HISTORY.isVisible()){
                Selection.HISTORY.show();
            }

            if (!Selection.UNDO.isVisible() && ge.getMovesCount().entrySet().stream().mapToInt(kv->kv.getValue()).sum() > 0){
                Selection.UNDO.show();
            }

            setPlayer();
            System.out.println("Hey " + current + ", its your turn ! \n");

        }
        catch (GameBoardFull | GameBoardWinner ex){

            System.out.println(b);
            System.out.println(ex.getMessage());
            System.out.println("- - - - - - - - - - - - - - - - - - -");
            t.pause();
            Selection.START.show();
            Selection.PLAY.hide();

        }

    }

}
