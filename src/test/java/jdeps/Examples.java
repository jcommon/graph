package jdeps;

import static jdeps.Solution.*;

public class Examples {
  public static final IDependencyGraph VALID_SIMPLE_1;
  public static final Solution SOLUTION_VALID_SIMPLE_1 = Solution.create(
      "VALID_SIMPLE_1"
    , CYCLE_NOT_EXPECTED
    , VALID_SIMPLE_1 = DependencyGraph.create()
    , ValidOrdering.build()
  );

  public static final IStringDependencyGraph VALID_SIMPLE_2;
  public static final Solution SOLUTION_VALID_SIMPLE_2 = Solution.create(
      "VALID_SIMPLE_2"
    , CYCLE_NOT_EXPECTED
    , VALID_SIMPLE_2 = DependencyGraph.buildFromStrings("A")
    , ValidOrdering.buildFromStrings("A")
  );

  public static final IStringDependencyGraph VALID_SIMPLE_3;
  public static final Solution SOLUTION_VALID_SIMPLE_3 = Solution.create(
      "VALID_SIMPLE_2"
    , CYCLE_NOT_EXPECTED
    , VALID_SIMPLE_3 = DependencyGraph.buildFromStrings("A", "B")
    , ValidOrdering.buildFromStrings("A", "B")
  );

  public static final IStringDependencyGraph VALID_SIMPLE_4;
  public static final Solution SOLUTION_VALID_SIMPLE_4 = Solution.create(
      "VALID_SIMPLE_4"
    , CYCLE_NOT_EXPECTED
    , VALID_SIMPLE_4 = DependencyGraph.buildFromStrings("A", "B")
        .addRelationship("B", "A")
    , ValidOrdering.buildFromStrings("B", "A")
  );

  //http://www.cs.washington.edu/education/courses/cse373/02sp/lectures/cse373-21-TopoSort-4up.pdf
  //Valid order: A, B, C, D, E, F
  public static final IStringDependencyGraph VALID_1;
  public static final Solution SOLUTION_VALID_1 = Solution.create(
      "VALID_1"
    , CYCLE_NOT_EXPECTED
    , VALID_1 = DependencyGraph.buildFromStrings("A", "B", "C", "D", "E", "F")
        .addRelationship("A", "B")
        .addRelationship("A", "D")
        .addRelationship("B", "C")
        .addRelationship("C", "D")
        .addRelationship("C", "E")
        .addRelationship("D", "E")
    , ValidOrdering.buildFromStrings("A", "B", "C", "D", "E", "F")
    , ValidOrdering.buildFromStrings("F", "A", "B", "C", "D", "E")
    , ValidOrdering.buildFromStrings("A", "F", "B", "C", "D", "E")
  );

  //http://www.cs.cornell.edu/courses/cs312/2004fa/lectures/lecture15.htm
  //Valid order: 7, 9, 1, 4, 6, 5, 8, 2, 3
  //Valid order: 1, 2, 9, 7, 4, 6, 3, 5, 8
  public static final INumberDependencyGraph VALID_2;
  public static final Solution SOLUTION_VALID_2 = Solution.create(
      "VALID_2"
    , CYCLE_NOT_EXPECTED
    , VALID_2 = DependencyGraph.buildFromNumbers(1, 2, 3, 4, 5, 6, 7, 8, 9)
        .addRelationship(1, 2)
        .addRelationship(1, 4)
        .addRelationship(2, 3)
        .addRelationship(4, 3)
        .addRelationship(4, 6)
        //.addRelationship(5, 4) //<- would create a cycle
        .addRelationship(5, 8)
        .addRelationship(6, 5)
        .addRelationship(6, 8)
        .addRelationship(9, 8)
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
  public static final IStringDependencyGraph VALID_3;
  public static final Solution SOLUTION_VALID_3 = Solution.create(
      "VALID_3"
    , CYCLE_NOT_EXPECTED
    , VALID_3 = DependencyGraph.createForStrings()
        .addDependency("A")
        .addDependency("B")
        .addDependency("C")
        .addDependency("D")
        .addDependency("E")
        .addDependency("F")
        .addRelationship("A", "B")
        .addRelationship("A", "C")
        .addRelationship("A", "D")
        .addRelationship("B", "C")
        .addRelationship("B", "D")
        .addRelationship("D", "E")
        .addRelationship("F", "B")
    , ValidOrdering.buildFromStrings("A", "F", "B", "C", "D", "E")
  );

  public static final IStringDependencyGraph CYCLE_1;
  public static final Solution SOLUTION_CYCLE_1 = Solution.create(
      "CYCLE_1"
    , CYCLE_EXPECTED
    , CYCLE_1 = DependencyGraph.buildFromStrings("A", "B")
        .addRelationship("A", "B")
        .addRelationship("B", "A")
  );

  public static final IStringDependencyGraph CYCLE_2;
  public static final Solution SOLUTION_CYCLE_2 = Solution.create(
      "CYCLE_2"
    , CYCLE_EXPECTED
    , CYCLE_2 = DependencyGraph.buildFromStrings("A", "B", "C")
        .addRelationship("A", "B")
        .addRelationship("B", "C")
        .addRelationship("C", "A")
  );

  public static final INumberDependencyGraph CYCLE_3;
  public static final Solution SOLUTION_CYCLE_3 = Solution.create(
      "CYCLE_3"
    , CYCLE_EXPECTED
    , CYCLE_3 = VALID_2.copyAsNumberGraph()
        .addRelationship(5, 4) //<-- creates a cycle
  );

  public static final Solution[] ALL_SOLUTIONS = {
      SOLUTION_VALID_SIMPLE_1
    , SOLUTION_VALID_SIMPLE_2
    , SOLUTION_VALID_SIMPLE_3
    , SOLUTION_VALID_SIMPLE_4

    , SOLUTION_VALID_1
    , SOLUTION_VALID_2
    , SOLUTION_VALID_3

    , SOLUTION_CYCLE_1
    , SOLUTION_CYCLE_2
    , SOLUTION_CYCLE_3
  };
}
