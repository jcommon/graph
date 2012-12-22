package jdeps.impl;

import jdeps.ITopologicalSortAsyncResult;
import jdeps.ITopologicalSortCoordinator;

/**
 * @see jdeps.ITopologicalSortCoordinator
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
