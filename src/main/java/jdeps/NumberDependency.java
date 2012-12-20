package jdeps;

public class NumberDependency implements IDependency {
  private final Number value;

  public NumberDependency(Number value) {
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

    NumberDependency that = (NumberDependency) o;

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

  public static IDependency from(Number name) {
    return new NumberDependency(name);
  }
}
