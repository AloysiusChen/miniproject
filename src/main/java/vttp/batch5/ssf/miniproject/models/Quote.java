package vttp.batch5.ssf.miniproject.models;

public class Quote {
    private String fullName;
    private String symbol;
    private double currentPrice;
    private double change;
    private double percentChange;
    
    public Quote(String fullName, String symbol, double currentPrice, double change, double percentChange) {
        this.fullName = fullName;
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.change = change;
        this.percentChange = percentChange;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(double percentChange) {
        this.percentChange = percentChange;
    }

    @Override
    public String toString() {
        return "Quote [fullName=" + fullName + ", symbol=" + symbol + ", currentPrice=" + currentPrice + ", change="
                + change + ", percentChange=" + percentChange + "]";
    }
}
