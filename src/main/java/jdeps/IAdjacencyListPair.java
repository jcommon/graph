package jdeps;

import java.util.List;

public interface IAdjacencyListPair<TVertex extends IVertex> {
  TVertex getVertex();
  List<TVertex> getOutNeighbors();
}
