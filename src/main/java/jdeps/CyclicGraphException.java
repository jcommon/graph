package jdeps;

public class CyclicGraphException extends Exception {
  public CyclicGraphException() {
  }

  public CyclicGraphException(String message) {
    super(message);
  }

  public CyclicGraphException(String message, Throwable cause) {
    super(message, cause);
  }

  public CyclicGraphException(Throwable cause) {
    super(cause);
  }
}
