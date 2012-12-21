package jdeps;

public class StringVertex implements IVertex {
  private final String name;

  public StringVertex(String name) {
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

    StringVertex that = (StringVertex) o;

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

  public static IVertex from(String name) {
    return new StringVertex(name);
  }
}
