package pl.softie;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import java.util.concurrent.TimeUnit;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProjectMetalShop{

    static WebDriver driver = new ChromeDriver();
    Faker faker = new Faker();
    Wait wait = new WebDriverWait(driver,5);
    private String username = "testowy@wp.pl";
    private String password = "zaq1@#$RFV";
    private String wrongPass = "zaq1@WSX";

    static boolean loginMethod(String username, String password){
        driver.findElement(By.cssSelector("#username")).sendKeys(username);
        driver.findElement(By.cssSelector("#password")).sendKeys(password);
        driver.findElement(By.cssSelector(".woocommerce-form-login__submit")).click();
        return driver.findElement(By.xpath("//a[contains(text(), 'Kokpit')]")).isDisplayed();
    }

    @BeforeAll
    public static void prepareBrowser(){
        driver.manage().window().maximize();
        driver.get("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772/");
        driver.manage().timeouts().implicitlyWait( 5 , TimeUnit. SECONDS);
    }
    @Order(1)
    @Test
    public void emptyUsernameLogin(){
        driver.findElement(By.xpath("//a[text() = 'Moje konto']")).click();
        driver.findElement(By.cssSelector("#password")).sendKeys(password);
        driver.findElement(By.xpath("//button[@name='login']")).click();

        String errorText = driver.findElement(By.className("woocommerce-error")).getText();
        Assertions.assertEquals("Błąd: Nazwa użytkownika jest wymagana.", errorText);
    }

    @Order(2)
    @Test
    public void emptyPasswordLogin(){
        driver.findElement(By.xpath("//a[text() = 'Moje konto']")).click();
        driver.findElement(By.cssSelector("#username")).sendKeys(username);
        driver.findElement(By.xpath("//button[@name='login']")).click();

        String errorText = driver.findElement(By.className("woocommerce-error")).getText();
        Assertions.assertEquals(String.format("Błąd: pole hasła jest puste."),
                driver.findElement(By.cssSelector(".woocommerce-error")).getText());
    }

    @Order(3)
    @Test
    public void successRegister(){
        driver.findElement(By.linkText("register")).click();
        String userName = faker.pokemon().name();;
        String email = userName+faker.name().username()+"@wp.pl";
        driver.findElement(By.cssSelector("#user_login")).sendKeys(userName);
        driver.findElement(By.cssSelector("#user_email")).sendKeys(email);
        driver.findElement(By.cssSelector("#user_pass")).sendKeys(password);
        driver.findElement(By.cssSelector("#user_confirm_password")).sendKeys(password);
        driver.findElement(By.cssSelector(".ur-submit-button ")).click();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        String successMessage = driver.findElement(By.cssSelector("#ur-submit-message-node")).getText();
        Assertions.assertEquals("User successfully registered.",successMessage);
    }

    @Order(4)
    @Test
    public void homePageHasLogoAndSearch(){
        WebElement search = driver.findElement(By.cssSelector("#woocommerce-product-search-field-0"));
        String logoText = driver.findElement(By.cssSelector(".site-title")).getText();
        Assertions.assertEquals("Softie Metal Shop",logoText);
        Assertions.assertTrue(search.isDisplayed());
    }

    @Order(5)
    @Test
    public void loginPageHasLogoAndSearch(){
        driver.findElement(By.xpath("//a[text()='Moje konto']")).click();
        WebElement search = driver.findElement(By.cssSelector("#woocommerce-product-search-field-0"));
        String logoText = driver.findElement(By.cssSelector(".site-title")).getText();

        Assertions.assertEquals("Softie Metal Shop",logoText);
        Assertions.assertTrue(search.isDisplayed());
    }

    @Order(6)
    @Test
    public void goFromHomePageToContact(){
        driver.findElement(By.xpath("//a[text()='Kontakt']")).click();
        Assertions.assertTrue(driver.findElement(By.cssSelector(".entry-title")).isDisplayed());
    }

    @Order(7)
    @Test
    public void goFromLogoPageToHomePage(){
        driver.get("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772/moje-konto/");
        driver.findElement(By.linkText("Strona główna")).click();
        Assertions.assertTrue(driver.findElement(By.cssSelector(".page-title")).isDisplayed());
    }

    @Order(8)
    @Test
    public void testSendMessage(){
        driver.get("http://serwer169007.lh.pl/autoinstalator/serwer169007.lh.pl/wordpress10772/kontakt/");
        driver.findElement(By.xpath("//input[@name='your-name']")).sendKeys("Jan Kowalski");
        driver.findElement(By.xpath("//input[@name='your-email']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@name='your-subject']")).sendKeys("Test");
        driver.findElement(By.xpath("//textarea[@name='your-message']")).sendKeys("Test");
        driver.findElement(By.xpath("//input[@value='Wyślij']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".wpcf7-response-output")));
        Assertions.assertTrue(driver.findElement(By.cssSelector(".wpcf7-response-output")).isDisplayed());
    }

    @Order(9)
    @Test
    public void addingTheProductToTheCart(){
        String textCart = driver.findElement(By.cssSelector(".amount")).getText();
        Assertions.assertEquals("0,00 zł",textCart);
        driver.findElement(By.xpath("//a[@data-product_id='24']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class = 'count']")));
        Assertions.assertTrue(driver.findElement(By.xpath("//span[@class = 'count']")).isDisplayed());
    }

    @Order(10)
    @Test
    public void addingAndRemoveTheProduct(){
        driver.findElement(By.xpath("//a[@data-product_id='24']")).click();
        driver.findElement(By.xpath("//a[@title='Zobacz koszyk']")).click();
        driver.findElement(By.xpath("//table//tbody//tr//td//a")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".woocommerce-message")));
        Assertions.assertTrue(driver.findElement(By.cssSelector(".woocommerce-message")).isDisplayed());
    }

    @Order(11)
    @Test
    public void loginSuccess() {
        driver.findElement(By.xpath("//a[text() = 'Moje konto']")).click();
        loginMethod("Testowy@wp.pl","zaq1@#$RFV");

    }
    @Order(12)
    @Test
    public void addingTheProductInThePromotion(){
    List<WebElement> listPromotion = driver.findElements(By.xpath("//li//span[@class='onsale']"));
    double sumOfPaymants =0;

    for(int i=0; i<= listPromotion.size()-1;i++){

        driver.findElements(By.xpath("//li//span[@class='onsale']")).get(i).click();
        double value = Double.parseDouble(driver.findElement(By.xpath("//p[@class='price']//ins//bdi")).getText().substring(0,7).replace(",","."));
        sumOfPaymants+=value;
        driver.findElement(By.xpath("//button[@name='add-to-cart']")).click();
        driver.findElement(By.xpath("//nav[@class='woocommerce-breadcrumb']//a[text()='Strona główna']")).click();
    }

    Assertions.assertEquals(sumOfPaymants,Double.parseDouble(driver.findElement(By.cssSelector(".amount")).getText().substring(0,7).replace(",",".")));
    }

    @AfterAll
    public static void closeBrowser(){
        driver.quit();
    }
}
