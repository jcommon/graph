/*
  Copyright (C) 2012-2013 the original author or authors.

  See the LICENSE.txt file distributed with this work for additional
  information regarding copyright ownership.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package jcommon.graph;

/**
 * A representation of an {@link IVertex} instance using {@link Number}s.
 *
 * @see IVertex
 */
public class NumberVertex implements IVertex {
  private final Number value;

  /**
   * Instantiates a new instance of {@link NumberVertex}.
   *
   * @param value The value which this {@link NumberVertex} will represent.
   */
  public NumberVertex(final Number value) {
    if (value == null)
      throw new IllegalArgumentException("value cannot be null");

    this.value = value;
  }

  /**
   * Retrieves the value that this {@link NumberVertex} represents.
   *
   * @return An instance of a {@link Number} that this {@link NumberVertex} represents.
   */
  public Number get() {
    return getValue();
  }

  /**
   * Retrieves the value that this {@link NumberVertex} represents.
   *
   * @return An instance of a {@link Number} that this {@link NumberVertex} represents.
   */
  public Number getValue() {
    return value;
  }

  /**
   * @see Object#equals(Object)
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final NumberVertex that = (NumberVertex) o;

    if (!value.equals(that.value)) return false;

    return true;
  }

  /**
   * @see Object#toString()
   */
  @Override
  public String toString() {
    return value.toString();
  }

  /**
   * @see Object#hashCode()
   */
  @Override
  public int hashCode() {
    return value.hashCode();
  }

  /**
   * Convenience method for instantiating a new {@link NumberVertex}.
   *
   * @param value The value which this {@link NumberVertex} will represent.
   * @return A new instance of {@link NumberVertex}.
   */
  public static NumberVertex from(final Number value) {
    return new NumberVertex(value);
  }
}
