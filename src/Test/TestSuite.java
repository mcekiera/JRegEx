package Test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ConstructsAbstractFactoryTest.class,
        SegmentTest.class,
        RegexLibTest.class,
        MatcherLibTest.class,
        ConstructTest.class,
        SingleTest.class,
        CompositeTest.class,
        CompositeBuilderTest.class,
        QuantifierTest.class,
        DescLibTest.class,
        XModerFalseTest.class,
        XModerTrueTest.class

})
/**
 * Contain reference to all test classes, to allow TestRunner to use every one.
 */
public class TestSuite {
}  