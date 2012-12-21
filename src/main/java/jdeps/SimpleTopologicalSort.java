package jdeps;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Please see the following for a description of this algorithm:
 *   http://www.cs.washington.edu/education/courses/cse373/02sp/lectures/cse373-21-TopoSort-4up.pdf
 */
public final class SimpleTopologicalSort implements ITopologicalSortStrategy {
  @Override
  public IVertex[] sort(IAdjacencyList adjacencyList) throws CyclicGraphException {
    if (adjacencyList.isEmpty())
      return IGraph.EMPTY_VERTICES;

    int ordered_index = 0;
    IVertex[] ordered = new IVertex[adjacencyList.size()];

    Queue<IAdjacencyListPair> queue = new LinkedList<IAdjacencyListPair>();
    int[] in_degrees = adjacencyList.calculateInDegrees();

    //Find all vertices who have an in-degree of zero.
    for(int i = 0; i < in_degrees.length; ++i) {
      if (in_degrees[i] == 0)
        queue.add(adjacencyList.get(i));
    }

    //If there are no vertices with an in-degree of zero (meaning they have no one pointing to them),
    //then this is not a DAG (directed acyclic graph).
    if (queue.isEmpty())
      throw new CyclicGraphException("Cycle detected when topologically sorting the graph");

    IAdjacencyListPair p;
    while((p = queue.poll()) != null) {
      ordered[ordered_index++] = p.getVertex();

      for(IVertex d: p.getOutNeighbors()) {
        int index = adjacencyList.indexOf(d);
        //Enqueue any vertex whose in-degree will become zero
        if (in_degrees[index] == 1)
          queue.add(adjacencyList.get(index));
        --in_degrees[index];
      }
    }

    //If we haven't filled the array, then there's a cycle somewhere.
    if (ordered_index != ordered.length)
      throw new CyclicGraphException("Cycle detected when topologically sorting the graph");

    return ordered;
  }

  @Override
  public <T extends IVertex> ITopologicalSortAsyncResult sortAsync(final ExecutorService executorProcessors, final IAdjacencyList adjacencyList, final ITopologicalSortCallback<T> callback, final ITopologicalSortErrorCallback<T> errorCallback) {
    final TopologicalSortAsyncResult asyncResult = new TopologicalSortAsyncResult(executorProcessors);
    if (adjacencyList.isEmpty()) {
      asyncResult.asyncComplete(true);
      return asyncResult;
    }

    final Queue<Callable<Object>> queue = new LinkedList<Callable<Object>>();
    final int[] in_degrees = adjacencyList.calculateInDegrees();
    final ArrayList<Callable<Object>> callables = new ArrayList<Callable<Object>>(in_degrees.length);
    final Set<IAdjacencyListPair> remaining = new HashSet<IAdjacencyListPair>();
    final AtomicInteger[] atomics = new AtomicInteger[in_degrees.length];
    final AtomicInteger outstanding_submissions = new AtomicInteger(0);

    //Find all vertices who have an in-degree of zero.
    //Also initialize latches to the size of the the in-degree + 1.
    for(int i = 0; i < in_degrees.length; ++i) {
      final IAdjacencyListPair pair = adjacencyList.get(i);
      final AtomicInteger atom = atomics[i] = new AtomicInteger(in_degrees[i] == 0 ? 1 : in_degrees[i]);

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
          T dependency = (T)pair.getVertex();

          try {

            if (atom.decrementAndGet() == 0) {
              //Remove from the list of remaining vertices.
              synchronized (remaining) {
                remaining.remove(pair);
              }

              //Call the callback to let him handle this dependency.
              try {
                callback.handle(dependency);
              } catch(Throwable t) {
                //We need to handle the exception later after we've done other work.
                //Save it off for later evaluation.
                handled_exception = t;
              }

              //Submit a task for everyone who is dependent on me.
              //On the next round if all of their dependencies have been evaluated,
              //the count will be at zero.
              for(IVertex dep : pair.getOutNeighbors()) {
                final int index = adjacencyList.indexOf(dep);
                final Callable<Object> dep_callable = callables.get(index);

                //Ensure we track the number of submissions. This will be used to
                //detect an effective deadlock -- we can't make progress b/c there's
                //a cycle preventing it.
                outstanding_submissions.incrementAndGet();
                executorProcessors.submit(dep_callable);
              }
            }

            //Check if we cannot make any more progress. We know to do this check b/c
            //on every submission we atomically increment a counter and then when that
            //submission is processed we decrement the counter. If the counter reaches
            //zero, then we know we've effectively drained the submissions. At that
            //point, if there are any remaining then we know there's a cycle.

            int outstanding = outstanding_submissions.decrementAndGet();

            if (outstanding == 0) {
              //Save a notification for later processing after we've handled errors.
              handle_all_done = true;

              if (remaining.size() > 0) {
                throw new CyclicGraphException("Cycle detected when topologically sorting the graph");
              }
            }

            //Now deal with any exception that was thrown upon processing the dependency.
            //Will bubble up to the callable-wide handler below.
            if (handled_exception != null)
              throw handled_exception;

          } catch(Throwable t) {
            if (errorCallback != null) {
              try {
                errorCallback.handleError(t, dependency);
              } catch(Throwable t2) {
                //Swallow any exceptions thrown by our error handler.
              }
            }
          }

          if (handle_all_done) {
            asyncResult.asyncComplete(remaining.size() == 0);
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
        errorCallback.handleError(new CyclicGraphException("Cycle detected when topologically sorting the graph"), null);
      }
      asyncResult.asyncComplete(false);
      return asyncResult;
    }

    //Initialize number of outstanding executor submissions.
    //When this reaches zero, we need to check if there's deadlock (no path forward), which
    //is a sign of a cycle.
    outstanding_submissions.set(queue.size());

    Callable<Object> callable;
    while((callable = queue.poll()) != null)
      executorProcessors.submit(callable);

    return asyncResult;
  }
}
