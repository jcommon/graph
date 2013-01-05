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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValidList<TValue extends Object> {
  private final TValue[] list;

  public ValidList(TValue... values) {
    this.list = values;
  }

  public TValue[] getList() {
    return list;
  }

  @Override
  public String toString() {
    return "" + (list == null ? null : Arrays.asList(list));
  }

  public static <TValue extends Object> ValidList build(TValue...values) {
    return new ValidList<TValue>(values);
  }

  public static ValidList<String> buildFromStrings(String...values) {
    return new ValidList<String>(values);
  }

  public static <TValue extends Number> ValidList<TValue> buildFromNumbers(TValue...values) {
    return new ValidList<TValue>(values);
  }

  public boolean matches(TValue...values) {
    return matches(Arrays.asList(values));
  }

  public boolean matches(Iterable<TValue> values) {
    //Convert to a list first.
    final List<TValue> l = new ArrayList<TValue>();
    for (TValue t : values) {
      l.add(t);
    }
    return matches(l);
  }

  public boolean matches(List<TValue> values) {
    if (list.length != values.size())
      return false;

    //Order does not matter -- just presence

    for(TValue v : list) {
      if (!values.contains(v))
        return false;
    }

    return true;
  }
}
