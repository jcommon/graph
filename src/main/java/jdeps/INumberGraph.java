package jdeps;

public interface INumberGraph extends IGraph {
  INumberGraph copyAsNumberGraph();

  INumberGraph addVertex(Number vertex);
  INumberGraph removeVertex(Number vertex);
  INumberGraph addEdge(Number from, Number to);
  INumberGraph removeEdge(Number from, Number to);
}
