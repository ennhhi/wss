import java.util.Random;

public abstract class Trader {
    private int patienceLevel;
    private int food;
    private int water;
    private int gold;
    private boolean isTrading;
    private String type;
    private final Random random = new Random();

    public Trader(){
        this.patienceLevel = 0;
        this.food = 0;
        this.water = 0;
        this.gold = 0;
        isTrading = true;
        type="";
    }

    public Trader(int patienceLevel, int food, int water, int gold, String type){
        this.patienceLevel = patienceLevel;
        this.food = food;
        this.water = water;
        this.gold = gold;
        isTrading = true;
        this.type = type;
    }

    public Offer evaluateTrade(Offer offer){
        if(offer == null || !isTrading){
            System.out.println("Trade evaluation aborted: Invalid offer or trader stopped trading.");
            return null;
        }

        System.out.println("Evaluating trade...");
        System.out.printf("Player Offers - Food: %d, Water: %d, Gold: %d%n", 
            offer.getOfferFood(), offer.getOfferWater(), offer.getOfferGold());
        System.out.printf("Player Wants - Food: %d, Water: %d, Gold: %d%n",
            offer.getWantFood(), offer.getWantWater(), offer.getWantGold());

        int netGain = calculateNetGain(offer);
        boolean successful = roll(netGain);
        Offer trade;

        if(successful && netGain >= 0){
            System.out.println("Trade accepted by trader.");
            trade = new Offer(offer.getWantFood(), offer.getWantWater(), 0, offer.getOfferFood(), offer.getOfferWater(), offer.getOfferGold());

        } else if(patienceLevel > 0){
            System.out.println("Trade rejected; generating counteroffer.");
            trade = generateCounterOffer(offer, netGain);
            System.out.printf("Counteroffer - Player gets Food: %d, Water: %d, Pays Gold: %d%n",
                trade.getWantFood(), trade.getWantWater(), trade.getOfferGold());
            System.out.println("Remaining patience level: " + patienceLevel);
        } else {
            System.out.println("Trader has no patience left and quits negotiation.");
            quitNegotiation();
            trade = null;
        }

        return trade;
    }

    private int calculateNetGain(Offer offer){
        int weight = (offer.getOfferFood() - offer.getWantFood()) +
                     (offer.getOfferWater() - offer.getWantWater()) +
                     (offer.getOfferGold() - offer.getWantGold());
        return weight;
    }

    private boolean roll(int weight){
        int roll = random.nextInt(101);
        int patienceMinus = 1;
        int firstLower, firstUpper, secondLower, secondUpper, thirdUpper, firstRoll, secondRoll;

        switch(type){
            case "Cheap":
                firstLower = -1; firstUpper = 2;
                secondLower = 2; secondUpper = 4;
                thirdUpper = 4;
                firstRoll = 40; secondRoll = 10;
                patienceMinus = 3;
                break;
            case "Expensive":
                firstLower = 1; firstUpper = 4;
                secondLower = 4; secondUpper = 6;
                thirdUpper = 6;
                firstRoll = 60; secondRoll = 40;
                patienceMinus = 2;
                break;
            default:
                firstLower = 0; firstUpper = 3;
                secondLower = 3; secondUpper = 5;
                thirdUpper = 5;
                firstRoll = 50; secondRoll = 30;
                break;
        }

        boolean successful = false;
        if(firstLower <= weight && weight < firstUpper){
            successful = roll >= firstRoll;
        } else if(secondLower <= weight && weight < secondUpper){
            successful = roll >= secondRoll;
        } else if(weight >= thirdUpper){
            successful = true;
        }

        if(!successful){
            patienceLevel -= patienceMinus;
        }

        return successful;
    }

    private Offer generateCounterOffer(Offer offer, int netGain){
        int adjustment = type.equals("Expensive") ? 2 : 1;
    
        Offer counter = new Offer();
    
        int requestedFood = Math.max(offer.getWantFood() - adjustment, 0);
        int requestedWater = Math.max(offer.getWantWater() - adjustment, 0);
        
        // Only charge gold if at least one resource is offered
        int requiredGold = (requestedFood + requestedWater) > 0 ? requestedFood + requestedWater + adjustment : 0;
    
        // Avoid scenario where player pays gold for nothing
        if(requiredGold == 0){
            System.out.println("Trader cannot make a valid counteroffer without resources. Negotiation stops.");
            quitNegotiation();
            return null;
        }
    
        counter.setWantFood(requestedFood);
        counter.setWantWater(requestedWater);
        counter.setOfferGold(Math.min(requiredGold, gold));
    
        patienceLevel--;
        System.out.println("Patience decreased to: " + patienceLevel);
    
        if(patienceLevel <= 0){
            System.out.println("Patience exhausted; trader quits negotiation.");
            quitNegotiation();
        }
    
        return counter;
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
