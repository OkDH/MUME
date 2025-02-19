/**
 * 
 */
package com.ocko.aventador.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import yahoofinance.Stock;
import yahoofinance.Utils;
import yahoofinance.exchanges.ExchangeTimeZone;
import yahoofinance.quotes.stock.StockDividend;
import yahoofinance.quotes.stock.StockQuote;
import yahoofinance.quotes.stock.StockStats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author ok
 * YahooFinance-API가 header에 crumb를 포함해서 보내지 않기 때문에 stock정보를 가져올 때 가끔 오류가 발생하여 
 * API에서 통신부분을 대신하여 UrlConnection으로 직접 구현.
 */
@Component
public class YahooFinanceComponent {
	
	private final String API_URL = "https://query1.finance.yahoo.com/v6/finance/options";
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public Stock get(String symbol) throws IOException {
		 return getResult(symbol);
	}
	
	public Map<String, Stock> get(List<String> symbols) throws IOException {
		Map<String, Stock> result = new HashMap<String, Stock>();
		for(String symbol : symbols) {
            result.put(symbol, getResult(symbol));
        }
		return result;
	}
	
	private Stock getResult(String symbol) throws IOException {
		
        String urlStr = API_URL + "/" + symbol;
        URL url = new URL(urlStr);
        
        // HttpURLConnection 객체 생성
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");
        
        // API 응답 결과를 읽어오기 위해 BufferedReader 객체 생성
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        
    	JsonNode node = objectMapper.readTree(response.toString());
        if(node.has("optionChain") && node.get("optionChain").has("result")
        		&& node.get("optionChain").get("result").has(0)
        		&& node.get("optionChain").get("result").get(0).has("quote")) {
            return parseJson(node.get("optionChain").get("result").get(0).get("quote"));
        } else {
            throw new IOException("Invalid response");
        }
	}
	
	public Stock parseJson(JsonNode node) {
        String symbol = node.get("symbol").asText();
        Stock stock = new Stock(symbol);

        if(node.has("longName")) {
            stock.setName(node.get("longName").asText());
        } else {
            stock.setName(getStringValue(node, "shortName"));
        }

        stock.setCurrency(getStringValue(node, "currency"));
        stock.setStockExchange(getStringValue(node, "fullExchangeName"));

        stock.setQuote(getQuote(node));
        stock.setStats(getStats(node));
        stock.setDividend(getDividend(node));

        return stock;
    }
	
	private String getStringValue(JsonNode node, String field) {
        if(node.has(field)) {
            return node.get(field).asText();
        }
        return null;
    }


    private StockQuote getQuote(JsonNode node) {
        String symbol = node.get("symbol").asText();
        StockQuote quote = new StockQuote(symbol);

        quote.setPrice(Utils.getBigDecimal(getStringValue(node,"regularMarketPrice")));
        // quote.setLastTradeSize(null);
        quote.setAsk(Utils.getBigDecimal(getStringValue(node,"ask")));
        quote.setAskSize(Utils.getLong(getStringValue(node,"askSize")));
        quote.setBid(Utils.getBigDecimal(getStringValue(node,"bid")));
        quote.setBidSize(Utils.getLong(getStringValue(node,"bidSize")));
        quote.setOpen(Utils.getBigDecimal(getStringValue(node,"regularMarketOpen")));
        quote.setPreviousClose(Utils.getBigDecimal(getStringValue(node,"regularMarketPreviousClose")));
        quote.setDayHigh(Utils.getBigDecimal(getStringValue(node,"regularMarketDayHigh")));
        quote.setDayLow(Utils.getBigDecimal(getStringValue(node,"regularMarketDayLow")));

        if(node.has("exchangeTimezoneName")) {
            quote.setTimeZone(TimeZone.getTimeZone(node.get("exchangeTimezoneName").asText()));
        } else {
            quote.setTimeZone(ExchangeTimeZone.getStockTimeZone(symbol));
        }

        if(node.has("regularMarketTime")) {
            quote.setLastTradeTime(Utils.unixToCalendar(node.get("regularMarketTime").asLong()));
        }

        quote.setYearHigh(Utils.getBigDecimal(getStringValue(node,"fiftyTwoWeekHigh")));
        quote.setYearLow(Utils.getBigDecimal(getStringValue(node,"fiftyTwoWeekLow")));
        quote.setPriceAvg50(Utils.getBigDecimal(getStringValue(node,"fiftyDayAverage")));
        quote.setPriceAvg200(Utils.getBigDecimal(getStringValue(node,"twoHundredDayAverage")));

        quote.setVolume(Utils.getLong(getStringValue(node,"regularMarketVolume")));
        quote.setAvgVolume(Utils.getLong(getStringValue(node,"averageDailyVolume3Month")));

        return quote;
    }

