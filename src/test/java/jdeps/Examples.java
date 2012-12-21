package jdeps;

import static jdeps.Solution.*;

@SuppressWarnings("unchecked")
public class Examples {
  public static final IGraph VALID_SIMPLE_1;
  public static final Solution SOLUTION_VALID_SIMPLE_1 = Solution.create(
      "VALID_SIMPLE_1"
    , CYCLE_NOT_EXPECTED
    , VALID_SIMPLE_1 = DependencyGraph.create()
    , ValidOrdering.build()
  );

  public static final StringGraph VALID_SIMPLE_2;
  public static final Solution<StringVertex> SOLUTION_VALID_SIMPLE_2 = Solution.create(
      "VALID_SIMPLE_2"
    , CYCLE_NOT_EXPECTED
    , VALID_SIMPLE_2 = StringGraph.buildFromStrings("A")
    , ValidOrdering.buildFromStrings("A")
  );

  public static final StringGraph VALID_SIMPLE_3;
  public static final Solution<StringVertex> SOLUTION_VALID_SIMPLE_3 = Solution.create(
      "VALID_SIMPLE_2"
    , CYCLE_NOT_EXPECTED
    , VALID_SIMPLE_3 = StringGraph.buildFromStrings("A", "B")
    , ValidOrdering.buildFromStrings("A", "B")
  );

  public static final StringGraph VALID_SIMPLE_4;
  public static final Solution<StringVertex> SOLUTION_VALID_SIMPLE_4 = Solution.create(
      "VALID_SIMPLE_4"
    , CYCLE_NOT_EXPECTED
    , VALID_SIMPLE_4 = StringGraph.buildFromStrings("A", "B")
        .addEdge("B", "A")
    , ValidOrdering.buildFromStrings("B", "A")
  );

  //http://www.cs.washington.edu/education/courses/cse373/02sp/lectures/cse373-21-TopoSort-4up.pdf
  //Valid order: A, B, C, D, E, F
  public static final StringGraph VALID_1;
  public static final Solution<StringVertex> SOLUTION_VALID_1 = Solution.create(
      "VALID_1"
    , CYCLE_NOT_EXPECTED
    , VALID_1 = StringGraph.buildFromStrings("A", "B", "C", "D", "E", "F")
        .addEdge("A", "B")
        .addEdge("A", "D")
        .addEdge("B", "C")
        .addEdge("C", "D")
        .addEdge("C", "E")
        .addEdge("D", "E")
    , ValidOrdering.buildFromStrings("A", "B", "C", "D", "E", "F")
    , ValidOrdering.buildFromStrings("F", "A", "B", "C", "D", "E")
    , ValidOrdering.buildFromStrings("A", "F", "B", "C", "D", "E")
  );

  //http://www.cs.cornell.edu/courses/cs312/2004fa/lectures/lecture15.htm
  //Valid order: 7, 9, 1, 4, 6, 5, 8, 2, 3
  //Valid order: 1, 2, 9, 7, 4, 6, 3, 5, 8
  public static final NumberGraph VALID_2;
  public static final Solution<NumberVertex> SOLUTION_VALID_2 = Solution.create(
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
  public static final StringGraph VALID_3;
  public static final Solution<StringVertex> SOLUTION_VALID_3 = Solution.create(
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
    , ValidOrdering.buildFromStrings("A", "F", "B", "C", "D", "E")
  );

  //https://ece.uwaterloo.ca/~cmoreno/ece250/2012-03-16--topological-sort.pdf
  //Valid order: 1, 6, 2, 5, 3, 4
  public static final NumberGraph VALID_4;
  public static final Solution<NumberVertex> SOLUTION_VALID_4 = Solution.create(
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
    , ValidOrdering.buildFromNumbers(1, 6, 2, 5, 3, 4)
  );

  public static final StringGraph CYCLE_1;
  public static final Solution<StringVertex> SOLUTION_CYCLE_1 = Solution.create(
      "CYCLE_1"
    , CYCLE_EXPECTED
    , CYCLE_1 = StringGraph.buildFromStrings("A", "B")
        .addEdge("A", "B")
        .addEdge("B", "A")
  );

  public static final StringGraph CYCLE_2;
  public static final Solution<StringVertex> SOLUTION_CYCLE_2 = Solution.create(
      "CYCLE_2"
    , CYCLE_EXPECTED
    , CYCLE_2 = StringGraph.buildFromStrings("A", "B", "C")
        .addEdge("A", "B")
        .addEdge("B", "C")
        .addEdge("C", "A")
  );

  public static final NumberGraph CYCLE_3;
  public static final Solution<NumberVertex> SOLUTION_CYCLE_3 = Solution.create(
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
