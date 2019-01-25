package LambdaBox;

public class TestRequest {
    private Integer testId;
    private String testCaseClass;
    private String testCaseName;
    private String resFolder;
    private String tmpFolder;
    private String browser;
    private String host;

    public TestRequest(Integer testId, String testCaseClass, String testCaseName) {
        this.testId = testId;
        this.testCaseClass = testCaseClass;
        this.testCaseName = testCaseName;
    }

    public TestRequest() {

    }



    @Override
    public String toString() {
        return "{" +
                "\"testId\":\"" + testId + "\"," +
                "\"testCaseClass\":\"" + testCaseClass + "\"," +
                "\"testCaseName\":" + testCaseName +"\"" +
                "}";
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public String getTestCaseClass() {
        return testCaseClass;
    }

    public void setTestCaseClass(String testCaseClass) {
        this.testCaseClass = testCaseClass;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public String getResFolder() {
        return resFolder;
    }

    public void setResFolder(String resFolder) {
        this.resFolder = resFolder;
    }

    public String getTmpFolder() {
        return tmpFolder;
    }

    public void setTmpFolder(String tmpFolder) {
        this.tmpFolder = tmpFolder;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
