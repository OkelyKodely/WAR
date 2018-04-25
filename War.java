import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.AudioFormat;
import javax.swing.*;

public class War implements KeyListener {

    public void randomizeNewEnemySoldiers() {
        for(int i = 0; i < 8; i++) {
            es.add(new EnemySoldier(this));
        }
    }

    public void randomizeMiniNewEnemySoldiers() {
        for(int i = 0; i < 6; i++) {
            es.add(new EnemySoldier(this));
        }
    }

    public void randomizeNewPrisoners() {
        for(int i = 0; i < 5; i++) {
            p.add(new Prisoner());
        }
    }

    private Graphics g = null;
    
    private Rambo rambo = new Rambo();
            
    private JFrame frame = new JFrame();
    
    private JPanel panel = new JPanel();
    
    public int deadCount = 0;
    
    public int bomb = 0;
    
    public int life = 100;
    
    public int bullet = 100;

    public int bx = 0;

    public int bx2 = 1200;

    public int level = 1;
    
    public boolean levelBossStart = false;
    
    public int step = 0;
    
    public List bs1 = new ArrayList();

    public List<EnemySoldier.Shoot> shoots = new ArrayList<EnemySoldier.Shoot>();
    
    private List<EnemySoldier> es = new ArrayList<EnemySoldier>();
    
    private List<Prisoner> p = new ArrayList<Prisoner>();

    private List<Coin> coins = new ArrayList<Coin>();
    
    private JLabel asdfsdf = new JLabel("How to play?");
    
    private JLabel txt = new JLabel("To shoot space; To bomb enter~~~~~~~~~");

    private boolean firstTime = true;
    
    public War() {
        run();
    }
    
