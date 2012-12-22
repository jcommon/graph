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

package jdeps;

/**
 * A representation of an {@link IVertex} instance using {@link String}s.
 *
 * @see IVertex
 */
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

  public static StringVertex from(String name) {
    return new StringVertex(name);
  }
}
