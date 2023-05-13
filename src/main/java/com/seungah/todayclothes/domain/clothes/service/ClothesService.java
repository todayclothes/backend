package com.seungah.todayclothes.domain.clothes.service;


import com.seungah.todayclothes.domain.clothes.dto.response.BottomResponse;
import com.seungah.todayclothes.domain.clothes.dto.response.TopResponse;
import com.seungah.todayclothes.domain.clothes.entity.*;
import com.seungah.todayclothes.domain.clothes.repository.BottomRepository;
import com.seungah.todayclothes.domain.clothes.repository.ClothesGroupRepository;
import com.seungah.todayclothes.domain.clothes.repository.ClothesTypeRepository;
import com.seungah.todayclothes.domain.clothes.repository.TopRepository;
import com.seungah.todayclothes.global.type.Gender;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClothesService {

    private final TopRepository topRepository;
    private final BottomRepository bottomRepository;
    private final ClothesTypeRepository clothesTypeRepository;
    private final ClothesGroupRepository clothesGroupRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${crawl.url-man}")
    private String manUrl;
    @Value("${crawl.url-woman}")
    private String womanUrl;

    private ChromeDriver driver;

    public void crawling(){
        List<ClothesType> clothesTypes = clothesTypeRepository.findAll();
        for (ClothesType clothesType : clothesTypes) {
            if (clothesType.getGender() == Gender.MALE){
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

            saveWoManClothes(type, itemUrl, imageUrl[1]);
        }
        driver.quit();
    }
    public void saveManClothes(String type, String itemUrl, String imageUrl){
        boolean isDuplicate = topRepository.existsByItemUrl(itemUrl) || bottomRepository.existsByItemUrl(itemUrl);
        if (!isDuplicate) {
            ClothesType clothesType = clothesTypeRepository.findByGenderAndType(Gender.MALE, type);
            List<ClothesGroupType> clothesGroupTypes = clothesType.getClothesGroupTypes();
            for (ClothesGroupType clothesGroupType : clothesGroupTypes) {
                ClothesGroup clothesGroup = clothesGroupType.getClothesGroup();
                Integer groupNumber = clothesGroup.getGroupNumber();
                if (groupNumber >= 1 && groupNumber <= 40) {
                    topRepository.save(Top.of(itemUrl, imageUrl, clothesGroup));
                }
                if (groupNumber >= 51 && groupNumber <= 90) {
                    bottomRepository.save(Bottom.of(itemUrl, imageUrl, clothesGroup));
                }
            }
        }
    }
    public void saveWoManClothes(String type, String itemUrl, String imageUrl){
        boolean isDuplicate = topRepository.existsByItemUrl(itemUrl) || bottomRepository.existsByItemUrl(itemUrl);
        if (!isDuplicate) {
            ClothesType clothesType = clothesTypeRepository.findByGenderAndType(Gender.FEMALE, type);
            List<ClothesGroupType> clothesGroupTypes = clothesType.getClothesGroupTypes();
            for (ClothesGroupType clothesGroupType : clothesGroupTypes) {
                ClothesGroup clothesGroup = clothesGroupType.getClothesGroup();
                Integer groupNumber = clothesGroup.getGroupNumber();
                if (groupNumber >= 101 && groupNumber <= 140) {
                    topRepository.save(Top.of(itemUrl, imageUrl, clothesGroup));
                }
                if (groupNumber >= 151 && groupNumber <= 190) {
                    bottomRepository.save(Bottom.of(itemUrl, imageUrl, clothesGroup));
                }
            }
        }
    }
    public ChromeDriver setDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        return new ChromeDriver(options);
    }

    public ResponseEntity<List<TopResponse>> getTopClothes(Integer groupNumber) {
        List<TopResponse> topResponseList = new ArrayList<>();

        List<Top> topList = topRepository.findByClothesGroup(clothesGroupRepository.findByGroupNumber(groupNumber));
        for (Top top : topList) {
            topResponseList.add(TopResponse.of(top));
        }
        Collections.shuffle(topResponseList);
        return ResponseEntity.ok(topResponseList);
    }

    public ResponseEntity<List<BottomResponse>> getBottomClothes(Integer groupNumber) {
        List<BottomResponse> bottomResponseList = new ArrayList<>();

        List<Bottom> bottomList = bottomRepository.findByClothesGroup(clothesGroupRepository.findByGroupNumber(groupNumber));
        for (Bottom bottom : bottomList) {
            bottomResponseList.add(BottomResponse.of(bottom));
        }
        Collections.shuffle(bottomResponseList);
        return ResponseEntity.ok(bottomResponseList);
    }
}
