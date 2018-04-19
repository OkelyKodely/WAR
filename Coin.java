public class Coin {

    public int ttl = 350;
    
    public int x = 0;
    
    public int y = 0;
    
    public int width = 60;
    
    public int height = 60;
    
    public Coin(int x, int y) {
        
        this.x = x;
        
        this.y = y;
    }

    public boolean didYouEatMe(int x, int y) {
        if(this.x >= x - 0 && this.y >= y - 0 && this.x <= x + 40 && this.y <= y + 40) {
            return true;
        }
        return false;
    }
}