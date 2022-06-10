package com.bu.pipecontent.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

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

    @Test
    public void getTagElement() {
        String address = "https://news.naver.com/main/list.naver?mode=LS2D&mid=shm&sid2=260&sid1=101&date=20220609&page=4";
        try {
            Document document = Jsoup.connect(address).timeout(5000).get();
            Assertions.assertNotNull(document);
            Elements element = document.select("dl dt");

            ArrayList<String> titileList = new ArrayList<>();
            ArrayList<String> linkList = new ArrayList<>();

            for (Element newsElement : element) {
                if (newsElement.select("dt.photo").isEmpty()) {
                    linkList.add(newsElement.select("a").attr("href"));
                    titileList.add(newsElement.select("a").text());
                }
            }

            for (int i = 0; i < titileList.size(); i++) {
                System.out.println(i + "번째 Title : " + titileList.get(i) + "\n" + i + "번째 Link :" + linkList.get(i));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}