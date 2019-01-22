import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.*; // List

/**
 * 
 * 
 * @author Abdelilah Jabri
 * @version 0.1
 */
public class nodeMap extends Actor
{
    private int x; // our position on the grid
    private int y;
    private boolean i = false;
    // if this node is at all traversable (is not a wall)
    boolean traversable = true;
    // the cost to traverse this node
    int cost = 1;
    // the neighbouring nodes
    nodeMap N;
    nodeMap NE;
    nodeMap E;
    nodeMap SE;
    nodeMap S;
    nodeMap SW;
    nodeMap W;
    nodeMap NW;
    public boolean active = true;
    public nodeMap() {
    }

    public nodeMap(int cost, int x, int y) {
        this.cost = cost;
        this.x = x;
        this.y = y;
    }

    public void act() {
        flip();
        setStatus();

    }

    public int getX() { return x; }

    public int getY() { return y; }

    /**
     * @returns the nodes above, beneath, left and right of this node.
     */
    public List<nodeMap> getTraversableNeighbors() {
        List<nodeMap> traversableNeighbors = new ArrayList();
        if(N != null && N.traversable)
            traversableNeighbors.add(N);
        if(E != null && E.traversable)
            traversableNeighbors.add(E);
        if(S != null && S.traversable)
            traversableNeighbors.add(S);
        if(W != null && W.traversable)
            traversableNeighbors.add(W);
        return traversableNeighbors;
    }

    /**
     * @returns the nodes diagonally adgject to this node.
     */
    public List<nodeMap> getDiagonallyTraversableNeighbors() {
        List<nodeMap> traversableNeighbors = new ArrayList();
        if(NE != null && NE.traversable)
            traversableNeighbors.add(NE);
        if(SE != null && SE.traversable)
            traversableNeighbors.add(SE);
        if(SW != null && SW.traversable)
            traversableNeighbors.add(SW);
        if(NW != null && NW.traversable)
            traversableNeighbors.add(NW);
        return traversableNeighbors;
    }

    /**
     * if the mouse is pressed on this node toggle it's state depending on the key pressed.
     */
    private void setStatus() {
        MyWorld world = (MyWorld) getWorld();
        if(Greenfoot.mousePressed(this) && world.playing && !world.finished) {
            if(traversable) {
                traversable = false;
                setImage("ds.png");
            } else {
                traversable = true;
                i= false;
                getImage().clear();
            }
        }
    }

    public void flip()
    {
        if(!traversable)
        {
            MyWorld world = (MyWorld) getWorld();
            int x = world.gi5.getX();
            if(x > getX() && !i)
            {
                getImage().mirrorHorizontally();
                i = true;
            }else if(x < getX() && i)
            {
                getImage().mirrorHorizontally();
                i = false;
            }
        }

    }
}