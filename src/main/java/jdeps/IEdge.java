package jdeps;

/**
 * Represents a connection between two instances of {@link IVertex} of type {@link TVertex}.
 *
 * @param <TVertex> Type of {@link IVertex} instances that this edge will connect.
 */
public interface IEdge<TVertex extends IVertex> {
  /**
   * Retrieves a reference of {@link IVertex} of type {@link TVertex} that will begin the edge.
   *
   * @return An instance of {@link IVertex} of type {@link TVertex} that begins the edge.
   */
  TVertex getFrom();

  /**
   * Retrieves a reference of {@link IVertex} of type {@link TVertex} that is being pointed to.
   *
   * @return An instance of {@link IVertex} of type {@link TVertex} that is being pointed to.
   */
  TVertex getTo();
}
