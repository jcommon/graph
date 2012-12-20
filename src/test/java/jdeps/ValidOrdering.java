package jdeps;

import java.util.Arrays;

public class ValidOrdering {
  private final IDependency[] ordering;

  public ValidOrdering(IDependency...dependencies) {
    this.ordering = dependencies;
  }

  public ValidOrdering(String...dependencies) {
    IDependency[] ordering = new IDependency[dependencies.length];
    for(int i = 0; i < dependencies.length; ++i) {
      ordering[i] = StringDependency.from(dependencies[i]);
    }
    this.ordering = ordering;
  }

  public ValidOrdering(Number...dependencies) {
    IDependency[] ordering = new IDependency[dependencies.length];
    for(int i = 0; i < dependencies.length; ++i) {
      ordering[i] = NumberDependency.from(dependencies[i]);
    }
    this.ordering = ordering;
  }

  public IDependency[] getOrdering() {
    return ordering;
  }

  @Override
  public String toString() {
    return "" + (ordering == null ? null : Arrays.asList(ordering));
  }

  public static ValidOrdering build(IDependency...dependencies) {
    return new ValidOrdering(dependencies);
  }

  public static ValidOrdering buildFromStrings(String...dependencies) {
    return new ValidOrdering(dependencies);
  }

  public static ValidOrdering buildFromNumbers(Number...dependencies) {
    return new ValidOrdering(dependencies);
  }

  public boolean matches(IDependency[] dependencies) {
    if (ordering.length != dependencies.length)
      return false;

    for(int i = 0; i < ordering.length; ++i) {
      if (!ordering[i].equals(dependencies[i]))
        return false;
    }

    return true;
  }
}
