package jdeps;

import org.junit.Test;

import static org.junit.Assert.*;

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
      public void handle(IVertex dependency) throws Throwable {
      }
    };

    final ITopologicalSortCallback<IVertex> CALLBACK_DEBUG = new ITopologicalSortCallback<IVertex>() {
      @Override
      public void handle(IVertex dependency) throws Throwable {
        System.out.println(dependency.toString());
        Thread.sleep(1000);
      }
    };

    for(IGraph g : Examples.ALL_VALID_GRAPHS) {
      assertTrue(g.sortAsync(CALLBACK_NOOP).waitForCompletion());
    }

    for(IGraph g : Examples.ALL_GRAPHS_WITH_CYCLES) {
      assertFalse(g.sortAsync(CALLBACK_NOOP).waitForCompletion());
    }
  }
}
