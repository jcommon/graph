package jdeps;

public interface IEdge<TVertex extends IVertex> {
  TVertex getFrom();
  TVertex getTo();
}
