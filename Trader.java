public abstract class Trader {
    private int patienceLevel;
    private int food;
    private int water;
    private int gold;
    private boolean isTrading;
    private Offer offer;
    private Offer counterOffer;

    public Trader(){
        this.patienceLevel = 0;
        this.food = 0;
        this.water = 0;
        this.gold = 0;
        isTrading = true;
        offer=null;
    }
    
    public Trader(int patienceLevel, int food, int water, int gold){
        this.patienceLevel = patienceLevel;
        this.food = food;
        this.water = water;
        this.gold = gold;
        isTrading = true;
        offer=null;
    }

    public void evaluateTrade(){
        int weight=0;
        if(offer.getOfferFood() >=0 && offer.getOfferWater() >=0 && offer.getOfferGold() >=0 
        && offer.getWantFood()  >=0 && offer.getWantWater()  >=0 && offer.getWantGold()  >=0 ){
            if( (offer.getWantFood() - offer.getOfferFood()) > food){
                patienceLevel--;
            }

            if( (offer.getWantWater() - offer.getOfferWater()) >water){
                patienceLevel--;
            }

            if( (offer.getWantGold() - offer.getOfferGold()) >gold){
                patienceLevel--;
            }

            weight+= offer.getOfferFood()-offer.getWantFood();
            weight+= offer.getOfferWater()-offer.getWantWater();
            weight+= offer.getOfferGold()-offer.getWantGold();
            
            
        } else {
            patienceLevel-=2;
        }

        if(0 < weight && weight < 3){

        } else if(weight >= 3){
            
        } else {
            patienceLevel--;
        }

        if(patienceLevel<=0 ){
            quitNegotiation();
        }
    }

    public void counter_offer(){
        
    }
    
    public void quitNegotiation(){
        isTrading=false;
    }

    public int getPatienceLevel() {
        return patienceLevel;
    }
    
    public void setPatienceLevel(int patienceLevel) {
        this.patienceLevel = patienceLevel;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public boolean isTrading() {
        return isTrading;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}
