import java.awt.*;
/**
 *
 * @author ShameOnU
 */
class GraphicsProgram extends Canvas
{
    private int X;
    private int Y;
    public int test;
    public int[] display;
    public int pass = 0;
    Graphics myG;
    Image myI;
    
    public GraphicsProgram(int x, int y){
        X = x;
        Y = y;
        test = y-51;
        setSize(X, Y);
        setBackground(Color.white);
        display = new int[Y/100];
    }
    
    
    @Override
    public void paint(Graphics g)
    {        
        myI = createImage(X,Y);
        myG = myI.getGraphics();
        myG.setColor(Color.red);
        myG.fillRect(0, test, 50, 50);
        myG.setColor(Color.black);
        for(int i = 0;i<display.length;i++)
            myG.drawString(display[i]+" Person(s) on this floor", 100, 375-(i*100));
        myG.drawString(""+pass, 20, test+30);
        g.drawImage(myI, 0, 0, this);         
    }
    
    public void move_Up()
    {
        for(int i = 0;i < 100; i++)
        {
            test--;
        }
        repaint();        
    }
        
    public void move_Down()
    {
        for(int i = 0;i < 100; i++)
        {
            test++;                        
        }
        repaint();
    }
}