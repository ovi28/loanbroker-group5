
public class Request {

    private int ssn;
    private int creditScore;
    private float loanAmount;
    private float loanDuration;

    public Request(int ssn, int creditScore, float loanAmount, float loanDuration) {
        this.ssn = ssn;
        this.creditScore = creditScore;
        this.loanAmount = loanAmount;
        this.loanDuration = loanDuration;
    }


    public int getSsn() {
        return ssn;
    }

    public void setSsn(int ssn) {
        this.ssn = ssn;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public float getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(float loanAmount) {
        this.loanAmount = loanAmount;
    }

    public float getLoanDuration() {
        return loanDuration;
    }

    public void setLoanDuration(float loanDuration) {
        this.loanDuration = loanDuration;
    }
}
