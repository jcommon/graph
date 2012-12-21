package jdeps;

/**
 * Provides a means for communication with the asynchronous sorting driver (which is typically done by an
 * instance of {@link ITopologicalSortStrategy}).
 */
public interface ITopologicalSortCoordinator {
  /**
   * Requests the driver (which is typically done by an instance of {@link ITopologicalSortStrategy}) to stop
   * further processing. Any remaining submissions will be drained out as they complete.
   *
   * @return True if the operation was successful, otherwise false.
   */
  boolean discontinueScheduling();
}
