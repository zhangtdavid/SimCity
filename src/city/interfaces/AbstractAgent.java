package city.interfaces;

public interface AbstractAgent {

    boolean runScheduler();
    String getName();
    void stateChanged();
    void print(String msg);
    void print(String msg, Throwable e);
    public void startThread();
    public void stopThread();
    public void pauseResume();

}
