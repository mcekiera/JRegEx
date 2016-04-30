package View.Observer;

public interface Observed {
    void notifyObservers();
    void addObserver(Observer observer);
}
