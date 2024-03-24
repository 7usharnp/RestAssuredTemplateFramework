package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.util.Arrays;

public class Setup implements ITestListener {
    private static ExtentReports extentReports;
    public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    public static String name ="Tushar";

  public  void onTestStart(ITestResult result) {
     ExtentTest test= extentReports.createTest(result.getTestClass().getName()+"-"+result.getMethod().getMethodName());
     extentTest.set(test);
  }

    public void onStart(ITestContext context) {
        String fileName = ExtentReportManager.getReportWithTimestamp();
        String fullReportPath = System.getProperty("user.dir") + "\\reports\\" + fileName;
        try {
            extentReports =  ExtentReportManager.createInstance(fullReportPath, "Test Api Automation ","Test Execution Report");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void onFinish(ITestContext context) {
        if (extentReports != null){
            extentReports.flush();
        }
    }
    public  void onTestFailure(ITestResult result) {
        ExtentReportManager.logFailureDetails(result.getThrowable().getMessage());
        String stackTrace = Arrays.toString(result.getThrowable().getStackTrace());
        stackTrace =stackTrace.replaceAll(",","</br>");
        String FormattedStackTrace = "<details>\n" +
                "<summary>Click here to see exception logs</summary>\n"+
                "    "+ stackTrace+ "\n"+
                "</details>\n";
        ExtentReportManager.logExceptionDetails(FormattedStackTrace);


    }
}
