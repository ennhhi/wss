public abstract class Trader {
    int patienceLevel;
    String[] items;

    public Trader(){
        
    }

    public void counter_offer(){
        
    }
    
    public void quitNegotiation(){
        
    }

    public int getPatienceLevel() {
        return patienceLevel;
    }
    
    public void setPatienceLevel(int patienceLevel) {
        this.patienceLevel = patienceLevel;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }
}
