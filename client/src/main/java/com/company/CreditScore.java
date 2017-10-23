package com.company;

import com.company.creditScore.CreditScoreService;
import com.company.creditScore.CreditScoreService_Service;

public class CreditScore {


    public int creditScore(java.lang.String ssn) {
        CreditScoreService_Service service = new CreditScoreService_Service();
        CreditScoreService port = service.getCreditScoreServicePort();
        return port.creditScore(ssn);
    }

}
