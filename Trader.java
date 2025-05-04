import java.util.Random;

public abstract class Trader {
    private int patienceLevel;
    private int food;
    private int water;
    private int gold;
    private boolean isTrading;
    private int change;
    private final Random random = new Random();

    public Trader(){
        this.patienceLevel = 0;
        this.food = 0;
        this.water = 0;
        this.gold = 0;
        this.change = 1;
        isTrading = true;
    }
    
    public Trader(int patienceLevel, int food, int water, int gold, int change){
        this.patienceLevel = patienceLevel;
        this.food = food;
        this.water = water;
        this.gold = gold;
        this.change = change;
        isTrading = true;
    }

    public Offer evaluateTrade(Offer offer){
        int weight=0;
        boolean successful=false;
        Offer trade= null;
        if(offer.getOfferFood() >=0 && offer.getOfferWater() >=0 && offer.getOfferGold() >=0 
        && offer.getWantFood()  >=0 && offer.getWantWater()  >=0 && offer.getWantGold()  >=0 ){

            weight+= offer.getOfferFood()-offer.getWantFood();
            weight+= offer.getOfferWater()-offer.getWantWater();
            weight+= offer.getOfferGold()-offer.getWantGold();
            
            successful = roll(weight, "default");

            if(successful){
                trade=new Offer();
                trade.setOfferFood(offer.getWantFood()-offer.getOfferFood());
                trade.setOfferWater(offer.getWantWater()-offer.getOfferWater());
                trade.setOfferGold(offer.getWantGold()-offer.getOfferGold());
            } else {
                trade = new Offer();
                while(weight<=3){
                    trade.setOfferFood(offer.getWantFood()-offer.getOfferFood()-change);
                    trade.setOfferWater(offer.getWantWater()-offer.getOfferWater()-change);
                    trade.setOfferGold(offer.getWantGold()-offer.getOfferGold()-change);
                    weight+=3*change;
                }
            }
            
        } else {
            patienceLevel-=2;
        }        

        if(patienceLevel<=0 ){
            quitNegotiation();
        }
        return trade;
    }

    private boolean roll( int weight, String type){
        int roll = random.nextInt(100);
        boolean successful = false;
        int firstLower = -1;
        int firstUpper = 2;
        int secondLower = 2;
        int secondUpper = 4;
        int ThirdUpper = 4;
        int firstRoll= 40;
        int secondRoll= 10;
        int patienceMinus = 1;

        switch(type){
            case "Cheap": 
                firstLower = 0;
                firstUpper = 3;
                secondLower = 3;
                secondUpper = 5;
                ThirdUpper = 5;
                firstRoll= 60;
                secondRoll= 40;
                break;
            case "Expensive": 
                firstLower = 1;
                firstUpper = 4;
                secondLower = 4;
                secondUpper = 6;
                ThirdUpper = 6;
                break;
            case "Impatient": 
                patienceMinus=2;
                break;
            default: break;
        }  

        if(firstLower <= weight && weight < firstUpper){
            if(roll>=firstRoll){
                successful=true;
            } else {
                patienceLevel-=patienceMinus;
            }
        } else if(secondLower <= weight && weight < secondUpper){
            if(roll>=secondRoll){
                successful=true;
            }
        } else if(weight >= ThirdUpper){
            successful=true;
        } else {
            patienceLevel-=patienceMinus;
        }
        return successful;
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
}
