package arsath.SeleniumUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.junit.Assert;

public class ExecuteActions {
	public static WebDriver driver;
	public static int WaitTimeInmillis = 60 * 1000;

	public void OpenBrowser(String BrowserName) {

		boolean alreadydriveropen = false;
		try {
			// System.out.print(driver.toString());
			alreadydriveropen = !(driver.toString().contains("null"));
		} catch (Exception e) {
		}
		if (!alreadydriveropen) {
			if (BrowserName.equalsIgnoreCase("Firefox")) {
				driver = new FirefoxDriver();
			} else if (BrowserName.equalsIgnoreCase("IE")) {
				try {
					System.setProperty("webdriver.ie.driver",
							new File(".").getCanonicalPath() + "\\Browsers\\IEDriverServer.exe");
				} catch (IOException e) {

				}
				DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
				ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
				ieCapabilities.setJavascriptEnabled(true);
				ieCapabilities.setCapability("nativeEvents", false);
				ieCapabilities.setCapability("requireWindowFocus", true);
				driver = new InternetExplorerDriver(ieCapabilities);
			} else if (BrowserName.equalsIgnoreCase("Chrome")) {
				try {
					System.setProperty("webdriver.chrome.driver",
							new File(".").getCanonicalPath() + "\\Browsers\\chromedriver.exe");
				} catch (IOException e) {
				}
				driver = new ChromeDriver();
			}
			driver.manage().window().maximize();
		} else {
			driver.switchTo().window(driver.getWindowHandle());
		}

	}

	public void SetWaitTimeInSecs(int WaitTimeInSeconds) {
		WaitTimeInmillis = 1000 * WaitTimeInSeconds;
	}

	public void CloseBrowser() {
		try {
			driver.close();
			driver.quit();
		} catch (Exception e) {
		}
	}

