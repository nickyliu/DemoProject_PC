package com.example.liweiliu.personalcapitaldemo;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class RssParser extends DefaultHandler {
    private List<ListItem> mListItems;
    ListItem rssItem;
    final int RSS_TITLE = 1;
    final int RSS_LINK = 2;
    final int RSS_DESCRIPTION = 3;
    final int RSS_IMAGE = 4;
    final int RSS_PUBDATE = 5;
    int currentstate = 0;

    private static final String CHANNEL_TAG = "channel";
    private static final String ITEM_TAG = "item";
    private static final String TITLE_TAG = "title";
    private static final String IMAGE_TAG = "content";
    private static final String DESCRIPTION_TAG = "description";
    private static final String DATE_TAG = "pubDate";
    private static final String LINK_TAG = "link";

    public RssParser() {

    }

    public List<ListItem> getFeed() {
        return mListItems;
    }

    public void startDocument() throws SAXException {
        mListItems = new ArrayList<>();
        rssItem = new ListItem();
    }

    public void endDocument() throws SAXException {

    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (localName.equals(CHANNEL_TAG)) {
            currentstate = 0;
            return;
        }
        if (localName.equals(ITEM_TAG)) {
            rssItem = new ListItem();
            return;
        }
        if (localName.equals(TITLE_TAG)) {
            currentstate = RSS_TITLE;
            return;
        }
        if (localName.equals(DESCRIPTION_TAG)) {
            currentstate = RSS_DESCRIPTION;
            return;
        }
        if (localName.equals(LINK_TAG)) {
            currentstate = RSS_LINK;
            return;
        }
        if (localName.equals(IMAGE_TAG)) {
            currentstate = RSS_IMAGE;
            rssItem.setImage(atts.getValue("url"));
            return;
        }
        if (localName.equals(DATE_TAG)) {
            currentstate = RSS_PUBDATE;
            return;
        }

        currentstate = 0;
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

        if (localName.equals("item")) {
            mListItems.add(rssItem);
            return;
        }
    }

    public void characters(char ch[], int start, int length) {
        String theString = new String(ch, start, length);
        switch (currentstate) {
            case RSS_TITLE:
                rssItem.setTitle(theString);
                currentstate = 0;
                break;
            case RSS_LINK:
                rssItem.setLink(theString);
                currentstate = 0;
                break;
            case RSS_DESCRIPTION:
                rssItem.setDescription(theString);
                currentstate = 0;
                break;
            case RSS_IMAGE:
                currentstate = 0;
                break;
            case RSS_PUBDATE:
                rssItem.setDate(theString);
                currentstate = 0;
                break;
            default:
                return;
        }
    }
}