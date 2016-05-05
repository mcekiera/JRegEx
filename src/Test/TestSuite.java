package Test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ConstructsAbstractFactoryTest.class,
        SegmentTest.class,
        RegexLibTest.class,
        MatcherLibTest.class,
        ConstructTest.class
})
public class TestSuite {
}  