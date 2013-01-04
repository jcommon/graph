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
 * A representation of an {@link IVertex} instance using {@link Object}s.
 *
 * @see IVertex
 */
public class ObjectVertex implements IVertex {
  private final Object value;

  /**
   * Instantiates a new instance of {@link ObjectVertex}.
   *
   * @param value The value which this {@link ObjectVertex} will represent.
   */
  public ObjectVertex(final Object value) {
    this.value = value;
  }

  /**
   * Retrieves the value that this {@link ObjectVertex} represents.
   *
   * @return An instance of a {@link Object} that this {@link ObjectVertex} represents.
   */
  public Object get() {
    return getValue();
  }

  /**
   * Retrieves the value that this {@link ObjectVertex} represents.
   *
   * @return An instance of a {@link Object} that this {@link ObjectVertex} represents.
   */
  public Object getValue() {
    return value;
  }

  /**
   * @see Object#equals(Object)
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final ObjectVertex that = (ObjectVertex) o;

    if (value != null ? !value.equals(that.value) : that.value != null) return false;

    return true;
  }

  /**
   * @see Object#toString()
   */
  @Override
  public String toString() {
    return "" + value;
  }

  /**
   * @see Object#hashCode()
   */
  @Override
  public int hashCode() {
    return value != null ? value.hashCode() : 0;
  }

  /**
   * Convenience method for instantiating a new {@link ObjectVertex}.
   *
   * @param value The value which this {@link ObjectVertex} will represent.
   * @return A new instance of {@link ObjectVertex}.
   */
  public static ObjectVertex from(final Object value) {
    return new ObjectVertex(value);
  }
}
