package jdeps;

public class Pair<TValue1, TValue2> {
  private final TValue1 value1;
  private final TValue2 value2;

  public Pair(TValue1 value1, TValue2 value2) {
    this.value1 = value1;
    this.value2 = value2;
  }

  public TValue1 getValue1() {
    return value1;
  }

  public TValue2 getValue2() {
    return value2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Pair pair = (Pair) o;

    if (value1 != null ? !value1.equals(pair.value1) : pair.value1 != null) return false;
    if (value2 != null ? !value2.equals(pair.value2) : pair.value2 != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = value1 != null ? value1.hashCode() : 0;
    result = 31 * result + (value2 != null ? value2.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "(" + (value1 != null ? value1.toString() : "null") + ", " + (value2 != null ? value2.toString() : "null") + ")";
  }
}
