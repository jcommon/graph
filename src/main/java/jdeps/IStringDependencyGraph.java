package jdeps;

public interface IStringDependencyGraph extends IDependencyGraph {
  IStringDependencyGraph copyAsStringGraph();

  IStringDependencyGraph addDependency(String dependency);
  IStringDependencyGraph removeDependency(String dependency);
  IStringDependencyGraph addRelationship(String from, String to);
  IStringDependencyGraph removeRelationship(String from, String to);
}
