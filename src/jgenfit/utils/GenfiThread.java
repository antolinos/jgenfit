package jgenfit.utils;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import jgenfit.utils.ThreadCompleteListener;

public class GenfiThread extends Thread {

    GenfiThread(OutputDisplayer aThis) {
        super(aThis);
    }
 
    /*
    private final Set<ThreadCompleteListener> listeners = new CopyOnWriteArraySet<ThreadCompleteListener>();
  public final void addListener(final ThreadCompleteListener listener) {
    listeners.add(listener);
  }
  public final void removeListener(final ThreadCompleteListener listener) {
    listeners.remove(listener);
  }
  private final void notifyListeners() {
    for (ThreadCompleteListener listener : listeners) {
      listener.notifyOfThreadComplete(this);
    }
  }*/
  @Override
  public final void run() {
    try {
      super.run();
      System.out.println("Finished");
    } finally {
      //notifyListeners();
    }
  }
  //public abstract void doRun();
}