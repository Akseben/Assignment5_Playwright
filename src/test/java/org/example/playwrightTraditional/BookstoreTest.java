package org.example.playwrightTraditional;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.*;

import java.nio.file.Paths;

import com.microsoft.playwright.Page;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookstoreTest {

    static Playwright playwright;
    static Browser browser;
    static BrowserContext context;
    static Page page;

    @BeforeAll
    static void setup(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(true));
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos/"))
                .setRecordVideoSize(1280, 720));
        page = context.newPage();
        page.navigate("https://depaul.bncollege.com/");
    }

    @AfterAll
    static void closeBrowser(){
        context.close();
        browser.close();
        playwright.close();
    }

    @Test
    @Order(1)
    void testBookstore(){
        page.getByPlaceholder("Enter your search details (").click();
        page.getByPlaceholder("Enter your search details (").fill("earbuds");
        page.getByPlaceholder("Enter your search details (").press("Enter");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("brand")).click();
        page.locator("#facet-brand").getByRole(AriaRole.LIST).locator("label").filter(new Locator.FilterOptions().setHasText("brand JBL (10)")).getByRole(AriaRole.IMG).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Color")).click();
        page.locator("label").filter(new Locator.FilterOptions().setHasText("Color Black (5)")).locator("svg").first().click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Price")).click();
        page.locator("#facet-price svg").first().click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("JBL Quantum True Wireless")).click();
        assertThat(page.getByLabel("main").getByRole(AriaRole.HEADING)).containsText("JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black");
        assertThat(page.getByLabel("main")).containsText("sku 668972707");
        assertThat(page.getByLabel("main")).containsText("$164.98");
        assertThat(page.getByLabel("main")).containsText("Adaptive noise cancelling allows awareness of environment when gaming on the go. Light weight, durable, water resist. USB-C dongle for low latency connection < than 30ms.");
        page.getByLabel("Add to cart").click();
        page.waitForCondition(() -> page.locator("#headerDesktopView").textContent().contains("1 items"));
        assertThat(page.locator("#headerDesktopView")).containsText("1 items");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cart 1 items")).click();
    }

    @Test
    @Order(2)
    void testShoppingCart(){
        assertThat(page.getByLabel("main")).containsText("Your Shopping Cart");
        assertThat(page.getByLabel("main")).containsText("JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black");
        assertThat(page.getByLabel("Quantity, edit and press")).hasValue("1");
        assertThat(page.getByLabel("main")).containsText("$164.98");
        page.getByText("FAST In-Store PickupDePaul").click();
        assertThat(page.getByLabel("main")).containsText("Subtotal $164.98");
        assertThat(page.getByLabel("main")).containsText("Handling To support the bookstore's ability to provide a best-in-class online and campus bookstore experience, and to offset the rising costs of goods and services, an online handling fee of $3.00 per transaction is charged. This fee offsets additional expenses including fulfillment, distribution, operational optimization, and personalized service. No minimum purchase required. $3.00");
        assertThat(page.getByLabel("main")).containsText("Taxes TBD");
        assertThat(page.getByLabel("main")).containsText("Estimated Total $167.98");
        page.getByLabel("Enter Promo Code").click();
        page.getByLabel("Enter Promo Code").fill("TEST");
        page.getByLabel("Apply Promo Code").click();
        assertThat(page.locator("#js-voucher-result")).containsText("The coupon code entered is not valid.");
        page.getByLabel("Proceed To Checkout").click();
    }

    @Test
    @Order(3)
    void testCreateAccountPage(){
        assertThat(page.getByLabel("main")).containsText("Create Account");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Proceed As Guest")).click();
    }

    @Test
    @Order(4)
    void testContactInfoPage(){
        assertThat(page.getByLabel("main")).containsText("Contact Information");
        page.getByPlaceholder("Please enter your first name").click();
        page.getByPlaceholder("Please enter your first name").fill("Test First Name");
        page.getByPlaceholder("Please enter your last name").click();
        page.getByPlaceholder("Please enter your last name").fill("Test Last Name");
        page.getByPlaceholder("Please enter a valid email").click();
        page.getByPlaceholder("Please enter a valid email").fill("testemail@gmail.com");
        page.getByPlaceholder("Please enter a valid phone").click();
        page.getByPlaceholder("Please enter a valid phone").fill("1234567890");
        assertThat(page.getByLabel("main")).containsText("Order Subtotal $164.98");
        assertThat(page.getByLabel("main")).containsText("Handling To support the bookstore's ability to provide a best-in-class online and campus bookstore experience, and to offset the rising costs of goods and services, an online handling fee of $3.00 per transaction is charged. This fee offsets additional expenses including fulfillment, distribution, operational optimization, and personalized service. No minimum purchase required. $3.00");
        assertThat(page.getByLabel("main")).containsText("Tax TBD");
        assertThat(page.getByLabel("main")).containsText("Total $167.98 167.98 $");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
    }

    @Test
    @Order(5)
    void testPickupInfoPage(){
        assertThat(page.getByLabel("main")).containsText("Test First Name");
        assertThat(page.getByLabel("main")).containsText("Test Last Name");
        assertThat(page.getByLabel("main")).containsText("testemail@gmail.com");
        assertThat(page.getByLabel("main")).containsText("11234567890");
        assertThat(page.locator("#bnedPickupPersonForm")).containsText("Pickup Location DePaul University Loop Campus & SAIC 1 E. Jackson Boulevard, , Illinois, Chicago, 60604");
        assertThat(page.getByLabel("main")).containsText("Order Subtotal $164.98");
        assertThat(page.getByLabel("main")).containsText("Handling To support the bookstore's ability to provide a best-in-class online and campus bookstore experience, and to offset the rising costs of goods and services, an online handling fee of $3.00 per transaction is charged. This fee offsets additional expenses including fulfillment, distribution, operational optimization, and personalized service. No minimum purchase required. $3.00");
        assertThat(page.getByLabel("main")).containsText("Tax TBD");
        assertThat(page.getByLabel("main")).containsText("Total $167.98 167.98 $");
        assertThat(page.getByLabel("main")).containsText("JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black");
        assertThat(page.getByLabel("main")).containsText("$164.98");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
    }

    @Test
    @Order(6)
    void testPaymentInfoPage(){
        assertThat(page.getByLabel("main")).containsText("Order Subtotal $164.98");
        assertThat(page.getByLabel("main")).containsText("Handling To support the bookstore's ability to provide a best-in-class online and campus bookstore experience, and to offset the rising costs of goods and services, an online handling fee of $3.00 per transaction is charged. This fee offsets additional expenses including fulfillment, distribution, operational optimization, and personalized service. No minimum purchase required. $3.00");
        assertThat(page.getByLabel("main")).containsText("Tax $17.22");
        assertThat(page.getByLabel("main")).containsText("Total $185.20 185.2 $");
        assertThat(page.getByLabel("main")).containsText("JBL Quantum True Wireless Noise Cancelling Gaming Earbuds- Black");
        assertThat(page.getByLabel("main")).containsText("$164.98");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Back to cart")).click();
    }

    @Test
    @Order(7)
    void testShoppingCartAgainPage(){
        page.getByLabel("Remove product JBL Quantum").click();
        assertThat(page.locator("#headerDesktopView")).containsText("0 items");
    }
}