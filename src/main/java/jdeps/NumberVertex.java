package jdeps;

public class NumberVertex implements IVertex {
  private final Number value;

  public NumberVertex(Number value) {
    if (value == null)
      throw new IllegalArgumentException("value cannot be null");

    this.value = value;
  }

  public Number getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NumberVertex that = (NumberVertex) o;

    if (!value.equals(that.value)) return false;

    return true;
  }

  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  public static IVertex from(Number name) {
    return new NumberVertex(name);
  }
}
