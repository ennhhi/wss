public class Offer {
    private int offerFood;
    private int offerWater;
    private int offerGold;
    private int wantFood;
    private int wantWater;
    private int wantGold;

    public Offer(){
        offerFood = 0;
        offerWater = 0;
        offerGold = 0;
        wantFood = 0;
        wantWater = 0;
        wantGold = 0;
    }
    
    public Offer(int offerFood, int offerWater, int offerGold, int wantFood, int wantWater, int wantGold ){
        this.offerFood = offerFood;
        this.offerWater = offerWater;
        this.offerGold = offerGold;
        this.wantFood = wantFood;
        this.wantWater = wantWater;
        this.wantGold = wantGold;
    }

    public int getOfferFood() {
        return offerFood;
    }

    public void setOfferFood(int offerFood) {
        this.offerFood = offerFood;
    }

    public int getOfferWater() {
        return offerWater;
    }

    public void setOfferWater(int offerWater) {
        this.offerWater = offerWater;
    }

    public int getOfferGold() {
        return offerGold;
    }

    public void setOfferGold(int offerGold) {
        this.offerGold = offerGold;
    }

    public int getWantFood() {
        return wantFood;
    }

    public void setWantFood(int wantFood) {
        this.wantFood = wantFood;
    }

    public int getWantWater() {
        return wantWater;
    }

    public void setWantWater(int wantWater) {
        this.wantWater = wantWater;
    }

    public int getWantGold() {
        return wantGold;
    }

    public void setWantGold(int wantGold) {
        this.wantGold = wantGold;
    }

    
    
}
