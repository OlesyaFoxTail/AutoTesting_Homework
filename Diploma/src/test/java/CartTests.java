import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CartTests {
    private static WebDriver driver;
    private static WebDriverWait wait;

    @RegisterExtension
    ListenerTest watcher = new ListenerTest(driver, "target/surefire-reports");

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "drivers//chromedriver_win32//chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @BeforeEach
    public void openEach(){
        driver.navigate().to("http://intershop5.skillbox.ru/");
    }

    @AfterEach
    public void cleanUpEach(){
       //act
        List <WebElement> allQtyEmptyList = driver.findElements(goodsQty);
        int getAllQtyEmpty = allQtyEmptyList.size();
        for (int i = 0; i < getAllQtyEmpty; i++) {
            allQtyEmptyList = driver.findElements(goodsQty);
            allQtyEmptyList.get(i).clear();
        }
        wait.until(ExpectedConditions.invisibilityOfAllElements(allQtyEmptyList));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='cart-empty woocommerce-info']")));
    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }

    private By cartMenuLinkHeader = By.xpath("//div[@class='store-menu']//a[contains(text(), 'Корзина')]"); //Кнопка в хедере Корзина
    private By spanFlag = By.xpath("//div[@class='ak-container']/div[1]/span");//флаг с наименование раздела на странице соответствующего раздела
    private By headerCatalogButton = By.xpath("//ul[@id='menu-primary-menu']/li[contains(@class,'menu-item-object-product_cat')]/a");// Ссылка в главном меню на каталог
    private By addToCartButton = By.xpath("//a[contains(@class,'add_to_cart_button')]"); //Кнопка в корзину карточки
    private By addedToCart = By.xpath("//a[contains(@class,'added_to_cart')]"); //добавлено в корзину
    private By goodsQty = By.xpath("//input[@type='number']"); //Количество товара в корзине по счетчику
    private By catalogPageLast = By.xpath("//ul[@class='page-numbers']/li[last()]"); //Страница каталога последняя
    private By amountPerProduct = By.xpath("//td[@class='product-subtotal']/span[contains(@class,'amount')]"); //Общая стоимость продукта
    private By totalAmount = By.xpath("//tr[@class='order-total']//bdi"); //Корзина сумма заказа
    private By couponCodeField = By.xpath("//input[@id='coupon_code']"); //Поле ввода купона
    private By applyCoupon = By.xpath("//button[@name='apply_coupon']"); // Кнопка применить купон
    private By removeCoupon = By.xpath("//a[@class='woocommerce-remove-coupon']"); //Удалить купон
    private By noCoupon = By.xpath("//div[contains(text(), 'Купон удален.\t')]"); //алерт купон удален

    //Сценарий "Открыть страницу "Корзина" по клику на кнопку главного меню Корзина"
    @Test
    public void GoToBasketByСartLinkMainMenu() {
        //act
        driver.findElement(cartMenuLinkHeader).click();
        String getSpanFlagName = driver.findElement(spanFlag).getText();
        //assert
        Assertions.assertEquals("Корзина", getSpanFlagName, "Это не страница Корзина" );
    }

    // Сценарий "Положить один товар в корзину со страницы "Каталог" и проверить находится ли он на странице "Корзина"
    @Test
    public void PutOneGoodsToCart() {
        //act
        driver.findElement(headerCatalogButton).click();
        driver.findElement(addToCartButton).click();
        wait.until(ExpectedConditions.elementToBeClickable(addedToCart));
        driver.findElement(addedToCart).click();
        String getGoodsQty = (driver.findElement(goodsQty).getAttribute("value"));
        //assert
        Assertions.assertEquals("1", getGoodsQty, "Количество больше одного");
    }

//Сценарий "Положить в корзину все доступные к покупке товары с последней страницы "Каталога" и проверить их количество
// на странице "Корзина"
    @Test
    public void PutAllGoodsToCartPageLastAndCheckQty() {
        //act
        driver.findElement(headerCatalogButton).click();
        driver.findElement(catalogPageLast).click();
        List<WebElement> cardButtonList = driver.findElements(addToCartButton);
        int totalButtons = cardButtonList.size();
        for (int i = 0; i < totalButtons; i++) {
            cardButtonList = driver.findElements(addToCartButton);
            cardButtonList.get(i).click();
        }
/* Проверяем работает ли Jquery*/
        Boolean isJqueryUsed = (Boolean) ((JavascriptExecutor) driver)
                .executeScript("return (typeof(jQuery) != 'undefined')");
        if (isJqueryUsed) {
            while (true) {
                // JavaScript test to verify jQuery is active or not
                Boolean ajaxIsComplete = (Boolean) (((JavascriptExecutor) driver)
                        .executeScript("return jQuery.active == 0"));
                if (ajaxIsComplete)
                    break;
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException e) {
                }
            }
        }
