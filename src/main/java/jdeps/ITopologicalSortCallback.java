package jdeps;

/**
 * A callback for when a dependency has been found in topological order.
 *
 * Allows for parallel dependency processing unless a dependency is found that
 * is waiting for processing of 1 or more transitive dependencies. In this
 * case, processing will not proceed until the transitive dependencies have
 * completed their work.
 */
public interface ITopologicalSortCallback<T extends IVertex> {
  /**
   * The callback that will be executed inside a thread other than the one
   * invoking the sort.
   *
   * @param dependency Instance of {@link IVertex} that can now be processed.
   * @param coordinator Instance of {@link ITopologicalSortCoordinator} that allows for communication between
   *                    asynchronous sorting submissions done by the driver (which is typically done by an instance
   *                    of {@link ITopologicalSortStrategy}).
   */
  void handle(T dependency, ITopologicalSortCoordinator coordinator) throws Throwable;
}
