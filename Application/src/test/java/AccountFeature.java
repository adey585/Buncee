import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;

public class AccountFeature extends SubBase {

    public AccountFeature() throws IOException { }

    @Test(priority = 1, enabled = false)
    public void login() throws Exception {
        try {
            loginToAccount();
            Thread.sleep(9000);
            Assert.assertEquals(driver.getTitle(), "Dashboard");
        } catch (Exception e) {
            takeScreenshot(new Object(){}.getClass().getEnclosingMethod().getName());
            Assert.fail();
        }
    }

    @Test(priority = 2, enabled = false)
    public void logout() throws InterruptedException {
        loginToAccount();
        logoutFromAccount();
    }

    @Test(priority = 3, enabled = true)
    public void links() throws InterruptedException {
        loginToAccount();
        clickOnLinks();
        logoutFromAccount();
    }
}
