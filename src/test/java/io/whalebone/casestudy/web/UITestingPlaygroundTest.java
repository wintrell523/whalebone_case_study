package io.whalebone.casestudy.web;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class UITestingPlaygroundTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;

    @BeforeClass
    public void setUp() {
        // Initialize Playwright and launch a browser
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterClass
    public void tearDown() {
        // Close the browser and release resources
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test
    public void testSampleAppFunctionality() {
        // Open the home page and navigate to Sample App
        page.navigate("http://uitestingplayground.com/");
        page.click("text=Sample App");
        // Assert that we are on the correct page
        assert page.title().contains("Sample App");

        // Test login with valid credentials
        page.getByPlaceholder("User Name").fill("testuser");
        page.getByPlaceholder("********").fill("pwd");
        page.click("#login");
        assert page.textContent("#loginstatus").contains("Welcome, testuser!");

        // Test logout
        page.click("#login");
        assert page.textContent("#loginstatus").contains("User logged out.");

        // Test login with invalid credentials
        page.getByPlaceholder("User Name").fill("wrong");
        page.getByPlaceholder("********").fill("wrong");

        page.click("#login");
        assert page.textContent("#loginstatus").contains("Invalid username/password");
    }

    @Test
    public void testLoadDelay() {
        // Open the home page and navigate to Load Delay
        page.navigate("http://uitestingplayground.com/");
        page.click("text=Load Delay");

        // Wait for the button to appear within a reasonable time
        page.waitForSelector(".btn-primary", new Page.WaitForSelectorOptions().setTimeout(15000));
        // Assert that the button is visible
        assert page.isVisible(".btn-primary");
    }

    @Test
    public void testProgressBar() {
        // Open the home page and navigate to Progress Bar
        page.navigate("http://uitestingplayground.com/");
        page.click("text=Progress Bar");

        // Click the start button to begin the progress bar
        page.click("#startButton");

        // Wait for the progress bar to reach 100% or until the timeout
        page.waitForSelector("#progressBar[aria-valuenow='75']", new Page.WaitForSelectorOptions().setTimeout(50000));

        page.click("#stopButton");
        
        // Get the text from the result element
        String resultText = page.textContent("#result");

        // Assert that the result does NOT contain 'n/a'
        assert !resultText.toLowerCase().contains("n/a") : "Result contains 'n/a'";

        // Parse the result value
        // Example: "Result: 1, duration: 14574"
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("Result:\\s*(-?\\d+)");
        java.util.regex.Matcher matcher = pattern.matcher(resultText);
        assert matcher.find() : "Result value not found in string: " + resultText;

        int resultValue = Integer.parseInt(matcher.group(1));
        // Assert that the result is 0 or a positive integer
        assert resultValue >= 0 : "Result value is negative: " + resultValue;
    }
}
