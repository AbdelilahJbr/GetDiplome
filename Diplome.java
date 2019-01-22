import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 
 * @author Abdelilah Jabri
 * @version 0.1
 */
public class Diplome extends Actor
{
    /**
     * Act - do whatever the Diplome wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private boolean active = false;

    public Diplome(){
        setImage("diplome.png");
        getImage().setTransparency(0);
    }

    public void act() 
    {
        walk();
    }

    public void activate()
    {
        active = true;
        getImage().setTransparency(255);
    }

    public void deactivate()
    {
        active = false;
    }

    public void walk(){ 
        if(active){
            nodeMap current = getNodeAt( getX(), getY());
            MyWorld world = (MyWorld) getWorld();
            Gi5 gi5 = world.gi5;
            if(Greenfoot.isKeyDown("right")) {
                if(current.E != null && current.E.traversable && !gi5.isAt( 
                    getX()+1, getY()))
                    setLocation(getX()+1,getY());
            } 
            if(Greenfoot.isKeyDown("down")) 
            { 
                if(current.S != null && current.S.traversable && !gi5.isAt( getX(), getY()+1))
                    setLocation(getX(),getY()+1); 
            }
            if(Greenfoot.isKeyDown("left")) 
            {
                if(current.W != null && current.W.traversable && !gi5.isAt( getX()-1, getY()))
                    setLocation(getX()-1,getY());
            } 
            if(Greenfoot.isKeyDown("up")) 
            { 
                if(current.N != null && current.N.traversable && !gi5.isAt( getX(), getY()-1))
                    setLocation(getX(),getY()-1);
            }

            world.finish = getNodeAt( getX(), getY());

        }
    }

    public nodeMap getNodeAt(int x, int y)
    {
        MyWorld world = (MyWorld) getWorld();
        nodeMap node = world.nodes[x][y];
        return node;

    }

    public boolean isAt( int x, int y)
    {
        return getX()==x && getY()==y;
    }
}
