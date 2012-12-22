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
 * Provides a means for communication with the asynchronous sorting driver (which is typically done by an
 * instance of {@link ITopologicalSortStrategy}).
 */
public interface ITopologicalSortCoordinator {
  /**
   * Requests the driver (which is typically done by an instance of {@link ITopologicalSortStrategy}) to stop
   * further processing. Any remaining submissions will be drained out as they complete.
   *
   * @return True if the operation was successful, otherwise false.
   */
  boolean discontinueScheduling();
}
