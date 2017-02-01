/**
 *
 * @author ShameOnU
 */
public class Complex {
    public static void main(String[] args)
    {
        int HEIGHT = 4;
        GraphicsProgram gp = new GraphicsProgram(400,(HEIGHT*100));
        Elevator myElevator = new Elevator(gp,HEIGHT);
        ElevatorGUI gui = new ElevatorGUI(myElevator);        
        gui.setVisible(true);
    }    
}
