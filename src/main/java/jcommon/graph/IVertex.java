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
 * Marker interface that designates an object as something that can be
 * reasoned about in an {@link IGraph} instance.
 */
public interface IVertex<TValue extends Object> {
  /**
   * Returns the value associated with this {@link IVertex}.
   *
   * @return The instance associated with this {@link IVertex}.
   */
  TValue get();

  /**
   * Returns the value associated with this {@link IVertex}. Alias for {@link #get()}.
   *
   * @return The instance associated with this {@link IVertex}.
   */
  TValue getValue();
}
