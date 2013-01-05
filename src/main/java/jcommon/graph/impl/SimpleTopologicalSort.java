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

package jcommon.graph.impl;


import jcommon.graph.CyclicGraphException;
import jcommon.graph.IAdjacencyList;
import jcommon.graph.IAdjacencyListPair;
import jcommon.graph.ITopologicalSortAsyncResult;
import jcommon.graph.ITopologicalSortCallback;
import jcommon.graph.ITopologicalSortCoordinator;
import jcommon.graph.ITopologicalSortErrorCallback;
import jcommon.graph.ITopologicalSortInput;
import jcommon.graph.ITopologicalSortStrategy;
import jcommon.graph.IVertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implements {@link ITopologicalSortStrategy} for doing iterative and concurrent topological sorts.
 *
 * Please see the following for a description of the basic algorithm:
 *   <a href="http://www.cs.washington.edu/education/courses/cse373/02sp/lectures/cse373-21-TopoSort-4up.pdf">http://www.cs.washington.edu/education/courses/cse373/02sp/lectures/cse373-21-TopoSort-4up.pdf</a>
 *
 * @param <TValue> Type of {@link Object} that a topological sort will operate on.
 *
 * @see <a href="http://www.cs.washington.edu/education/courses/cse373/02sp/lectures/cse373-21-TopoSort-4up.pdf">http://www.cs.washington.edu/education/courses/cse373/02sp/lectures/cse373-21-TopoSort-4up.pdf</a>
 */
public final class SimpleTopologicalSort<TVertex extends IVertex<TValue>, TValue extends Object> implements ITopologicalSortStrategy<TVertex, TValue> {
  /**
   * @see ITopologicalSortStrategy
   */
  public SimpleTopologicalSort() {
  }

  /**
   * @see ITopologicalSortStrategy#sort(IAdjacencyList)
   */
  @Override
  public List<TValue> sort(final IAdjacencyList<TVertex, TValue> adjacencyList) throws CyclicGraphException {
    if (adjacencyList.isEmpty())
      return new ArrayList<TValue>(0);

    final List<TVertex> ordered = new ArrayList<TVertex>(adjacencyList.size());

    final Queue<IAdjacencyListPair<TVertex>> queue = new LinkedList<IAdjacencyListPair<TVertex>>();
    final int[] in_degrees = adjacencyList.calculateInDegrees();

    //Find all vertices who have an in-degree of zero.
    for(int i = 0; i < in_degrees.length; ++i) {
      if (in_degrees[i] == 0)
        queue.add(adjacencyList.pairAt(i));
    }

    //If there are no vertices with an in-degree of zero (meaning they have no one pointing to them),
    //then this is not a DAG (directed acyclic graph).
    if (queue.isEmpty())
      throw new CyclicGraphException(STANDARD_CYCLE_MESSAGE);

    IAdjacencyListPair<TVertex> p;
    while ((p = queue.poll()) != null) {
      ordered.add(p.getVertex());

      for(TVertex d: p.getOutNeighbors()) {
        final int index = adjacencyList.indexOf(d);
        //Enqueue any vertex whose in-degree will become zero
        if (in_degrees[index] == 1)
          queue.add(adjacencyList.pairAt(index));
        --in_degrees[index];
      }
    }

    //If we haven't filled the array, then there's a cycle somewhere.
    if (ordered.size() != adjacencyList.size())
      throw new CyclicGraphException(STANDARD_CYCLE_MESSAGE);

    //Convert list of vertices to the objects they contain.
    final List<TValue> ordered_list = new ArrayList<TValue>(ordered.size());
    for (int i = 0; i < ordered.size(); ++i) {
      ordered_list.add(ordered.get(i).get());
    }
    return ordered_list;
  }

