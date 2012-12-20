package jdeps;

public interface IDependencyRelationship {
  IDependency getFrom();
  IDependency getTo();
}
