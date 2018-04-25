import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemySoldier {

    private Random r = new Random();
    
    public int x = 0;
    
    public int y = 0;
    
    public int dest1x = 0, dest1y = 0, dest2x = 0, dest2y = 0,
            dest3x = 0, dest3y = 0, dest4x = 0, dest4y = 0, dest5x = 0, dest5y = 0,
            dest6x = 0, dest6y = 0, dest7x = 0, dest7y = 0;
    
    public int dest = 0;
    
    public int width = 38;
    
    public int height = 38;
    
    public String PLACE = "normal";
    
    public War war = null;
    
    public List<Shoot> shoots = new ArrayList<Shoot>();
    
    public Shoot shoot = null;
    
    public boolean firing = false;

    public void shoot(Rambo rambo) {
        if(rambo.y >= this.y - 10 && rambo.y <= this.y + 10) {
            int w = r.nextInt(3);
            if(w == 1) {
                shoot = new Shoot(this.x, this.y);
                war.shoots.add(shoot);
            }
        }
    }
    
    public class Shoot {
        
        public int x;
        
        public int y;
        
        public Shoot(int x, int y) {
            
            this.x = x;
            
            this.y = y;
        }

        public void moveLeft() {
            x-=7;
        }
    }
    
    public EnemySoldier(War war) {
        
        this.war = war;
        
        x = r.nextInt(600) + 400;
        
        y = r.nextInt(600) + 50;
        
        dest1x = x - 30;
        dest1y = y - 30;
        
        dest2x = x - 45;
        dest2y = y - 30;
        
        dest3x = x - 60;
        dest3y = y;
        
        dest4x = x - 45;
        dest4y = y + 30;
        
        dest5x = x - 30;
        dest5y = y + 30;
        
        dest6x = x;
        dest6y = y;
        
        dest7x = x - 5;
        dest7y = y;
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
        if(y > 620)
            y-=10;
    }

    public void moveLeft() {
        x-=10;
        if(x > 1200)
            x-=10;
        if(x < 0) {
            ++war.deadCount;
            if((double)war.deadCount/(double)10>0.8) {
                war.randomizeMiniNewEnemySoldiers();
                war.deadCount = 0;
            }
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
        /*
        if(dest == 0) {
            x = dest1x;
            y = dest1y;
            dest = 1;
        }
        else if(dest == 1) {
            x = dest2x;
            y = dest2y;
            dest = 2;
        }
        else if(dest == 2) {
            x = dest3x;
            y = dest3y;
            dest = 3;
        }
        else if(dest == 3) {
            x = dest4x;
            y = dest4y;
            dest = 4;
        }
        else if(dest == 4) {
            x = dest5x;
            y = dest5y;
            dest = 5;
        }
        else if(dest == 5) {
            x = dest6x;
            y = dest6y;
            dest = 6;
        }
        else if(dest == 6) {
            x = dest7x;
            y = dest7y;
            dest = 7;
        }
        else if(dest == 7) {
            dest1x = x - 30;
            dest1y = y - 30;

            dest2x = x - 45;
            dest2y = y - 30;

            dest3x = x - 60;
            dest3y = y;

            dest4x = x - 45;
            dest4y = y + 30;

            dest5x = x - 30;
            dest5y = y + 30;

            dest6x = x;
            dest6y = y;

            dest7x = x - 5;
            dest7y = y;

            dest = 0;
        }
        */
        
        if(steps == 5)
            steps = 0;
        if(steps == 0)
            v = r.nextInt(4);
        if(v == 0) {
            moveLeft();
        } else if(v == 1) {
            moveTop();
        } else if(v == 2) {
            moveRight();
        } else if(v == 3) {
            moveBottom();
        }
        miniMoveLeft();
        ++steps;
    }
    int v;
    int steps;
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