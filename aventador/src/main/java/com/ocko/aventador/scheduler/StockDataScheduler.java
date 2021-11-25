/**
 * 
 */
package com.ocko.aventador.scheduler;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ocko.aventador.component.StockComponent;
import com.ocko.aventador.constant.EtfSymbol;
import com.ocko.aventador.dao.model.aventador.StockHistory;
import com.ocko.aventador.dao.persistence.aventador.StockHistoryMapper;
import com.ocko.aventador.model.StockDetail;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

/**
 * @author ok
 *
 */
@Component
public class StockDataScheduler {
	
	private static final Logger log = LoggerFactory.getLogger(StockDataScheduler.class);
	
	@Autowired private StockHistoryMapper stockHistoryMapper;
	@Autowired private StockComponent stockComponent;
	
	/**
	 * 월~토
	 * 오후 5시 부터 다음날 오전 6시까지 5분마다 작동
	 * 
	 */
	@Scheduled(cron="0 0/5 17-23 * * MON-FRI")
	public void updateStockHistory1() {
		
//		// 심볼리스트
//		List<String> symbolList = new ArrayList<String>();
//		for(EtfSymbol symbol : EtfSymbol.values()) {
//			symbolList.add(symbol.name());
//		}
//		
//		// 심볼리스트 To 배열
//		String[] symbols = symbolList.toArray(new String[symbolList.size()]);
//		// 심볼 주가 복수 조회
//		Map<String, StockDetail> stocks = stockComponent.getStocks(symbols);
//		
//		for(String symbol : stocks.keySet()) {
//			
//		}

		log.info("하이1");
	}
	
	@Scheduled(cron="0 0/5 0-6 * * THU-SAT")
	public void updateStockHistory2() {
		log.info("하이2");
	}
	
	
	
	/**
	 * ETFs 주가 최근 1년치 초기 구축 (1회성)
	 * @throws IOException 
	 */
	public void firstCollectStockHistory() throws IOException {
		
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.YEAR, -2);
		
		for(EtfSymbol item : EtfSymbol.values()) {
			Stock stock = YahooFinance.get(item.name());
			List<HistoricalQuote> histQuotes = stock.getHistory(from, to, Interval.DAILY);

			Queue<Map<String, Float>> queue = new LinkedList<>();
			float beforeClose = 0;
			float beforeUpAvg = 0;
			float beforeDwAvg = 0;
			for(HistoricalQuote data : histQuotes) {
				StockHistory stockHistory = new StockHistory();
				stockHistory.setSymbol(item.name());
				stockHistory.setPriceLow(data.getLow().floatValue());
				stockHistory.setPriceHigh(data.getHigh().floatValue());
				stockHistory.setPriceOpen(data.getOpen().floatValue());
				stockHistory.setPriceClose(data.getClose().floatValue());
				stockHistory.setVolume(data.getVolume());
				LocalDate date = LocalDateTime.ofInstant(data.getDate().toInstant(), ZoneId.systemDefault()).toLocalDate();
				stockHistory.setStockDate(date);
				
				// rsi
				// rsi 계산을 위한 상승폭 또는 하락폭 큐에 저장
				float nowClose = data.getClose().floatValue();
				float up = 0;
				float down = 0;
				Map<String, Float> upDownMap = new HashMap<String, Float>();
				
				// 전날 데이터가 있을 경우에 상승폭, 하락폭 계산
				if(beforeClose != 0) { 
					stockHistory.setPrevClose(beforeClose);
					stockHistory.setChg(data.getClose().floatValue() - beforeClose);
					stockHistory.setChgp((stockHistory.getChg() / beforeClose) * 100);
					
					if(beforeClose < nowClose) {
						up = nowClose - beforeClose;
						down = 0.0f;
					} else if(beforeClose > nowClose) {
						up = 0.0f;
						down = beforeClose - nowClose;
					} else {
						up = 0.0f;
						down = 0.0f;
					}
					upDownMap.put("up", up);
					upDownMap.put("down", down);
				}
				
				// 15일째 부터 rsi 계산 가능 
				if(queue.size() == 14) {
					
					if(beforeUpAvg == 0 && beforeDwAvg == 0) { // 14일은 상승값 평균, 하락값 평균값 도출
						float upSum = 0;
						float downSum = 0;
						for(Map<String, Float> map : queue) {
							upSum += map.get("up");
							downSum += map.get("down");
						}
						
						beforeUpAvg = upSum/14;
						beforeDwAvg = downSum/14;
					} else { // 15일째부터 (전날 평균 값 * 13) + 15일 상승,하락값으로 계산
						
						beforeUpAvg = ((beforeUpAvg * 13) + up) / 14;
						beforeDwAvg = ((beforeDwAvg * 13) + down) / 14;

						float rsi = 100 - (100 / (1 + (beforeUpAvg / beforeDwAvg)));
						stockHistory.setRsi(rsi);
					}
					
					stockHistory.setUpAvg(beforeUpAvg);
					stockHistory.setDwAvg(beforeDwAvg);
					
					queue.remove();
				}
				
				if(beforeClose != 0)
					queue.add(upDownMap);
				
				beforeClose = nowClose;
				
				stockHistoryMapper.insertIgnore(stockHistory);
			}
		}
	}
	

}
