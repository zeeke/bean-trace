package org.laborra.beantrace

import org.junit.Test
import org.mockito.Mockito


class DemoTest {

    @Test
    void "class loader test"() {
        BeanTraces.printBeanTrace(BeanTraces.class.getClassLoader());
    }

    @Test
    void "mockito"() {
        def subject = Mockito.mock(Runnable);
        BeanTraces.printBeanTrace(subject);
    }
}
