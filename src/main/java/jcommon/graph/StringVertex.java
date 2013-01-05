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
 * A representation of an {@link IVertex} instance using {@link String}s.
 *
 * @see IVertex
 */
public class StringVertex implements IVertex<String> {
  private final String name;

  /**
   * Instantiates a new instance of {@link StringVertex}.
   *
   * @param name The value which this {@link StringVertex} will represent.
   */
  public StringVertex(final String name) {
    if (name == null)
      throw new IllegalArgumentException("name cannot be null");

    this.name = name;
  }

  /**
   * Retrieves the value that this {@link StringVertex} represents.
   *
   * @return An instance of a {@link String} that this {@link StringVertex} represents.
   */
  @Override
  public String get() {
    return getName();
  }

  /**
   * Retrieves the name that this {@link StringVertex} represents.
   *
   * @return An instance of a {@link String} that this {@link StringVertex} represents.
   */
  public String getName() {
    return name;
  }

  /**
   * Retrieves the value that this {@link StringVertex} represents.
   *
   * @return An instance of a {@link String} that this {@link StringVertex} represents.
   */
  @Override
  public String getValue() {
    return name;
  }

  /**
   * @see Object#equals(Object)
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final StringVertex that = (StringVertex) o;

    if (!name.equals(that.name)) return false;

    return true;
  }

  /**
   * @see Object#toString()
   */
  @Override
  public String toString() {
    return name;
  }

  /**
   * @see Object#hashCode()
   */
  @Override
  public int hashCode() {
    return name.hashCode();
  }

  /**
   * Convenience method for instantiating a new {@link StringVertex}.
   *
   * @param name The value which this {@link StringVertex} will represent.
   * @return A new instance of {@link StringVertex}.
   */
  public static StringVertex from(final String name) {
    return new StringVertex(name);
  }
}
