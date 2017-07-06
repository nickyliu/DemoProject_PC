package com.example.liweiliu.personalcapitaldemo;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class InfoDownloader extends AsyncTask<String, Integer, List<ListItem>> {
    public static final String urlString = "https://blog.personalcapital.com/feed/?cat=3,891,890,68,284";

    private ProgressBar mProgress;
    private RecyclerAdapter mAdapter;

    public InfoDownloader(ProgressBar progressBar, RecyclerAdapter adapter) {
        this.mProgress = progressBar;
        this.mAdapter = adapter;
    }

    protected List<ListItem> doInBackground(String... urls) {
        List<ListItem> feedList = new ArrayList<>();
        URL url = null;
        try {
            publishProgress(1);
            url = new URL(urlString);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();
            RssParser rssParser = new RssParser();
            xmlreader.setContentHandler(rssParser);
            InputSource is = new InputSource(url.openStream());
            xmlreader.parse(is);
            // refresh list
            feedList = rssParser.getFeed();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return feedList;
    }

    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate();
        mProgress.setVisibility(View.VISIBLE);
    }

    protected void onPostExecute(List<ListItem> result) {
        mAdapter.setItems(result);
        mAdapter.notifyDataSetChanged();
        mProgress.setVisibility(View.GONE);
    }
}