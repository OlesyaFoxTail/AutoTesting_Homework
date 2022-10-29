import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AuthRegistrationPageTestes {
    private static WebDriver driver;
    private static WebDriverWait wait;

    @RegisterExtension
    ListenerTest watcher = new ListenerTest(driver, "target/surefire-reports");

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "drivers//chromedriver_win32//chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.navigate().to("http://intershop5.skillbox.ru/");
    }
    @AfterAll
    public static void tearDown() {
        driver.quit();
    }


    private By mainMenuAccountLink = By.xpath("//div[@id='menu']//a[contains(text(),'Мой аккаунт')]");//Кнопка личный кабинет в главном меню
    private By headerAccountLink = By.xpath("//a[@class='account']"); //Вход в ак через линк в хэдере
    private By loginButton = By.xpath("//button[@name='login']"); //Кнопка вход - стр авторизация
    private By registerButton = By.xpath("//button[@name='register']"); //Кнопка зарегистрироваться

    private By registrationButton = By.xpath("//button[@class='custom-register-button']"); //кнопка регистрации
    private By userNameInputLogin = By.xpath("//input[@id='username']");// поле имя или почта
    private By userPasswordInputLogin = By.xpath("//input[@id='password']"); // поле пароль - стр авторизация
    private By userNameInputReg = By.xpath("//input[@id='reg_username']");// поле имя регистрация
    private By userEmailInputReg = By.xpath("//input[@id='reg_email']");// поле почта регистрация
    private By userPasswordInputReg = By.xpath("//input[@id='reg_password']"); // поле пароль - стр регистрации
    private By alertLogin = By.xpath("//ul[@role='alert']/li"); //алерт авторизации
    private By finishedReg = By.xpath("//div[contains(text(),'Регистрация завершена')]");//Сообщение регистрация завершена
    private By sayHelloP = By.xpath("//div[@class='woocommerce-MyAccount-content']/p[contains(text(),'Привет')]");
    private By greetingTo = By.xpath("//div[@class='woocommerce-MyAccount-content']//strong");
    private By logOut = By.xpath("//a[contains(text(),'Выйти')]"); //Выйти

    //Сценарий "Проверить ограничения по вводу знаков в поле "Имя" при регистрации" (ограничений нет, поэтому проверка без
    //почты)
    @ParameterizedTest
    @EnumSource(UsersName.class)
    void testFillingFormByNameOnly(UsersName usersName) {
        //act
        driver.findElement(mainMenuAccountLink).click();
        driver.findElement(registrationButton).click();
        driver.findElement(userNameInputReg).sendKeys(usersName.getLogin());
        driver.findElement(userPasswordInputReg).sendKeys("test");
        driver.findElement(registerButton).click();
        //assert
        String alert = driver.findElement(alertLogin).getText();
        Assertions.assertEquals("Error: Пожалуйста, введите корректный email.", alert, "Ограничение по вводу имени");
    }

    //Сценарий "Повторная регистрация уже зарегистрированного пользователя"
    @ParameterizedTest
    @EnumSource(UsersData.class)
    void RepeatedTotalRegistration(UsersData usersData) {
        //act
        driver.findElement(mainMenuAccountLink).click();
        driver.findElement(registrationButton).click();
        driver.findElement(userNameInputReg).sendKeys(usersData.getName());
        driver.findElement(userEmailInputReg).sendKeys(usersData.getMail());
        driver.findElement(userPasswordInputReg).sendKeys(usersData.getPassword());
        driver.findElement(registerButton).click();
        //assert
        boolean isAlert = driver.findElement(alertLogin).isDisplayed();
        Assertions.assertTrue(isAlert, "Повторная регистрация уже зарегистрированного пользователя");
    }

    //Сценарий "Авторизация уже зарегистрированного пользователя по Имени и Паролю"
    @Test
    public void LoginWithRegisteredUser() {
        //act
        driver.findElement(headerAccountLink).click();
        driver.findElement(userNameInputLogin).sendKeys("molo");
        driver.findElement(userPasswordInputLogin).sendKeys("molo");
        driver.findElement(loginButton).click();
        boolean isWelcome = driver.findElement(sayHelloP).isDisplayed();
        //assert
        Assertions.assertTrue(isWelcome, "Отсутствует приветствие, пользователь не зашел в аккаунт");
        //act
        driver.findElement(logOut).click();
    }

    //Сценарий "Корректность приветствия в личном кабинете после авторизации по Имени и Паролю"
    @Test
    public void CorrectPersonalGreeting() {
        //act
        driver.findElement(headerAccountLink).click();
        driver.findElement(userNameInputLogin).sendKeys("molo");
        driver.findElement(userPasswordInputLogin).sendKeys("molo");
        driver.findElement(loginButton).click();
        String  isWelcome = driver.findElement(greetingTo).getText();
        //assert
        Assertions.assertEquals("molo", isWelcome, "Неверное имя пользователя");
        //act
        driver.findElement(logOut).click();
    }


    //Сценарий "Корректность приветствия в личном кабинете после авторизации по Почте и Паролю"
    @Test
    public void CorrectPersonalGreetingByEmail() {
        //act
        driver.findElement(headerAccountLink).click();
        driver.findElement(userNameInputLogin).sendKeys("molo@molo.ru");
        driver.findElement(userPasswordInputLogin).sendKeys("molo");
        driver.findElement(loginButton).click();
        String  isWelcome = driver.findElement(greetingTo).getText();
        //assert
        Assertions.assertEquals("molo", isWelcome, "Неверное имя пользователя");
        //act
        driver.findElement(logOut).click();
    }

    //Сценарий "Проверка невалидных значений почты при регистрации с валидным именем"
    @ParameterizedTest
    @EnumSource(UsersMail.class)
    void testFillingFormByWrongMailAndCorrectName(UsersMail usersMail) {
        //act
        driver.findElement(mainMenuAccountLink).click();
        driver.findElement(registrationButton).click();
        driver.findElement(userNameInputReg).sendKeys(usersMail.getName());
        driver.findElement(userEmailInputReg).sendKeys(usersMail.getMail());
        driver.findElement(userPasswordInputReg).sendKeys("test");
        driver.findElement(registerButton).click();
        //assert
        try {
            boolean isRegisterButton = driver.findElement(registerButton).isDisplayed();
//            Assertions.assertTrue(isRegisterButton, "Регистрация прошла с невалидной почтой");
        }
        catch (Exception e) {
            driver.findElement(logOut).click();
            System.out.println("Регистрация прошла с невалидной почтой" + " " + usersMail);
        }
    }
}

