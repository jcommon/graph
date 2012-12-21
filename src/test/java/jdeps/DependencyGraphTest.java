package jdeps;

import org.junit.Test;

import java.util.concurrent.ExecutorService;

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

    final ITopologicalSortCallback<IVertex> CALLBACK_DEBUG = new ITopologicalSortCallback<IVertex>() {
      @Override
      public void handle(IVertex vertex, ITopologicalSortCoordinator coordinator) throws Throwable {
        System.out.println(vertex.toString());
        Thread.sleep(1000);
      }
    };

    //Test one-by-one execution.

    for(IGraph g : Examples.ALL_VALID_GRAPHS) {
      assertTrue(g.sortAsync(CALLBACK_NOOP).waitForCompletion());
    }

    for(IGraph g : Examples.ALL_GRAPHS_WITH_CYCLES) {
      assertFalse(g.sortAsync(CALLBACK_NOOP).waitForCompletion());
    }

    //Test multiple in-parallel sharing the same executor service.
    //ExecutorService
  }
}
