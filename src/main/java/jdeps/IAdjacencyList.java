package jdeps;

import java.util.List;

/**
 * An adjacency list is essentially an array of size n where A[i] is the list of out-neighbors of node i.
 *
 * Please see http://www.cs.cmu.edu/~avrim/451f08/lectures/lect1002.pdf
 */
public interface IAdjacencyList<TVertex extends IVertex> extends Iterable<IAdjacencyListPair<TVertex>> {
  boolean isEmpty();
  List<TVertex> get(TVertex vertex);
  IAdjacencyListPair<TVertex> get(int index);
  int indexOf(TVertex vertex);
  int[] calculateInDegrees();
  int size();
}
