package jdeps;

public class StringDependency implements IDependency {
  private final String name;

  public StringDependency(String name) {
    if (name == null)
      throw new IllegalArgumentException("name cannot be null");

    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    StringDependency that = (StringDependency) o;

    if (!name.equals(that.name)) return false;

    return true;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  public static IDependency from(String name) {
    return new StringDependency(name);
  }
}
