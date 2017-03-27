import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.*;

public class Main {

    public static int counter = 0;

    public static void decodePage(List<HtmlElement> elementList){

        if (elementList.isEmpty()){
            System.out.println("No Results were found!");
        } else {
            for (HtmlElement element : elementList){
                HtmlAnchor anchor = (HtmlAnchor)element.getFirstByXPath(".//a");
                String itemName = anchor.asText();
                HtmlElement priceSpanElement = element.getFirstByXPath(".//span[@class='result-price']");
                String itemPrice = "no price";

                if (priceSpanElement!=null) {
                    itemPrice = priceSpanElement.asText();
                }

                counter++;
                System.out.println(String.valueOf(counter) + String.format(" Name : %s\nPrice : %s", itemName, itemPrice));
            }
        }
    }


    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your craigslist query");
        String searchQuery = scanner.next();
        HtmlPage page = null;
        String searchURL = "";

        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        try{
            searchURL = "http://newyork.craigslist.org/search/sss?sort=rel&query=" + URLEncoder.encode(searchQuery,"UTF-8");
            page = client.getPage(searchURL);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

       java.util.List<HtmlElement> elementList = page.getByXPath("//p[@class='result-info']");
        int nextPageResult = elementList.size();

        int pageNumber = 1;
        while (nextPageResult != 0 && pageNumber<=5) {
            decodePage(elementList);
            page = client.getPage(searchURL + "&s=" + nextPageResult);
            elementList = page.getByXPath("//p[@class='result-info']");
            nextPageResult = elementList.size();
            pageNumber++;
        }

    }



}
