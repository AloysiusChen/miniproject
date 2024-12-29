package vttp.batch5.ssf.miniproject.services;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.http.MediaType;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import vttp.batch5.ssf.miniproject.models.Quote;
import vttp.batch5.ssf.miniproject.repositories.FinnhubRepository;

@Service
public class FinnhubService {

    // Section: Field Declarations, Dependencies, and Constants
    @Value("${finnhub.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FinnhubRepository finnhubRepo;

    private final Logger logger = Logger.getLogger(FinnhubService.class.getName());

    public static final String QUOTE_URL = "https://finnhub.io/api/v1/quote";
    public static final String MARKET_STATUS_URL = "https://finnhub.io/api/v1/stock/market-status?exchange=US";

    // Initialise ETF Symbols and store it to Redis (Market Overview)
    public void initialiseETFSymbols() {
        List<String> etfSymbolsList = List.of("VT", "SPY", "EWU", "EWJ");

        finnhubRepo.addEtfSymbolsToList("marketOverviewEtfOrder", etfSymbolsList);

        Map<String, String> etfSymbolsMap = Map.of(
                "VT", "Vanguard Total World Stock ETF",
                "SPY", "SPDR S&P 500 ETF Trust",
                "EWU", "iShares MSCI United Kingdom ETF",
                "EWJ", "iShares MSCI Japan ETF");

        finnhubRepo.addEtfSymbolsToMap("marketOverviewEtfMap", etfSymbolsMap);
    }

    public List<Quote> getQuotes() {
        List<Quote> quotes = new ArrayList<>();

        List<String> etfSymbolsList = finnhubRepo.getEtfSymbolsListFromRedis("marketOverviewEtfOrder");
        Map<Object, Object> marketOverviewEtfMap = finnhubRepo.getEtfMapFromRedis("marketOverviewEtfMap");

        for (String symbol : etfSymbolsList) {
            String key = "marketOverview:" + symbol;

            Map<Object, Object> cachedQuote = finnhubRepo.getQuoteFromRedis(key);

            if (cachedQuote.isEmpty()) {
                String fullName = (String) marketOverviewEtfMap.get(symbol);
                Quote quote = fetchQuoteFromApi(symbol, fullName);

                if (quote != null) {
                    finnhubRepo.saveQuoteToRedis(key, Map.of(
                            "fullName", quote.getFullName(),
                            "currentPrice", String.valueOf(quote.getCurrentPrice()),
                            "change", String.valueOf(quote.getChange()),
                            "percentChange", String.valueOf(quote.getPercentChange()),
                            "timestamp", LocalDateTime.now().toString()));
                    quotes.add(quote);
                }
            } else {
                // If cached quote exists, use it
                Quote cachedQuoteObj = getCachedQuoteFromRedis(cachedQuote, symbol);
                quotes.add(cachedQuoteObj);
            }
        }
        return quotes;
    }

    // Fetch a quote from the Finnhub API
    private Quote fetchQuoteFromApi(String symbol, String fullName) {
        String json = fetchJsonQuote(symbol);
        return parseJsonQuote(json, symbol, fullName);
    }

    // Get a cached quote object from Redis
    private Quote getCachedQuoteFromRedis(Map<Object, Object> cachedQuote, String symbol) {
        String fullName = (String) cachedQuote.get("fullName");
        double currentPrice = Double.parseDouble((String) cachedQuote.get("currentPrice"));
        double change = Double.parseDouble((String) cachedQuote.get("change"));
        double percentChange = Double.parseDouble((String) cachedQuote.get("percentChange"));

        return new Quote(fullName, symbol, currentPrice, change, percentChange);
    }

    @Scheduled(fixedRate = 300000)
    public void refreshRedisCache() {
        if (isMarketOpen()) {
            logger.info("Refreshing ETF quotes in Redis cache...");

            List<String> etfSymbolsList = finnhubRepo.getEtfSymbolsListFromRedis("marketOverviewEtfOrder");
            Map<Object, Object> marketOverviewEtfMap = finnhubRepo.getEtfMapFromRedis("marketOverviewEtfMap");

            for (String symbol : etfSymbolsList) {
                String fullName = (String) marketOverviewEtfMap.get(symbol);
                // Fetch quote from API
                Quote quote = fetchQuoteFromApi(symbol, fullName);

                // Cache the new quote
                if (quote != null) {
                    finnhubRepo.saveQuoteToRedis("marketOverview:" + symbol, Map.of(
                            "fullName", quote.getFullName(),
                            "currentPrice", String.valueOf(quote.getCurrentPrice()),
                            "change", String.valueOf(quote.getChange()),
                            "percentChange", String.valueOf(quote.getPercentChange()),
                            "timestamp", LocalDateTime.now().toString()));
                }
            }
        }
    }

    // Helper codes

    // https://finnhub.io/api/v1/quote?
    // symbol=SPY&
    // token=apiKey
    private String fetchJsonQuote(String symbol) {
        String url = UriComponentsBuilder
                .fromUriString(QUOTE_URL)
                .queryParam("symbol", symbol)
                .queryParam("token", apiKey)
                .toUriString();

        // REMOVE, TESTING
        logger.info("Constructed URL: " + url);

        RequestEntity<Void> req = RequestEntity
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        try {
            ResponseEntity<String> resp = restTemplate.exchange(req, String.class);

            String payload = resp.getBody();
            return payload;
        } catch (HttpStatusCodeException ex) {
            logger.severe("HTTP error fetching quote for symbol: " + symbol + ", status: " + ex.getStatusCode());
        } catch (RestClientException ex) {
            logger.severe("Client error while fetching quote for symbol: " + symbol + ": " + ex.getMessage());
        } catch (Exception ex) {
            logger.severe("Unexpected error fetching quote for symbol: " + symbol + ": " + ex.getMessage());
        }
        return null;
    }

    private Quote parseJsonQuote(String json, String symbol, String fullName) {
        try {
            JsonReader reader = Json.createReader(new StringReader(json));

            JsonObject result = reader.readObject();

            double c = result.getJsonNumber("c").doubleValue(); // Current price
            double d = result.getJsonNumber("d").doubleValue(); // Change
            double dp = result.getJsonNumber("dp").doubleValue(); // Percent change

            return new Quote(fullName, symbol, c, d, dp);
        } catch (Exception ex) {
            logger.severe("Error parsing JSON response: " + json + ": " + ex.getMessage());
        }
        return null;
    }

    public boolean isMarketOpen() {
        String url = MARKET_STATUS_URL + "&token=" + apiKey;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String responseBody = response.getBody();

            JsonObject jsonResponse = Json.createReader(new StringReader(responseBody)).readObject();
            boolean isOpen = jsonResponse.getBoolean("isOpen");
            // default to empty string if null
            String session = jsonResponse.isNull("session") ? "" : jsonResponse.getString("session");

            return isOpen && "regular".equals(session);
        } catch (Exception e) {
            logger.severe("Error checking market status: " + e.getMessage());
            // Proceed with the assumption that market is open to continue caching.
            return true;
        }
    }

    public LocalDateTime getLastCacheUpdate() {
        Map<Object, Object> quote = finnhubRepo.getQuoteFromRedis("marketOverview:SPY");
        return LocalDateTime.parse((String) quote.get("timestamp"));
    }
}