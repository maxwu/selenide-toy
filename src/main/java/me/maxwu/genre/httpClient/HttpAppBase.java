package me.maxwu.httpClient;

import me.maxwu.genre.GenreTerm;
import me.maxwu.genre.IGenreCmd;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by maxwu on 3/19/17.
 */
public class HttpAppBase implements IGenreCmd{
    static Logger logger = LoggerFactory.getLogger(HttpAppBase.class.getName());

    @Override
    public List<String> getSongGenres(String song, String artist) {
        return searchGenreFor(song + " genre");
    }

    private List<String> searchGenreFor(String text){
        String req = "https://www.google.com/search?q=" + text;
        List<String> genres;

        try {
            Document doc = Jsoup.connect(req)
                    .userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                    .timeout(5000).get();
            String results = doc.select("#rso div._uX div._XWk").text();

            logger.debug("Query: [" + text + "] , return: [" + results + "]");

            genres = Arrays.asList(results.split("/"));

        }catch (IOException e){
            logger.warn("Failed to query with [" + text + "]: " + e.getMessage());
            genres = GenreTerm.getDefaultGenreList();
        }
        return genres;
    }

    public static void main(String[] args){
        new HttpAppBase().getSongGenres("That's What I Like", "Bruno Mars").stream().forEach(System.out::println);
    }
}
