package com.seungah.todayclothes.domain.clothes.service;


import com.seungah.todayclothes.domain.clothes.dto.response.BottomResponse;
import com.seungah.todayclothes.domain.clothes.dto.response.TopResponse;
import com.seungah.todayclothes.domain.clothes.entity.Bottom;
import com.seungah.todayclothes.domain.clothes.entity.ClothesGroup;
import com.seungah.todayclothes.domain.clothes.entity.Top;
import com.seungah.todayclothes.domain.clothes.repository.BottomRepository;
import com.seungah.todayclothes.domain.clothes.repository.ClothesGroupRepository;
import com.seungah.todayclothes.domain.clothes.repository.TopRepository;
import com.seungah.todayclothes.global.type.ClothesName;
import com.seungah.todayclothes.global.type.ScheduleType;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ClothesService {

    private final TopRepository topRepository;
    private final BottomRepository bottomRepository;
    private final ClothesGroupRepository clothesGroupRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${crawl.url-man}")
    private String manUrl;
    @Value("${crawl.url-woman}")
    private String womanUrl;
    @Value("${crawl.path}")
    private String path;

    private ChromeDriver driver;


    public void crawling() {

    }

    public void crawlManClothes(String keyword) {
        driver = setDriver();
        driver.executeScript("window.open('about:blank','_blank');");
        List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0));

        String pageUrl = manUrl + keyword;
        driver.get(pageUrl);
        List<WebElement> elements = driver.findElements(By.className("img-block"));

        for (WebElement element : elements) {
            String itemUrl = element.getAttribute("href");

            driver.executeScript("window.open('about:blank','_blank');");
            List<String> tabs1 = new ArrayList<String>(driver.getWindowHandles());
            driver.switchTo().window(tabs1.get(1));
            driver.get(itemUrl);

            String imageUrl = driver.findElement(By.className("product-img")).findElement(By.tagName("img"))
                    .getAttribute("src");

            driver.close();
            driver.switchTo().window(tabs.get(0));

            saveManClothes(keyword, itemUrl, imageUrl);
        }
        driver.quit();
    }
    public void crawlWomanClothes(String keyword) {
        driver = setDriver();
        driver.executeScript("window.open('about:blank','_blank');");
        List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0));

        String pageUrl = womanUrl + keyword;
        driver.get(pageUrl);
        List<WebElement> elements = driver.findElements(By.tagName("a"));

        for (WebElement element : elements) {
            String itemUrl = element.getAttribute("href");
            driver.executeScript("window.open('about:blank','_blank');");
            List<String> tabs1 = new ArrayList<String>(driver.getWindowHandles());
            driver.switchTo().window(tabs1.get(1));
            driver.get(itemUrl);

            String imageUrl = driver.findElement(By.className("pdp__image")).findElement(By.tagName("img"))
                    .getAttribute("src");

            driver.close();
            driver.switchTo().window(tabs.get(0));
            saveWoManClothes(keyword, itemUrl, imageUrl);
        }
        driver.quit();
    }

    public void saveManClothes(String keyword, String itemUrl, String imageUrl){
        List<ClothesGroup> clothesGroups = clothesGroupRepository.findByClothesNames(ClothesName.valueOf(keyword));
        for (ClothesGroup clothesGroup : clothesGroups) {
            Integer groupNumber = clothesGroup.getGroupNumber();
            if (groupNumber>=1 && groupNumber <=40) {
                topRepository.save(Top.of(itemUrl, imageUrl, clothesGroup));
            }
            if (groupNumber>=51 && groupNumber <=90) {
                bottomRepository.save(Bottom.of(itemUrl, imageUrl, clothesGroup));
            }
        }
    }
    public void saveWoManClothes(String keyword, String itemUrl, String imageUrl){
        List<ClothesGroup> clothesGroups = clothesGroupRepository.findByClothesNames(ClothesName.valueOf(keyword));
        for (ClothesGroup clothesGroup : clothesGroups) {
            Integer groupNumber = clothesGroup.getGroupNumber();
            if (groupNumber>=101 && groupNumber <=140) {
                topRepository.save(Top.of(itemUrl, imageUrl, clothesGroup));
            }
            if (groupNumber>=151 && groupNumber <=190) {
                bottomRepository.save(Bottom.of(itemUrl, imageUrl, clothesGroup));
            }
        }
    }
    public ChromeDriver setDriver() {
        System.setProperty("webdriver.chrome.driver", path);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        return new ChromeDriver(options);
    }

    public ResponseEntity<List<TopResponse>> getTopClothes(Long userId) {
        List<TopResponse> topResponseList = new ArrayList<>();
        //Todo 유저에 대한 추천 옷을 주기 위한 알고리즘 구현
        LocalDateTime now = LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0);

        if (userId == null){
            // Todo 모델에 날씨랑 랜덤한 일정 넣어서 그룹 아이디 가져오고, 가져온 그룹 아이디에 해당하는 의류 키워드로 List 만들어서 응답
            // Todo userId가 null이면 그룹 아이디를 통해 옷을 꺼내오는데 그룹 아이디에 날씨 정보가 있으므로 당일 날씨에 대한 그룹 아이디를 cron을 통해 저장할 필요가 있음
            List<Top> topList = topRepository.findAll();
            for (Top top : topList) {
                topResponseList.add(TopResponse.of(top));
            }
        }
        return ResponseEntity.ok(topResponseList);
    }

    public ResponseEntity<List<BottomResponse>> getBottomClothes(Long userId) {
        List<BottomResponse> bottomResponseList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0);

        if (userId == null){
            List<Bottom> bottomList = bottomRepository.findAll();
            for (Bottom bottom : bottomList) {
                bottomResponseList.add(BottomResponse.of(bottom));
            }
        }
        return ResponseEntity.ok(bottomResponseList);
    }

    public static ScheduleType getRandomScheduleType() {
        ScheduleType[] values = ScheduleType.values();
        int length = values.length;
        return values[new Random().nextInt(length)];
    }
}
