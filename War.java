import javax.swing.*;
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

public class War implements KeyListener {

    public void randomizeNewEnemySoldiers() {
        for(int i = 0; i < 10; i++) {
            es.add(new EnemySoldier(this));
        }
    }

    public void randomizeMiniNewEnemySoldiers() {
        for(int i = 0; i < 10; i++) {
            es.add(new EnemySoldier(this));
        }
    }

    public int deadCount = 0;

    private Graphics g = null;
    
    private Rambo rambo = new Rambo();
            
    private JFrame frame = new JFrame();
    
    private JPanel panel = new JPanel();
    
    private List<EnemySoldier> es = new ArrayList<EnemySoldier>();
    
    
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
//audioClip.start();
//audioClip.close();
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
//audioClip.start();
//audioClip.close();
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
            
            frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

            if(true) {
            
            randomizeNewEnemySoldiers();
            
            g = panel.getGraphics();
            try {
                java.lang.Thread t = new java.lang.Thread(new java.lang.Runnable() {
                    public void run() {
                        while(true) {
                            try {
                                Thread.sleep(20);
                                
                                drawBattleField(g);
                                
                                drawSoldier(g);
            
                                drawEnemies(g);

                                for(Iterator<Rambo.Shoot> it = rambo.theShoot.iterator(); it.hasNext(); ) {
                                    Rambo.Shoot shoot = it.next();
                                    shoot.moveRight();
                                    drawShoots(shoot);
                                    for(Iterator<EnemySoldier> i = es.iterator(); i.hasNext(); ) {
                                        EnemySoldier enemySoldier = i.next();
                                        if(enemySoldier.didYouDie(shoot.x, shoot.y)) {
                                            i.remove();
                                        }
                                    }
                                    //if(shoot.x > 1200)
                                        //it.remove();
                                }

                                for(Iterator<Rambo.Bomb> it = rambo.bombs.iterator(); it.hasNext(); ) {
                                    Rambo.Bomb bomb = it.next();
                                    --bomb.ttl;
                                    bomb.moveRight();
                                    drawBombs(bomb);
                                    for(Iterator<EnemySoldier> i = es.iterator(); i.hasNext(); ) {
                                        EnemySoldier enemySoldier = i.next();
                                        if(enemySoldier.didYouExplode(bomb.x, bomb.y)) {
                                            drawExplosion(bomb.x, bomb.y);
                                            i.remove();
                                        }
                                    }
                                    if(bomb != null)
                                    if(bomb.ttl <= 0) {
                                        drawExplosion(bomb.x, bomb.y);
                                        drawExplosion(bomb.x, bomb.y);
                                        drawExplosion(bomb.x, bomb.y);
                                        drawExplosion(bomb.x, bomb.y);
                                        //it.remove();
                                    }
                                }
                                
                                if(es.size() == 0) {
                                    randomizeNewEnemySoldiers();
                                }
                                
                            } catch(Exception e) {
                                System.out.println(e.getMessage());
                                
                                System.out.println("shoots: " + rambo.theShoot.size());
                                System.out.println("bombs: " + rambo.bombs.size());
                                }
                        }
                    }
                });
                t.start();
            } catch(Exception e) {System.out.println(e.getMessage());}
            }        
        } catch(Exception e) {System.out.println(e.getMessage());}
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
    
    private void drawEnemies(Graphics g) {
        for(Iterator<EnemySoldier> it = es.iterator(); it.hasNext(); ) {
            EnemySoldier sold = it.next();

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
            rambo.throwBomb();
            for(Iterator<Rambo.Bomb> it = rambo.bombs.iterator(); it.hasNext(); ) {
                Rambo.Bomb bomb = it.next();
                bomb.moveRight();
                drawBombs(bomb);
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            rambo.shoot();
            for(Iterator<Rambo.Shoot> it = rambo.theShoot.iterator(); it.hasNext(); ) {
                Rambo.Shoot shoot = it.next();
                shoot.moveRight();
                drawShoots(shoot);
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