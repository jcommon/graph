package jdeps;

public interface IStringGraph extends IGraph {
  IStringGraph copyAsStringGraph();

  IStringGraph addVertex(String vertex);
  IStringGraph removeVertex(String vertex);
  IStringGraph addEdge(String from, String to);
  IStringGraph removeEdge(String from, String to);
}
