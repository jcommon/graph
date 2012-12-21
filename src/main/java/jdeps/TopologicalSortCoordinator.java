package jdeps;

/**
 * @see ITopologicalSortCoordinator
 */
final class TopologicalSortCoordinator implements ITopologicalSortCoordinator {
  private final ITopologicalSortAsyncResult asyncResult;

  public TopologicalSortCoordinator(ITopologicalSortAsyncResult asyncResult) {
    this.asyncResult = asyncResult;
  }

  @Override
  public boolean discontinueScheduling() {
    return asyncResult.discontinueScheduling();
  }
}
