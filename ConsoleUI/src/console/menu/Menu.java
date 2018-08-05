package console.menu;

import console.menu.exceptions.IllegalSelection;

import java.util.Arrays;
import java.util.Scanner;

public class Menu {

    private Selection currentSelection;

    private Boolean select() {
        try {
            System.out.println(this);
            Scanner scanner = new Scanner(System.in);
            String userString = scanner.nextLine();
            currentSelection=Selection.Choose(userString);
            if (currentSelection.isVisible()){
                return true;
            }
            else{
                throw new IllegalSelection("Selection not exit, please choose from menu");
            }

        } catch (IllegalSelection ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }


    public Selection showMenu(boolean human){
        if (!human && false){
            return Selection.PLAY;
        }
        else{
            while (!select());
            return currentSelection;
        }
    }

    public Selection showMenu(){
        return showMenu(true);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator());
        Arrays.stream(Selection.values()).filter(p -> p.isVisible())
                .forEach(s-> sb.append((s.getNo() > 0) ? s.getNo() + ": "+ s: s).append(System.lineSeparator()));

        sb.append("Choose an action:");

        return sb.toString();
    }
}