    private void run() {
        life = 100;
        bullet = 100;
        bomb = 0;
        deadCount = 0;
        p.clear();
        es.clear();
        coins.clear();
        shoots.clear();
        
        if(firstTime) {
            firstTime = false;
            try {
                File audioFile = new File("a.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                AudioFormat format = audioStream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                Clip audioClip = (Clip) AudioSystem.getLine(info);
                audioClip.open(audioStream);
                audioClip.loop(Clip.LOOP_CONTINUOUSLY);
                audioStream.close();
            } catch (Exception ex) {
                try {
                    File audioFile = new File("src/a.wav");
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                    AudioFormat format = audioStream.getFormat();
                    DataLine.Info info = new DataLine.Info(Clip.class, format);
                    Clip audioClip = (Clip) AudioSystem.getLine(info);
                    audioClip.open(audioStream);
                    audioClip.loop(Clip.LOOP_CONTINUOUSLY);
                    audioStream.close();
                } catch(Exception e) {System.out.println(e.getMessage());}
            }
        
            String title = "WAR";
            frame.setTitle(title);
            frame.setLayout(null);
            frame.getContentPane().setBackground(Color.black);
            frame.setSize(new Dimension(1200, 900));
            frame.setLocation(0, 0);
            panel.setSize(new Dimension(1200, 700));
            panel.setLocation(0, 0);

            frame.add(panel);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            frame.addKeyListener(this);

            asdfsdf.setFont(new java.awt.Font("tahoma", java.awt.Font.PLAIN, 44));
            asdfsdf.setForeground(java.awt.Color.yellow);
            txt.setFont(new java.awt.Font("tahoma", java.awt.Font.PLAIN, 22));
            txt.setForeground(java.awt.Color.black);
            asdfsdf.setLocation(0, 0);
            txt.setLocation(0, 40);
            JPanel panel2 = new JPanel();
            panel2.setBackground(java.awt.Color.BLUE);
            panel2.setSize(new Dimension(600, 660));
            panel2.setLocation(10, 720);
            panel2.add(asdfsdf);
            panel2.add(txt);
            frame.add(panel2);
            
            frame.setVisible(true);
            
            frame.setTitle("WAR " + "life: " + life + " bullet: " + this.bullet + " bombs: " + bomb);
        }
        
        try {
            
            boolean advancedLvl = false;
            
            Boss1 bs_1 = new Boss1(this);

            Boss2 bs_2 = new Boss2(this);

            bs_1.pb.setSize(new Dimension(400, 20));
            bs_1.pb.setLocation(700, 720);
            
            JLabel bossStr1 = new JLabel("Level 1/2: ");
            bossStr1.setSize(new Dimension(100, 20));
            bossStr1.setLocation(620, 720);
            bossStr1.setForeground(Color.green);
            
            frame.add(bs_1.pb);
            frame.add(bossStr1);
            
            bs_2.pb.setSize(new Dimension(400, 20));
            bs_2.pb.setLocation(700, 750);
            
            JLabel bossStr2 = new JLabel("Level 2/2: ");
            bossStr2.setSize(new Dimension(100, 20));
            bossStr2.setLocation(620, 750);
            bossStr2.setForeground(Color.green);
            
            frame.add(bs_2.pb);
            frame.add(bossStr2);

            randomizeNewEnemySoldiers();
            
            randomizeNewPrisoners();
            
            g = panel.getGraphics();

            String level1Str = "Level 1";
            int level1DoneLoading = 0;
            String level2Str = "Level 2";
            int level2DoneLoading = 0;
            String level3Str = "Mt. Zion";
            int level3DoneLoading = 0;
            
            g.setColor(Color.green);
            g.setFont(new Font("tahoma", Font.BOLD, 135));
                                
            while(true) {

                try {
                   
                    if(level != 3)
                        frame.setTitle("WAR " + "life: " + life + " bullet: " + bullet + " bombs: " + bomb);

                    Thread.sleep(20);

                    if(level != 3) {
                        drawBattleField(g);
                        drawSoldier(g);
                        drawEnemies(g);
                        drawRamboAssets();
                        drawCoins();
                        drawPrisoners();
                    }

                    for(int i=0; i<coins.size(); i++) {
                        Coin coin = coins.get(i);
                        if(coin.didYouEatMe(rambo.x, rambo.y)) {
                            coins.remove(coin);
                            bullet+=10;
                            bomb+=5;
                        }
                        --coin.ttl;
                        if(coin.ttl <= 0) {
                            coins.remove(coin);
                        }
                    }

                    for(int i=0; i<p.size(); i++) {
                        Prisoner prisoner = p.get(i);
                        if(prisoner.didYouEatMe(rambo.x, rambo.y)) {
                            makeDohSound();
                            p.get(i).image = "prisoner_running.gif";
                        }
                    }

                   if(es.size() > 0)
                    for(int i=0; i<rambo.theShoot.size(); i++) {
                        Rambo.Shoot shoot = rambo.theShoot.get(i);
                        shoot.moveRight();
                        drawShoots(shoot);
                        for(int j=0; j<es.size(); j++) {
                            EnemySoldier enemySoldier = es.get(j);
                            if(enemySoldier.didYouDie(shoot.x, shoot.y)) {
                                makeDyingSound();
                                es.remove(enemySoldier);
                                Coin coin = new Coin(shoot.x, shoot.y);
                                coins.add(coin);
                                life+=3;
                            }
                        }
                        if(shoot.x > 1200)
                            rambo.theShoot.remove(shoot);
                    }

                    for(int i=0; i<rambo.bombs.size(); i++) {
                        Rambo.Bomb bomb = rambo.bombs.get(i);
                        --bomb.ttl;
                        bomb.moveRight();
                        drawBombs(bomb);
                        for(int j=0; j<es.size(); j++) {
                            EnemySoldier enemySoldier = es.get(j);
                            if(enemySoldier.didYouExplode(bomb.x, bomb.y)) {
                                makeDyingSound();
                                drawExplosion(bomb.x, bomb.y);
                                es.remove(enemySoldier);
                                Coin coin = new Coin(bomb.x, bomb.y);
                                coins.add(coin);
                                life+=3;
                            }
                        }
                        if(bomb != null)
                        if(bomb.ttl <= 0) {
                            drawExplosion(bomb.x, bomb.y);
                            rambo.bombs.remove(bomb);
                        }
                    }

                    for(int j=0; j<es.size(); j++) {
                        EnemySoldier enemySoldier = es.get(j);
                        enemySoldier.shoot(rambo);
                    }

                    for(int i=0; i<shoots.size(); i++) {
                        EnemySoldier.Shoot shoot = shoots.get(i);
                        shoot.moveLeft();
                        drawEnemyShoots(shoot);
                        if(shoot.x >= rambo.x && shoot.y >= rambo.y &&
                                shoot.x <= rambo.x + 40 && shoot.y <= rambo.y + 40) {
                            life-=2;
                            shoots.remove(shoot);
                        }
                        if(shoot.x < 0) {
                            shoots.remove(shoot);
                        }
                    }

                    if(life <= 0) {
                        run();
                    }
                    if(p.size() == 0 && level != 3) {
                        randomizeNewPrisoners();
                    }
                    if(step != 3 && es.size() == 0) {
                        ++step;
                        randomizeNewEnemySoldiers();
                        if(level == 3) {
                            step = 3;
                            es.clear();
                        }
                    }
                    if(step == 3) {
                        if(level == 1) {
                            if(!levelBossStart) {
                                bs_1.x = 800;
                                bs_1.y = 230;
                                levelBossStart = true;
                            }
                            bs_1.shoot(rambo);
                            for(int i=0; i<bs1.size(); i++) {
                                ((Boss1.Shoot)bs1.get(i)).x += ((Boss1.Shoot)bs1.get(i)).movx;
                                ((Boss1.Shoot)bs1.get(i)).y += ((Boss1.Shoot)bs1.get(i)).movy;
                                if(((Boss1.Shoot)bs1.get(i)).x >= rambo.x && ((Boss1.Shoot)bs1.get(i)).y >= rambo.y &&
                                        ((Boss1.Shoot)bs1.get(i)).x <= rambo.x + 40 &&
                                        ((Boss1.Shoot)bs1.get(i)).y <= rambo.y + 40) {
                                    life = life - 2;
                                    bs1.remove(bs1.get(i));
                                }
                            }
                            bs_1.nextMove();
                            drawBossLvlShoots();
                            drawBossLvl(bs_1.x, bs_1.y);
                            for(int i=0; i<rambo.theShoot.size(); i++) {
                                Rambo.Shoot shoot = rambo.theShoot.get(i);
                                shoot.moveRight();
                                drawShoots(shoot);
                                if(shoot.x >= bs_1.x && shoot.y >= bs_1.y &&
                                        shoot.x <= bs_1.x + 300 &&
                                        shoot.y <= bs_1.y + 300) {
                                    bs_1.life = bs_1.life - 1;
                                    bs_1.pb.setValue(bs_1.life);
                                    if(bs_1.life <= 0) {
                                        advancedLvl = true;
                                    }
                                }
                            }
                        }
                        else if(level == 2) {
                            if(!levelBossStart) {
                                bs_2.x = 800;
                                bs_2.y = 230;
                                levelBossStart = true;
                            }
                            bs_2.shoot(rambo);
                            for(int i=0; i<bs1.size(); i++) {
                                ((Boss2.Shoot)bs1.get(i)).x += ((Boss2.Shoot)bs1.get(i)).movx;
                                ((Boss2.Shoot)bs1.get(i)).y += ((Boss2.Shoot)bs1.get(i)).movy;
                                if(((Boss2.Shoot)bs1.get(i)).x >= rambo.x && ((Boss2.Shoot)bs1.get(i)).y >= rambo.y &&
                                        ((Boss2.Shoot)bs1.get(i)).x <= rambo.x + 40 &&
                                        ((Boss2.Shoot)bs1.get(i)).y <= rambo.y + 40) {
                                    life = life - 2;
                                    bs_2.pb.setValue(bs_2.life);
                                    bs1.remove(bs1.get(i));
                                }
                            }
                            bs_2.nextMove();
                            drawBossLvlShoots();
                            drawBossLvl(bs_2.x, bs_2.y);
                            for(int i=0; i<rambo.theShoot.size(); i++) {
                                Rambo.Shoot shoot = rambo.theShoot.get(i);
                                shoot.moveRight();
                                drawShoots(shoot);
                                if(shoot.x >= bs_2.x && shoot.y >= bs_2.y &&
                                        shoot.x <= bs_2.x + 300 &&
                                        shoot.y <= bs_2.y + 300) {
                                    bs_2.life = bs_2.life - 1;
                                    if(bs_2.life <= 0) {
                                        advancedLvl = true;
                                    }
                                }
                            }
                        }
                        else if(level == 3) {
                            drawHeaven(panel.getGraphics());
                            frame.setTitle("You have come to Mt. Zion!");
                        }
                    }
                    if(advancedLvl) {
                        if(level == 1) {
                            bs_1.x = -1000;
                            bs_1.y = -1000;
                            bs_1.life = 1000;
                        }
                        else if(level == 2) {
                            bs_2.x = -1000;
                            bs_2.y = -1000;
                            bs_2.life = 2000;
                        }
                        step = 0;
                        levelBossStart = false;
                        advancedLvl = false;
                        bs1.clear();
                        ++level;
                    }

                    if(level1DoneLoading!=100 && level == 1) {
                        g.drawString(level1Str, 100, 100);
                        level1DoneLoading++;
                    }

                    if(level2DoneLoading!=100 && level == 2) {
                        g.drawString(level2Str, 100, 100);
                        level2DoneLoading++;
                    }

                    if(level3DoneLoading!=100 && level == 3) {
                        g.drawString(level3Str, 100, 100);
                        level3DoneLoading++;
                    }

                } catch(Exception e) {
                    List<Rambo.Shoot> l = new ArrayList<Rambo.Shoot>();
                    for(int i=0; i<rambo.theShoot.size(); i++) {
                        l.add(rambo.theShoot.get(i));
                    }
                    List<Rambo.Bomb> b = new ArrayList<Rambo.Bomb>();
                    for(int i=0; i<rambo.bombs.size(); i++) {
                        b.add(rambo.bombs.get(i));
                    }
                    rambo.theShoot.clear();
                    rambo.bombs.clear();
                    for(int i=0; i<l.size(); i++) {
                        rambo.theShoot.add(l.get(i));
                    }
                    for(int i=0; i<b.size(); i++) {
                        rambo.bombs.add(b.get(i));
                    }
                }
            }
        } catch(Exception e) {}
    }

    private void drawBossLvlShoots() {
        java.awt.Image img = null;
        String image = "bomb.gif";
        javax.swing.ImageIcon j = new javax.swing.ImageIcon(this.getClass().getResource(image));
        img = j.getImage();
        for(int i=0; i<bs1.size(); i++) {
            try {
                g.drawImage(img, ((Boss1.Shoot)bs1.get(i)).x, ((Boss1.Shoot)bs1.get(i)).y, 22, 22, null);
            } catch(Exception e) {
                g.drawImage(img, ((Boss2.Shoot)bs1.get(i)).x, ((Boss2.Shoot)bs1.get(i)).y, 22, 22, null);
            }
        }
    }
    
    private void drawBossLvl(int x, int y) {
        java.awt.Image img = null;
        String image = null;
        if(level == 1)
            image = "bosslvl1.gif";
        else if(level == 2)
            image = "bosslvl2.gif";
        javax.swing.ImageIcon i = new javax.swing.ImageIcon(this.getClass().getResource(image));
        img = i.getImage();
        g.drawImage(img, x, y, 300, 300, null);
    }
    
    private void makeDohSound() {
        try {
            makeSound("woohoo.wav");
        } catch(Exception e1) {
            try {
                makeSound("src/woohoo.wav");
            } catch(Exception e2) {}
        }
    }
        
    private void makeDyingSound() {
        try {
            makeSound("dying.wav");
        } catch(Exception e1) {
            try {
                makeSound("src/dying.wav");
            } catch(Exception e2) {}
        }
    }

    private void makeSound(String file) throws Exception {
        File audioFile = new File(file);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip audioClip = (Clip) AudioSystem.getLine(info);
        audioClip.open(audioStream);
        audioClip.start();
        audioStream.close();
    }

    public void drawCoins() {
        for(int i=0; i<coins.size(); i++) {
            Coin coin = coins.get(i);
            drawCoins(coin);
        }
    }
    
    public void drawCoins(Coin coin) {
        java.awt.Image img = null;
        String image = "coin.gif";
        javax.swing.ImageIcon i = new javax.swing.ImageIcon(this.getClass().getResource(image));
        img = i.getImage();
        g.drawImage(img, coin.x, coin.y, 80, 80, null);
    }

    private void drawRamboAssets() {
        for(int i=0; i<rambo.theShoot.size(); i++) {
            Rambo.Shoot shoot = rambo.theShoot.get(i);
            shoot.moveRight();
            drawShoots(shoot);
        }
        for(int i=0; i<rambo.bombs.size(); i++) {
            Rambo.Bomb bomb = rambo.bombs.get(i);
            bomb.moveRight();
            drawBombs(bomb);
        }
    }
    
    private void drawHeaven(Graphics g) {
        if(level != 3)
            return;
        
        java.awt.Image img = null;
        String image = "heaven.gif";
        
        javax.swing.ImageIcon i = new javax.swing.ImageIcon(this.getClass().getResource(image));
        img = i.getImage();
        
        g.drawImage(img, 0, 0, panel.getWidth(), panel.getHeight(), null);
    }
    
    private void drawBattleField(Graphics g) {
        java.awt.Image img = null;
        String image = null;
        if(level == 1) {
            image = "b1.png";
        } else if(level == 2) {
            image = "b2.png";
        }
        javax.swing.ImageIcon i = new javax.swing.ImageIcon(this.getClass().getResource(image));
        img = i.getImage();
        
        g.drawImage(img, bx, 0, panel.getWidth(), panel.getHeight(), null);
        g.drawImage(img, bx2, 0, panel.getWidth(), panel.getHeight(), null);
        
        bx-=5;
        bx2-=5;
        
        if(bx == -1200) {
            bx = 1200;
        } else if(bx2 == -1200) {
            bx2 = 1200;
        }
    }

    private void drawExplosion(int x, int y) {
        java.awt.Image imgFb = null;
        String imageFb = "explosion.gif";
        javax.swing.ImageIcon iFb = new javax.swing.ImageIcon(this.getClass().getResource(imageFb));
        imgFb = iFb.getImage();
        g.drawImage(imgFb, x- 75, y-75, 150, 150, null);
    }
    
    private void drawBombs(Rambo.Bomb bomb) {
        if(bomb == null)
            return;
        if(bomb.ttl > 0) {
            java.awt.Image imgFb = null;
            String imageFb = "bomb.gif";
            javax.swing.ImageIcon iFb = new javax.swing.ImageIcon(this.getClass().getResource(imageFb));
            imgFb = iFb.getImage();
            g.drawImage(imgFb, bomb.x, bomb.y, 50, 50, null);
        }
    }

    private void drawShoots(Rambo.Shoot shoot) {
        if(shoot == null)
            return;
        java.awt.Image imgFb = null;
        String imageFb = "shoot.gif";
        javax.swing.ImageIcon iFb = new javax.swing.ImageIcon(this.getClass().getResource(imageFb));
        imgFb = iFb.getImage();
        g.drawImage(imgFb, shoot.x, shoot.y, 50, 50, null);
    }
    
    private void drawEnemyShoots(EnemySoldier.Shoot shoot) {
        if(shoot == null)
            return;
        java.awt.Image imgFb = null;
        String imageFb = "laser.gif";
        javax.swing.ImageIcon iFb = new javax.swing.ImageIcon(this.getClass().getResource(imageFb));
        imgFb = iFb.getImage();
        g.drawImage(imgFb, shoot.x, shoot.y, 50, 50, null);
    }

    private void drawPrisoners() {
        for(int i=0; i<p.size(); i++) {
            java.awt.Image imgFb = null;
            String imageFb = p.get(i).image;
            javax.swing.ImageIcon iFb = new javax.swing.ImageIcon(this.getClass().getResource(imageFb));
            imgFb = iFb.getImage();
            if(p.get(i).image.equals("prisoner_running.gif")) {
                p.get(i).x-=10;
            }
            g.drawImage(imgFb, p.get(i).x, p.get(i).y, 120, 120, null);
            if(p.get(i).x < 0) {
                p.remove(p.get(i));
                bullet+=10;
            }
        }
    }

    private void drawEnemies(Graphics g) {
        for(int i=0; i<es.size(); i++) {
            EnemySoldier sold = es.get(i);

            java.awt.Image imgSold = null;
            String imageSold = "enemysoldier.gif";
            Random r = new Random();
            int v = r.nextInt(2);
            if(v == 0) {
                imageSold = "soldier2.gif";
            }
            javax.swing.ImageIcon iSold = new javax.swing.ImageIcon(this.getClass().getResource(imageSold));
            imgSold = iSold.getImage();

            g.drawImage(imgSold, sold.x, sold.y, sold.width, sold.height, null);

            sold.nextMove();
            
            if(sold.x < 700 && sold.PLACE.equals("normal")) {
                
                sold.PLACE = "700";
                sold.width += 10;
                sold.height += 10;
            }
            if(sold.x < 600 && sold.PLACE.equals("700")) {

                sold.PLACE = "600";
                sold.width += 10;
                sold.height += 10;
            }
            if(sold.x < 500 && sold.PLACE.equals("600")) {

                sold.PLACE = "500";
                sold.width += 10;
                sold.height += 10;
            }
            if(sold.x < 400 && sold.PLACE.equals("500")) {

                sold.PLACE = "400";
                sold.width += 10;
                sold.height += 10;
            }

            if(sold.x < 0) {
                es.remove(sold);
            }
        }
    }
    
    private void drawSoldier(Graphics g) {
        java.awt.Image img = null;
        String image = "soldier.gif";
        javax.swing.ImageIcon i = new javax.swing.ImageIcon(this.getClass().getResource(image));
        img = i.getImage();

        g.drawImage(img, rambo.x, rambo.y, 60, 60, null);
    }

    public void keyPressed(KeyEvent e) {

    }
    
    public void keyTyped(KeyEvent e) {
        
    }
    
    public void keyEntered(KeyEvent e) {
        
    }

    public void keyReleased(KeyEvent e) {
        
        
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(bomb > 0) {
                rambo.throwBomb();
                --bomb;
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            if(bullet > 2) {
                rambo.shoot();
                bullet-=3;
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_UP) {
            rambo.moveTop();
        }

        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            rambo.moveLeft();
        }

        if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            rambo.moveBottom();
        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rambo.moveRight();
        }
        
        drawSoldier(panel.getGraphics());
    }

    public static void main(String[] args) {
        War war = new War();
    }
}