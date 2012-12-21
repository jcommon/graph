package jdeps;

/**
 * A callback for when an error has occurred during asynchronous processing.
 */
public interface ITopologicalSortErrorCallback<T extends IVertex> {
  /**
   * The callback that will be executed inside a thread pool other than the
   * one invoking the sort.
   *
   * @param t Exception that was thrown.
   * @param dependency Instance of {@link T} that produced the error. Could be null.
   */
  void handleError(Throwable t, T dependency);
}
