import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainPageTests {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static WebElement hover;

    @RegisterExtension
    ListenerTest watcher = new ListenerTest(driver, "target/surefire-reports");

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "drivers//chromedriver_win32//chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }

    private By promoCardLinkBooks = By.xpath("//aside[@id='accesspress_storemo-2']//a");//первая карточка Книги в разделе промо
    private By entryTitleHeader = By.xpath("//h1[contains(@class,'entry-title')]"); //заголовок раздела на странице соответствующего раздела
    private By spanFlag = By.xpath("//div[@class='ak-container']/div[1]/span");//флаг с наименование раздела на странице соответствующего раздела
    private By promoCardLinkPads = By.xpath("//aside[@id='accesspress_storemo-3']//a");//вторая карточка Планшеты в разделе промо
    private By promoCardLinkPhoto = By.xpath("//aside[@id='accesspress_storemo-4']//a");//Третья карточка Фото в разделе промо
    private By mainLogo = By.xpath("//div[@id='site-branding']/a[1]");//лого сайта в хидере
    private By promoBlock = By.xpath("//div[@class='promo-wrap1']");//наличие блока промо на гавной странице
    private By headerMenuMainPageButton = By.xpath("//li[contains(@class,'menu-item-home')]//a");//ССылка в главном меню на главную страницу

    //Сценарий "Переход на станицу Каталог раздела Книги при клике на раздел Промотовары Книги"
    @Test
    public void PromoSectionBooksClickPassToCorrectPage() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");
        /* act */
        driver.findElement(promoCardLinkBooks).click();
        String getEntryTitleHeaderBooks = driver.findElement(entryTitleHeader).getText();
        String getSpanFlagBooks = driver.findElement(spanFlag).getText();
        //assert
        Assertions.assertEquals("КНИГИ", getEntryTitleHeaderBooks, "Неверный главный заголовок раздела книги");
        Assertions.assertEquals("Книги", getSpanFlagBooks, "Неверный заголовок флажка раздела книги");
    }

    //Сценарий "Переход на станицу Каталог раздела Планшеты при клике на раздел Промотовары Планшеты"
    @Test
    public void PromoSectionPadsClickPassToCorrectPage() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");
        /* act */
        driver.findElement(promoCardLinkPads).click();
        String getEntryTitleHeaderPads = driver.findElement(entryTitleHeader).getText();
        String getSpanFlagPads = driver.findElement(spanFlag).getText();
        //assert
        Assertions.assertEquals("ПЛАНШЕТЫ", getEntryTitleHeaderPads, "Неверный главный заголовок раздела книги");
        Assertions.assertEquals("Планшеты", getSpanFlagPads, "Неверный заголовок флажка раздела книги");
    }
//Сценарий "Переход на станицу Каталог раздела Фотоаппараты при клике
// на раздел Промотовары Фотоаппараты" и возврат на главную по клику на  лого
    @Test
    public void PromoSectionPhotoClickPassToCorrectPageAndBackByClickMainLogo() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");
        // act
        driver.findElement(promoCardLinkPhoto).click();
        String getEntryTitleHeaderPhoto = driver.findElement(entryTitleHeader).getText();
        String getSpanFlagPhoto = driver.findElement(spanFlag).getText();
        driver.findElement(mainLogo).click();
        //assert
        Assertions.assertEquals("ФОТО/ВИДЕО", getEntryTitleHeaderPhoto, "Неверный главный заголовок раздела фото");
        Assertions.assertEquals("Фото/Видео", getSpanFlagPhoto, "Неверный заголовок флажка раздела фото");
        //act
        driver.findElement(mainLogo).click();
        boolean isPresentPromoBlock = driver.findElement(promoBlock).isDisplayed();
        //assert
        Assertions.assertEquals(true, isPresentPromoBlock,"Промоблок отсутствует на главной странице");
    }

    //Сценарий "Переход на станицу Главная по клику на кнопку "Главная" меню хидера"
    @Test
    public void MainPageStoreMenuClickAllButtons() {
        //Кнопка Главная из меню в хэдере на главной странице
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");
        // act
        driver.findElement(headerMenuMainPageButton).click();
        //assert
        boolean isPresentPromoBlock = driver.findElement(promoBlock).isDisplayed();
        Assertions.assertEquals(true, isPresentPromoBlock, "Промоблок отсутствует на главной странице");
    }
}

