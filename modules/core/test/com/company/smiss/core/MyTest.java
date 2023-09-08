package com.company.smiss.core;


import com.company.smiss.SmissTestContainer;
import com.company.smiss.service.ClientLoader;
import com.haulmont.cuba.core.global.AppBeans;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;

public class MyTest {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MyTest.class);
    private ClientLoader clientLoader;

    @ClassRule
    public static SmissTestContainer cont = SmissTestContainer.Common.INSTANCE;

    @Before
    public void setUp() throws Exception {
        clientLoader = AppBeans.get(ClientLoader.class);
    }

    @Test
    public void runTest() {

        clientLoader.loadData();
    }


}