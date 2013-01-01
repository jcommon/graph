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

package jcommon.deps;

/**
 * A representation of an {@link IVertex} instance using {@link Number}s.
 *
 * @see IVertex
 */
public class NumberVertex implements IVertex {
  private final Number value;

  public NumberVertex(Number value) {
    if (value == null)
      throw new IllegalArgumentException("value cannot be null");

    this.value = value;
  }

  public Number get() {
    return getValue();
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

  public static NumberVertex from(Number name) {
    return new NumberVertex(name);
  }
}
