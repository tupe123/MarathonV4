package net.sourceforge.marathon.javadriver.progressbar;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.sourceforge.marathon.javadriver.JavaDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import components.ProgressBarDemo;

@Test public class JProgressBarTest {

    private WebDriver driver;
    protected JFrame frame;

    @BeforeMethod public void showDialog() throws Throwable {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override public void run() {
                frame = new JFrame(JProgressBarTest.class.getSimpleName());
                frame.setName("frame-" + JProgressBarTest.class.getSimpleName());
                frame.getContentPane().add(new ProgressBarDemo(), BorderLayout.CENTER);
                frame.pack();
                frame.setAlwaysOnTop(true);
                frame.setVisible(true);
            }
        });
    }

    @AfterMethod public void disposeDriver() throws Throwable {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override public void run() {
                frame.setVisible(false);
                frame.dispose();
            }
        });
        if (driver != null)
            driver.quit();
    }

    public void progress() throws Throwable {
        driver = new JavaDriver();
        final WebElement progressbar = driver.findElement(By.cssSelector("progress-bar"));
        AssertJUnit.assertNotNull("could not find progress-bar", progressbar);
        AssertJUnit.assertEquals("0", progressbar.getAttribute("value"));
        driver.findElement(By.cssSelector("button")).click();

        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
        // Wait for a process to complete
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver webDriver) {
                return progressbar.getAttribute("value").equals("100");
            }
        });

        AssertJUnit.assertEquals("100", progressbar.getAttribute("value"));
        AssertJUnit.assertTrue(driver.findElement(By.cssSelector("text-area")).getText().contains("Done!"));
    }

}