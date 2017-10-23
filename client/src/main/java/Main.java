import com.company.CreditScore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.javafx.css.Rule;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {


    public static void main(String[] argv) {

       // CreditScore.creditScore("1212950424");
        LoanRequest req = new LoanRequest(1212950424,new CreditScore().creditScore("121295-0424"), 10, 120);
        System.out.println("credit score "+req.getCreditScore());
        getBankList(req);
        callBank1(req);
        callBank2(req);
        callBank3(req);
        callBank4(req);
    }

    private static void getBankList(LoanRequest req){
        RuleBaseSoap ruleBaseSoap = new RuleBaseSoap(req);
        System.out.println("Banks " +ruleBaseSoap.callSoapWebService());
    }

    private static void callBank1(LoanRequest req){
        Bank1RequestRabbitMq bank1Req = null;
        String bankResponse1 = null;
        try {
            bank1Req = new Bank1RequestRabbitMq();
            ObjectMapper mapper = new ObjectMapper();
            bankResponse1 = bank1Req.call(mapper.writeValueAsString(req));
            System.out.println("Bank 1: " + bankResponse1);
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (bank1Req != null) {
                try {
                    bank1Req.close();
                } catch (IOException _ignore) {
                }
            }
        }
    }


    private static void callBank2(LoanRequest req){
        Bank2RequestSoap bank2Soap = new Bank2RequestSoap(req);
        System.out.println("Bank 2: " +bank2Soap.callSoapWebService());
    }

    private static void callBank3(LoanRequest req){
        Bank3Json bank3JsonReq = null;
        String bankResponse3 = null;
        try {
            bank3JsonReq = new Bank3Json();
            ObjectMapper mapper = new ObjectMapper();
            bankResponse3 = bank3JsonReq.call(mapper.writeValueAsString(req));
            System.out.println("Bank 3: " + bankResponse3);
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bank3JsonReq != null) {
                try {
                    bank3JsonReq.close();
                } catch (IOException _ignore) {
                }
            }
        }
    }

    private static void callBank4(LoanRequest req){
        Bank4Xml bank4Xml = null;
        String bankResponse4 = null;
        try {
            bank4Xml  = new Bank4Xml();
            bankResponse4 = bank4Xml .call(req);
            System.out.println("Bank 4: " + bankResponse4);
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bank4Xml  != null) {
                try {
                    bank4Xml.close();
                } catch (IOException _ignore) {
                }
            }
        }
    }




}
