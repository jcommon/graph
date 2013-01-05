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

import java.util.Arrays;
import java.util.List;

public class ValidOrdering<TValue extends Object> {
  private final TValue[] ordering;

  public ValidOrdering(TValue...vertices) {
    this.ordering = vertices;
  }

  public TValue[] getOrdering() {
    return ordering;
  }

  @Override
  public String toString() {
    return "" + (ordering == null ? null : Arrays.asList(ordering));
  }

  public static <TValue extends Object> ValidOrdering build(TValue...vertices) {
    return new ValidOrdering<TValue>(vertices);
  }

  public static ValidOrdering<String> buildFromStrings(String...vertices) {
    return new ValidOrdering<String>(vertices);
  }

  public static ValidOrdering<Number> buildFromNumbers(Number...vertices) {
    return new ValidOrdering<Number>(vertices);
  }

  public boolean matches(TValue...vertices) {
    return matches(Arrays.asList(vertices));
  }

  public boolean matches(List<TValue> vertices) {
    if (ordering.length != vertices.size())
      return false;

    for(int i = 0; i < ordering.length; ++i) {
      if ((ordering[i] == null && vertices.get(i) != null) || !ordering[i].equals(vertices.get(i)))
        return false;
    }

    return true;
  }
}
