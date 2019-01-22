import greenfoot.*;// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;  

/**
 * 
 * @author Abdelilah Jabri
 * @version 0.1
 */
public class MyWorld extends World
{
    nodeMap[][] nodes;
    nodeMap start;
    nodeMap finish;
    Gi5 gi5 = new Gi5();
    Diplome diplome = new Diplome();
    Message msg = new Message(this);
    boolean playing = false;
    boolean finished = false;
    double time,cost;
    int nodeNum;

    public MyWorld()
    {    
        super(9,8,72);
        setBackground("Tile_14.png");
        setPaintOrder(Message.class, Gi5.class, Diplome.class, nodeMap.class);
        createNodes();
        addObject(gi5, getWidth(), getHeight());
        addObject(diplome, 0, 0);
        addObject(msg, getWidth()/2, 3);
        Greenfoot.start();
    }

    public void act()
    {
        if(Greenfoot.isKeyDown("escape") && !playing)
        {
            removeObject(msg);
            playing = true;
        }
        if(Greenfoot.isKeyDown("i") && playing && !finished)
        {
            addObject(msg, getWidth()/2, 3);
            gi5.deactivate();
            diplome.deactivate();
            playing = false;
        }
        if(Greenfoot.isKeyDown("r") && !playing && finished)
        {
            msg.showResults( nodeNum, time, cost);
            addObject(msg, getWidth()/2, 3);
            gi5.deactivate();
            diplome.deactivate();
        }
        if(Greenfoot.isKeyDown("d") && playing && !finished)
        {
            diplome.activate();
            gi5.deactivate();
        }

        if(Greenfoot.isKeyDown("g") && playing && !finished)
        {
            gi5.activate();
            diplome.deactivate();
        }

    }

    private void createNodes() {
        int width = getWidth();
        int height = getHeight();
        nodes = new nodeMap[width][height];
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                nodes[i][j] = new nodeMap(1, i, j);
                addObject(nodes[i][j], i, j);
            }
        }
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                // left nodes
                if(i-1>=0) {
                    if(j-1>=0) {
                        nodes[i][j].NW = nodes[i-1][j-1];
                    }
                    if(j+1<height) {
                        nodes[i][j].SW = nodes[i-1][j+1];
                    }
                    nodes[i][j].W = nodes[i-1][j];
                }
                // centre nodes
                if(j-1>=0) {
                    nodes[i][j].N = nodes[i][j-1];
                }
                if(j+1<height) {
                    nodes[i][j].S = nodes[i][j+1];
                }
                // right nodes
                if(i+1<width) {
                    if(j-1>=0) {
                        nodes[i][j].NE = nodes[i+1][j-1];
                    }
                    if(j+1<height) {
                        nodes[i][j].SE = nodes[i+1][j+1];
                    }
                    nodes[i][j].E = nodes[i+1][j];
                }
            }
        }
    }
    public boolean isBlocked(nodeMap node)
    {
        List<nodeMap> neighbours = node.getTraversableNeighbors();
        neighbours.addAll(node.getDiagonallyTraversableNeighbors());
        if(neighbours.isEmpty())
        node.active = false;
        return neighbours.isEmpty();
    }
    public void gameOver()
    {
            msg.showErrors(this,this.start.active,this.finish.active);
            addObject(msg, getWidth()/2, 3);
            gi5.deactivate();
            diplome.deactivate();
    }
}
