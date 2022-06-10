package com.bu.pipecontent.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class NaverNewsCrawlerTest {

    @Test
    public void getDocument() {
        String address = "https://news.naver.com/main/list.naver?mode=LS2D&mid=shm&sid2=260&sid1=101&date=20220609&page=4";
        try {
            Document document = Jsoup.connect(address).timeout(5000).get();
            Assertions.assertNotNull(document);
            System.out.println(document);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}