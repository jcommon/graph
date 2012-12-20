package jdeps;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Please see the following for a description of this algorithm:
 *   http://www.cs.washington.edu/education/courses/cse373/02sp/lectures/cse373-21-TopoSort-4up.pdf
 */
public final class SimpleTopologicalSort implements ITopologicalSortStrategy {
  @Override
  public IDependency[] sort(IAdjacencyList adjacencyList) throws CyclicGraphException {
    if (adjacencyList.isEmpty())
      return IDependencyGraph.EMPTY_DEPENDENCIES;

    int ordered_index = 0;
    IDependency[] ordered = new IDependency[adjacencyList.size()];

    Queue<IAdjacencyListPair> queue = new LinkedList<IAdjacencyListPair>();
    int[] in_degrees = adjacencyList.calculateInDegrees();

    //Find all vertices who have an in-degree of zero.
    for(int i = 0; i < in_degrees.length; ++i) {
      if (in_degrees[i] == 0)
        queue.add(adjacencyList.get(i));
    }

    //If there are no vertices with an in-degree of zero (meaning they have no one pointing to them),
    //then this is not a DAG (directed acyclic graph).
    if (queue.isEmpty())
      throw new CyclicGraphException("Cycle detected when topologically sorting the graph");

    IAdjacencyListPair p;
    while((p = queue.poll()) != null) {
      ordered[ordered_index++] = p.getValue();

      for(IDependency d: p.getDependencies()) {
        int index = adjacencyList.indexOf(d);
        //Enqueue any vertex whose in-degree will become zero
        if (in_degrees[index] == 1)
          queue.add(adjacencyList.get(index));
        --in_degrees[index];
      }
    }

    //If we haven't filled the array, then there's a cycle somewhere.
    if (ordered_index != ordered.length)
      throw new CyclicGraphException("Cycle detected when topologically sorting the graph");

    return ordered;
  }
}
