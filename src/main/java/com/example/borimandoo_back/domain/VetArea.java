package com.example.borimandoo_back.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VetArea {
    SEOUL(2, "서울특별시"),
    BUSAN(51, "부산광역시"),
    DAEGU(53, "대구광역시"),
    INCHEON(32, "인천광역시"),
    GWANGJU(62, "광주광역시"),
    DAEJEON(42, "대전광역시"),
    ULSAN(52, "울산광역시"),
    SEJONG(44, "세종특별자치시"),
    GYEONGGI(31, "경기도"),
    GANGWON(33, "강원특별자치도"),
    CHUNGBUK(43, "충청북도"),
    CHUNGNAM(41, "충청남도"),
    JEONBUK(63, "전북특별자치도"),
    JEONNAM(61, "전라남도"),
    GYEONGBUK(54, "경상북도"),
    GYEONGNAM(55, "경상남도");

    private final int code;
    private final String description;
}
