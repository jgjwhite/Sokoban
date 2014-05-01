/*
 * Sokoban Project
 * Justin White
 * Final Submission 
 *
 * April 15th, 2014
 */
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;


public class Sokoban extends JPanel {

    private LevelReader lr; //initialize LevelReader field variable
    private Contents[][] field; //initialize field 2D array instance
    private boolean isReady = false; //Constructor is Ready method
    private int currentlevel; //how to manage currentlevel ##
    private int w,h;
    private int xloc, yloc;
    private JLabel instructions, moves;
    private int countMoves;
    
    //constructor for class that calls the readLevels method of the LevelReader object.
    public Sokoban(String fileName) {
       
        lr = new LevelReader();
        lr.readLevels(fileName);
        currentlevel=0; 
        
        //Displays instructions for play
        instructions = new JLabel("Instructions for Play:  Move Player by using Directional Arrows. Hit 'N' for Next Level, 'R' to Restart Level and 'B' for Back one level || ");
        this.add(instructions);
        instructions.setFont(new Font("Californian FB", Font.BOLD, 12));
        //instructions.setOpaque(true);
        //instructions.setBackground(Color.BLUE);
        //instructions.setForeground(Color.RED);
        
        //Move counter
        moves = new JLabel("");
        this.add(moves);
        moves.setText("Move Count: " +countMoves);
        moves.setFont(new Font("Californian FB", Font.BOLD, 12));
        
        this.setPreferredSize(new Dimension(1250,750)); //size set to fit largest level available in m1.txt (@40px*40px tiles)
        this.addKeyListener(new myKeyListener());
        this.setFocusable(true);
        this.requestFocus();
        
        initLevel(currentlevel); //calls the initLevel method on the current level
        isReady=( lr.readLevels(fileName) > 0 )?true:false; //isReady code
        
    }
    
    public void initLevel(int level) {
       
       field = new Contents[lr.getWidth(level)][lr.getHeight(level)]; //set field by creating new 2D array
       w= lr.getWidth(level);
       h= lr.getHeight(level); 
       
       //loop that sets the tile contents for the level
        for ( int r = 0; r < lr.getWidth(level); r++ ) {
            for ( int c = 0; c < lr.getHeight(level); c++ ) {
                field[r][c] = lr.getTile(level, r, c); //use getTile method to populate each array element
            
                //also need to memorize player location...
                if ( field[r][c] == Contents.PLAYER || field[r][c] == Contents.PLAYERONGOAL) {
                    xloc=r;
                    yloc=c;
                }
            }
        }
        countMoves=0; //reset moves every time new level mapped
        moves.setText("Move Count: " + countMoves);
        repaint(); 
    }
    
    
    private boolean checkWin () {
       //loop through field, if any Content.BOX=true, set boolean to false
       boolean win = true;
        
       for ( int r = 0; r < w; r++ ) {
            for ( int c = 0; c < h; c++ ) {
                if (field[r][c] == Contents.BOX ) { win = false; }
                
            }
       }
       return win;
    } 
    
    public void paintComponent(Graphics g) {
        double centerX = 612.50 - (lr.getWidth(currentlevel)/2 * 40); //meant to accomodate largest level in m1.txt
        double centerY = 362.50 - (lr.getHeight(currentlevel)/2 * 40); 
         //w= lr.getWidth(level);
      // h= lr.getHeight(level); 
        
        if(isReady) {
        
            Color BROWN = new Color (139,69,19);
            
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
            
            //Coloring code:
            for(int x = 0; x < w; x++) {  
               for(int y = 0; y < h; y++) {
                    g2.setPaint(Color.BLACK);
                    g2.draw(new Rectangle2D.Double((x*40)+centerX,(y*40)+centerY,40,40));
                    
               
                        if (field[x][y] == Contents.WALL) { g2.setPaint(Color.DARK_GRAY); }
                        if (field[x][y] == Contents.EMPTY) { g2.setPaint(Color.LIGHT_GRAY); }
                        if (field[x][y] == Contents.PLAYER) { g2.setPaint(Color.BLUE); }
                        if (field[x][y] == Contents.BOX) { g2.setPaint(BROWN); }
                        if (field[x][y] == Contents.GOAL) { g2.setPaint(Color.RED); }
                        if (field[x][y] == Contents.BOXONGOAL) { g2.setPaint(Color.MAGENTA); }
                        if (field[x][y] == Contents.PLAYERONGOAL) { g2.setPaint(Color.YELLOW); }
                
                    g2.fill(new Rectangle2D.Double((x*40)+centerX,(y*40)+centerY,40,40)); // tiles set at 40px * 40px          
                    g2.setPaint(Color.BLACK); // outline each tile in black
                    g2.draw(new Rectangle2D.Double((x*40)+centerX,(y*40)+centerY,40,40));
                    }
            }
        }  

            
        }
    
    //following methods used to quickly differentiate tiles in the keyListener
    public boolean wall (int x, int y) {
        return field[xloc+x][yloc+y]==Contents.WALL;
    }    
        
    public boolean emptyGoal (int x, int y) {
        return field[xloc+x][yloc+y]==Contents.EMPTY || field[xloc+x][yloc+y]==Contents.GOAL;
    }
    
