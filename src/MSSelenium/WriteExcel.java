package MSSelenium;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class WriteExcel {
	
	private WritableCellFormat captionFormat1;
	private WritableCellFormat cellFormat1;
	private WritableCellFormat cellFormat2;
	private WritableCellFormat cellFormat3;
	private WritableCellFormat cellFormat4;
	private WritableCellFormat cellFormat5;
	private String inputFile;

	private void setup() throws WriteException {
		WritableFont cellFont1 = new WritableFont(WritableFont.TIMES, 10);
		cellFormat1 = new WritableCellFormat(cellFont1);
		cellFormat1.setWrap(true);
		
		WritableFont cellFont2 = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.RED);
		cellFormat2 = new WritableCellFormat(cellFont2);
		cellFormat2.setWrap(true);

		WritableFont cellFont3 = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.GOLD);
		cellFormat3 = new WritableCellFormat(cellFont3);
		cellFormat3.setWrap(true);

		WritableFont cellFont4 = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD, false,
				UnderlineStyle.NO_UNDERLINE, Colour.GOLD);
		cellFormat4 = new WritableCellFormat(cellFont4);
		cellFormat4.setBackground(Colour.GRAY_50);
		cellFormat4.setWrap(true);

		WritableFont cellFont5 = new WritableFont(WritableFont.TIMES, 18, WritableFont.BOLD, false,
				UnderlineStyle.SINGLE, Colour.BLACK);
		cellFormat5 = new WritableCellFormat(cellFont5);
		cellFormat5.setWrap(true);

		WritableFont captionFont1 = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,
				UnderlineStyle.SINGLE);
		captionFormat1 = new WritableCellFormat(captionFont1);
		captionFormat1.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(cellFormat1);
		cv.setFormat(captionFormat1);
		cv.setAutosize(true);
	}
	
	public void setOutputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public void writeLinksReport(List<HistoryItem> history) throws IOException, WriteException {
		setup();
		
		File file = new File(inputFile);
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		
		workbook.createSheet("All Elements", 0);
		WritableSheet excelSheet0 = workbook.getSheet(0);
		excelSheet0.setColumnView(0, 20);
		addCaption(excelSheet0, 0, 0, "Label");
		excelSheet0.setColumnView(1, 70);
		addCaption(excelSheet0, 1, 0, "URL");
		excelSheet0.setColumnView(2, 10);
		addCaption(excelSheet0, 2, 0, "Status");
		excelSheet0.setColumnView(3, 6);
		addCaption(excelSheet0, 3, 0, "Repeat");
		excelSheet0.setColumnView(4, 20);
		addCaption(excelSheet0, 4, 0, "Message");
		excelSheet0.setColumnView(5, 70);
		addCaption(excelSheet0, 5, 0, "Final URL");
		excelSheet0.setColumnView(6, 20);
		addCaption(excelSheet0, 6, 0, "Type");
		excelSheet0.getSettings().setVerticalFreeze(1);
		
		workbook.createSheet("Errors", 1);
		WritableSheet excelSheet1 = workbook.getSheet(1);
		excelSheet1.setColumnView(0, 50);
		addCaption(excelSheet1, 0, 0, "Source");
		excelSheet1.setColumnView(1, 20);
		addCaption(excelSheet1, 1, 0, "Label");
		excelSheet1.setColumnView(2, 70);
		addCaption(excelSheet1, 2, 0, "Link");
		excelSheet1.setColumnView(3, 10);
		addCaption(excelSheet1, 3, 0, "Status");
		excelSheet1.setColumnView(4, 6);
		addCaption(excelSheet1, 4, 0, "Repeat");
		excelSheet1.setColumnView(5, 20);
		addCaption(excelSheet1, 5, 0, "Message");
		excelSheet1.setColumnView(6, 70);
		addCaption(excelSheet1, 6, 0, "Final URL");
		excelSheet1.setColumnView(7, 20);
		addCaption(excelSheet1, 7, 0, "Type");
		excelSheet1.getSettings().setVerticalFreeze(1);
		
		int i = 1;
		int j = 1;
		String lastGroup = "";
		for (HistoryItem item : history) {
			int format;
			switch(item.status) {
			case "failed":
				format = 2;
				break;
			case "check":
				format = 3;
				break;
			default:
				format = 1;
				break;
			}
			
			if (lastGroup != item.group) {
				if(i != 1) {
					i++;
					addLabel(excelSheet0, 0, i, "", 4);
					addLabel(excelSheet0, 1, i, "", 4);
					addLabel(excelSheet0, 2, i, "", 4);
					addLabel(excelSheet0, 3, i, "", 4);
					addLabel(excelSheet0, 4, i, "", 4);
					addLabel(excelSheet0, 5, i, "", 4);
					addLabel(excelSheet0, 6, i, "", 4);
					i = i + 2;
				}
				excelSheet0.mergeCells(0, i, 6, i);
				excelSheet0.setRowView(i, 400);
				addLabel(excelSheet0, 0, i, item.group, 5);
				i = i + 2;
			}
			
			addLabel(excelSheet0, 0, i, item.name, 1);
			addLabel(excelSheet0, 1, i, item.description1, 1);
			addLabel(excelSheet0, 2, i, item.status, format);
			addLabel(excelSheet0, 3, i, item.repeat, 1);
			addLabel(excelSheet0, 4, i, item.msg.replaceAll("\n", " - "), 1);
			addLabel(excelSheet0, 5, i, item.description2, 1);
			addLabel(excelSheet0, 6, i, item.type, 1);
			i++;
			
			lastGroup = item.group;
			
			if(item.status == "failed" || item.status == "check") {
				addLabel(excelSheet1, 0, j, item.group, 1);
				addLabel(excelSheet1, 1, j, item.name, 1);
				addLabel(excelSheet1, 2, j, item.description1, 1);
				addLabel(excelSheet1, 3, j, item.status, format);
				addLabel(excelSheet1, 4, j, item.repeat, 1);
				addLabel(excelSheet1, 5, j, item.msg.replaceAll("\n", " - "), 1);
				addLabel(excelSheet1, 6, j, item.description2, 1);
				addLabel(excelSheet1, 7, j, item.type, 1);
				j++;
			}
		}		
		
		workbook.write();
		workbook.close();
	}


	private void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, captionFormat1);
		sheet.addCell(label);
	}

	private void addNumber(WritableSheet sheet, int column, int row, Integer integer)
			throws WriteException, RowsExceededException {
		Number number;
		number = new Number(column, row, integer, cellFormat1);
		sheet.addCell(number);
	}

	private void addLabel(WritableSheet sheet, int column, int row, String s, int format)
			throws WriteException, RowsExceededException {
		WritableCellFormat cellFormat = getFormat(format);
		Label label;
		label = new Label(column, row, s, cellFormat);
		sheet.addCell(label);
	}
	
	private WritableCellFormat getFormat(int formatId) {
		WritableCellFormat cellFormat;
		
		switch(formatId) {
		case 1:
			cellFormat = cellFormat1;
			break;
			
		case 2:
			cellFormat = cellFormat2;
			break;
			
		case 3:
			cellFormat = cellFormat3;
			break;
			
		case 4:
			cellFormat = cellFormat4;
			break;
			
		case 5:
			cellFormat = cellFormat5;
			break;
			
		default:
			cellFormat = cellFormat1;
			break;
		}
		
		return cellFormat;
	}
	
	private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");
	public static String removeTags(String string) {
	    if (string == null || string.length() == 0) {
	        return string;
	    }

	    Matcher m = REMOVE_TAGS.matcher(string);
	    return m.replaceAll("");
	}

}
