package ca.jrvs.apps.stockquote.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;

public class QuoteHttpHelper {
    final Logger logger = LoggerFactory.getLogger(QuoteHttpHelper.class);

    private String apiKey;
    private OkHttpClient client;

    public QuoteHttpHelper(String apiKey, OkHttpClient client) {
        this.apiKey = apiKey;
        this.client = client;
    }


    //main for testing purposes
//    public static void main(String[] args) {
//        QuoteHttpHelper helper = new QuoteHttpHelper("", client);
//        helper.fetchQuoteInfo("MSFT");
//    }

    /**
     * Fetch latest quote data from Alpha Vantage endpoint
     * @param ticker pk
     * @return Quote with latest data
     * @throws IllegalArgumentException - if no data was found for the given symbol
     */
    public Quote fetchQuoteInfo(String ticker) throws IllegalArgumentException {
        Quote quote = new Quote();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol="+ticker+"&datatype=json"))
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            logger.info("Response received from Vantage API.");
            ObjectMapper om = new ObjectMapper();

            JsonNode jsonNode = om.readTree(response.body());

            quote = om.treeToValue(jsonNode.get("Global Quote"), Quote.class);
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

            quote.setTimestamp(currentTimestamp);


        } catch (InterruptedException e) {
            logger.error("Could not receive response properly from Vantage API.");
        } catch (JsonMappingException e) {
            logger.error("Could not parse data into JSON.");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return quote;
    }
}
