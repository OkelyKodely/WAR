import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemySoldier {

    private Random r = new Random();
    
    public int x = 0;
    
    public int y = 0;
    
    public int width = 38;
    
    public int height = 38;
    
    public String PLACE = "normal";
    
    public War war = null;
    
    public EnemySoldier(War war) {
        
        this.war = war;
        
        x = r.nextInt(600) + 400;
        
        y = r.nextInt(600) + 50;
    }
    
    public void moveTop() {
        y-=10;
        if(y < 0)
            y+=10;
        if(y > 700)
            y-=10;
    }

    public void moveBottom() {
        y+=10;
        if(y < 0)
            y+=10;
        if(y > 700)
            y-=10;
    }

    public void moveLeft() {
        x-=10;
        if(x > 1200)
            x-=10;
        if(x < 0) {
            ++war.deadCount;
        }
        if((double)war.deadCount/(double)10>0.5) {
            war.randomizeMiniNewEnemySoldiers();
        }
    }

    public void moveRight() {
        x+=10;
        if(x > 1200)
            x-=10;
    }
    
    private void miniMoveLeft() {
        x-=1;
        if(x > 1200)
            x-=10;
    }
    
    public void nextMove() {
        int v = r.nextInt(4);
        if(v == 0) {
            moveTop();
        }
        if(v == 1) {
            moveLeft();
        }
        if(v == 2) {
            moveRight();
        }
        if(v == 3) {
            moveBottom();
        }
        miniMoveLeft();
    }
    
    public boolean didYouDie(int x, int y) {
        if(this.x >= x - 14 && this.y >= y - 14 && this.x <= x + 14 && this.y <= y + 14) {
            return true;
        }
        return false;
    }

    public boolean didYouExplode(int x, int y) {
        if(this.x >= x - 44 && this.y >= y - 44 && this.x <= x + 44 && this.y <= y + 44) {
            return true;
        }
        return false;
    }
}