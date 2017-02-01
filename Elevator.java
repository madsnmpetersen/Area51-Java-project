/**
 *
 * @author ShameOnU
 */
public class Elevator {
    private final int maxClearence = 2;
    private int leastClearence = maxClearence;
    private static final java.util.Random rand = new java.util.Random();;
    private final int MAX_LOAD = 4; //maximum amount of passengers in the elevator at once.
    private final Person[] passengers; //equivalent of the headcounter from the fsp model, using an array instead of ArrayList to make sure MAX_LOAD is enforced.
    private int emptyPointer = 0; //used to point to where in the Person array the next person is placed.
    private int freeSpace = MAX_LOAD;
    private int atfloor; //FLOOR_POINTER from the fsp model.
    private int tofloor; //also FLOOR_POINTER from the fsp model.
    Floor[] floors;
    private static final boolean WAITING = false; //states that the elevator can be in, these determine if it can be called or not.
    private static final boolean MOVING = true;
    private boolean STATE;
    GraphicsProgram prettyPicture;
    
    public Elevator(GraphicsProgram gp, int floors)
    {
        this.floors = new Floor[floors];
        prettyPicture = gp;
        passengers = new Person[MAX_LOAD];
        atfloor = 0;
        tofloor = 0;
        STATE = WAITING;
        init();
    }
    private void init()
    {
        floors[0] = new Floor(0,0,this); //adding the floors specified in the assignment, others could be added but might ruin the visual somewhat.
        floors[1] = new Floor(1,1,this);
        floors[2] = new Floor(2,1,this);
        floors[3] = new Floor(3,2,this);
        for(int i = 0;i<10;i++)
        {
            floors[0].personEnters(new Person(rand.nextInt(4),i,floors[0],this)); //adds people to the ground floor.
            setFloorStats(0,i+1);
        }
        for(int i = 0;i < floors[0].pop.size();i++) 
        {
            Person p = (Person)floors[0].pop.get(i);
            new Thread(p).start();
        }
        for(int i = 0;i < passengers.length;i++)
            passengers[i] = null;        
    }
    
    synchronized Person[] printFloor(int floor) throws InterruptedException
    {
        while(STATE) wait();
        STATE = MOVING;
        Person[] print = new Person[floors[floor].pop.size()];
        for(int i = 0;i<floors[floor].pop.size();i++)
        {
            print[i] = (Person)floors[floor].pop.get(i);
        }
        STATE = WAITING;
        notifyAll();
        return print;
    }
    
    synchronized Person[] printElevator() throws InterruptedException
    {
        while(STATE) wait();
        STATE = MOVING;
        int size = 0;
        Person[] print;
        for(int i = 0;i < MAX_LOAD;i++)
        {
            if(passengers[i] != null)
                size++;
        }
        print = new Person[size];
        size = 0;
        for(int i = 0;i < MAX_LOAD;i++)
        {
            if(passengers[i] != null)
            {
                print[size] = passengers[i];
                size++;
            }
        }
        STATE = WAITING;
        notifyAll();
        return print;
    }
    
    synchronized void pause()
    {
        STATE = MOVING;
    }
    
    synchronized void resume()
    {
        if(STATE == MOVING)
        {
            STATE = WAITING;
            notifyAll();
        }
    }
    
    synchronized void call(int floor) throws InterruptedException
    {
        while(STATE) wait();
        changeState();
        tofloor = floor;
        move();
        stopping();
        notify();
    }
    
    synchronized void button_press(int floor) throws InterruptedException
    {
        if(floor <= leastClearence)
        call(floor);        
    }
    
    public void setFloorStats(int pos, int number)
    {
        prettyPicture.display[pos] = number;
    }
    
    synchronized boolean personEnters(Person p)
    {
        if(!isFull() && emptyPointer < MAX_LOAD)
        {
            passengers[emptyPointer] = p;
            freeSpace--;
            prettyPicture.pass++;
            p.getInElevator();
            emptyPointer++;
            prettyPicture.display[atfloor]--;
            if(p.clearence < leastClearence)
                leastClearence = p.clearence;
            return true;
        }
        else
        {
            return false;         
        }
    }
    
    private void personExits(Person p)
    {        
            int tempclr = maxClearence;
            for(int i = 0;i<passengers.length;i++)
            {            
                if(passengers[i] == p)
                {
                    passengers[i] = null;
                    emptyPointer = i;
                }
                else if(passengers[i] != null)
                {
                    if(passengers[i].clearence < tempclr)
                        tempclr = passengers[i].clearence;
                }            
            }   
            leastClearence = tempclr;
            freeSpace++;
            prettyPicture.pass--;
            p.leaveElevator();
            prettyPicture.display[atfloor]++;        
    }
    
    public boolean isMoving()
    {
        return STATE;
    }
    
    public int atfloorCheck()
    {
        return atfloor;
    }
    
    private boolean isEmpty()
    {
        return freeSpace == MAX_LOAD;
    }
    
    private boolean isFull()
    {
        return freeSpace == 0;
    }
    
    private void stopping()
    {
        if(!isEmpty())
            while( rand.nextInt(MAX_LOAD-1)> 0 && !isEmpty() ) //a random number of people gets off
            {
                Person p = passengers[rand.nextInt(MAX_LOAD-1)];
                if(p != null)
                if(floors[atfloor].personEnters(p))
                {
                    personExits(p);
                }
            }
        if(!isFull())
            floors[atfloor].arrive();            
    }
    
    private void changeState()
    {
        if(STATE == WAITING)
            STATE = MOVING;
        else
            STATE = WAITING;
    }
    
    private void move()
    {
        if(tofloor > atfloor)
        {
            while(tofloor != atfloor)
            {
                prettyPicture.move_Up();
                atfloor++;
            }
            changeState();
        }
        else if(tofloor < atfloor)
        {
            while(tofloor != atfloor)
            {
                prettyPicture.move_Down();
                atfloor--;
            }
            changeState();
        }
        else
            changeState();
        
    }
}
