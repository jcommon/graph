package jdeps;

import java.util.List;

/**
 * An adjacency list is essentially an array of size n where A[i] is the list of out-neighbors of node i.
 *
 * Please see http://www.cs.cmu.edu/~avrim/451f08/lectures/lect1002.pdf
 *
 * @param <TVertex> Type of {@link IVertex}.
 */
public interface IAdjacencyList<TVertex extends IVertex> extends Iterable<IAdjacencyListPair<TVertex>> {
  /**
   * Determines if the adjacency list is empty.
   *
   * @return <code>true</code> if the adjacency list is empty; <code>false</code> otherwise.
   */
  boolean isEmpty();

  /**
   * Gets the list of out-neighbors for a given vertex of type {@link TVertex}.
   *
   * @param vertex A vertex of type {@link TVertex} for whom we wish to retrieve its out-neighbors.
   * @return A list of {@link TVertex} instances who are the out-neighbors for the provided vertex argument.
   *
   * @see jdeps.IAdjacencyListPair#getOutNeighbors()
   */
  List<TVertex> outNeighborsFor(TVertex vertex);

  /**
   * Retrieves an instance of {@link IAdjacencyListPair} at the specified index.
   *
   * @param index The index in the list for which we wish to get the associated {@link IAdjacencyListPair}.
   * @return An instance of {@link IAdjacencyListPair} if found; otherwise <code>null</code>.
   *
   * @see IAdjacencyListPair
   */
  IAdjacencyListPair<TVertex> pairAt(int index);

  /**
   * Gets the list of out-neighbors for a given vertex of type {@link TVertex} at the provided index argument.
   *
   * @param index The index in the list for which we wish to get the associated out-neighbors.
   * @return A list of {@link TVertex} instances who are the out-neighbors for an instance of type {@link TVertex}
   *         at the specified index.
   *
   * @see jdeps.IAdjacencyListPair#getOutNeighbors()
   */
  List<TVertex> outNeighborsAt(int index);

  /**
   * Find the index in the {@link IAdjacencyList} at which the provided vertex argument can be found.
   *
   * @param vertex An instance of {@link IVertex} of type {@link TVertex}.
   * @return The index in the {@link IAdjacencyList} at which the provided vertex argument can be found.
   *         <code>-1</code> if the vertex cannot be found.
   */
  int indexOf(TVertex vertex);

  /**
   * Scans the {@link IAdjacencyList} and for each vertex it counts the number of other vertices referencing it.
   *
   * @return An int array of size {@link jdeps.IAdjacencyList#size()} representing the in-degrees for each vertex.
   */
  int[] calculateInDegrees();

  /**
   * The size of the {@link IAdjacencyList}. This is the same as the number of vertices in the {@link IGraph}.
   *
   * @return An int representing the size of the {@link IAdjacencyList}.
   */
  int size();
}
