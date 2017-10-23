
public class BankResponse {

    private int ssn;
    private float interestRate;

    public BankResponse(int ssn, float interestRate) {
        this.ssn = ssn;
        this.interestRate = interestRate;
    }

    public int getSsn() {
        return ssn;
    }

    public void setSsn(int ssn) {
        this.ssn = ssn;
    }

    public float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }
}
