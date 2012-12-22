package jdeps;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class DependencyGraphTest {
  @Test
  public void testAllSolutions() throws CyclicGraphException {
    for(Solution solution : Examples.ALL_SOLUTIONS)
      solution.checkSort();
  }

  @Test
  public void testAsync() {
    final ITopologicalSortCallback<IVertex> CALLBACK_NOOP = new ITopologicalSortCallback<IVertex>() {
      @Override
      public void handle(IVertex vertex, ITopologicalSortCoordinator coordinator) throws Throwable {
      }
    };

    final ITopologicalSortErrorCallback<IVertex> ERROR_CALLBACK_NOOP = new ITopologicalSortErrorCallback<IVertex>() {
      @Override
      public void handleError(IVertex vertex, Throwable t, ITopologicalSortCoordinator coordinator) {
      }
    };

    final ITopologicalSortCallback<IVertex> CALLBACK_DEBUG = new ITopologicalSortCallback<IVertex>() {
      @Override
      public void handle(IVertex vertex, ITopologicalSortCoordinator coordinator) throws Throwable {
        System.out.println(Thread.currentThread().getName() + ": " + vertex.toString());
        Thread.sleep(1000);
      }
    };

    final ITopologicalSortErrorCallback<IVertex> ERROR_CALLBACK_DEBUG = new ITopologicalSortErrorCallback<IVertex>() {
      @Override
      public void handleError(IVertex vertex, Throwable t, ITopologicalSortCoordinator coordinator) {
        System.err.println(Thread.currentThread().getName() + ": " + t.getMessage());
      }
    };

    //Test one-by-one execution.

    for(IGraph g : Examples.ALL_VALID_GRAPHS) {
      assertTrue(g.sortAsync(CALLBACK_NOOP, ERROR_CALLBACK_NOOP).waitForCompletion());
    }

    for(IGraph g : Examples.ALL_GRAPHS_WITH_CYCLES) {
      assertFalse(g.sortAsync(CALLBACK_NOOP, ERROR_CALLBACK_NOOP).waitForCompletion());
    }

    //Test multiple in-parallel sharing the same executor service.
    ExecutorService executor = Executors.newFixedThreadPool(6);
    ITopologicalSortAsyncResult[] results = new ITopologicalSortAsyncResult[Examples.ALL_VALID_GRAPHS.length];
    for(int i = 0; i < results.length; ++i)
      results[i] = Examples.ALL_VALID_GRAPHS[i].sortAsync(executor, CALLBACK_NOOP, ERROR_CALLBACK_NOOP);
    assertTrue(allSuccessfullyCompleted(results));
    executor.shutdownNow();
  }

  private boolean allSuccessfullyCompleted(ITopologicalSortAsyncResult[] results) {
    for(ITopologicalSortAsyncResult result : results) {
      if (!result.waitForCompletion())
        return false;
    }
    return true;
  }
}
