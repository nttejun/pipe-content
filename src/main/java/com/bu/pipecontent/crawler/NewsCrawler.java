package com.bu.pipecontent.crawler;

import org.jsoup.nodes.Document;

import java.util.List;

public interface NewsCrawler {
    List<String> crawl(String address);
    Document getDocument(String naverRealstateAddress);
    List<String> crawlResultList(Document document);
}
