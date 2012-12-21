package jdeps;

/**
 * Associates two values together. Effectively a tuple of arity 2.
 *
 * @param <TValue1> Type associated with the first value.
 * @param <TValue2> Type associated with the second value.
 */
public class Pair<TValue1, TValue2> {
  private final TValue1 value1;
  private final TValue2 value2;

  /**
   * Creates a new instance of {@link Pair}.
   *
   * @param value1 The first value.
   * @param value2 The second value.
   */
  public Pair(TValue1 value1, TValue2 value2) {
    this.value1 = value1;
    this.value2 = value2;
  }

  /**
   * Retrieves a reference to the first value provided.
   *
   * @return A reference to the first value.
   */
  public TValue1 getValue1() {
    return value1;
  }

  /**
   * Retrieves a reference to the second value provided.
   *
   * @return A reference to the second value.
   */
  public TValue2 getValue2() {
    return value2;
  }

  /**
   * @see Object#equals(Object)
   *
   * @param obj The reference object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;

    Pair pair = (Pair) obj;

    if (value1 != null ? !value1.equals(pair.value1) : pair.value1 != null) return false;
    if (value2 != null ? !value2.equals(pair.value2) : pair.value2 != null) return false;

    return true;
  }

  /**
   * @see Object#hashCode()
   *
   * @return A hash code value for the {@link Pair}.
   */
  @Override
  public int hashCode() {
    int result = value1 != null ? value1.hashCode() : 0;
    result = 31 * result + (value2 != null ? value2.hashCode() : 0);
    return result;
  }

  /**
   * @see Object#toString()
   *
   * @return A string representing the {@link Pair}.
   */
  @Override
  public String toString() {
    return "(" + (value1 != null ? value1.toString() : "null") + ", " + (value2 != null ? value2.toString() : "null") + ")";
  }
}
