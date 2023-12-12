package ca.jrvs.apps.stockquote.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class QuoteHttpHelper {
    private String apiKey;
    private OkHttpClient client;

    public QuoteHttpHelper(String apiKey) {
        this.apiKey = apiKey;
    }


    //main for testing purposes
    public static void main(String[] args) {
        QuoteHttpHelper helper = new QuoteHttpHelper("");
        helper.fetchQuoteInfo("MSFT");
    }

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
            String JSONresp = response.body();
            ObjectMapper om = new ObjectMapper();

            quote = om.readValue(JSONresp, Quote.class);

            System.out.println(response.body()+"\n e"+ quote.getTicker());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
