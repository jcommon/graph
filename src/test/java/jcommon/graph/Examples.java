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

import static jcommon.graph.Solution.CYCLE_EXPECTED;
import static jcommon.graph.Solution.CYCLE_NOT_EXPECTED;

@SuppressWarnings("unchecked")
public class Examples {

  //Expected ending vertices: <empty>
  public static final IGraph VALID_SIMPLE_1;
  public static final Solution<ObjectVertex<Object>, Object, Object> SOLUTION_VALID_SIMPLE_1 = Solution.create(
      "VALID_SIMPLE_1"
    , CYCLE_NOT_EXPECTED
    , VALID_SIMPLE_1 = ObjectGraph.buildFromObjects()
    , ValidList.build()
    , ValidOrdering.build()
  );

  //Expected ending vertices: A
  public static final StringGraph VALID_SIMPLE_2;
  public static final Solution<ObjectVertex<String>, String, String> SOLUTION_VALID_SIMPLE_2 = Solution.create(
      "VALID_SIMPLE_2"
    , CYCLE_NOT_EXPECTED
    , VALID_SIMPLE_2 = StringGraph.buildFromStrings("A")
    , ValidList.buildFromStrings("A")
    , ValidOrdering.buildFromStrings("A")
  );

  //Expected ending vertices: A, B
  public static final StringGraph VALID_SIMPLE_3;
  public static final Solution<ObjectVertex<String>, String, String> SOLUTION_VALID_SIMPLE_3 = Solution.create(
      "VALID_SIMPLE_2"
    , CYCLE_NOT_EXPECTED
    , VALID_SIMPLE_3 = StringGraph.buildFromStrings("A", "B")
    , ValidList.buildFromStrings("A", "B")
    , ValidOrdering.buildFromStrings("A", "B")
  );

  //Expected ending vertices: A
  public static final StringGraph VALID_SIMPLE_4;
  public static final Solution<ObjectVertex<String>, String, String> SOLUTION_VALID_SIMPLE_4 = Solution.create(
      "VALID_SIMPLE_4"
    , CYCLE_NOT_EXPECTED
    , VALID_SIMPLE_4 = StringGraph.buildFromStrings("A", "B")
        .addEdge("B", "A")
    , ValidList.buildFromStrings("A")
    , ValidOrdering.buildFromStrings("B", "A")
  );

  //http://www.cs.washington.edu/education/courses/cse373/02sp/lectures/cse373-21-TopoSort-4up.pdf
  //Valid order: A, B, C, D, E, F
  //Expected ending vertices: E, F
  public static final StringGraph VALID_1;
  public static final Solution<ObjectVertex<String>, String, String> SOLUTION_VALID_1 = Solution.create(
      "VALID_1"
    , CYCLE_NOT_EXPECTED
    , VALID_1 = StringGraph.buildFromStrings("A", "B", "C", "D", "E", "F")
        .addEdge("A", "B")
        .addEdge("A", "D")
        .addEdge("B", "C")
        .addEdge("C", "D")
        .addEdge("C", "E")
        .addEdge("D", "E")
    , ValidList.buildFromStrings("E", "F")
    , ValidOrdering.buildFromStrings("A", "B", "C", "D", "E", "F")
    , ValidOrdering.buildFromStrings("F", "A", "B", "C", "D", "E")
    , ValidOrdering.buildFromStrings("A", "F", "B", "C", "D", "E")
  );

  //http://www.cs.cornell.edu/courses/cs312/2004fa/lectures/lecture15.htm
  //Valid order: 7, 9, 1, 4, 6, 5, 8, 2, 3
  //Valid order: 1, 2, 9, 7, 4, 6, 3, 5, 8
  //Expected ending vertices: 3, 7, 8
  public static final NumberGraph<Integer> VALID_2;
  public static final Solution<ObjectVertex<Integer>, Integer, Integer> SOLUTION_VALID_2 = Solution.create(
      "VALID_2"
    , CYCLE_NOT_EXPECTED
    , VALID_2 = NumberGraph.buildFromNumbers(1, 2, 3, 4, 5, 6, 7, 8, 9)
        .addEdge(1, 2)
        .addEdge(1, 4)
        .addEdge(2, 3)
        .addEdge(4, 3)
        .addEdge(4, 6)
        //.addEdge(5, 4) //<- would create a cycle
        .addEdge(5, 8)
        .addEdge(6, 5)
        .addEdge(6, 8)
        .addEdge(9, 8)
    , ValidList.buildFromNumbers(3, 7, 8)
    , ValidOrdering.buildFromNumbers(7, 9, 1, 4, 6, 5, 8, 2, 3)
    , ValidOrdering.buildFromNumbers(1, 2, 9, 7, 4, 6, 3, 5, 8)
    , ValidOrdering.buildFromNumbers(1, 7, 9, 2, 4, 3, 6, 5, 8)
  );