  /**
   * @see ITopologicalSortStrategy#sortAsync(ExecutorService, IAdjacencyList, ITopologicalSortCallback, ITopologicalSortErrorCallback)
   */
  @Override
  public ITopologicalSortAsyncResult<TValue> sortAsync(final ExecutorService executorProcessors, final IAdjacencyList<TVertex, TValue> adjacencyList, final ITopologicalSortCallback<TValue> callback, final ITopologicalSortErrorCallback<TValue> errorCallback) {
    final TopologicalSortAsyncResult<TValue> asyncResult = new TopologicalSortAsyncResult<TValue>(executorProcessors);
    if (adjacencyList.isEmpty()) {
      asyncResult.asyncComplete(adjacencyList.createResultMap(), true);
      return asyncResult;
    }

    final Queue<Callable<Object>> queue = new LinkedList<Callable<Object>>();
    final int[] in_degrees = adjacencyList.calculateInDegrees();
    final List<Callable<Object>> callables = new ArrayList<Callable<Object>>(in_degrees.length);
    final Set<IAdjacencyListPair<TVertex>> remaining = new HashSet<IAdjacencyListPair<TVertex>>();
    final AtomicInteger[] atomics = new AtomicInteger[in_degrees.length];
    final List<Map<IVertex, TValue>> inputs = new ArrayList<Map<IVertex, TValue>>(in_degrees.length);
    final AtomicInteger outstanding_submissions = new AtomicInteger(0);
    final ITopologicalSortCoordinator coordinator = new TopologicalSortCoordinator(asyncResult);
    final Map<TValue, TValue> results = adjacencyList.createResultMap();

    //Find all vertices who have an in-degree of zero.
    //Also initialize latches to the size of the the in-degree + 1.
    for(int i = 0; i < in_degrees.length; ++i) {
      final IAdjacencyListPair<TVertex> pair = adjacencyList.pairAt(i);
      final AtomicInteger atom = atomics[i] = new AtomicInteger(in_degrees[i] == 0 ? 1 : in_degrees[i]);
      final Map<IVertex, TValue> my_input = new HashMap<IVertex, TValue>(in_degrees[i], 1.0f);
      inputs.add(my_input);

      //Ensure that we add all items in the adjacency list to our list of remaining items.
      //As we discover the proper order to visit them and then process them, we will drain
      //the list of remaining vertices. If there is no cycle, then the list of remaining
      //vertices will be empty when we're through.
      remaining.add(pair);

      final Callable<Object> callable = new Callable<Object>() {
        @Override
        @SuppressWarnings("unchecked")
        public Object call() throws Exception {
          Throwable handled_exception = null;
          boolean handle_all_done = false;
          final TVertex vertex = pair.getVertex();
          ITopologicalSortInput<TValue> input;

          try {

            if (atom.decrementAndGet() == 0) {
              //Remove from the list of remaining vertices.
              synchronized (remaining) {
                remaining.remove(pair);
              }

              synchronized (my_input) {
                input = new TopologicalSortInput<TValue>(my_input);
              }

              //Call the callback to let him handle this vertex.
              final TValue vertex_value = vertex.get();
              TValue result = null;
              try {
                result = callback.handle(vertex_value, input, vertex, coordinator);
              } catch(Throwable t) {
                //We need to handle the exception later after we've done other work.
                //Save it off for later evaluation.
                handled_exception = t;
              }

              //Add to result set if this is a vertex with no out neighbors -- which
              //means that it is not pointing to any other vertices. As such, we're
              //likely interested in its callback's result. In that case, we save
              //it off so we can provide it in the ITopologicalSortAsyncResult instance.
              if (adjacencyList.isEndingVertex(vertex)) {
                synchronized (results) {
                  results.put(vertex_value, result);
                }
              }

              //Ensure we haven't been asked to stop processing. If so,
              //we don't want to schedule anything else. We need to let the existing
              //submissions drain.
              if (!asyncResult.isProcessingDiscontinued()) {
                //Submit a task for everyone who is dependent on me.
                //On the next round if all of their vertices have been evaluated,
                //the count will be at zero.
                for(TVertex dep : pair.getOutNeighbors()) {
                  final int index = adjacencyList.indexOf(dep);
                  final Callable<Object> dep_callable = callables.get(index);
                  final Map<IVertex, TValue> dep_inputs = inputs.get(index);

                  synchronized (dep_inputs) {
                    dep_inputs.put(vertex, result);
                  }

                  //Ensure we track the number of submissions. This will be used to
                  //detect an effective deadlock -- we can't make progress b/c there's
                  //a cycle preventing it.
                  outstanding_submissions.incrementAndGet();
                  try {
                    executorProcessors.submit(dep_callable);
                  } catch(Throwable t) {
                    //If the submission was rejected, then decrement the count we just incremented.
                    //This should result in a leftover and the remaining.size() will be > 0 and
                    //will thus fail later on.
                    outstanding_submissions.decrementAndGet();
                  }
                }
              }
            }

            //Check if we cannot make any more progress. We know to do this check b/c
            //on every submission we atomically increment a counter and then when that
            //submission is processed we decrement the counter. If the counter reaches
            //zero, then we know we've effectively drained the submissions. At that
            //point, if there are any remaining then we know there's a cycle.

            final int outstanding = outstanding_submissions.decrementAndGet();

            if (outstanding == 0) {
              //Save a notification for later processing after we've handled errors.
              handle_all_done = true;

              if (remaining.size() > 0) {
                throw new CyclicGraphException(STANDARD_CYCLE_MESSAGE);
              }
            }

            //Now deal with any exception that was thrown upon processing the vertex.
            //Will bubble up to the callable-wide handler below.
            if (handled_exception != null)
              throw handled_exception;

          } catch(Throwable t) {
            if (errorCallback != null) {
              try {
                errorCallback.handleError(vertex.get(), t, vertex, coordinator);
              } catch(Throwable t2) {
                //Swallow any exceptions thrown by our error handler.
              }
            }
          }

          if (handle_all_done) {
            asyncResult.asyncComplete(results, remaining.size() == 0);
          }

          return null;
        }
      };
      callables.add(callable);

      if (in_degrees[i] == 0) {
        //We do this to ensure that callables has been fully
        //initialized prior to starting up any tasks.
        queue.add(callable);
      }
    }

    //If there are no vertices with an in-degree of zero (meaning they have no one pointing to them),
    //then this is not a DAG (directed acyclic graph).
    if (queue.isEmpty()) {
      if (errorCallback != null) {
        errorCallback.handleError(null, new CyclicGraphException(STANDARD_CYCLE_MESSAGE), null, coordinator);
      }
      asyncResult.asyncComplete(results, false);
      return asyncResult;
    }

    //Initialize number of outstanding executor submissions.
    //When this reaches zero, we need to check if there's deadlock (no path forward), which
    //is a sign of a cycle.
    outstanding_submissions.set(queue.size());

    Callable<Object> callable;

    while ((callable = queue.poll()) != null) {
      try {
        executorProcessors.submit(callable);
      } catch(Throwable t) {
        if (errorCallback != null)
          errorCallback.handleError(null, new CyclicGraphException(STANDARD_CYCLE_MESSAGE), null, coordinator);
        asyncResult.discontinueScheduling();
        asyncResult.asyncComplete(results, false);
        return asyncResult;
      }
    }

    return asyncResult;
  }
}
