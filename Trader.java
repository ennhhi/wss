import java.util.Random;

public abstract class Trader {
    //PatienceLevel dictates how much will the trader counteroffer
    private int patienceLevel;
    private int food;
    private int water;
    private int gold;
    private boolean isTrading;
    //Type is the type of trader for any subclasses
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
/**
 * EvaluateTrade is being passed an offer which contains what the player wants from the trader and 
 * what the player is willing to give the trader in return. Then the passed offer is evaluated and determined if
 * the trader will accept or will create a counter offer
 * @param offer This is the offer that the player is initially making to the trader
 * @return The returned offer is the offer that the trader is willing to give to the player and what is asked for in return
 */ 
    public Offer evaluateTrade(Offer offer){
        //Check validity of trade
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

        //If the trader accepts the trade then create offer to return
        if(successful){
            System.out.println("Trade accepted by trader.");
            trade = new Offer(offer.getWantFood(), offer.getWantWater(), 0, offer.getOfferFood(), offer.getOfferWater(), offer.getOfferGold());

        // else if trader is still trading, create a counteroffer
        } else if(patienceLevel > 0){
            System.out.println("Trade rejected; generating counteroffer.");
            trade = generateCounterOffer(offer, netGain);
            
            if (trade != null) {
                System.out.printf("Counteroffer - Player gets Food: %d, Water: %d, Pays Gold: %d%n",
                    trade.getWantFood(), trade.getWantWater(), trade.getOfferGold());
                System.out.println("Remaining patience level: " + patienceLevel);
            }
        // else if the trader is not trading anymore, return null
        } else {
            System.out.println("Trader has no patience left and quits negotiation.");
            quitNegotiation();
            trade = null;
        }

        return trade;
    }

    /**
     * Calculate the total resources gained from an offer
     * @param offer Offer to be analyzed
     * @return int that calculates offered resources - taken resources 
     */
    private int calculateNetGain(Offer offer){
        int weight = (offer.getOfferFood() - offer.getWantFood()) +
                     (offer.getOfferWater() - offer.getWantWater()) +
                     (offer.getOfferGold() - offer.getWantGold());
        return weight;
    }

    /**
     * Roll is a method to randomly determine if a trader is willing to accept a trade or not
     * There are many types of traders, each type of trader has a different set of values to compare to
     * There are different sets of ranges the parameter is compared to and a different chance of success
     * @param weight The net gain from inside evaluateTrade
     * @return Return whether or not the trader decided to accept the trade or not
     */
    private boolean roll(int weight){
        int roll = random.nextInt(101);
        int patienceMinus = 1;
        int firstLower, firstUpper, secondLower, secondUpper, thirdUpper, firstRoll, secondRoll;

        //Depending on the type of trader, change the ranges and success chances
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
        //if the net gain is between the firstLower and firstUpper then roll a random int and compare to firstRoll
        boolean successful = false;
        if(firstLower <= weight && weight < firstUpper){
            successful = roll >= firstRoll;
        //else if the net gain is between the secondLower and secondUpper then roll a random int and compare to secondRoll
        } else if(secondLower <= weight && weight < secondUpper){
            successful = roll >= secondRoll;
        } else if(weight >= thirdUpper){
            successful = true;
        }

        //If not successful, lower the patience to reduce the amount of times the trade is willing to trade
        if(!successful){
            patienceLevel -= patienceMinus;
        }

        //Return if the trade was a success or not
        return successful;
    }
    /**
     * Create a counteroffer when the initial trading offer is rejected. 
     * The counteroffer reduces how much is being given to the player
     * This can happen unless the trader runs out of patience, which it then quits trading
     * Counter offers typically come from expensive traders due to their higher patience levels and price
     * @param offer The Offer that the new counteroffer is based on
     * @param netGain 
     * @return Return the counteroffer that is created
     */
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

    /**
     * Stop the trader from any further trading
     */
    public void quitNegotiation(){
        isTrading=false;
    }

    /**
     * Get the amount of patience that the trader has remaining
     * @return Amount of patience 
     */
    public int getPatienceLevel() {
        return patienceLevel;
    }

    /**
     * Return the amount of food that the player currently has
     * @return Amount of food
     */
    public int getFood() {
        return food;
    }

    /**
     * Set the current amount of food that the player has
     * @param food Amount to be set to
     */
    public void setFood(int food) {
        this.food = food;
    }

    /**
     * Return how much water the player currently has
     * @return Amount of water
     */
    public int getWater() {
        return water;
    }

    /**
     * Set the current amount of water for the player
     * @param water Amount of water to be set
     */
    public void setWater(int water) {
        this.water = water;
    }

    /**
     * Return how much gold the player currently has
     * @return Amount of gold
     */
    public int getGold() {
        return gold;
    }

    /**
     * Set the current gold amount of the player
     * @param gold Amount to be set to
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Return if the trader is trading or not
     * @return True or false
     */
    public boolean isTrading() {
        return isTrading;
    }
}
