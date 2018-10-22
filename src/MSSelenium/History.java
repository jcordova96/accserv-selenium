package MSSelenium;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import jxl.write.WriteException;

public class History {

	ArrayList<HistoryItem> temp;
	List<HistoryItem> history;

	public History() {
		this.history = new ArrayList<HistoryItem>();
		this.temp = new ArrayList<HistoryItem>();
	}

	public void add(String type, String name, String description1, String description2, String group, String status, String msg, String repeat, int order) {
		this.history.add(new HistoryItem(type, name, description1, description2, group, status, msg, repeat, order));
	}
	
	public void addTemp(String type, String name, String description1, String description2, String group, String status, String msg, String repeat, int order) {
		this.temp.add(new HistoryItem(type, name, description1, description2, group, status, msg, repeat, order));
	}
	
	public void processTemp() {
    	temp.sort((o1, o2) -> o1.order.compareTo(o2.order));
		addMultiple(this.temp);
		this.temp = new ArrayList<HistoryItem>();
	}
	
	public void addMultiple(ArrayList<HistoryItem> items) {
		for(HistoryItem item : items) {
			this.history.add(item);
		}
	}

	public void printJSON() {
		Gson gson = new Gson();
		System.out.println(gson.toJson(this.history));
	}

	public void saveAsXls() {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		Date date = new Date();
		String dateStr = dateFormat.format(date);
		WriteExcel xlWriter = new WriteExcel();
//		xlWriter.setOutputFile("logs/history - " + dateStr + ".xls");
		xlWriter.setOutputFile("history - " + dateStr + ".xls");
		
		try {
			xlWriter.writeLinksReport(history);
		} catch (WriteException | IOException e) {
			e.printStackTrace();
		}
	}

}
