package jdeps;

import java.util.Arrays;

public class ValidOrdering {
  private final IVertex[] ordering;

  public ValidOrdering(IVertex...vertices) {
    this.ordering = vertices;
  }

  public ValidOrdering(String...vertices) {
    IVertex[] ordering = new IVertex[vertices.length];
    for(int i = 0; i < vertices.length; ++i) {
      ordering[i] = StringVertex.from(vertices[i]);
    }
    this.ordering = ordering;
  }

  public ValidOrdering(Number...vertices) {
    IVertex[] ordering = new IVertex[vertices.length];
    for(int i = 0; i < vertices.length; ++i) {
      ordering[i] = NumberVertex.from(vertices[i]);
    }
    this.ordering = ordering;
  }

  public IVertex[] getOrdering() {
    return ordering;
  }

  @Override
  public String toString() {
    return "" + (ordering == null ? null : Arrays.asList(ordering));
  }

  public static ValidOrdering build(IVertex...vertices) {
    return new ValidOrdering(vertices);
  }

  public static ValidOrdering buildFromStrings(String...vertices) {
    return new ValidOrdering(vertices);
  }

  public static ValidOrdering buildFromNumbers(Number...vertices) {
    return new ValidOrdering(vertices);
  }

  public boolean matches(IVertex[] vertices) {
    if (ordering.length != vertices.length)
      return false;

    for(int i = 0; i < ordering.length; ++i) {
      if (!ordering[i].equals(vertices[i]))
        return false;
    }

    return true;
  }
}
