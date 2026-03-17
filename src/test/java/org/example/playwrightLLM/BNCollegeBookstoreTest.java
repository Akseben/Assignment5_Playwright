package org.example.playwrightLLM;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 test suite for BN College bookstore UI testing using Playwright.
 * Tests the complete user journey: search, filter, product selection, cart operations, and checkout.
 * Generated with GitHub Copilot via Playwright MCP, restructured for shared browser session.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BNCollegeBookstoreTest {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    private static final String BASE_URL = "https://depaul.bncollege.com/";
    private static final int TIMEOUT = 30000;

    @BeforeAll
    static void setupBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false));
        context = browser.newContext();
        page = context.newPage();
        page.setDefaultTimeout(TIMEOUT);
        page.setDefaultNavigationTimeout(TIMEOUT);
        page.navigate(BASE_URL);
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    @AfterAll
    static void teardownBrowser() {
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @Test
    @Order(1)
    @DisplayName("Search for earbuds in the search box")
    void testSearchForEarbuds() {
        Locator searchBox = page.locator("#bned_site_search");
        assertTrue(searchBox.isVisible(), "Search box should be visible");

        searchBox.click();
        searchBox.fill("earbuds");
        page.keyboard().press("Enter");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        Locator searchResults = page.locator("[role='main']");
        assertTrue(searchResults.isVisible(), "Search results should be displayed");
    }

    @Test
    @Order(2)
    @DisplayName("Filter by Brand: JBL, Color: Black, Price: Over $50")
    void testFilterProducts() {
        // Apply Brand filter: JBL
        page.locator("[data-target='#facet-brand']").click();
        Locator jblOption = page.getByText("JBL", new Page.GetByTextOptions().setExact(false)).first();
        if (jblOption.isVisible()) {
            jblOption.click();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        }

        // Apply Color filter: Black
        page.locator("[data-target='#facet-Color']").click();
        Locator blackOption = page.getByText("Black", new Page.GetByTextOptions().setExact(false)).first();
        if (blackOption.isVisible()) {
            blackOption.click();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        }

        // Apply Price filter: Over $50
        page.locator("[data-target='#facet-price']").click();
        Locator overFiftyOption = page.locator("#facet-price svg").first();
        if (overFiftyOption.isVisible()) {
            overFiftyOption.click();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        }

        Locator mainContent = page.locator("[role='main']");
        assertTrue(mainContent.isVisible(), "Filtered results should be displayed");
    }

    @Test
    @Order(3)
    @DisplayName("Click the JBL Quantum True Wireless Noise Cancelling Gaming Earbuds")
    void testClickJBLQuantumProduct() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("JBL Quantum True Wireless")).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.waitForCondition(() -> page.locator("body").textContent().contains("JBL"));

        assertTrue(page.locator("body").textContent().contains("JBL"),
                "Product detail page should be loaded");
    }

    @Test
    @Order(4)
    @DisplayName("Assert the product name, SKU, price, and description")
    void testAssertProductDetails() {
        // ✅ Use assertThat which has built-in retry/wait unlike textContent()
        com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat(
                page.getByLabel("main")).containsText("JBL");

        com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat(
                page.getByLabel("main")).containsText("sku");

        com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat(
                page.getByLabel("main")).containsText("$");

        assertTrue(page.locator("body").textContent().length() > 0,
                "Page content should not be empty");
    }

    @Test
    @Order(5)
    @DisplayName("Add 1 to cart and assert cart shows 1 item")
    void testAddToCart() {
        page.getByLabel("Add to cart").click();
        page.waitForCondition(() -> page.locator("#headerDesktopView").textContent().contains("1 items"));

        assertTrue(page.locator("#headerDesktopView").textContent().contains("1 items"),
                "Cart should show 1 item");

        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cart 1 items")).click();

        // ✅ Wait until we're actually on the cart page before moving on
        page.waitForURL("**/cart**");
        page.waitForCondition(() -> page.locator("body").textContent().contains("Your Shopping Cart"));
    }

    @Test
    @Order(6)
    @DisplayName("Proceed through checkout as a guest")
    void testCheckoutAsGuest() {
        assertTrue(page.locator("body").textContent().contains("Your Shopping Cart"),
                "Should be on shopping cart page");

        // Select in-store pickup
        page.getByText("FAST In-Store PickupDePaul").click();

        // Apply and verify failed promo code
        page.getByLabel("Enter Promo Code").fill("TEST");
        page.getByLabel("Apply Promo Code").click();
        assertTrue(page.locator("#js-voucher-result").textContent().contains("not valid"),
                "Promo code should be rejected");

        // Proceed to checkout
        page.getByLabel("Proceed To Checkout").click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        // Verify create account page and proceed as guest
        assertTrue(page.locator("body").textContent().contains("Create Account"),
                "Create Account page should be displayed");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Proceed As Guest")).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    @Test
    @Order(7)
    @DisplayName("Fill in contact information and continue")
    void testFillContactInformation() {
        assertTrue(page.locator("body").textContent().contains("Contact Information"),
                "Should be on Contact Information page");

        page.getByPlaceholder("Please enter your first name").fill("Test First Name");
        page.getByPlaceholder("Please enter your last name").fill("Test Last Name");
        page.getByPlaceholder("Please enter a valid email").fill("testemail@gmail.com");
        page.getByPlaceholder("Please enter a valid phone").fill("1234567890");

        assertTrue(page.locator("body").textContent().contains("$164.98"),
                "Order subtotal should be visible");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    @Test
    @Order(8)
    @DisplayName("Verify pickup information page")
    void testVerifyPickupInformation() {
        assertTrue(page.locator("body").textContent().contains("Test First Name"),
                "First name should be displayed");
        assertTrue(page.locator("body").textContent().contains("Test Last Name"),
                "Last name should be displayed");
        assertTrue(page.locator("body").textContent().contains("testemail@gmail.com"),
                "Email should be displayed");

        assertTrue(page.locator("#bnedPickupPersonForm").textContent().contains("DePaul University Loop Campus & SAIC"),
                "Pickup location should be DePaul");

        com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat(
                page.getByLabel("I'll pick them up")).isChecked();

        assertTrue(page.locator("body").textContent().contains("$164.98"),
                "Order subtotal should be visible");

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    @Test
    @Order(9)
    @DisplayName("Continue to payment page and go back to cart")
    void testContinueToPaymentAndReturn() {
        String pageContent = page.locator("body").textContent();
        assertTrue(pageContent.contains("$164.98"),
                "Order subtotal should be visible on payment page");
        assertTrue(pageContent.contains("JBL Quantum True Wireless"),
                "Product name should be visible on payment page");

        // ✅ Use href pattern instead of link text which may vary on payment page
        page.locator("a[href*='cart']").first().click();
        page.waitForURL("**/cart**");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }

    @Test
    @Order(10)
    @DisplayName("Delete the item and assert cart is empty")
    void testDeleteItemAndVerifyEmptyCart() {
        // ✅ Wait for cart page to fully load before looking for remove button
        page.waitForCondition(() -> page.locator("body").textContent().contains("Your Shopping Cart"));
        page.waitForSelector("[aria-label*='Remove product JBL Quantum']");
        page.getByLabel("Remove product JBL Quantum").click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        assertTrue(page.locator("#headerDesktopView").textContent().contains("0 items"),
                "Cart should be empty");
    }
}