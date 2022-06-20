package com.bu.pipecontent.crawler;

import com.bu.pipecontent.utils.DateUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class NaverNewsCrawlerTest {

    private static final String LOCAL_FILE_PATH = "/Users/il/dev/local/pipe-content/src/test/result/getDocument";
    @Test
    public void getDocument() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String today = now.format(formatter);

        String realstateNewsAddressByNaver = "https://news.naver.com/main/list.naver?mode=LS2D&sid2=260&mid=shm&sid1=101&date=" + today;

        try {
            Document document = Jsoup.connect(realstateNewsAddressByNaver).timeout(5000).get();
            BufferedReader reader = new BufferedReader(new FileReader(LOCAL_FILE_PATH));
            String documentStr = String.valueOf(document);
            String fileStr = "";

            while(reader.read()!=-1) {
                fileStr = reader.readLine();
            }

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

            /***
             * Next 페이지 만큼 add data
             * 현재 필요한 작업
             * */


            for (int i = 0; i < titileList.size(); i++) {
                System.out.println(i + "번째 Title : " + titileList.get(i) + "\n" + i + "번째 Link :" + linkList.get(i));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void crawl() {
        String realstateNewsAddressByNaver = "https://news.naver.com/main/list.naver?mode=LS2D&sid2=260&sid1=101&mid=shm&date=" + DateUtils.todayDate() + "&page="+"1";
        JSONArray jsonArray = new NaverNewsCrawler().crawl(realstateNewsAddressByNaver);

        for (Object newsObject : jsonArray) {
            JSONObject newsJsonObject = (JSONObject) newsObject;
            System.out.println("제목 : " + newsJsonObject.get("title") + "\n" + "링크 : " + newsJsonObject.get("link"));
        }
    }
}