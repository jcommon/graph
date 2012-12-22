package jdeps;

import java.util.List;

/**
 * Maintains a reference between a {@link IVertex} ({@link #getVertex()}) and its
 * out-neighbors ({@link #getOutNeighbors()}). Meant for use in conjunction with {@link Pair}.
 *
 * @param <TVertex> Type of {@link IVertex}.
 * @see Pair
 */
public interface IAdjacencyListPair<TVertex extends IVertex> {
  /**
   * The vertex for this pair.
   *
   * @return An instance of {@link IVertex} of type <code>TVertex</code>.
   */
  TVertex getVertex();

  /**
   * The list of out-neighbors for the vertex provided by {@link #getVertex()}.
   *
   * @return An instance of a list of {@link IVertex} of type <code>TVertex</code>.
   */
  List<TVertex> getOutNeighbors();
}
