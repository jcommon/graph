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

public class ValidOrdering<TVertex extends IVertex> {
  private final TVertex[] ordering;

  public ValidOrdering(TVertex...vertices) {
    this.ordering = vertices;
  }

  public IVertex[] getOrdering() {
    return ordering;
  }

  @Override
  public String toString() {
    return "" + (ordering == null ? null : Arrays.asList(ordering));
  }

  public static <TVertex extends IVertex> ValidOrdering build(TVertex...vertices) {
    return new ValidOrdering<TVertex>(vertices);
  }

  public static ValidOrdering<StringVertex> buildFromStrings(String...vertices) {
    StringVertex[] ordering = new StringVertex[vertices.length];
    for(int i = 0; i < vertices.length; ++i) {
      ordering[i] = StringVertex.from(vertices[i]);
    }
    return new ValidOrdering<StringVertex>(ordering);
  }

  public static ValidOrdering<NumberVertex> buildFromNumbers(Number...vertices) {
    NumberVertex[] ordering = new NumberVertex[vertices.length];
    for(int i = 0; i < vertices.length; ++i) {
      ordering[i] = NumberVertex.from(vertices[i]);
    }
    return new ValidOrdering<NumberVertex>(ordering);
  }

  public boolean matches(TVertex...vertices) {
    return matches(Arrays.asList(vertices));
  }

  public boolean matches(List<TVertex> vertices) {
    if (ordering.length != vertices.size())
      return false;

    for(int i = 0; i < ordering.length; ++i) {
      if (!ordering[i].equals(vertices.get(i)))
        return false;
    }

    return true;
  }
}
