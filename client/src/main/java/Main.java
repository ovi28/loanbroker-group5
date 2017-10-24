import com.company.CreditScore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Main {

    static boolean bank1 = false, bank2 = false, bank3 = false, bank4 = false;
    static float bank1Interest = 0, bank2Interest = 0, bank3Interest = 0, bank4Interest = 0;
    static Normalizer normalizer = new Normalizer();

    public static void main(String[] argv) {

        Scanner userInput = new Scanner(System.in);
        System.out.println("Please enter the SSN");
        String ssn = userInput.next();
        System.out.println("Please enter the loan amount ");
        String loanAmount = userInput.next();
        System.out.println("Please enter the duration of the loan");
        String duration = userInput.next();
        int creditScore = new CreditScore().creditScore(ssn);
        ssn = ssn.replace("-", "");
        LoanRequest req = new LoanRequest(Long.parseLong(ssn), creditScore, Float.parseFloat(loanAmount), Float.parseFloat(duration));
        if (creditScore == -1 || Float.parseFloat(duration) < 1 || Float.parseFloat(loanAmount) < 1) {
            System.out.println("Invalid values");
        } else {
            recipientList(req);
        }

    }

    private static void recipientList(LoanRequest req) {
        String banks = getBankList(req);
        System.out.println("Banks " + banks);
        if (banks.contains("1")) {
            //bank1=true;
        }
        if (banks.contains("2")) {
            bank2 = true;
        }
        if (banks.contains("3")) {
            bank3 = true;
        }
        if (banks.contains("4")) {
            bank4 = true;
        }
        if (banks.contains("1")) {
            //callBank1(req);
        }
        if (banks.contains("2")) {
            callBank2(req);
        }
        if (banks.contains("3")) {
            callBank3(req);
        }
        if (banks.contains("4")) {
            callBank4(req);
        }
    }

    private static String getBankList(LoanRequest req) {
        RuleBaseSoap ruleBaseSoap = new RuleBaseSoap(req);
        return ruleBaseSoap.callSoapWebService();
    }

    private static void callBank1(LoanRequest req) {
        Bank1RequestRabbitMq bank1Req = null;
        String bankResponse1 = null;
        try {
            bank1Req = new Bank1RequestRabbitMq();
            ObjectMapper mapper = new ObjectMapper();
            bankResponse1 = bank1Req.call(mapper.writeValueAsString(req));
            System.out.println("Bank 1: " + bankResponse1);
            bank1Interest = normalizer.normalizeJson(bankResponse1);
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (bank1Req != null) {
                try {
                    bank1 = false;
                    bank1Req.close();
                    if (!bank1 && !bank2 && !bank3 && !bank4) {
                        agregate();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static void callBank2(LoanRequest req) {
        Bank2RequestSoap bank2Soap = new Bank2RequestSoap(req);
        String resp = bank2Soap.callSoapWebService();
        bank2Interest = normalizer.normalizeBankSoap(resp);
        System.out.println("Bank 2: " + bank2Soap.callSoapWebService());
        bank2 = false;
        if (!bank1 && !bank2 && !bank3 && !bank4) {
            agregate();
        }
    }

    private static void callBank3(LoanRequest req) {
        Bank3Json bank3JsonReq = null;
        String bankResponse3 = null;
        try {
            bank3JsonReq = new Bank3Json();
            ObjectMapper mapper = new ObjectMapper();
            bankResponse3 = bank3JsonReq.call(mapper.writeValueAsString(req));
            System.out.println("Bank 3: " + bankResponse3);
            bank3Interest = normalizer.normalizeJson(bankResponse3);
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bank3JsonReq != null) {
                try {
                    bank3JsonReq.close();
                    bank3 = false;
                    if (!bank1 && !bank2 && !bank3 && !bank4) {
                        agregate();
                    }
                } catch (IOException ignore) {
                }
            }
        }
    }

    private static void callBank4(LoanRequest req) {
        Bank4Xml bank4Xml = null;
        String bankResponse4 = null;
        try {
            bank4Xml = new Bank4Xml();
            bankResponse4 = bank4Xml.call(req);
            System.out.println("Bank 4: " + bankResponse4);
            bank4Interest = normalizer.normalizeXml(bankResponse4);
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bank4Xml != null) {
                try {
                    bank4Xml.close();
                    bank4 = false;
                    if (!bank1 && !bank2 && !bank3 && !bank4) {
                        agregate();
                    }
                } catch (IOException ignore) {
                }
            }
        }
    }

    private static void agregate() {
        float min = Integer.MAX_VALUE;
        int bank = 0;
        if (bank1Interest < min && bank1Interest>0) {
            min = bank1Interest;
            bank = 1;
        }
        if (bank2Interest < min && bank2Interest>0) {
            min = bank2Interest;
            bank = 2;
        }
        if (bank3Interest < min && bank3Interest>0) {
            min = bank3Interest;
            bank = 3;
        }
        if (bank4Interest < min && bank4Interest>0) {
            min = bank4Interest;
            bank = 4;
        }

        System.out.println("The best quote is " + min + " from bank "+bank);

    }


}
