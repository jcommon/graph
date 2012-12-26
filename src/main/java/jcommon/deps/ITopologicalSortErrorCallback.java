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
 * A callback for when an error has occurred during asynchronous processing.
 */
public interface ITopologicalSortErrorCallback<TVertex extends IVertex> {
  /**
   * The callback that will be executed inside a thread pool other than the
   * one invoking the sort.
   *
   * @param vertex Instance of {@link IVertex} of type <code>TVertex</code> that produced the error. Could be null.
   * @param t Exception that was thrown.
   * @param coordinator Instance of {@link ITopologicalSortCoordinator} that allows for communication between
   *                    asynchronous sorting submissions done by the driver (which is typically done by an instance
   *                    of {@link ITopologicalSortStrategy}).
   */
  void handleError(TVertex vertex, Throwable t, ITopologicalSortCoordinator coordinator);
}
