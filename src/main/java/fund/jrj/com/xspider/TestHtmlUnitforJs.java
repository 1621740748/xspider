package fund.jrj.com.xspider;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class TestHtmlUnitforJs {

	public static void main(String[] args) {
        
        HtmlUnitDriver driver = new HtmlUnitDriver();
        driver.setJavascriptEnabled(true);
        String url="http://fund.jrj.com.cn/";
        driver.get(url);
        List<WebElement> webElements=driver.findElementsByCssSelector("link");
        System.out.println("--------------link------------------------");
        for(WebElement we:webElements) {
        	System.out.println(we.getAttribute("href"));
        }
        webElements=driver.findElementsByCssSelector("script");
        System.out.println("--------------script------------------------");
        for(WebElement we:webElements) {
        	System.out.println(we.getAttribute("src"));
        }
        webElements=driver.findElementsByCssSelector("img");
        System.out.println("--------------img------------------------");
        for(WebElement we:webElements) {
        	System.out.println(we.getAttribute("src"));
        }

	}

}
