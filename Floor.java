import java.util.ArrayList;
import java.util.Random;
/**
 *
 * @author ShameOnU
 */
public class Floor
{
    ArrayList pop; //what people are currently on the floor
    int height; //where in the complex the floor is located, i.e. height 0 is groundfloor.
    private int clearenceReq; //what clearence is needed to enter this floor
    Elevator ele;
    private static final Random rand = new Random();
    
    public Floor(int height, int clearence, Elevator e)
    {
        ele = e;
        pop = new ArrayList(10);
        this.height = height;
        clearenceReq = clearence;
    }
    public void arrive()
    {
            while(rand.nextBoolean() && !pop.isEmpty()) //a random number of people gets on the elevator if it isn't full
            {
                Person p = (Person)pop.get(rand.nextInt(pop.size()));
                if(ele.personEnters(p)) //checks if the elevator is full and adds the person if it is not
                    personLeaves(p);
            }
    }
    
    private void personLeaves(Person person)
    {
        if(pop.contains(person))
        {
            pop.remove(person);
        }
        else
            System.out.println("Person not on this floor");
           
    }
    
    public boolean personEnters(Person person) //Verifies that a person has the clearence required to enter the floor.
    {
        if(person.clearence >= clearenceReq)
        {
            pop.add(person);
            return true;
        }
            
        else
            return false;
                     
    }
    
    public void call()
    {
        try 
        {
            ele.call(height);
        } 
        catch (InterruptedException ex) 
        {
            //does nothing
        }
    }    
}
