import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class Normalizer {

    public float normalizeJson(String jsonStr){
        float interest = 0;
        JSONObject obj = (JSONObject) JSONValue.parse(jsonStr);

        interest =Float.parseFloat( obj.get("interestRate").toString());
        return interest;
    }

    public float normalizeBankSoap(String text){
        return Float.parseFloat(text.split(",")[1]);
    }

    public float normalizeXml(String xml){
        Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
        return Float.parseFloat(doc.select("interestRate").text());

    }
}
