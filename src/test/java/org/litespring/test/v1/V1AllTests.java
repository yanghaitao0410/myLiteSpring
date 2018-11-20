package org.litespring.test.v1;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//运行环境
@RunWith(Suite.class)
//运行的测试类
@Suite.SuiteClasses({ApplicationContextTest.class, BeanFactoryTest.class, ResourceTest.class})
public class V1AllTests {
}
