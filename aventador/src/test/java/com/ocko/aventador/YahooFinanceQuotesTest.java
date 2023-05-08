package com.ocko.aventador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import yahoofinance.Stock;
import yahoofinance.Utils;
import yahoofinance.YahooFinance;
import yahoofinance.exchanges.ExchangeTimeZone;
import yahoofinance.quotes.stock.StockDividend;
import yahoofinance.quotes.stock.StockQuote;
import yahoofinance.quotes.stock.StockStats;
import yahoofinance.util.RedirectableRequest;

public class YahooFinanceQuotesTest {
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * -DEPRECATED-
	 * 작업 배경
	 * YahooFinace API 라이브러리가 정상적으로 작동하지 않아서, 직접 API URL 호출하는 방법을 테스트 
	 * API URL : https://query1.finance.yahoo.com/v7/finance/quote
	 * 이 방법도 막혀서 DEPRECATED 처리
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		String crumb = getCrumb();
		
		Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("symbols", "TQQQ,SOXL");

        String urlStr = YahooFinance.QUOTES_QUERY1V7_BASE_URL + "?" + Utils.getURLParameters(params);
        URL url = new URL(urlStr);
     // HttpURLConnection 객체 생성
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        // 요청 헤더에 User-Agent, Cookie, crumb 값을 추가
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Cookie", "B=abcdefghi; ywadpuc=" + crumb);
        conn.setRequestProperty("crumb", crumb);
        
        int responseCode = conn.getResponseCode();
        
        System.out.println("결과값 : " + responseCode);
        
        // API 응답 결과를 읽어오기 위해 BufferedReader 객체 생성
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        
        // API 응답 결과 출력
        System.out.println(response.toString());
        
        {
        	List<Stock> result = new ArrayList<Stock>();
        	JsonNode node = objectMapper.readTree(response.toString());
	        if(node.has("quoteResponse") && node.get("quoteResponse").has("result")) {
	            node = node.get("quoteResponse").get("result");
	            for(int i = 0; i < node.size(); i++) {
	                result.add(this.parseJson(node.get(i)));
	            }
	        } else {
	            throw new IOException("Invalid response");
	        }
	        for(Stock s : result) {
	        	System.out.println(s.getSymbol());
	        }
        }
	}
	
	String getCrumb() throws IOException {
		String tickerSymbol = "AAPL";
        
        // 인증 토큰과 crumb 값을 얻어오기 위해 해당 종목의 Yahoo Finance 페이지를 가져옴
        String urlStr = "https://finance.yahoo.com/quote/" + tickerSymbol;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        int responseCode = conn.getResponseCode();
        
        // 가져온 페이지의 HTML 내용을 읽어옴
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        
        // 인증 토큰과 crumb 값을 추출하는 정규식
        Pattern pattern = Pattern.compile("root\\.App\\.main = (\\{.*?\\});", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response.toString());
        
        // HTML 페이지에서 인증 토큰과 crumb 값을 추출하여 변수에 저장
        String authToken = null;
        String crumb = null;
        if (matcher.find()) {
            String jsonString = matcher.group(1);
            int firstIndex = jsonString.indexOf("token\":");
            int lastIndex = jsonString.indexOf(",", firstIndex);
            authToken = jsonString.substring(firstIndex + 8, lastIndex - 1);
            firstIndex = jsonString.indexOf("crumb\":\"");
            lastIndex = jsonString.indexOf("\"", firstIndex + 9);
            crumb = jsonString.substring(firstIndex + 8, lastIndex);
        }
        
        // 추출한 인증 토큰과 crumb 값을 출력
        System.out.println("Auth Token : " + authToken);
        System.out.println("Crumb : " + crumb);
        
        return crumb;
	}
	
	Stock parseJson(JsonNode node) {
        String symbol = node.get("symbol").asText();
        Stock stock = new Stock(symbol);

        if(node.has("longName")) {
            stock.setName(node.get("longName").asText());
        } else {
            stock.setName(getStringValue(node, "shortName"));
        }

        stock.setCurrency(getStringValue(node, "currency"));
        stock.setStockExchange(getStringValue(node, "fullExchangeName"));

        stock.setQuote(this.getQuote(node));
        stock.setStats(this.getStats(node));
        stock.setDividend(this.getDividend(node));

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