    private StockStats getStats(JsonNode node) {
        String symbol = getStringValue(node,"symbol");
        StockStats stats = new StockStats(symbol);

        stats.setMarketCap(Utils.getBigDecimal(getStringValue(node,"marketCap")));
        // stats.setSharesFloat(Utils.getLong(getStringValue(node,"sharesOutstanding")));
        stats.setSharesOutstanding(Utils.getLong(getStringValue(node,"sharesOutstanding")));
        // stats.setSharesOwned(Utils.getLong(getStringValue(node,"symbol")));

        stats.setEps(Utils.getBigDecimal(getStringValue(node,"epsTrailingTwelveMonths")));
        stats.setPe(Utils.getBigDecimal(getStringValue(node,"trailingPE")));
        // stats.setPeg(Utils.getBigDecimal(getStringValue(node,"symbol")));

        stats.setEpsEstimateCurrentYear(Utils.getBigDecimal(getStringValue(node,"epsForward")));
        // stats.setEpsEstimateNextQuarter(Utils.getBigDecimal(getStringValue(node,"symbol")));
        // stats.setEpsEstimateNextYear(Utils.getBigDecimal(getStringValue(node,"symbol")));

        stats.setPriceBook(Utils.getBigDecimal(getStringValue(node,"priceToBook")));
        // stats.setPriceSales(Utils.getBigDecimal(getStringValue(node,"symbol")));
        stats.setBookValuePerShare(Utils.getBigDecimal(getStringValue(node,"bookValue")));

        // stats.setOneYearTargetPrice(Utils.getBigDecimal(getStringValue(node,"symbol")));
        // stats.setEBITDA(Utils.getBigDecimal(getStringValue(node,"symbol")));
        // stats.setRevenue(Utils.getBigDecimal(getStringValue(node,"symbol")));

        // stats.setShortRatio(Utils.getBigDecimal(getStringValue(node,"symbol")));

        if(node.has("earningsTimestamp")) {
            stats.setEarningsAnnouncement(Utils.unixToCalendar(node.get("earningsTimestamp").asLong()));
        }

        return stats;
    }

    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    
    private StockDividend getDividend(JsonNode node) {
        String symbol = this.getStringValue(node, "symbol");
        StockDividend dividend = new StockDividend(symbol);

        if (node.has("dividendDate")) {
            long dividendTimestamp = node.get("dividendDate").asLong();
            dividend.setPayDate(Utils.unixToCalendar(dividendTimestamp));
            // dividend.setExDate(Utils.unixToCalendar(node.get("dividendDate").asLong()));
        }
        if (node.has("trailingAnnualDividendRate")) {
            dividend.setAnnualYield(Utils.getBigDecimal(this.getStringValue(node, "trailingAnnualDividendRate")));
        }
        if (node.has("trailingAnnualDividendYield")) {
            BigDecimal yield = Utils.getBigDecimal(this.getStringValue(node, "trailingAnnualDividendYield"));
            if (yield != null) {
                dividend.setAnnualYieldPercent(yield.multiply(ONE_HUNDRED));
            }
        }

        return dividend;
    }
}
