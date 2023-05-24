package com.seungah.todayclothes.global.type;

import java.util.Arrays;
import java.util.List;

public enum ClothesType {
    MALE_KNIT(1, Gender.MALE, "니트"),
    MALE_HOODIE(2, Gender.MALE, "후드티"),
    MALE_CARDIGAN(3, Gender.MALE, "가디건"),
    MALE_SHIRT(4, Gender.MALE, "셔츠"),
    MALE_SWEATSHIRT(5, Gender.MALE, "맨투맨"),
    MALE_LONG_SLEEVE(6, Gender.MALE, "롱슬리브"),
    MALE_HOODIE_ZIPUP(7, Gender.MALE, "후드집업"),
    MALE_BLAZER(8, Gender.MALE, "블레이저"),
    MALE_PEACOAT(9, Gender.MALE, "블루종"),
    MALE_RIDER_JACKET(10, Gender.MALE, "라이더자켓"),
    MALE_DENIM_JACKET(11, Gender.MALE, "청자켓"),
    MALE_WOOL_JACKET(12, Gender.MALE, "울자켓"),
    MALE_PADDED_VEST(13, Gender.MALE, "경량패딩조끼"),
    MALE_VARSITY_JACKET(14, Gender.MALE, "바시티자켓"),
    MALE_NYLON_JACKET(15, Gender.MALE, "나일론자켓"),
//    MALE_JERSEY(16, Gender.MALE, "져지"),
    MALE_KNIT_ZIPUP(17, Gender.MALE, "니트집업"),
    MALE_BOMBER_JACKET(18, Gender.MALE, "항공점퍼"),
    MALE_SHORT_SLEEVE(19, Gender.MALE, "반팔티"),
    MALE_SLEEVELESS(20, Gender.MALE, "민소매"),
    MALE_HALF_CARDIGAN(21, Gender.MALE, "하프가디건"),
    MALE_SHORT_SLEEVE_SHIRT(22, Gender.MALE, "반팔셔츠"),
    MALE_SHORT_SLEEVE_KNIT(23, Gender.MALE, "반팔니트"),
    MALE_COLLAR_TEE(24, Gender.MALE, "카라티"),
    MALE_LINEN_SHIRT(25, Gender.MALE, "린넨셔츠"),
    MALE_TRENCH_COAT(26, Gender.MALE, "트렌치코트"),
    MALE_WIDE_DENIM(27, Gender.MALE, "와이드데님"),
    MALE_SLIM_FIT_DENIM(28, Gender.MALE, "슬림핏데님"),
    MALE_LEGGINGS(29, Gender.MALE, "레깅스"),
    MALE_COTTON_PANTS(30, Gender.MALE, "코튼팬츠"),
    MALE_COTTON_SHORTS(31, Gender.MALE, "코튼반바지"),
    MALE_WIDE_SLACKS(32, Gender.MALE, "와이드슬랙스"),
    MALE_SLIM_FIT_SLACKS(33, Gender.MALE, "슬림핏슬랙스"),
    MALE_CARGO_PANTS(34, Gender.MALE, "카고팬츠"),
    MALE_TRAINING_PANTS(35, Gender.MALE, "트레이닝팬츠"),
    MALE_TRAINING_SHORTS(36, Gender.MALE, "트레이닝숏팬츠"),
    MALE_LINEN_WIDE_DENIM(37, Gender.MALE, "린넨와이드데님"),
    MALE_LINEN_WIDE_SLACKS(38, Gender.MALE, "린넨와이드슬랙스"),
    MALE_BERMUDA_SHORTS(39, Gender.MALE, "버뮤다팬츠"),
    MALE_PADDING(40, Gender.MALE, "패딩"),
    FEMALE_SHIRT(41, Gender.FEMALE, "셔츠"),
    FEMALE_BLOUSE(42, Gender.FEMALE, "블라우스"),
    FEMALE_KNIT(43, Gender.FEMALE, "니트"),
    FEMALE_CARDIGAN(44, Gender.FEMALE, "가디건"),
    FEMALE_BOLERO(45, Gender.FEMALE, "볼레로"),
    FEMALE_DRESS(46, Gender.FEMALE, "원피스"),
    FEMALE_HOODIE(47, Gender.FEMALE, "후드티"),
    FEMALE_SWEATSHIRT(48, Gender.FEMALE, "맨투맨"),
    FEMALE_LONG_SLEEVE(49, Gender.FEMALE, "롱슬리브"),
    FEMALE_HOODIE_ZIPUP(50, Gender.FEMALE, "후드집업"),
    FEMALE_PARKA(51, Gender.FEMALE, "야상"),
    FEMALE_COACH_JACKET(52, Gender.FEMALE, "코치자켓"),
    FEMALE_BLAZER(53, Gender.FEMALE, "블레이저"),
    FEMALE_PEACOAT(54, Gender.FEMALE, "블루종"),
    FEMALE_RIDER_JACKET(55, Gender.FEMALE, "라이더자켓"),
    FEMALE_DENIM_JACKET(56, Gender.FEMALE, "청자켓"),
    FEMALE_WOOL_JACKET(57, Gender.FEMALE, "울자켓"),
    FEMALE_KNIT_VEST(58, Gender.FEMALE, "니트베스트"),
    FEMALE_VARSITY_JACKET(59, Gender.FEMALE, "바시티자켓"),
    FEMALE_NYLON_JACKET(60, Gender.FEMALE, "나일론자켓"),
    FEMALE_TRENCH_COAT(61, Gender.FEMALE, "트렌치코트"),
    FEMALE_SHORT_SLEEVE_KNIT(62, Gender.FEMALE, "반팔니트"),
//    FEMALE_JERSEY(63, Gender.FEMALE, "져지"),
    FEMALE_TWEED_JACKET(64, Gender.FEMALE, "트위드자켓"),
    FEMALE_CROP_SWEATER(65, Gender.FEMALE, "크롭맨투맨"),
    FEMALE_CROP_HOODIE(66, Gender.FEMALE, "크롭후드집업"),
    FEMALE_CROP_HOODIE_TEE(67, Gender.FEMALE, "크롭후드티"),
    FEMALE_HOODIE_ZIP_UP(68, Gender.FEMALE, "니트집업"),
    FEMALE_CROP_KNIT_ZIP_UP(69, Gender.FEMALE, "크롭니트집업"),
    FEMALE_BUSTIER(70, Gender.FEMALE, "뷔스티에"),
    FEMALE_SHORT_SLEEVE_TOP(71, Gender.FEMALE, "반팔티"),
    FEMALE_CROP_SHORT_SLEEVE_TOP(72, Gender.FEMALE, "크롭반팔티"),
    FEMALE_CROP_SHORT_SLEEVE_SHIRT(73, Gender.FEMALE, "크롭반팔셔츠"),
    FEMALE_SHORT_SLEEVE_SHIRT(74, Gender.FEMALE, "반팔셔츠"),
    FEMALE_RIBBED_SHORT_SLEEVE_TOP(75, Gender.FEMALE, "골지반팔티"),
    FEMALE_COLLAR_TEE(76, Gender.FEMALE, "카라티"),
    FEMALE_OFF_SHOULDER(77, Gender.FEMALE, "오프숄더"),
    FEMALE_CAMI_BLOUSE(78, Gender.FEMALE, "나시블라우스"),
    FEMALE_CAMI_TOP(79, Gender.FEMALE, "나시티"),
    FEMALE_LINEN_SHIRT(80, Gender.FEMALE, "린넨셔츠"),
    FEMALE_WIDE_DENIM(81, Gender.FEMALE, "와이드데님"),
    FEMALE_SLIM_FIT_DENIM(82, Gender.FEMALE, "슬림핏데님"),
    FEMALE_LEGGINGS(83, Gender.FEMALE, "레깅스"),
    FEMALE_TWEED_SKIRT(84, Gender.FEMALE, "트위드스커트"),
    FEMALE_LONG_PLEATS(85, Gender.FEMALE, "롱플리츠"),
    FEMALE_SHORT_PLEATS(86, Gender.FEMALE, "숏플리츠"),
    FEMALE_DENIM_SKIRT(87, Gender.FEMALE, "데님스커트"),
    FEMALE_LEATHER_SKIRT(88, Gender.FEMALE, "가죽치마"),
    FEMALE_COTTON_PANTS(89, Gender.FEMALE, "코튼팬츠"),
    FEMALE_COTTON_SHORTS(90, Gender.FEMALE, "코튼반바지"),
    FEMALE_WIDE_SLACKS(91, Gender.FEMALE, "와이드슬랙스"),
    FEMALE_SLIM_FIT_SLACKS(92, Gender.FEMALE, "슬림핏슬랙스"),
    FEMALE_CARGO_PANTS(93, Gender.FEMALE, "카고팬츠"),
    FEMALE_TRAINING_PANTS(94, Gender.FEMALE, "트레이닝팬츠"),
    FEMALE_TRAINING_SHORTS(95, Gender.FEMALE, "트레이닝숏팬츠"),
    FEMALE_LEATHER_SHORTS(96, Gender.FEMALE, "가죽반바지"),
    FEMALE_LINEN_WIDE_DENIM(97, Gender.FEMALE, "린넨와이드데님"),
    FEMALE_LINEN_WIDE_SLACKS(98, Gender.FEMALE, "린넨와이드슬랙스"),
    FEMALE_NYLON_SKIRT(99, Gender.FEMALE, "나일론스커트"),
    FEMALE_LINEN_COTTON_PANTS(100, Gender.FEMALE, "린넨코튼팬츠"),
    FEMALE_PADDING(101, Gender.FEMALE, "패딩");

    private final int code;
    private final Gender gender;
    private final String type;

    ClothesType(int code, Gender gender, String type) {
        this.code = code;
        this.gender = gender;
        this.type = type;
    }
    public int getCode() {
        return code;
    }

    public Gender getGender() {
        return gender;
    }

    public String getType() {
        return type;
    }
    public static List<ClothesType> getAllClothesTypes() {
        return Arrays.asList(ClothesType.values());
    }

    public static ClothesType findByTypeAndGender(String type, Gender gender) {
        for (ClothesType clothesType : ClothesType.values()) {
            if (clothesType.getType().equals(type) && clothesType.getGender().equals(gender)) {
                return clothesType;
            }
        }
        return null;
    }
}
