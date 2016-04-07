package View;

public interface Observed {
    void notifyObservers();
    void addObserver(Observer observer);
}
