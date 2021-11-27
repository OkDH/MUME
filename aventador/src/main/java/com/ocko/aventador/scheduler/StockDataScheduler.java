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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ocko.aventador.component.StockComponent;
import com.ocko.aventador.constant.EtfSymbol;
import com.ocko.aventador.dao.model.aventador.StockHistory;
import com.ocko.aventador.dao.model.aventador.StockHistoryExample;
import com.ocko.aventador.dao.model.aventador.ViewTodayStock;
import com.ocko.aventador.dao.model.aventador.ViewTodayStockExample;
import com.ocko.aventador.dao.persistence.aventador.StockHistoryMapper;
import com.ocko.aventador.dao.persistence.aventador.ViewTodayStockMapper;
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
	
	@Autowired private StockComponent stockComponent;
	@Autowired private StockHistoryMapper stockHistoryMapper;
	@Autowired private ViewTodayStockMapper viewTodayStockMapper;
	
	/**
	 * 주가 데이터 수집 스케쥴러
	 * 전체 프로세스 : 월~토 오후 5시 부터 다음날 오전 6시까지 5분마다 작동
	 * schedulerUpdate1 : 월~금 오후 5시부터 11시 55분까지 5분마다 작동
	 * schedulerUpdate2 : 화~토 오전 0시부터 5시 55분까지 5분마다 작동
	 * schedulerUpdate3 : 화~토 오전 6시 5분에 작동(최종)
	 */
	@Scheduled(cron="0 0/5 17-23 * * MON-FRI")
	public void schedulerUpdate1() {
		updateStocksHistory(LocalDate.now());
	}
	@Scheduled(cron="0 0/5 0-5 * * THU-SAT")
	public void schedulerUpdate2() {
		updateStocksHistory(LocalDate.now().minusDays(1)); // 하루 넘어갔으니 전일 날짜로 업데이트
	}
	@Scheduled(cron="0 5 6 * * THU-SAT")
	public void schedulerUpdate3() {
		updateStocksHistory(LocalDate.now().minusDays(1)); // 하루 넘어갔으니 전일 날짜로 업데이트
	}
	
	/**
	 * 심볼별로 주가 데이터를 수집
	 * 1. 야후 파이낸스 API에서 각 심볼별로 주가 데이터를 가져옴
	 * 2. ViewTodayStock에서 심볼별로 가장 최신주가 데이터를 가져옴
	 * 3. viewTodayStock의 update_time과 파이낸스 데이터의 LastTradeTime과 비교
	 * 4. LastTradeTime이 updateTime보다 나중이라면 업데이트
	 * @param stockDate 주가 일자
	 */
	private void updateStocksHistory(LocalDate stockDate) {
		Map<String, StockDetail> stocks = getStocksData();
		if(stocks == null)
			return;
		
		for(String symbol : stocks.keySet()) {
			StockDetail stock = stocks.get(symbol);
			
			// 2. ViewTodayStock에서 가장 최신을 가져옴.
			ViewTodayStockExample viewExample = new ViewTodayStockExample();
			viewExample.createCriteria().andSymbolEqualTo(stock.getSymbol());
			List<ViewTodayStock> viewTodayStockList = viewTodayStockMapper.selectByExample(viewExample);
			if(!viewTodayStockList.isEmpty()) {
				ViewTodayStock viewTodayStock = viewTodayStockList.get(0);
				
				// 3. viewTodayStock의 update_time과 파이낸스 데이터의 LastTradeTime과 비교
				// 4. LastTradeTime이 updateTime보다 나중이라면 업데이트
				if(stock.getLastTradeTime().isAfter(viewTodayStock.getUpdateTime())) {
					StockHistory stockHistory = new StockHistory();
					
					// 객체복사
					BeanUtils.copyProperties(stock, stockHistory);
					stockHistory.setStockDate(stockDate);
					
					// 전일 데이터 가져오기
					StockHistoryExample example = new StockHistoryExample();
					example.createCriteria().andSymbolEqualTo(stock.getSymbol()).andStockDateNotEqualTo(stockDate);
					example.setOrderByClause("stock_date desc");
					example.setLimit(1);
					List<StockHistory> stockHistorys = stockHistoryMapper.selectByExample(example);

					if(!stockHistorys.isEmpty()) {
						StockHistory yesterDayStock = stockHistorys.get(0);
						
						float up = stock.getChg() > 0 ? stock.getChg() : 0.0f;
						float down = stock.getChg() < 0 ? stock.getChg()*(-1) : 0.0f;
						
						float upAvg = ((yesterDayStock.getUpAvg() * 13) + up) / 14;
						float dwAvg = ((yesterDayStock.getDwAvg() * 13) + down) / 14;
						float rsi = 100 - (100 / (1 + (upAvg / dwAvg)));
						
						stockHistory.setUpAvg(upAvg);
						stockHistory.setDwAvg(dwAvg);
						stockHistory.setRsi(rsi);
						
						stockHistory.setUpdateTime(LocalDateTime.now());
						stockHistoryMapper.upsert(stockHistory);
					}
				}
			}
		}
	}
	
	/**
	 * 실시간 symbol 주가 데이터 가져오기
	 * @return
	 */
	private Map<String, StockDetail> getStocksData(){
		// 심볼리스트
		List<String> symbolList = new ArrayList<String>();
		for(EtfSymbol symbol : EtfSymbol.values()) {
			symbolList.add(symbol.name());
		}
		
		// 심볼리스트 To 배열
		String[] symbols = symbolList.toArray(new String[symbolList.size()]);
		return stockComponent.getStocks(symbols);
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
				stockHistory.setUpdateTime(LocalDateTime.now());
				
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
