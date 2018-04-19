import java.awt.Graphics;
import java.awt.Dimension;
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
        for(int i = 0; i < 10; i++) {
            es.add(new EnemySoldier(this));
        }
    }

    public void randomizeMiniNewEnemySoldiers() {
        for(int i = 0; i < 11; i++) {
            es.add(new EnemySoldier(this));
        }
    }

    private Graphics g = null;
    
    private Rambo rambo = new Rambo();
            
    private JFrame frame = new JFrame();
    
    private JPanel panel = new JPanel();
    
    public int deadCount = 0;
    
    public int bomb = 0;
    
    public int life = 100;

    public List<EnemySoldier.Shoot> shoots = new ArrayList<EnemySoldier.Shoot>();
    
    private List<EnemySoldier> es = new ArrayList<EnemySoldier>();
    
    private List<Coin> coins = new ArrayList<Coin>();
    
    private JLabel asdfsdf = new JLabel("How to play?");
    
    private JLabel txt = new JLabel("To shoot space; To bomb enter~~~~~~~~~");
    
    public War() {
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
        
        try {
            String title = "WAR";
            frame.setTitle(title);
            frame.setLayout(null);
            frame.setSize(new Dimension(1200, 900));
            frame.setLocation(0, 0);
            panel.setSize(new Dimension(1200, 700));
            panel.setLocation(0, 0);

            frame.add(panel);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            frame.addKeyListener(this);

            asdfsdf.setFont(new java.awt.Font("tahoma", java.awt.Font.PLAIN, 44));
            asdfsdf.setForeground(java.awt.Color.MAGENTA);
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
            
            ///frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            frame.setTitle("WAR " + "life: " + life + " bombs: " + bomb);

            randomizeNewEnemySoldiers();
            
            g = panel.getGraphics();
            try {
                java.lang.Thread t = new java.lang.Thread(new java.lang.Runnable() {
                    public void run() {
                        while(true) {
                            try {
                                frame.setTitle("WAR " + "life: " + life + " bombs: " + bomb);
                                Thread.sleep(20);
                                drawBattleField(g);
                                drawSoldier(g);
                                drawEnemies(g);
                                drawRamboAssets();
                                drawCoins();
                                
                                for(int i=0; i<coins.size(); i++) {
                                    Coin coin = coins.get(i);
                                    if(coin.didYouEatMe(rambo.x, rambo.y)) {
                                        coins.remove(coin);
                                        ++bomb;
                                    }
                                    --coin.ttl;
                                    if(coin.ttl <= 0) {
                                        coins.remove(coin);
                                    }
                                }
                                
                                for(int i=0; i<rambo.theShoot.size(); i++) {
                                    Rambo.Shoot shoot = rambo.theShoot.get(i);
                                    shoot.moveRight();
                                    drawShoots(shoot);
                                    for(int j=0; j<es.size(); j++) {
                                        EnemySoldier enemySoldier = es.get(j);
                                        if(enemySoldier.didYouDie(shoot.x, shoot.y)) {
                                            es.remove(enemySoldier);
                                            Coin coin = new Coin(shoot.x, shoot.y);
                                            coins.add(coin);
                                            ++life;
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
                                            drawExplosion(bomb.x, bomb.y);
                                            es.remove(enemySoldier);
                                            Coin coin = new Coin(bomb.x, bomb.y);
                                            coins.add(coin);
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
                                    }
                                }

                                if(life <= 0)
                                    System.exit(1);
                                if(es.size() == 0) {
                                    randomizeNewEnemySoldiers();
                                }
                                
                            } catch(Exception e) {
                                e.printStackTrace();
                                 System.out.println(e.getMessage());
                                
                                System.out.println("shoots: " + rambo.theShoot.size());
                                System.out.println("bombs: " + rambo.bombs.size());
                            
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
                    }
                });
                t.start();
            } catch(Exception e) {}
        } catch(Exception e) {}
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
        g.drawImage(img, coin.x, coin.y, 40, 40, null);
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

    private void drawBattleField(Graphics g) {
        java.awt.Image img = null;
        String image = "battlefield.jpg";
        javax.swing.ImageIcon i = new javax.swing.ImageIcon(this.getClass().getResource(image));
        img = i.getImage();
        g.drawImage(img, 0, 0, panel.getWidth(), panel.getHeight(), null);
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

        g.drawImage(img, rambo.x, rambo.y, 90, 90, null);
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
            rambo.shoot();
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