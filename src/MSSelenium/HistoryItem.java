package MSSelenium;

public class HistoryItem {
	
	String type;
	String name;
	String description1;
	String description2;
	String group;
	String status;
	String msg;
	String repeat;
	Integer order;
	
	public HistoryItem(String type, String name, String description1, String description2, String group, String status, String msg, String repeat, int order) {
		this.type = type;
		this.name = name;
		this.description1 = description1;
		this.description2 = description2;
		this.group = group;
		this.status = status;
		this.msg = msg;
		this.repeat = repeat;
		this.order = order;
	}
}
