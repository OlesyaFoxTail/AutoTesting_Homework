import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.SourceType;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.assertj.core.api.Assertions.*;

public class CatalogTests {
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
        driver.manage().window().setSize(new Dimension(1200, 2000));

    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }


    private By mainLogo = By.xpath("//div[@id='site-branding']/a[1]");//лого сайта в хидере
    private By searchInput = By.xpath("//input[@class='search-field']");//Поле поиска
    private By submitSearchButton = By.xpath("//button[@type='submit']"); //Кнопка поиска
    private By productCardTitle = By.xpath("//li[contains(@class,'type-product')]//h3[1]");//Заголовок карточки товара поиск нескольких карточек
    private By headerCatalogButton = By.xpath("//ul[@id='menu-primary-menu']/li[contains(@class,'menu-item-object-product_cat')]/a");// Ссылка в главном меню на каталог
    private By headerCatalogElectroButton = By.xpath("//ul[@class='sub-menu']//a[contains(text(),'Электроника')]"); //Ссылка в главном меню каталог, подменю электроника
    private By headerCatalogTVButton = By.xpath("//ul[@class='sub-menu']//a[contains(text(),'Телевизоры')]"); //Ссылка в главном меню каталог, подменю телевизоры
    private By headerCatalogHomeTechLinkButton = By.xpath("//ul[@class='sub-menu']//a[contains(text(),'Бытовая техника')]");// Ссылка в главном меню на каталог подменю бытовая техника
    private By entryTitleHeader = By.xpath("//h1[contains(@class,'entry-title')]"); //заголовок раздела на странице соответствующего раздела
    private By spanFlag = By.xpath("//div[@class='ak-container']/div[1]/span");//флаг с наименование раздела на странице соответствующего раздела
    private By footerLinkAllGoods = By.xpath("//div[@class='top-footer-block']/aside[@id='pages-2']//a[contains(text(),'Все товары')]"); //ссылка в футере все товары
    private By spanOnSail = By.xpath("//div[@class='wc-products']//span[@class='onsale']"); //все товары, флаг скдика
    private By salePrice = By.xpath("//div[@class='wc-products']//ins/span[contains(@class, 'woocommerce-Price-amount amount')]");//Все товары, скидочная цена в карточке
    private By regularPrice = By.xpath("//div[@class='wc-products']//del/span[contains(@class,'woocommerce-Price-amount')]");//Все товары, скидочная цена в карточке
    private By cardTitle = By.xpath("//div[@class='wc-products']//li"); //Заголовок карточки
    //Поиск товара в строке поиска по полному названию подкатегории/категории
    private By linkNextPage = By.xpath("//a[@class='next page-numbers']"); //переключение страниц (следующая)
    private By linkPage = By.xpath("//ul[@class='page-numbers']//a[@class='page-numbers']"); //переключение страниц (следующая)

    //Сценарий "Поиск товара по названию подкатегории в единственном числе, по полному слову"
    @ParameterizedTest
    @ValueSource(strings = {"Телевизор", "Книга", "Стиральная машина", "Планшет"})
    void SearchByFullSubcat(String message) {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");
        //act
        driver.findElement(searchInput).sendKeys(message);
        driver.findElement(submitSearchButton).click();
        List <WebElement> listTitle = driver.findElements(productCardTitle);
        int cardsCount = driver.findElements(productCardTitle).size();
        for (int i = 0; i < cardsCount; i++) {
            listTitle = driver.findElements(productCardTitle);
            String cardTitleText = listTitle.get(i).getText();
            assertThat(cardTitleText).containsIgnoringCase(message);
            System.out.println(cardTitleText);
        }
    }

     //Сценарий "Поиск товара по названию подкатегории по части слова"
    //Тест падает по слову Фото - т.к. находит планшет Ipad вместо фототехники
    //Тест падает по слову Тел - т.к. находит группу товаров без указания принадлежности товара к подкатегории. Я считаю это багом, некорректное название товара.
    //Тест падает по слову Хол - т.к. находит стиральную машину  вместо холодильников
    @DisplayName("Add part of word to the input")
    @ParameterizedTest
    @ValueSource(strings = {"Фото", "Тел", "Хол"})
    void SearchByPartOfWordUppercase(String message) {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");
        //act
        driver.findElement(searchInput).sendKeys(message);
        driver.findElement(submitSearchButton).click();
        List <WebElement> listTitles = driver.findElements(productCardTitle);
        int cardsCount = driver.findElements(productCardTitle).size();
        for (int i = 0; i < cardsCount; i++) {
            listTitles = driver.findElements(productCardTitle);
            String cardTitleText = listTitles.get(i).getText();
            assertThat(cardTitleText).containsIgnoringCase(message);
            System.out.println(cardTitleText);
        }
    }

    //Сценарий "Переход на станицу Каталог по клику на кнопку "Каталог" меню хэдера"
    @Test
    public void CatalogMenuAndSubMenuClickMoveToCorrectPage() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");
        //Ссылка Каталог из меню в хэдере на главной странице
        //act
        driver.findElement(headerCatalogButton).click();
        String getEntryTitleHeaderCatalog = driver.findElement(entryTitleHeader).getText();
        String getSpanFlagCatalog = driver.findElement(spanFlag).getText();
        //assert
        Assertions.assertEquals("КАТАЛОГ", getEntryTitleHeaderCatalog, "Неверный главный заголовок раздела каталог");
        Assertions.assertEquals("Каталог", getSpanFlagCatalog, "Неверный заголовок флажка раздела каталог");
    }

    //Сценарий "Переход на страницу в подменю "Бытовая техника" по клику на кнопку "Каталог"=> "Бытовая техника" меню хидера"
    @Test
    public void GoToSubcategoryHomeEquipment() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");
        //act
        driver.findElement(mainLogo).click();
        WebElement hoverable = driver.findElement(headerCatalogButton);
        new Actions(driver)
                .moveToElement(hoverable)
                .perform();
        driver.findElement(headerCatalogHomeTechLinkButton).click();
        String getHeaderCatalogHomeTechLink = driver.findElement(entryTitleHeader).getText();
        String getSpanFlagCatalogHomeTechLink = driver.findElement(spanFlag).getText();
        //assert
        Assertions.assertEquals("БЫТОВАЯ ТЕХНИКА", getHeaderCatalogHomeTechLink, "Неверный главный заголовок раздела каталог");
        Assertions.assertEquals("Бытовая Техника", getSpanFlagCatalogHomeTechLink, "Неверный заголовок флажка раздела каталог");
    }

    //Сценарий "Переход на страницу в подменю "Телевизоры" по клику на кнопку "Каталог"=> "Электроника"=>"Телевизоры"
    // меню хидера"
    @Test
    public void GoToSubcategoryTV() {
        //arrange
        driver.navigate().to("http://intershop5.skillbox.ru/");
        //act
        driver.findElement(mainLogo).click();
        WebElement hoverableNew = driver.findElement(headerCatalogButton);
        new Actions(driver)
                .moveToElement(hoverableNew)
                .perform();
        WebElement clickableElectronic = driver.findElement(headerCatalogElectroButton);
        new Actions(driver)
                .clickAndHold(clickableElectronic)
                .perform();
        driver.findElement(headerCatalogTVButton).click();
        String getHeaderCatalogTV = driver.findElement(entryTitleHeader).getText();
        String getSpanFlagCatalogTV = driver.findElement(spanFlag).getText();
        //assert
        Assertions.assertEquals("ТЕЛЕВИЗОРЫ", getHeaderCatalogTV, "Неверный главный заголовок раздела каталог");
        Assertions.assertEquals("Телевизоры", getSpanFlagCatalogTV, "Неверный заголовок флажка");
    }


    //Сценарий "Карточки товаров в разделе "Все товары" с флагом "Скидка" имеют вторую цену"
    @Test
        public void AllGoodsWithDiscountHaveDiscountPrice() {
            //arrange
            driver.navigate().to("http://intershop5.skillbox.ru/");
            //act
            driver.findElement(headerCatalogButton).click();
            driver.findElement(footerLinkAllGoods).click();
            int pages = driver. findElements(linkPage).size();

        for (int i = 0; i < pages; i++ ) {
                int getSpanOnSail = driver.findElements(spanOnSail).size();
                int getSalePrice = driver.findElements(salePrice).size();
                Assertions.assertEquals(getSalePrice, getSpanOnSail);
            driver.findElement(linkNextPage).click();

        }
    }
}
