package test;

import cli.CLI;
import cli.CostumPrintStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import junitparams.JUnitParamsRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import translation.FunctionNameVisitor;
import utils.Utils;

@RunWith(JUnitParamsRunner.class)
public class AssignableTests2 {
    @BeforeClass
    public static void init() {
        System.setErr(new CostumPrintStream(System.err));
        System.setOut(new CostumPrintStream(System.out));
    }

    @AfterClass
    public static void after() {
        CLI.cleanUp();
    }

    public static Collection<Object[]> assignableParamter2() {
        init();
        return Utils.prepareParameters(Utils.baseTestFolder + "tests" + File.separator + "AssignableTests2.java");
    }

    @Test
    @junitparams.Parameters(method = "assignableParamter2")
    public void runAssignableTests2(String classFile, String function, String unwind, FunctionNameVisitor.TestBehaviour behaviour,
                                    String parentFolder) throws IOException, InterruptedException {
        Utils.runTests(classFile, function, unwind, behaviour, parentFolder);
    }
}