    public boolean box (int x, int y) {
        return field[xloc+x][yloc+y]==Contents.BOX || field[xloc+x][yloc+y]==Contents.BOXONGOAL;
    }
    
    private class myKeyListener implements KeyListener {
        public void keyPressed (KeyEvent ke) {
            boolean keyPressed = false;
            int kc = ke.getKeyCode();
            int dx = 0;
            int dy = 0;
           
            if (kc == KeyEvent.VK_N) { //N = move forward one level
                if (currentlevel<154) { currentlevel+=1; initLevel(currentlevel); }
            }
            if (kc == KeyEvent.VK_B) { //B = move back one level
                if (currentlevel>1) { currentlevel=currentlevel-1; initLevel(currentlevel); }
            }
            if (kc == KeyEvent.VK_R) {initLevel(currentlevel);} //reset level
            if (kc == KeyEvent.VK_UP) { dy=-1;keyPressed=true; }
            if (kc == KeyEvent.VK_DOWN) { dy=1;keyPressed=true; }
            if (kc == KeyEvent.VK_RIGHT) { dx=1;keyPressed=true; }
            if (kc == KeyEvent.VK_LEFT) { dx=-1;keyPressed=true; }
       
            if ( wall(dx,dy) ) { xloc=xloc;yloc=yloc; } //player does not move
            
            if ( emptyGoal(dx,dy) ) { //empty/goal adjacent to player...
                
                if ( field[xloc+dx][yloc+dy]==Contents.GOAL ) { //if GOAL adjacent..
                    field[xloc+dx][yloc+dy]=Contents.PLAYERONGOAL; //set adjacent to PLAYERONGOAL 
                       
                        
                }
              
                else { field[xloc+dx][yloc+dy]=Contents.PLAYER; } //else EMPTY=move player
                
                field[xloc][yloc] = field[xloc][yloc]==Contents.PLAYERONGOAL ?Contents.GOAL:Contents.EMPTY; //ternary: set GOAL if true...
                xloc+=dx; yloc+=dy; //move player position
            } 
            
            else if ( box(dx,dy) ) { //if player directed towards box 1 tile away...
                
                if ( wall(dx*2,dy*2) ) { xloc=xloc;yloc=yloc; } //wall is 2 tiles away, do nothing.
                
                if ( emptyGoal(dx*2,dy*2) ) { //either empty OR goal score 1 tile behind box...
                    
                    if ( field[xloc+dx][yloc+dy]==Contents.BOXONGOAL && field[xloc+dx*2][yloc+dy*2]==Contents.GOAL ) {  //  ||BOXONGOAL||GOAL = PLAYERONGOAL||BOXONGOAL
                        field[xloc+dx][yloc+dy]=Contents.PLAYERONGOAL; //set 1 tile away to GOAL
                        field[xloc+dx*2][yloc+dy*2]=Contents.BOXONGOAL; //set two tiles away to BOXONGOAL
                        
                    } 
                    
                    if ( field[xloc+dx][yloc+dy]==Contents.BOXONGOAL && field[xloc+dx*2][yloc+dy*2]==Contents.EMPTY ) {  // ||BOXONGOAL||EMPTY == PLAYERONGOAL||BOX **
                        field[xloc+dx][yloc+dy]=Contents.PLAYERONGOAL;
                        field[xloc+dx*2][yloc+dy*2]=Contents.BOX; 
                    }
                    
                    if ( field[xloc+dx][yloc+dy]==Contents.BOX && field[xloc+dx*2][yloc+dy*2]==Contents.GOAL ) { // BOX||GOAL == PLAYER||BOXONGOAL
                        field[xloc+dx][yloc+dy]=Contents.PLAYER;
                        field[xloc+dx*2][yloc+dy*2]=Contents.BOXONGOAL;
                    }
                    
                    
                    
                    if ( field[xloc+dx][yloc+dy]==Contents.BOX && field[xloc+dx*2][yloc+dy*2]==Contents.EMPTY ) { // |BOX||EMPTY == PLAYER||BOX
                     field[xloc+dx][yloc+dy]=Contents.PLAYER; field[xloc+dx*2][yloc+dy*2]=Contents.BOX;  }

                    field[xloc][yloc] = field[xloc][yloc]==Contents.PLAYERONGOAL ?Contents.GOAL:Contents.EMPTY; //same as above (ternary: set GOAL if true...)
                    xloc+=dx; yloc+=dy;
                }
                
                
                if ( box(dx*2,dy*2) ) { xloc=xloc;yloc=yloc; } //player does not move
                    
            }
             
           if ( checkWin() ) { // if checkWin=true, advance level
                currentlevel+=1; 
                initLevel(currentlevel);
                countMoves = countMoves-1;
            }
            repaint();
              
            //Update move counter
            countMoves++;
            moves.setText("Move Count: " + countMoves);
        }
        
        public void keyTyped(KeyEvent ke) {}
        public void keyReleased(KeyEvent ke) {}
        }
        
    
    
    public static void main (String[] args) {
        JFrame f2 = new JFrame("Sokoban Project");
          f2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
          f2.setLayout(new FlowLayout());
          f2.add(new Sokoban("m1.txt"));
          f2.pack();
          f2.setVisible(true);
          
          
    }
    
   
}
