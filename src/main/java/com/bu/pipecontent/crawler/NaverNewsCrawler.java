package com.bu.pipecontent.crawler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


// 1. 크롤링 (오늘 부동산 카테고리 조회)
// 2. 문서를 읽어서 페이지수 확인
// 3. 페이지 수만큼 Loop 크롤링
// 4. 배열 값에 기사 제목, 링크 추출


public class NaverNewsCrawler implements NewsCrawler{

    private static int PAGE_COUNT = 2;

    @Override
    public JSONArray crawl(String address) {
        return crawlResultList(getDocument(address));
    }

    @Override
    public Document getDocument(String address) {
        try {
            return Jsoup.connect(address).timeout(5000).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //
    @Override
    public JSONArray crawlResultList(Document document) {

        JSONArray jsonArray = new JSONArray();

        Elements newPageElements = getNewsPagingElements(document);
        List<String> page = addNewsList(newPageElements);

        Document newDocument = document;

        for(int i = 0; i < PAGE_COUNT; i++) {
            if (isExistNextPage(page)) {

                // 실제 기사 추출
                for (int j = 0; j < page.size(); j++) {
                    addNextNewsInfoList(newDocument, jsonArray);

                    // 다음 주소 추출
                    String nextPageAddress = getNextPageAddress(j+1, newPageElements);

                    // 다음 주소 탐색
                    newDocument = getDocument(nextPageAddress);
                }

                // 다음 주소 추출
                String nextPageAddress = getNextPageAddress(page.size(), newPageElements);

                // 다음 주소 탐색
                newDocument = getDocument(nextPageAddress);

                // 페이지 크롤링
                page = crawlNextPageNewsList(newDocument);
            }
        }

        return jsonArray;
    }

    public void addNextNewsInfoList(Document document, JSONArray jsonArray) {
        Elements newsElements = document.select("dl dt");
        addNewsInfoList(newsElements, jsonArray);
    }

    private void addNewsInfoList(Elements element, JSONArray jsonArray) {
        for (Element newsElement : element) {
            if (newsElement.select("dt.photo").isEmpty()) {
                if (!newsElement.select("dt").select("a").isEmpty()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("title", newsElement.select("a").text());
                    jsonObject.put("link", newsElement.select("a").attr("href"));
                    jsonArray.add(jsonObject);
                }
            }
        }
    }

    private List<String> crawlNextPageNewsList(Document document) {
        Elements elements = getNewsPagingElements(document);
        List<String> newsList = addNewsList(elements);
        return newsList;
    }

    private Elements getNewsPagingElements(Document document) {
        return document.select("div.paging").select("a");
    }

    private List<String> addNewsList(Elements elements) {
        List<String> newsList = new ArrayList<>();
        for (Element element : elements) {
            newsList.add(element.select("a").text());
        }
        return newsList;
    }

    private boolean isExistNextPage(List<String> newsList) {
        if (newsList.get(newsList.size()-1).equals("다음")) {
            return true;
        }
        return false;
    }

    private String getNextPageAddress(int newsListSize, Elements elements) {
        String nextPage = elements.get(newsListSize-1).select("a").attr("href");
        return "https://news.naver.com/main/list.naver" + nextPage;
    }
}
