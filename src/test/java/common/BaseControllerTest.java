package common;

import com.crixal.interview.config.ApplicationConfig;
import com.crixal.interview.web.WebStarter;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.rapidoid.setup.App;

public abstract class BaseControllerTest extends BaseTest {
    private static ApplicationConfig applicationConfig;

    static {
        applicationConfig = injector.getInstance(ApplicationConfig.class);
    }

    @BeforeClass
    public static void startTestServices() {
        RestAssured.baseURI = "http://127.0.0.1";
        RestAssured.port = applicationConfig.getServerPort();
    }

    protected void stopApp() {
        App.shutdown();
    }

    protected void startControllers() {
        injector.getInstance(WebStarter.class).start();
    }
}