	public void ExecuteSCriptsFromTXTFile(String TC_Name) throws Exception {

		BufferedReader br = null;
		List<String> ContentList = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(TC_Name));

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		@SuppressWarnings("unused")
		StringBuilder sb = new StringBuilder();
		try {
			String line = br.readLine();
			while (line != null) {
				ContentList.add(line);
				line = br.readLine();
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		String Action = "";
		String Element = "";
		String Xpath = "";
		String TestData = "";
		int LineNumber = 1;

		for (int i = 0; i < ContentList.size(); i = i + 4) {
			Action = ContentList.get(i);
			Element = ContentList.get(i + 1);
			Xpath = ContentList.get(i + 2);
			TestData = ContentList.get(i + 3);
			if (LineNumber == 0) {
				LineNumber = 1;
			}
			if (LineNumber != 0) {
				LineNumber = 1 + (i / 4);
			}
			individualRowExecution(Action, Element, Xpath, TestData, TC_Name, LineNumber);
		}

	}

	void individualRowExecution(String Action, String Element, String Xpath, String TestData, String TC_Name,
			int LineNumber) throws Exception {
		if (Action.equalsIgnoreCase("Action_Click")) {
			UserAction(By.xpath(Xpath), Action, TestData, Element, TC_Name, LineNumber);
		} else if (Action.equalsIgnoreCase("Action_Select")) {
			UserAction(By.xpath(Xpath), Action, TestData, Element, TC_Name, LineNumber);
		} else if (Action.equalsIgnoreCase("Action_Enter Text")) {
			UserAction(By.xpath(Xpath), Action, TestData, Element, TC_Name, LineNumber);
		} else if (Action.equalsIgnoreCase("Action_Assert Text")) {
			UserAction(By.xpath(Xpath), Action, TestData, Element, TC_Name, LineNumber);
		} else if (Action.equalsIgnoreCase("Action_Open URL")) {
			UserAction(By.xpath(Xpath), Action, TestData, Element, TC_Name, LineNumber);
		} else if (Action.equalsIgnoreCase("Action_Wait Box")) {
			UserAction(By.xpath(Xpath), Action, TestData, Element, TC_Name, LineNumber);
		} else if (Action.equalsIgnoreCase("Action_Wait For Secs")) {
			UserAction(By.xpath(Xpath), Action, TestData, Element, TC_Name, LineNumber);
		} else if (Action.equalsIgnoreCase("Action_Set Variable")) {
			UserAction(By.xpath(Xpath), Action, TestData, Element, TC_Name, LineNumber);
		} else if (Action.trim().isEmpty()) {
			// No Action
		} else {
			ExecuteActions newActions = new ExecuteActions();
			System.out.println(Action.substring(3, Action.length()));
			newActions.ExecuteSCriptsFromTXTFile(
					Main.TCFolder + Main.ProjectFolder + Action.substring(3, Action.length()) + ".txt");
		}

	}

	static HashMap<String, String> VariableHashMap = new HashMap<String, String>();

	private void UserAction(By Selector, String UserAction, String TestData, String Element, String TC_Name,
			int LineNumber) throws Exception {
		long CurrentTimeInMilliSeconds = System.currentTimeMillis();
		long WaitTillMilliSeconds = CurrentTimeInMilliSeconds + (WaitTimeInmillis);
		boolean Action_Successful = false;
		while (CurrentTimeInMilliSeconds < WaitTillMilliSeconds) {
			try {
				CurrentTimeInMilliSeconds = System.currentTimeMillis();
				// Manipulating Test Data Based on the Used Variable or KeyWord
				if (TestData.contains("Key_PCDate")) {
					TestData = calculateDate(TestData.replace("Key_", ""), "PCDate");
				} else if (TestData.contains("Key_TimeInMillis")) {
					TestData = Long.toString((System.currentTimeMillis()));
				} else if (TestData.contains("Var_")) {
					TestData = VariableHashMap.get(TestData);
				}

				switch (UserAction) {
				case "Action_Set Variable":
					if (Element.startsWith("Var_")) {
						VariableHashMap.put(Element, TestData);
						Action_Successful = true;
						return;
					} else {
						throw new Exception(
								"To set a variable, please use the prefix 'Var_' followed by the Variable Name");
					}
				case "Action_Click":
					driver.findElement(Selector).click();
					Action_Successful = true;
					return;
				case "Action_Select":
					Select ElementSelect = new Select(driver.findElement(Selector));
					if (TestData.isEmpty()) {
						ElementSelect.selectByIndex(0);
					} else {
						ElementSelect.selectByVisibleText(TestData);
					}
					Action_Successful = true;
					return;
				case "Action_Enter Text":
					driver.findElement(Selector).clear();
					driver.findElement(Selector).sendKeys(TestData);
					Action_Successful = true;
					return;
				case "Action_Assert Text":
					String ActualText = driver.findElement(Selector).getText();
					Assert.assertEquals(ActualText, TestData);
					Action_Successful = true;
					return;
				case "Action_Open URL":
					driver.get(TestData);
					try {
						driver.switchTo().alert().accept();
						driver.switchTo().defaultContent();
					} catch (Exception e) {
					}
					Action_Successful = true;
					return;
				case "Action_Wait Box":
					JOptionPane.showMessageDialog(null, TestData);
					Action_Successful = true;
					return;
				case "Action_Wait For Secs":
					if (Integer.parseInt(TestData) > 900) {
						JOptionPane.showMessageDialog(null,
								"Wait time cannot be greater than 900 secs, Please change in the script. Click 'Ok' to proceed with the next step");
					} else {
						Thread.sleep(Integer.parseInt(TestData) * 1000);
					}
					Action_Successful = true;
					return;
				default:
					break;
				}

			} catch (Exception e) {

				// Thread.sleep(1000);
			}
		}
		if (!Action_Successful)

		{
			throw new Exception("Action Not Successful on the element:'" + Element + "'  Test case:'" + TC_Name
					+ "'  Line Number:'" + LineNumber + "'");
		}
	}

	public static String calculateDate(String Value, String typeOfDate) {
		try {
			String dateValue = null;
			String year = "";
			String month = "";
			String day = "";
			String[] values = { "", "" };
			if (Value.contains("+")) {
				values = Value.split("\\+");
			}
			if (Value.contains("-")) {
				values = Value.split("-");
			}
			if (values[1].length() == 9) {
				year = values[1].substring(0, 2);
				month = values[1].substring(3, 5);
				day = values[1].substring(6, 8);
			} else if (values[1].length() == 6) {
				String first3 = values[1].substring(0, 3);
				if (first3.contains("Y")) {
					year = values[1].substring(0, 2);
					month = values[1].substring(3, 5);
				} else if (first3.contains("M")) {
					month = values[1].substring(0, 2);
					day = values[1].substring(3, 5);
				}
			} else if (values[1].length() == 3) {
				if (values[1].contains("Y")) {
					year = values[1].substring(0, 2);
				} else if (values[1].contains("M")) {
					month = values[1].substring(0, 2);
				} else if (values[1].contains("D")) {
					day = values[1].substring(0, 2);
				}
			} else if (values[1].length() == 4) {
				if (values[1].contains("Y")) {
					year = values[1].substring(0, 3);
				} else if (values[1].contains("M")) {
					month = values[1].substring(0, 3);
				} else if (values[1].contains("D")) {
					day = values[1].substring(0, 3);
				}
			} else if (values[1].length() == 5) {
				if (values[1].contains("Y")) {
					year = values[1].substring(0, 4);
				} else if (values[1].contains("M")) {
					month = values[1].substring(0, 4);
				} else if (values[1].contains("D")) {
					day = values[1].substring(0, 4);
				}
			} else if (values[1].length() == 2) {
				if (values[1].contains("Y")) {
					year = values[1].substring(0, 1);
				} else if (values[1].contains("M")) {
					month = values[1].substring(0, 1);
				} else if (values[1].contains("D")) {
					day = values[1].substring(0, 1);
				}
			}

			if (day == "") {
				day = "0";
			}
			if (month == "") {
				month = "0";
			}
			if (year == "") {
				year = "0";
			}
			int finalDay = Integer.parseInt(day);
			int finalMonth = Integer.parseInt(month);
			int finalYear = Integer.parseInt(year);
			if (Value.contains("-")) {
				finalDay = -Integer.parseInt(day);
				finalMonth = -Integer.parseInt(month);
				finalYear = -Integer.parseInt(year);
			}
			if (typeOfDate.equalsIgnoreCase("PCDate")) {
				dateValue = addDaysToSysDate(finalDay, finalMonth, finalYear);
			}
			return dateValue;
		} catch (Exception e) {
			// e.printStackTrace();
			return "";
		}
	}

	public static String addDaysToSysDate(int noOfDay, int noOfMonths, int noOfYear) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, noOfDay);
			calendar.add(Calendar.MONTH, noOfMonths);
			calendar.add(Calendar.YEAR, noOfYear);
			return sdf.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
