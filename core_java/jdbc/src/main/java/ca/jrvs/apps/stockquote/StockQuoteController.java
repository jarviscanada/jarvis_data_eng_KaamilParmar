package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.dao.PositionDao;
import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.dao.QuoteDao;
import ca.jrvs.apps.stockquote.dao.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.services.PositionService;
import ca.jrvs.apps.stockquote.services.QuoteService;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;


public class StockQuoteController {

    static final Logger logger = LoggerFactory.getLogger(StockQuoteController.class);
    private QuoteService sQuote;
    private PositionService sPos;
    public StockQuoteController(QuoteService sQuote, PositionService sPos) {
        this.sQuote = sQuote;
        this.sPos = sPos;
    }

    public static void client(String[] args) {
            Map<String, String> properties = new HashMap<>();


            try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/properties.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] tokens = line.split(":");
                    properties.put(tokens[0], tokens[1]);
                }
            } catch (FileNotFoundException e) {
                logger.error("Could not find properties file.");
            } catch (IOException e) {
                logger.error("Something went wrong accessing the properites file");
                throw new RuntimeException(e);
            }

            try {
                Class.forName(properties.get("db-class"));
            } catch (ClassNotFoundException e) {
                logger.error("Could not find database");
            }
            OkHttpClient client = new OkHttpClient();
            String url = "jdbc:postgresql://"+properties.get("server")+":"+properties.get("port")+"/"+properties.get("database");
            try (Connection c = DriverManager.getConnection(url, properties.get("username"), properties.get("password"))) {
                QuoteDao qRepo = new QuoteDao(c);
                PositionDao pRepo = new PositionDao(c);
                QuoteHttpHelper rcon = new QuoteHttpHelper(properties.get("api-key"), client);
                QuoteService sQuote = new QuoteService(qRepo, rcon);
                PositionService sPos = new PositionService(pRepo);
                StockQuoteController con = new StockQuoteController(sQuote, sPos);
                con.initClient();
            } catch (SQLException e) {
                logger.error("SQLException, could not establish connection.");
            }
        }

    public void initClient() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter a ticker followed by a command (or simply 'end' to exit): ");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("end")) {
                break;
            }

            String[] args = command.split(" ");
            if (args.length < 2) {
                System.out.println("Invalid command. Please try again.");
                continue;
            }

            String ticker = args[0].toUpperCase();
            String operation = args[1].toLowerCase();

            if (operation.equals("save")) {
                Optional<Quote> quoteOptional = sQuote.fetchQuoteDataFromAPI(ticker);

                if (quoteOptional.isPresent()) {
                    // Quote object is not empty, handle the data
                    sQuote.save(quoteOptional.get());
                } else {
                    // Quote object is empty, let client know
                    System.out.println("Quote could not be saved");
                };

            } else if (operation.equals("find")) {
                // Use DAO functions for finding
                Optional<Quote> quote = sQuote.find(ticker);
                if(quote.isPresent()) System.out.println(" "+quote.get().toString());
                else System.out.println("No quote exists for this. Try saving it first.");

            } else if (operation.equals("delete")) {
                sQuote.delete(ticker);
                System.out.println("Quote was deleted if it exists.");
            } else if (operation.equals("buy")) {
                if (args.length < 4) {
                    System.out.println("Insufficient arguments provided for buying.");
                    continue;
                }
                int numOfShares = Integer.parseInt(args[2]);
                double price = Double.parseDouble(args[3]);
                sPos.buy(ticker, numOfShares, price);
            } else if (operation.equals("sell")) {
                if (args.length < 3) {
                    System.out.println("Insufficient arguments provided for selling.");
                    continue;
                }
                sPos.sell(ticker);
            } else if (operation.equals("findall")) {

                for (Quote quote : sQuote.findAll()) {
                    System.out.println(quote.toString());
                }
            }else {
                System.out.println("Invalid command: " + operation + ". Please try again.");
            }
        }

        scanner.close();
    }

}
