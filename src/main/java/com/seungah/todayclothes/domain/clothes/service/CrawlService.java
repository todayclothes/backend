package com.seungah.todayclothes.domain.clothes.service;

import com.seungah.todayclothes.domain.clothes.entity.*;
import com.seungah.todayclothes.domain.clothes.repository.BottomRepository;
import com.seungah.todayclothes.domain.clothes.repository.ClothesGroupRepository;
import com.seungah.todayclothes.domain.clothes.repository.ClothesGroupTypeRepository;
import com.seungah.todayclothes.domain.clothes.repository.TopRepository;
import com.seungah.todayclothes.global.type.Gender;
import com.seungah.todayclothes.global.type.ClothesType;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlService {

    private final TopRepository topRepository;
    private final BottomRepository bottomRepository;
    private final ClothesGroupRepository clothesGroupRepository;
    private final ClothesGroupTypeRepository clothesGroupTypeRepository;


    @Value("${crawl.url-man}")
    private String manUrl;
    @Value("${crawl.url-woman}")
    private String womanUrl;

    private ChromeDriver driver;
    public void crawling(){
        List<ClothesType> allClothesTypes = ClothesType.getAllClothesTypes();
        for (ClothesType clothesType : allClothesTypes) {
            if (clothesType.getGender() == Gender.MALE) {
                crawlManClothes(clothesType.getType());
            } else {
                crawlWomanClothes(clothesType.getType());
            }
        }
    }

    public void crawlManClothes(String type) {
        driver = setDriver();
        driver.executeScript("window.open('about:blank','_blank');");
        List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0));

        String pageUrl = manUrl + type;
        driver.get(pageUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".n-search-contents")));

        List<WebElement> elements = driver.findElements(By.className("img-block"));
        for (WebElement element : elements) {
            String itemUrl = element.getAttribute("href");
            String imageUrl = "https:" + element.findElement(By.tagName("img")).getAttribute("data-original")
                    .replaceAll("_\\d+\\.", "_500.");
            saveManClothes(type, itemUrl, imageUrl);
        }
        driver.quit();
    }

    public void crawlWomanClothes(String type) {
        driver = setDriver();
        driver.executeScript("window.open('about:blank','_blank');");
        List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0));

        String pageUrl = womanUrl + type;
        driver.get(pageUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".search_result_prd")));

        List<WebElement> elements = driver.findElements(By.className("Mod_link-prod"));
        for (WebElement element : elements) {
            String itemUrl = element.findElement(By.tagName("a")).getAttribute("href");
            String[] imageUrl = element.findElement(By.className("thumb")).getAttribute("style")
                    .split("\"");

            saveWomanClothes(type, itemUrl, imageUrl[1]);
        }
        driver.quit();
    }
    public void saveManClothes(String type, String itemUrl, String imageUrl){
        boolean isDuplicate = topRepository.existsByItemUrl(itemUrl) || bottomRepository.existsByItemUrl(itemUrl);
        if (!isDuplicate) {
            ClothesType clothesType = ClothesType.findByTypeAndGender(type, Gender.MALE);
            Top top = null;
            Bottom bottom = null;

            if (clothesType.getCode() >= 1 && clothesType.getCode() <= 26) {
                top = topRepository.save(Top.of(itemUrl, imageUrl, clothesType));
            }
            if (clothesType.getCode() >= 27 && clothesType.getCode() <= 40) {
                bottom = bottomRepository.save(Bottom.of(itemUrl, imageUrl, clothesType));
            }
            List<ClothesGroup> clothesGroups = clothesGroupRepository.findByClothesTypes(clothesType);

            for (ClothesGroup clothesGroup : clothesGroups) {
                clothesGroupTypeRepository.save(ClothesGroupType.of(clothesGroup, top, bottom));
            }
        }
    }
    public void saveWomanClothes(String type, String itemUrl, String imageUrl){
        boolean isDuplicate = topRepository.existsByItemUrl(itemUrl) || bottomRepository.existsByItemUrl(itemUrl);
        if (!isDuplicate) {
            ClothesType clothesType = ClothesType.findByTypeAndGender(type, Gender.FEMALE);
            Top top = null;
            Bottom bottom = null;

            if (clothesType.getCode() >= 41 && clothesType.getCode() <= 80) {
                top = topRepository.save(Top.of(itemUrl, imageUrl, clothesType));
            }
            if (clothesType.getCode() >= 81 && clothesType.getCode() <= 101) {
                bottom = bottomRepository.save(Bottom.of(itemUrl, imageUrl, clothesType));
            }

            List<ClothesGroup> clothesGroups = clothesGroupRepository.findByClothesTypes(clothesType);

            for (ClothesGroup clothesGroup : clothesGroups) {
                clothesGroupTypeRepository.save(ClothesGroupType.of(clothesGroup, top, bottom));
            }
        }
    }
    public ChromeDriver setDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\boypo\\Desktop\\chromedriver.exe"); // Local에서만 필요함
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        return new ChromeDriver(options);
    }
}
