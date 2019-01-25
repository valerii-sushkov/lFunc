package LambdaBox;

import dto.L_PROPERTY;
import helpersLocal.LConfig;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Runner {
    public static void main(String[] args) {
        runExternalTest(dummyConf());
    }

    public static TestResponse lambdaRun(final TestRequest request) {
        LConfig.readProperties();
        EnvironmentHelper.prepareEnvironment();
        return runExternalTest(request);
    }

    public static TestResponse runExternalTest(final TestRequest request) {
        try {
        File file = new File(L_PROPERTY.TEMP.getValue() +
                L_PROPERTY.RUNNER_JAR.getValue());
        URLClassLoader child = new URLClassLoader(new URL[]{file.toURI().toURL()}, Runner.class.getClassLoader());
        Class classToLoad = Class.forName("Run.LambdaRun", true, child);
        Method method = classToLoad.getDeclaredMethod("runSingleTest", TestRequest.class);
        Object instance = classToLoad.getConstructor().newInstance();
        TestResponse result = (TestResponse) method.invoke(instance, request);
        return result;
        } catch (ClassNotFoundException|InstantiationException|
                InvocationTargetException|NoSuchMethodException|
                MalformedURLException|IllegalAccessException e) {
            throw new RuntimeException("Error initialising external jar runner!", e);
        }

    }

    public static TestRequest dummyConf() {
        TestRequest req = new TestRequest(1, "tests.Test1", "simpleTest1");
        req.setBrowser("chrome");
        req.setHost("https://www.google.com/");
        req.setResFolder("src\\main\\resources\\");
        req.setTmpFolder("tmp\\");
        return req;
    }
}
