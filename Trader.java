import java.util.Random;

public abstract class Trader {
    private int patienceLevel;
    private int food;
    private int water;
    private int gold;
    private boolean isTrading;
    private Offer offer;
    private Offer counterOffer;
    private final Random random = new Random();

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

    public Offer evaluateTrade(){
        int weight=0;
        boolean successful=false;
        Offer trade= new Offer();
        if(offer.getOfferFood() >=0 && offer.getOfferWater() >=0 && offer.getOfferGold() >=0 
        && offer.getWantFood()  >=0 && offer.getWantWater()  >=0 && offer.getWantGold()  >=0 ){

            weight+= offer.getOfferFood()-offer.getWantFood();
            weight+= offer.getOfferWater()-offer.getWantWater();
            weight+= offer.getOfferGold()-offer.getWantGold();
            
            int roll = random.nextInt(100);
            if(-1 <= weight && weight < 2){
                if(roll>=40){
                    successful=true;
                } else {
                    patienceLevel--;
                }
            } else if(2 <= weight && weight < 4){
                if(roll>=10){
                    successful=true;
                }
            } else if(weight >= 4){
                successful=true;
            } else {
                patienceLevel--;
            }

            if(successful){
                trade.setOfferFood(offer.getWantFood()-offer.getOfferFood());
                trade.setOfferWater(offer.getWantWater()-offer.getOfferWater());
                trade.setOfferGold(offer.getWantGold()-offer.getOfferGold());
            }
            
        } else {
            patienceLevel-=2;
        }        

        if(patienceLevel<=0 ){
            quitNegotiation();
        }
        return trade;
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
