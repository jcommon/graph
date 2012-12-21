package jdeps;

/**
 * An adjacency list is essentially an array of size n where A[i] is the list of out-neighbors of node i.
 *
 * Please see http://www.cs.cmu.edu/~avrim/451f08/lectures/lect1002.pdf
 */
public interface IAdjacencyList extends Iterable<IAdjacencyListPair> {
  boolean isEmpty();
  IVertex[] get(IVertex vertex);
  IAdjacencyListPair get(int index);
  int indexOf(IVertex vertex);
  int[] calculateInDegrees();
  int size();
}
