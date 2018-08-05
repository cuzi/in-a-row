package console.ui;

public class Timer {

    private int charsWritten ;
    private  long start;
    private long lep;
    private boolean stop=false;

    public Timer(){
        charsWritten = 0;
        start = System.currentTimeMillis();
    }

    public void pause(){
        lep=System.currentTimeMillis() - start;
        stop=true;
    }

    public void reset(){
        charsWritten = 0;
        start = System.currentTimeMillis();
        stop=false;
    }

    public void show() {


        long elapsedTime = (stop) ? lep: System.currentTimeMillis() - start;
        elapsedTime = elapsedTime / 1000;
        String seconds = Integer.toString((int) (elapsedTime % 60));
        String minutes = Integer.toString((int) ((elapsedTime % 3600) / 60));
        String hours = Integer.toString((int) (elapsedTime / 3600));

        if (seconds.length() < 2) {
            seconds = "0" + seconds; }if (minutes.length() < 2) {
            minutes = "0" + minutes; }if (hours.length() < 2) {
            hours = "0" + hours; }String writeThis = hours + ":" + minutes + ":" + seconds;

        for (int i = 0; i < charsWritten; i++) {
            System.out.print("\r");
        }
        System.out.println(writeThis + ((stop) ? " game duration" :" passed from start"));
        charsWritten = writeThis.length();

    }
}