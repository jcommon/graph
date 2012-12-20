package jdeps;

import org.junit.Test;
import static org.junit.Assert.*;

public class DependencyGraphTest {
  @Test
  public void testAllSolutions() throws CyclicGraphException {
    for(Solution solution : Examples.ALL_SOLUTIONS)
      solution.checkSort();
  }
}
