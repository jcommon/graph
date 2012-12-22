package jdeps;

/**
 * A callback for when an error has occurred during asynchronous processing.
 */
public interface ITopologicalSortErrorCallback<TVertex extends IVertex> {
  /**
   * The callback that will be executed inside a thread pool other than the
   * one invoking the sort.
   *
   * @param vertex Instance of {@link IVertex} of type <code>TVertex</code> that produced the error. Could be null.
   * @param t Exception that was thrown.
   * @param coordinator Instance of {@link ITopologicalSortCoordinator} that allows for communication between
   *                    asynchronous sorting submissions done by the driver (which is typically done by an instance
   *                    of {@link ITopologicalSortStrategy}).
   */
  void handleError(TVertex vertex, Throwable t, ITopologicalSortCoordinator coordinator);
}