/* Окончание проверки*/
        driver.findElement(cartMenuLinkHeader).click();
        int sum = 0;
        List <WebElement> allGoodsQtyList = driver.findElements(goodsQty);
        int getAllGoodsQty = allGoodsQtyList.size();
        for (int i = 0; i < getAllGoodsQty; i++) {
            allGoodsQtyList = driver.findElements(goodsQty);
            int pcQty = Integer.parseInt(allGoodsQtyList.get(i).getAttribute("value"));
            sum = sum + pcQty;
        }
        //assert
        Assertions.assertEquals(totalButtons, sum, "Количество не соответствует выбранным товаром");
        //act
    }

//    Сценарий "Проверить соответствие общей суммы заказа в поле "Общая стоимость" на странице "Корзина"
//    и значение в поле "К оплате" после изменения количества в корзине"
    @Test
    public void PutAllGoodsToCartPageOneAndCheckAmount() {
        //act
        driver.findElement(headerCatalogButton).click();
        List<WebElement> cardButtonList = driver.findElements(addToCartButton);
        int totalButtons = cardButtonList.size();
        for (int i = 0; i < totalButtons; i++) {
            cardButtonList = driver.findElements(addToCartButton);
            cardButtonList.get(i).click();
        }
        /* Проверяем работает ли Jquery*/
        Boolean isJqueryUsed = (Boolean) ((JavascriptExecutor) driver)
                .executeScript("return (typeof(jQuery) != 'undefined')");
        if (isJqueryUsed) {
            while (true) {
                // JavaScript test to verify jQuery is active or not
                Boolean ajaxIsComplete = (Boolean) (((JavascriptExecutor) driver)
                        .executeScript("return jQuery.active == 0"));
                if (ajaxIsComplete)
                    break;
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException e) {
                }
            }
        }
        /* Окончание проверки*/
        driver.findElement(cartMenuLinkHeader).click();
        driver.findElement(goodsQty).click();
        driver.findElement(goodsQty).sendKeys("2");
        double sum = 0.0;
          var  totalAmountItems = driver.findElements(amountPerProduct);

            List<String> textList = new ArrayList<String>();
            for (WebElement elem : totalAmountItems) {
                textList.add(elem.getText().split("₽")[0].replace(",", "."));
            }
            int getCount = textList.size();
            for (int i = 0; i < getCount; i++) {
                double amount = Double.parseDouble(textList.get(i));
                sum = sum + amount;
            }
            String expectedResultSum = driver.findElement(totalAmount).getText();
            double getExpectedResultSum = Double.parseDouble(expectedResultSum.split("₽")[0].replace(",", "."));
        //assert
        Assertions.assertEquals(getExpectedResultSum, sum, "Сумма считается неверно");
    }

    //   Сценарий "Проверить соответствие общей суммы заказа в поле "Общая стоимость" на странице "Корзина"
    //   и значение в поле "К оплате" после применения сертификата sert500 номиналом 500 р"
    @Test
    public void sertChecking() {
        //act
        driver.findElement(headerCatalogButton).click();
        List<WebElement> cardButtonList = driver.findElements(addToCartButton);
        int totalButtons = cardButtonList.size();
        for (int i = 0; i < totalButtons; i++) {
            cardButtonList = driver.findElements(addToCartButton);
            cardButtonList.get(i).click();
        }
        wait.until(ExpectedConditions.jsReturnsValue("return jQuery.active == 0"));
        driver.findElement(cartMenuLinkHeader).click();
        double sum = 0.0;
        var  totalAmountItems = driver.findElements(amountPerProduct);

        List<String> textList = new ArrayList<String>();
        for (WebElement elem : totalAmountItems) {
            textList.add(elem.getText().split("₽")[0].replace(",", "."));
        }
        int getCount = textList.size();
        for (int i = 0; i < getCount; i++) {
            double amount = Double.parseDouble(textList.get(i));
            sum = sum + amount;
        }
        String expectedResultSum = driver.findElement(totalAmount).getText();
        double getExpectedResultSum = Double.parseDouble(expectedResultSum.split("₽")[0].replace(",", "."));
        //assert
        Assertions.assertTrue(getExpectedResultSum > 500, "Cумма заказа недостаточна");
        //act
        driver.findElement(couponCodeField).sendKeys("sert500");
        driver.findElement(applyCoupon).click();
        wait.until(ExpectedConditions.jsReturnsValue("return jQuery.active == 0"));
        String expectedResultSumNew = driver.findElement(totalAmount).getText();
        double getExpectedResultSumNew = Double.parseDouble(expectedResultSumNew.split("₽")[0].replace(",", "."));
        //assert
        double correctResultChecking  = getExpectedResultSum - getExpectedResultSumNew;
        boolean result = correctResultChecking == 500.00;
        Assertions.assertTrue(result, "Cкидка посчитана неверно");
        //act
        driver.findElement(removeCoupon).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(noCoupon));

    }
}
