/**
 * 
 */
package com.ocko.aventador;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.ocko.aventador.constant.EtfSymbol;
import com.ocko.aventador.dao.model.aventador.StockHistory;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

/**
 * @author ok
 *
 */
@SpringBootTest
public class BackTest {
	
	@Test
	public void backTest() throws IOException {
		
		
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.YEAR, -1);
		
		String symbol = EtfSymbol.TQQQ.name();
		
		Stock stock = YahooFinance.get(symbol, from, to, Interval.DAILY);
		List<HistoricalQuote> histQuotes = stock.getHistory();

		Queue<Map<String, BigDecimal>> queue = new LinkedList<>();
		BigDecimal beforeClose = null;
		BigDecimal beforeUpAvg = null;
		BigDecimal beforeDwAvg = null;
		
		for(HistoricalQuote data : histQuotes) {
			
			StockHistory stockHistory = new StockHistory();
			stockHistory.setSymbol(symbol);
			stockHistory.setPriceLow(data.getLow());
			stockHistory.setPriceHigh(data.getHigh());
			stockHistory.setPriceOpen(data.getOpen());
			stockHistory.setPriceClose(data.getClose());
			stockHistory.setVolume(data.getVolume());
			LocalDate date = LocalDateTime.ofInstant(data.getDate().toInstant(), ZoneId.systemDefault()).toLocalDate();
			stockHistory.setStockDate(date);
			stockHistory.setUpdateTime(LocalDateTime.now());
			
			// rsi
			// rsi 계산을 위한 상승폭 또는 하락폭 큐에 저장
			BigDecimal nowClose = data.getClose();
			BigDecimal up = new BigDecimal(0);
			BigDecimal down = new BigDecimal(0);

			Map<String, BigDecimal> upDownMap = new HashMap<String, BigDecimal>();
			
			// 전날 데이터가 있을 경우에 상승폭, 하락폭 계산
			if(beforeClose != null) { 
				stockHistory.setPrevClose(beforeClose);
				stockHistory.setChg(data.getClose().subtract(beforeClose));
				stockHistory.setChgp((stockHistory.getChg().divide(beforeClose, 8, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100))));
				
				if(beforeClose.compareTo(nowClose) < 0) {
					up = nowClose.subtract(beforeClose);
					down = new BigDecimal(0);
				} else if(beforeClose.compareTo(nowClose) > 0) {
					up = new BigDecimal(0);
					down = beforeClose.subtract(nowClose);
				} else {
					up = new BigDecimal(0);
					down = new BigDecimal(0);
				}
				upDownMap.put("up", up);
				upDownMap.put("down", down);
			}
			
			// 15일째 부터 rsi 계산 가능 
			if(queue.size() == 14) {
				
				if(beforeUpAvg == null && beforeDwAvg == null) { // 14일째는 상승값 평균, 하락값 평균값 도출
					BigDecimal upSum = new BigDecimal(0);
					BigDecimal downSum = new BigDecimal(0);
					for(Map<String, BigDecimal> map : queue) {
						upSum = upSum.add(map.get("up"));
						downSum = downSum.add(map.get("down"));
					}
					
					beforeUpAvg = upSum.divide(new BigDecimal(14), 8, RoundingMode.HALF_EVEN);
					beforeDwAvg = downSum.divide(new BigDecimal(14), 8, RoundingMode.HALF_EVEN);
				} else { // 15일째부터 (전날 평균 값 * 13) + 15일 상승,하락값으로 계산
					
					// ((beforeUpAvg * 13) + up) / 14;
					beforeUpAvg = ((beforeUpAvg.multiply(new BigDecimal(13))).add(up)).divide(new BigDecimal(14), 8, RoundingMode.HALF_EVEN);
					// ((beforeDwAvg * 13) + down) / 14;
					beforeDwAvg = ((beforeDwAvg.multiply(new BigDecimal(13))).add(down)).divide(new BigDecimal(14), 8, RoundingMode.HALF_EVEN);

					// 100 - (100 / (1 + (beforeUpAvg / beforeDwAvg)));
					BigDecimal rsi = new BigDecimal(100).subtract(new BigDecimal(100).divide(new BigDecimal(1).add(beforeUpAvg.divide(beforeDwAvg, 8, RoundingMode.HALF_EVEN)), 8, RoundingMode.HALF_EVEN));
					
					stockHistory.setRsi(rsi);
				}
				
				stockHistory.setUpAvg(beforeUpAvg);
				stockHistory.setDwAvg(beforeDwAvg);
				
				queue.remove();
			}
			
			if(beforeClose != null)
				queue.add(upDownMap);
			
			beforeClose = nowClose;
			
			System.out.println(stockHistory.getSymbol());
		}
	}

}
