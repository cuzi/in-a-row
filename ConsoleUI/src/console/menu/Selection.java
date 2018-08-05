package console.menu;

import console.menu.exceptions.IllegalSelection;

import java.util.Map;
import java.util.HashMap;


public enum Selection {

    TITLE("* NinARow Menu *",0,true),
    UPLOAD("Upload game configuration file", 1, true),
    START("Start game", 2, false),
    DETAILS("Print game details", 3, false),
    PLAY("Play my turn", 4, false),
    HISTORY("Print game history", 5, false),
    EXIT("Exit game", 6, true),
    UNDO("Undo last move", 7, false);

    private String action;
    private int selectionNo;
    private boolean visible;
    private static Map<Integer, Selection> map = new HashMap<>();

    static {
        for (Selection sEnum : Selection.values()) {
            map.put(sEnum.selectionNo, sEnum);
        }
    }

    public static Selection Choose(String userSelect) throws IllegalSelection {
        try
        {
            int sNo = Integer.parseInt(userSelect);
            Selection s = Selection.valueOf(sNo);
            if (s == null){
                throw new IllegalSelection("Illegal selection. Please select an Action from ui");
            }
            return s;
        }
        catch (IllegalArgumentException ex){
            throw new IllegalSelection("Illegal selection. Please enter a Number between 1-6");
        }
    }
    private static Selection valueOf(int sNo) {
        return map.get(sNo);
    }

    public void hide(){visible=false;}

    public void show(){visible=true;}

    public boolean isVisible() {return visible; }

    public int getNo(){return selectionNo;}

    Selection(String action, int SelectionNo, boolean visible) {
        this.action = action;
        this.selectionNo = SelectionNo;
        this.visible = visible;
    }

    @Override
    public String toString() {
        return action;
    }

}