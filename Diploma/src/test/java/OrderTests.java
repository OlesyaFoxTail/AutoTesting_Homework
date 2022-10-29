import net.bytebuddy.description.field.FieldList;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OrderTests {
    private static WebDriver driver;
    private static WebDriverWait wait;
//    private static By headerAccountLink = By.xpath("//a[@class='account']"); //Вход в ак через линк в хэдере

    @RegisterExtension
    ListenerTest watcher = new ListenerTest(driver, "target/surefire-reports");

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "drivers//chromedriver_win32//chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.manage().window().setSize(new Dimension(1024, 1000));
        driver.navigate().to("http://intershop5.skillbox.ru/");

    }
    @AfterAll
    public static void tearDown() {
        driver.quit();
    }

    private By orderMenuLink = By.xpath("//li[contains(@class,'menu-item')]/a[contains(text(),'Оформление заказа')]"); //Ссылка в меню оформление заказа
    private By cartEmptyNotification = By.xpath("//p[contains(@class,'cart')]"); // Сообщение, что товара нет в корзине
    private By loginButton = By.xpath("//button[@name='login']"); //Кнопка вход - стр авторизация
    private By userNameInputLogin = By.xpath("//input[@id='username']");// поле имя или почта
    private By userPasswordInputLogin = By.xpath("//input[@id='password']"); // поле пароль - стр авторизация
    private By headerCatalogButton = By.xpath("//ul[@id='menu-primary-menu']/li[contains(@class,'menu-item-object-product_cat')]/a");// Ссылка в главном меню на каталог
    private By addToCartButton = By.xpath("//a[contains(@class,'add_to_cart_button')]"); //Кнопка в корзину карточки
    private By addedToCart = By.xpath("//a[contains(@class,'added_to_cart')]"); //добавлено в корзину
    private By nonAuthLink = By.xpath("//a[@class='showlogin']"); // Сcылка - авторизуйтесь
    private By billingFirstName = By.xpath("//input[@id='billing_first_name']");// поле формы имя
    private By billingLastName = By.xpath("//input[@id='billing_last_name']"); //поле формы фамилия
    private By billingAddressOne = By.xpath("//input[@id='billing_address_1']"); //поле формы адрес
    private By billingCity = By.xpath("//input[@id='billing_city']");//Поле формы город
    private By billingState = By.xpath("//input[@id='billing_state']");//поле формы регион
    private By billingPostcode = By.xpath("//input[@id='billing_postcode']");// поле формы индекс
    private By billingPhone = By.xpath("//input[@id='billing_phone']");// поле формы телефон
    private By billingEmail = By.xpath("//input[@id='billing_email']");//поле формы почта
    private By placeOrderButton = By.xpath("//button[@id='place_order']");// кнопка разместить заказ
    private By bankPaymentNotification = By.xpath("//li[contains(@class,'payment-method')]/strong");//
    private By radioButtonAfterDeliveryPayment = By.xpath("//input[@id='payment_method_cod']");//Радиокнопка оплата при доставке
    private By radioButtonBankPayment = By.xpath("//input[@id='payment_method_bacs']");//Радиокнопка банковская оплата
    private By countryArrow = By.xpath("//span[@class='select2-selection__arrow']"); // Вызов выпадающего списка стран
    private By chooseCountry = By.xpath("//span[@id='select2-billing_country-container']");//Поле выбрать страну
    private By noCountry = By.xpath("//li[contains(text(),'Select a country / region…')]");//Поле без выбора страны
    private By countryField = By.xpath("//input[@class='select2-search__field']"); //Поле ввода наименования страны
    private By logOut = By.xpath("//a[contains(text(),'Выйти')]"); //выход из акк
    private By titleOrder = By.xpath("//h2[contains(text(),'Заказ получен')]");// Заголовок получения заказа
    private By alert = By.xpath("//ul[@class='woocommerce-error']");//набор алертов неверно заполненные поля
    private By titleGotOrder = By.xpath("//h2[contains(text(),'Заказ получен')]");// Заголовок страницы получения заказа
    private By billingInputs = By.xpath("//input[@class='input-text ']"); //Все биллинговые поля


    //Сценарий "Оформить заказ при пустой корзине, неавторизованным пользователем"
    @Test
    public void OrderByNotAuthUserWithoutAnyGoods() {
        //act
        driver.findElement(orderMenuLink).click();
        String getEmptyCartText = driver.findElement(cartEmptyNotification).getText();
        //assert
        Assertions.assertEquals("Корзина пуста.", getEmptyCartText, "Корзина не пуста");

    }

    //Сценарий "Оформить заказ с одним товаром в  корзине, неавторизованным пользователем"
    @Test
    public void OrderByNotAuthUserPositiveTest() {
        //act
        driver.findElement(headerCatalogButton).click();
        driver.findElement(addToCartButton).click();
        wait.until(ExpectedConditions.elementToBeClickable(addedToCart));
        driver.findElement(orderMenuLink).click();
        Boolean isLinkVisible = driver.findElement(nonAuthLink).isDisplayed();
        //assert
        Assertions.assertTrue(isLinkVisible, "Заказ товара доступен неавторизованному пользователю");
    }

    //Сценарий "Оформить заказ с одним товаром в  корзине, авторизованным пользователем c помощью банковского перевода"
    @Test
    public void OrderByAuthUserPositiveTestBankPaymentMethod() {
        //act
        driver.findElement(headerCatalogButton).click();
        driver.findElement(addToCartButton).click();
        wait.until(ExpectedConditions.elementToBeClickable(addedToCart));
        driver.findElement(orderMenuLink).click();
        driver.findElement(nonAuthLink).click();
        wait.until(ExpectedConditions.elementToBeClickable(userNameInputLogin));

        driver.findElement(userNameInputLogin).sendKeys("molo");
        driver.findElement(userPasswordInputLogin).sendKeys("molo");
        driver.findElement(loginButton).click();

        List<WebElement> field = driver.findElements(billingInputs);
        int fieldsCount = driver.findElements(billingInputs).size();
        for(int i = 0; i < fieldsCount; i++) {
            field.get(i).clear();
        }

        driver.findElement(billingFirstName).sendKeys("Molo");
        driver.findElement(billingLastName).sendKeys("Molo");
        driver.findElement(billingAddressOne).sendKeys("ул. Советская, д. 13");
        driver.findElement(billingCity).sendKeys("Адлер");
        driver.findElement(billingState).sendKeys("Адлерский");
        driver.findElement(billingPostcode).sendKeys("123456");
        driver.findElement(billingPhone).sendKeys("+7-123-45-67");
        driver.findElement(billingEmail).sendKeys("molo@molo.ru");
        driver.findElement(radioButtonBankPayment).click();
        driver.findElement(placeOrderButton).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(placeOrderButton));
        //act
        try {
            //act
            wait.until(ExpectedConditions.visibilityOfElementLocated(titleGotOrder));
            String getBankTitleReply = driver.findElement(bankPaymentNotification).getText();
            //assert
            Assertions.assertEquals("Прямой банковский перевод",getBankTitleReply, "Отражен неверный метод перевода");
            driver.findElement(logOut).click();
        }
        catch (Exception e) {
            //act
            driver.findElement(logOut).click();
        }
    }

    //Сценарий "Оформить заказ с одним товаром в  корзине, авторизованным пользователем c оплатой при доставке"
    @Test
    public void OrderByAuthUserPositiveTestReceivePaymentMethod() {
        //act
        driver.findElement(headerCatalogButton).click();
        driver.findElement(addToCartButton).click();
        wait.until(ExpectedConditions.elementToBeClickable(addedToCart));
        driver.findElement(orderMenuLink).click();
        driver.findElement(nonAuthLink).click();
        wait.until(ExpectedConditions.elementToBeClickable(userNameInputLogin));
        driver.findElement(userNameInputLogin).sendKeys("molo");
        driver.findElement(userPasswordInputLogin).sendKeys("molo");
        driver.findElement(loginButton).click();

        List<WebElement> field = driver.findElements(billingInputs);
        int fieldsCount = driver.findElements(billingInputs).size();
        for(int i = 0; i < fieldsCount; i++) {
            field.get(i).clear();
        }

        driver.findElement(billingFirstName).sendKeys("Molo");
        driver.findElement(billingLastName).sendKeys("Molo");
        driver.findElement(billingAddressOne).sendKeys("ул. Советская, д. 13");
        driver.findElement(billingCity).sendKeys("Адлер");
        driver.findElement(billingState).sendKeys("Адлерский");
        driver.findElement(billingPostcode).sendKeys("123456");
        driver.findElement(billingEmail).sendKeys("molo@molo.ru");
        driver.findElement(billingPhone).sendKeys("+7-123-45-67");
        driver.findElement(radioButtonAfterDeliveryPayment).click();
        driver.findElement(placeOrderButton).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(placeOrderButton));
        //act
        try {
            //act
            wait.until(ExpectedConditions.visibilityOfElementLocated(titleGotOrder));
            String getBankTitleReply = driver.findElement(bankPaymentNotification).getText();
            //assert
            Assertions.assertEquals("Оплата при доставке",getBankTitleReply, "Отражен неверный метод перевода");
            driver.findElement(logOut).click();
        }
        catch (Exception e) {
            //act
            driver.findElement(logOut).click();
        }
    }

    //Сценарий "Оформить заказ с одним товаром в  корзине, авторизованным пользователем c помощью банковского перевода"
    // и частью незаполенных полей
    @ParameterizedTest
    @EnumSource(PairwiseSomethingEmpty.class)
    void TestSomeFieldEmpty(PairwiseSomethingEmpty pairwiseSomethingEmpty){
        driver.findElement(headerCatalogButton).click();
        driver.findElement(addToCartButton).click();
        wait.until(ExpectedConditions.elementToBeClickable(addedToCart));
        driver.findElement(orderMenuLink).click();
        driver.findElement(nonAuthLink).click();
        wait.until(ExpectedConditions.elementToBeClickable(userNameInputLogin));
        driver.findElement(userNameInputLogin).sendKeys("molo");
        driver.findElement(userPasswordInputLogin).sendKeys("molo");
        driver.findElement(loginButton).click();

        List<WebElement> field = driver.findElements(billingInputs);
        int fieldsCount = driver.findElements(billingInputs).size();
        for(int i = 0; i < fieldsCount; i++) {
            field.get(i).clear();
        }

        driver.findElement(billingFirstName).sendKeys(pairwiseSomethingEmpty.getNameNew());
        driver.findElement(billingLastName).sendKeys(pairwiseSomethingEmpty.getFamilyName());
        driver.findElement(billingAddressOne).sendKeys(pairwiseSomethingEmpty.getAddress());
        driver.findElement(countryArrow).click();
        driver.findElement(countryField).sendKeys(pairwiseSomethingEmpty.getCountry());
        driver.findElement(By.xpath("//li[@aria-selected='true']")).click();
        driver.findElements(billingCity).clear();
        driver.findElement(billingCity).sendKeys(pairwiseSomethingEmpty.getCity());
        driver.findElement(billingState).sendKeys(pairwiseSomethingEmpty.getState());
        driver.findElement(billingPostcode).sendKeys(pairwiseSomethingEmpty.getPostCode());
        driver.findElement(billingPhone).sendKeys(pairwiseSomethingEmpty.getPhone());
        driver.findElement(billingEmail).sendKeys(pairwiseSomethingEmpty.getEmail());
        driver.findElement(radioButtonAfterDeliveryPayment).click();
        driver.findElement(placeOrderButton).click();

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(titleGotOrder));
            String getTitle = driver.findElement(titleOrder).getText();
            //assert
            Assertions.assertEquals("Заказ получен", getTitle, "Заказ получен с незаполненными обязательными полями");
            //act
            driver.findElement(logOut).click();
        }
        catch (Exception e) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(alert));
            boolean getAlert = driver.findElement(alert).isDisplayed();
            //assert
            Assertions.assertTrue(getAlert,"Алерт о незаполненных полях");
            //act
            driver.findElement(logOut).click();
        }
    }
}

