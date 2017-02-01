/**
 *
 * @author ShameOnU
 */
public class Person implements Runnable{
    private boolean inElevator = false;
    int clearence; //what clearence this person has
    int id; // the id of this person
    Floor floor;
    Elevator elevator;
    private boolean alive;
    private static final java.util.Random rand = new java.util.Random();
    public Person(int clearence, int id, Floor floor,Elevator elevator)
    {
        this.clearence = clearence;
        this.id = id;
        alive = true;
        this.floor = floor;
        this.elevator = elevator;
    }
    
    public int returnID()
    {
        return id;
    }
    
    public int returnClearence()
    {
        return clearence;
    }
    
    public void kill()
    {
        alive = false;
    }
    
    public void leaveElevator()
    {
        if(inElevator)
            inElevator = false;
    }
    
    public void getInElevator()
    {
        if(!inElevator)
            inElevator = true;
    }

    @Override
    public void run() 
    {
        while(alive)
        {
            try
            {
                while(inElevator)
                {
                    Thread.sleep(rand.nextInt(5000));
                    elevator.button_press(rand.nextInt(elevator.floors.length)); //passengers press buttons at random.
                }
                while(!inElevator)
                {
                    Thread.sleep(rand.nextInt(10000));
                    floor.call();
                }
            }
            catch(InterruptedException e)
            {
                System.out.println(e);
            }
            
        }
    }
}
