import java.util.*;

public class Rambo {
 
    public int x = 200;
    
    public int y = 350;
    
    public List<Shoot> theShoot = new ArrayList<Shoot>();

    public List<Bomb> bombs = new ArrayList<Bomb>();

    public class Bomb {
        
        public int x;
        
        public int y;
        
        public int ttl = 35;
        
        public Bomb(int x, int y) {
            
            this.x = x;
            
            this.y = y;
        }

        public void moveRight() {
            x+=10;
            if(x < 0)
                x+=10;
            if(x > 1200)
                x-=10;
        }
    }

    public class Shoot {
        
        public int x;
        
        public int y;
        
        public Shoot(int x, int y) {
            
            this.x = x;
            
            this.y = y;
        }

        public void moveRight() {
            x+=30;
            if(x < 0)
                x+=10;
            if(x > 1200)
                x-=10;
        }
    }
    
    public void throwBomb() {
        
        if(bombs == null) {
            bombs = new ArrayList<Bomb>();
        }
        bombs.add(new Bomb(x, y));
    }

    public void shoot() {
        
        if(theShoot == null) {
            theShoot = new ArrayList<Shoot>();
        }
        theShoot.add(new Shoot(x, y));
        theShoot.add(new Shoot(x, y+16));
        theShoot.add(new Shoot(x, y+32));
    }
    
    public void moveTop() {
        y-=30;
        if(y < 0)
            y+=30;
        if(y > 700)
            y-=30;
    }

    public void moveBottom() {
        y+=30;
        if(y < 0)
            y+=30;
        if(y > 620)
            y-=30;
    }

    public void moveLeft() {
        x-=30;
        if(x < 0)
            x+=30;
        if(x > 1200)
            x-=30;
    }

    public void moveRight() {
        x+=30;
        if(x < 0)
            x+=30;
        if(x > 1200)
            x-=30;
    }
}