  //http://www.cs.cmu.edu/~avrim/451f08/lectures/lect1002.pdf
  //
  //We have the following:
  //A --> D --> E
  //| \   |\
  //|  \  |
  //|   \ |
  //|/   \|
  //C <-- B <-- F
  //
  //Valid ordering: A, F, B, C, D, E
  //Expected ending vertices: C, E
  public static final StringGraph VALID_3;
  public static final Solution<ObjectVertex<String>, String, String> SOLUTION_VALID_3 = Solution.create(
      "VALID_3"
    , CYCLE_NOT_EXPECTED
    , VALID_3 = StringGraph.createForStrings()
        .addVertex("A")
        .addVertex("B")
        .addVertex("C")
        .addVertex("D")
        .addVertex("E")
        .addVertex("F")
        .addEdge("A", "B")
        .addEdge("A", "C")
        .addEdge("A", "D")
        .addEdge("B", "C")
        .addEdge("B", "D")
        .addEdge("D", "E")
        .addEdge("F", "B")
    , ValidList.build("C", "E")
    , ValidOrdering.buildFromStrings("A", "F", "B", "C", "D", "E")
  );

  //https://ece.uwaterloo.ca/~cmoreno/ece250/2012-03-16--topological-sort.pdf
  //Valid order: 1, 6, 2, 5, 3, 4
  //Expected ending vertices: 4
  public static final NumberGraph<Integer> VALID_4;
  public static final Solution<ObjectVertex<Integer>, Integer, Integer> SOLUTION_VALID_4 = Solution.create(
      "VALID_4"
    , CYCLE_NOT_EXPECTED
    , VALID_4 = NumberGraph.buildFromNumbers(1, 2, 3, 4, 5, 6)
        .addEdge(1, 2)
        .addEdge(1, 4)
        .addEdge(2, 3)
        .addEdge(2, 4)
        .addEdge(2, 5)
        .addEdge(3, 4)
        .addEdge(5, 3)
        .addEdge(6, 3)
        .addEdge(6, 5)
    , ValidList.build(4)
    , ValidOrdering.build(1, 6, 2, 5, 3, 4)
  );

  //Valid order: 1, 2, 3, 4, 5
  //Expected ending vertices: 5
  public static final NumberGraph<Integer> VALID_5;
  public static final Solution<ObjectVertex<Integer>, Integer, Integer> SOLUTION_VALID_5 = Solution.create(
      "VALID_5"
    , CYCLE_NOT_EXPECTED
    , VALID_5 = NumberGraph.buildFromNumbers(1, 2, 3, 4, 5)
        .addEdge(1, 2)
        .addEdge(2, 3)
        .addEdge(3, 4)
        .addEdge(4, 5)
    , ValidList.build(5)
    , ValidOrdering.build(1, 2, 3, 4, 5)
  );

  public static final StringGraph CYCLE_1;
  public static final Solution<ObjectVertex<String>, String, String> SOLUTION_CYCLE_1 = Solution.create(
      "CYCLE_1"
    , CYCLE_EXPECTED
    , CYCLE_1 = StringGraph.buildFromStrings("A", "B")
        .addEdge("A", "B")
        .addEdge("B", "A")
  );

  public static final StringGraph CYCLE_2;
  public static final Solution<ObjectVertex<String>, String, String> SOLUTION_CYCLE_2 = Solution.create(
      "CYCLE_2"
    , CYCLE_EXPECTED
    , CYCLE_2 = StringGraph.buildFromStrings("A", "B", "C")
        .addEdge("A", "B")
        .addEdge("B", "C")
        .addEdge("C", "A")
  );

  public static final NumberGraph<Integer> CYCLE_3;
  public static final Solution<ObjectVertex<Integer>, Integer, Integer> SOLUTION_CYCLE_3 = Solution.create(
      "CYCLE_3"
    , CYCLE_EXPECTED
    , CYCLE_3 = VALID_2.copyAsNumberGraph()
        .addEdge(5, 4) //<-- creates a cycle
  );

  public static final Solution[] ALL_SOLUTIONS = {
      SOLUTION_VALID_SIMPLE_1
    , SOLUTION_VALID_SIMPLE_2
    , SOLUTION_VALID_SIMPLE_3
    , SOLUTION_VALID_SIMPLE_4

    , SOLUTION_VALID_1
    , SOLUTION_VALID_2
    , SOLUTION_VALID_3
    , SOLUTION_VALID_4
    , SOLUTION_VALID_5

    , SOLUTION_CYCLE_1
    , SOLUTION_CYCLE_2
    , SOLUTION_CYCLE_3
  };

  public static final IGraph[] ALL_GRAPHS = {
      VALID_SIMPLE_1
    , VALID_SIMPLE_2
    , VALID_SIMPLE_3
    , VALID_SIMPLE_4

    , VALID_1
    , VALID_2
    , VALID_3
    , VALID_4
    , VALID_5

    , CYCLE_1
    , CYCLE_2
    , CYCLE_3
  };

  public static final IGraph[] ALL_VALID_GRAPHS = {
      VALID_SIMPLE_1
    , VALID_SIMPLE_2
    , VALID_SIMPLE_3
    , VALID_SIMPLE_4

    , VALID_1
    , VALID_2
    , VALID_3
    , VALID_4
  };

  public static final IGraph[] ALL_GRAPHS_WITH_CYCLES = {
      CYCLE_1
    , CYCLE_2
    , CYCLE_3
  };
}
