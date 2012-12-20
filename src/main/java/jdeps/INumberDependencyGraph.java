package jdeps;

public interface INumberDependencyGraph extends IDependencyGraph {
  INumberDependencyGraph copyAsNumberGraph();

  INumberDependencyGraph addDependency(Number dependency);
  INumberDependencyGraph removeDependency(Number dependency);
  INumberDependencyGraph addRelationship(Number from, Number to);
  INumberDependencyGraph removeRelationship(Number from, Number to);
}
