package com.vertex.collector.arxiv;

import com.vertex.collector.CollectedItem;
import com.vertex.item.SourceType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ArxivCollector {
    private final RestClient restClient;

    public ArxivCollector(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<CollectedItem> collect() {
        String xml = restClient.get()
                .uri("https://export.arxiv.org/api/query?search_query=cat:cs.AI+OR+cat:cs.LG+OR+cat:cs.CL&sortBy=submittedDate&sortOrder=descending&max_results=30")
                .retrieve()
                .body(String.class);
        if (xml == null || xml.isBlank()) {
            return List.of();
        }
        return parse(xml);
    }

    private List<CollectedItem> parse(String xml) {
        try {
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xml)));
            NodeList entries = document.getElementsByTagName("entry");
            List<CollectedItem> items = new ArrayList<>();
            for (int index = 0; index < entries.getLength(); index++) {
                Element entry = (Element) entries.item(index);
                String title = text(entry, "title").replaceAll("\\s+", " ").trim();
                String url = text(entry, "id");
                String summary = text(entry, "summary").replaceAll("\\s+", " ").trim();
                Instant published = Instant.parse(text(entry, "published"));
                Map<String, Object> metadata = new LinkedHashMap<>();
                metadata.put("updated", text(entry, "updated"));
                metadata.put("authors", authors(entry));
                items.add(new CollectedItem(
                        SourceType.ARXIV,
                        title,
                        url,
                        summary,
                        List.of("arxiv", "ai-research"),
                        published,
                        50,
                        20,
                        metadata
                ));
            }
            return items;
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to parse arXiv response", exception);
        }
    }

    private String text(Element element, String tagName) {
        NodeList nodes = element.getElementsByTagName(tagName);
        return nodes.getLength() == 0 ? "" : nodes.item(0).getTextContent();
    }

    private List<String> authors(Element entry) {
        NodeList nodes = entry.getElementsByTagName("author");
        List<String> authors = new ArrayList<>();
        for (int index = 0; index < nodes.getLength(); index++) {
            Element author = (Element) nodes.item(index);
            authors.add(text(author, "name"));
        }
        return authors;
    }
}
