package game.pack;

import game.pack.config.GameDescriptor;
import game.pack.exceptions.GameBoardFull;
import game.pack.exceptions.GameBoardWinner;
import game.pack.exceptions.XmlConfigurationError;
import game.pack.exceptions.XmlConfigurationMissmatch;
import game.pack.tools.GamePlayer;
import game.pack.tools.GameType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public interface Interactive {


    GamePlayer playerTurn();

    boolean playMove(String col) throws GameBoardWinner, GameBoardFull;

    int getBoardHeight();

    int getBoardWidth();

    HashMap<Tuple<Integer,Integer>,String> getBoardTools();

    ArrayList<GamePlayer> getPlayers();

    boolean gameActive();

    boolean isHumanPlay();

    int getSequence();

    ArrayList<Tuple<GamePlayer,Tuple<Integer,Integer>>> getGameHistory();

    HashMap <GamePlayer,Integer> getMovesCount();

    int getPlayerCount();

    void addPlayer(GamePlayer player);

    void undoLastMove();

    void initEngine(int width, int height, GameType t, Variant v, int target);

    int getToolsCount();

    static boolean xmlType(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1).toLowerCase().compareTo("xml") == 0;
        } catch (Exception e) {
            return false;
        }
    }

    static GameDescriptor uploadXmlConfig(File f) throws XmlConfigurationMissmatch, XmlConfigurationError {

        if(f.exists() && !f.isDirectory()) {
            if(xmlType(f)){
                try {

                    JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);
                    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    GameDescriptor gd = (GameDescriptor) jaxbUnmarshaller.unmarshal(f);
                    int rows= gd.getGame().getBoard().getRows();
                    int cols= gd.getGame().getBoard().getColumns().intValue();

                    if ((!between(rows,5,50)) ||
                            (!between(cols,6,30)) ||
                            (!(gd.getGame().getTarget().intValue() < Math.min(rows,cols)))){
                        throw new XmlConfigurationMissmatch("Xml configuration file is not configured with appropriate values");
                    }

                    return gd;

                } catch (JAXBException e) {
                    throw new XmlConfigurationError("Xml schema is broken");
                }

            }
            else {
                throw new XmlConfigurationError("Input file type is not an XML file, please enter .xml type");
            }
        }
        else {
            throw new XmlConfigurationError("Input file was not found !");
        }
    }

    static boolean between(int i, int min, int max) {
        return (i >= min && i <= max);
    }

}
