package MSSelenium;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

public class App extends JFrame {

	public static void main(String[] args) {

		Tester tester = new Tester();

		SwingUtilities.invokeLater(() -> {
			new App(tester).setVisible(true);
		});

		// runTest(tester);

	}

	public static void runTest(Tester tester) {

		tester.init();

		// you can remove entire sections via ids/classes, before processing
		tester.removeClasses.add("social-popup-share");

		tester.runTestsOnPages();
		tester.getDriver().quit();
	}

	public App(Tester tester) {

		Dimension size;
		int offset = 15;
		int col0 = 15;
		int col1 = 80;
		int col2 = 240;
		int col3 = 320;
		int col4 = 400;
		int col5 = 480;
		

		int rowHeight = 30;
		int ddWidth = 100;
		int windowWidth = 800;
		int windowHeight = 600;
		int padding = 15;

		setTitle("HCL Selenium Task Runner");
		setSize(windowWidth, windowHeight);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		// main panel - holds subpanels
		JPanel panel = new JPanel();
		panel.setLayout(null);
		size = panel.getPreferredSize();
		getContentPane().add(panel);

		// ------------------------------------------------------------------------------>

		// options panel1
		JPanel panel1 = new JPanel();
		panel1.setLayout(null);
		size = panel1.getPreferredSize();
		panel1.setBounds(0, 0, windowWidth, 200);
		panel1.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		panel.add(panel1);

		// mode
		JLabel label2 = new JLabel("mode: ");
		size = label2.getPreferredSize();
		label2.setBounds(col0, (rowHeight * 0) + offset, size.width, size.height);
		panel1.add(label2);
		String[] modes = new String[] { "live", "sitemuse-dev" };
		JComboBox<String> mode = new JComboBox<>(modes);
		size = mode.getPreferredSize();
		mode.setBounds(col1, (rowHeight * 0) + offset, ddWidth, size.height);
		panel1.add(mode);

		// source
		JLabel label3 = new JLabel("source: ");
		size = label3.getPreferredSize();
		label3.setBounds(col0, (rowHeight * 1) + offset, size.width, size.height);
		panel1.add(label3);
		String[] sources = new String[] { "slugs", "sitemap", "urls" };
		JComboBox<String> source = new JComboBox<>(sources);
		size = source.getPreferredSize();
		source.setBounds(col1, (rowHeight * 1) + offset, ddWidth, size.height);
		panel1.add(source);

		// browser
		JLabel label4 = new JLabel("browser: ");
		size = label4.getPreferredSize();
		label4.setBounds(col0, (rowHeight * 2) + offset, size.width, size.height);
		panel1.add(label4);
		String[] browsers = new String[] { "Chrome", "Firefox", "Edge" };
		JComboBox<String> browser = new JComboBox<>(browsers);
		size = browser.getPreferredSize();
		browser.setBounds(col1, (rowHeight * 2) + offset, ddWidth, size.height);
		panel1.add(browser);

		// delay
		JLabel label5 = new JLabel("delay: ");
		size = label5.getPreferredSize();
		label5.setBounds(col0, (rowHeight * 3) + offset, size.width, size.height);
		panel1.add(label5);
		JTextField delay = new JTextField("500", 20);
		size = delay.getPreferredSize();
		delay.setBounds(col1, (rowHeight * 3) + offset, ddWidth, size.height);
		panel1.add(delay);

		// run button
		JButton runTestButton = new JButton("Run Test");
		size = runTestButton.getPreferredSize();
		runTestButton.setBounds(col1, (rowHeight * 4) + offset, ddWidth, size.height);
		panel1.add(runTestButton);

		// viewports
		JLabel label6 = new JLabel("viewports: ");
		size = label6.getPreferredSize();
		label6.setBounds(col2, padding, size.width, size.height);
		panel1.add(label6);
		String vps[] = { "desktop", "tablet", "mobile" };
		JList vp = new JList(vps);
		size = vp.getPreferredSize();
		vp.setBounds(col3, padding, size.width, size.height);
		vp.setVisibleRowCount(3);
		vp.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		vp.setSelectedIndex(0);
		panel1.add(vp);

		// tests
		JLabel label7 = new JLabel("tests: ");
		size = label7.getPreferredSize();
		label7.setBounds(col2, rowHeight * 3, size.width, size.height);
		panel1.add(label7);
		String tests[] = { "links", "pivot tables", "carousels", "scroll-page" };
		JList test = new JList(tests);
		size = test.getPreferredSize();
		test.setBounds(col3, rowHeight * 3, size.width, size.height);
		test.setVisibleRowCount(3);
		test.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		test.setSelectedIndex(0);
		panel1.add(test);
		
		// linkPattern
		JLabel label8 = new JLabel("link pattern: ");
		size = label8.getPreferredSize();
		label8.setBounds(col4, offset, size.width, size.height);
		panel1.add(label8);
		JTextArea patterns = new JTextArea();
		patterns.setEditable(true);
		patterns.setLineWrap(true);
		patterns.setWrapStyleWord(true);
		JScrollPane patternsPane = new JScrollPane(patterns);
		patternsPane.setPreferredSize(new Dimension(300, 50));
		patternsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		size = patternsPane.getPreferredSize();
		patternsPane.setBounds(col5, offset, size.width, size.height);
		panel1.add(patternsPane);

		// ------------------------------------------------------------------------------>

		// output panel2a
		JPanel panel2a = new JPanel();
		panel2a.setLayout(null);
		size = panel2a.getPreferredSize();
		panel2a.setBounds(0, 200, windowWidth, 200);
		panel.add(panel2a);

		JLabel labelSlugs = new JLabel("slugs: ");
		size = labelSlugs.getPreferredSize();
		labelSlugs.setBounds(col0, padding, size.width, size.height);
		panel2a.add(labelSlugs);

		JTextArea slugs = new JTextArea();
		slugs.setEditable(true);
		slugs.setLineWrap(true);
		slugs.setWrapStyleWord(true);
		JScrollPane slugsPane = new JScrollPane(slugs);
		slugsPane.setPreferredSize(new Dimension(400, 143));
		slugsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		size = slugsPane.getPreferredSize();
		slugsPane.setBounds(col0, padding * 3, size.width, size.height);
		panel2a.add(slugsPane);

		int panel2aCol2 = 430;

		// domain
		JLabel labelDomain = new JLabel("domain: ");
		size = labelDomain.getPreferredSize();
		labelDomain.setBounds(panel2aCol2, padding, size.width, size.height);
		panel2a.add(labelDomain);
		JTextField domain = new JTextField("https://www.microsoft.com/en-us", 30);
		size = domain.getPreferredSize();
		domain.setBounds(panel2aCol2, padding * 3, size.width, size.height);
		panel2a.add(domain);

		// site
		JLabel labelSite = new JLabel("site: ");
		size = labelSite.getPreferredSize();
		labelSite.setBounds(panel2aCol2, padding * 5, size.width, size.height);
		panel2a.add(labelSite);
		JTextField site = new JTextField("", 30);
		size = site.getPreferredSize();
		site.setBounds(panel2aCol2, padding * 7, size.width, size.height);
		panel2a.add(site);

		// ------------------------------------------------------------------------------>

		// output panel2b
		JPanel panel2b = new JPanel();
		panel2b.setLayout(null);
		size = panel2b.getPreferredSize();
		panel2b.setBounds(0, 200, windowWidth, 200);
		panel.add(panel2b);

		JLabel labelUrls = new JLabel("urls: ");
		size = labelUrls.getPreferredSize();
		labelUrls.setBounds(col0, padding, size.width, size.height);
		panel2b.add(labelUrls);

		JTextArea urls = new JTextArea();
		urls.setEditable(true);
		urls.setLineWrap(true);
		urls.setWrapStyleWord(true);
		JScrollPane urlsPane = new JScrollPane(urls);
		urlsPane.setPreferredSize(new Dimension(400, 143));
		urlsPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		size = urlsPane.getPreferredSize();
		urlsPane.setBounds(col0, padding * 3, size.width, size.height);
		panel2b.add(urlsPane);

		panel2b.setVisible(false);

		// ------------------------------------------------------------------------------>

		// output panel2c
		JPanel panel2c = new JPanel();
		panel2c.setLayout(null);
		size = panel2c.getPreferredSize();
		panel2c.setBounds(0, 200, windowWidth, 200);
		panel.add(panel2c);

		JLabel labelSitemap = new JLabel("sitemap url: ");
		size = labelSitemap.getPreferredSize();
		labelSitemap.setBounds(col0, padding, size.width, size.height);
		panel2c.add(labelSitemap);
		JTextField sitemap = new JTextField("https://", 30);
		size = sitemap.getPreferredSize();
		sitemap.setBounds(col0, padding * 3, size.width, size.height);
		panel2c.add(sitemap);

		panel2c.setVisible(false);

		// ------------------------------------------------------------------------------>

		// output panel3
		JPanel panel3 = new JPanel();
		panel3.setLayout(null);
		size = panel3.getPreferredSize();
		panel3.setBounds(0, 400, windowWidth, 200);
		panel3.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		panel.add(panel3);

		// url
		JLabel label1 = new JLabel("url: ");
		panel1.add(label1);
		JTextField textField = new JTextField("", 20);
		panel1.add(textField);

		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		int paneWidth = windowWidth - (padding * 2);
		scrollPane.setPreferredSize(new Dimension(paneWidth, 143));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		size = scrollPane.getPreferredSize();
		scrollPane.setBounds(col0, padding, paneWidth, size.height);
		panel3.add(scrollPane);

		 PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
		 System.setOut(printStream);
		 System.setErr(printStream);

		// JScrollPane scrollPane = new JScrollPane(textArea);
		// panel.add(scrollPane);

		runTestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				TestCaseConfig config = new TestCaseConfig();
				
				textArea.setText("");

				config.mode = (String) mode.getSelectedItem();
				config.source = (String) source.getSelectedItem();
				config.site = site.getText();
				config.tests = App.getValsFromJList(test);
				config.viewports = App.getValsFromJList(vp);
				
				String str = patterns.getText();
				String[] patternsArr = str.split("\\s*(\\r?\\n)\\s*");
				
				ArrayList<String> patternList = new ArrayList<String>();
				if(patternsArr.length > 0) {
					for(int i = 0; i < patternsArr.length; i++) {
						patternList.add(patternsArr[i]);
					}
				}
				else {
					patternList.add("//a[(text() or *) and not(@tabindex=-1) and not(@href='#')]");
				}
				patternList.add("//img[@src]");
				config.elemMatchPatterns.put("links", patternList);

				tester.setDriverType((String) browser.getSelectedItem());

				switch (config.source) {

				case "slugs":
					config.domain = domain.getText();
					config.site = site.getText();
					config.delay = Integer.parseInt(delay.getText());

					str = slugs.getText();
					String[] slugsArr = str.split("\\s*,\\s*");

					tester.config = config;

					try {
						tester.setPages(slugsArr);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					if (tester.config.domain.matches("^(http|https)://.*")) {
						runTest(tester);
					}

					break;

				case "sitemap":
					config.sitemapUrl = sitemap.getText();
					tester.config = config;
					try {
						tester.setPages(null);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					runTest(tester);
					break;

				case "urls":
					str = urls.getText();
					String[] pagesArr = str.split("\\s*(\\r?\\n)\\s*");
					tester.pages = pagesArr;

					tester.config = config;
					runTest(tester);
					break;

				}

			}
		});

		source.addItemListener(new ItemListener() {
			/**
			 * @param e
			 */
			public void itemStateChanged(ItemEvent e) {

				switch (e.getItem().toString()) {

				case "slugs":
					panel2a.setVisible(true);
					panel2b.setVisible(false);
					panel2c.setVisible(false);
					break;

				case "urls":
					panel2a.setVisible(false);
					panel2b.setVisible(true);
					panel2c.setVisible(false);
					break;

				case "sitemap":
					panel2a.setVisible(false);
					panel2b.setVisible(false);
					panel2c.setVisible(true);
					break;

				}

			}
		});

	}

	public static String[] getValsFromJList(JList list) {
		int[] indices = list.getSelectedIndices();
		String[] data = new String[indices.length];

		for (int i = 0; i < indices.length; i++) {
			data[i] = list.getModel().getElementAt(indices[i]).toString();
		}

		return data;
	}

}
