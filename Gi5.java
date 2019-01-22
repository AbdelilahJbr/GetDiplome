import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.lang.*;
import java.awt.Color;

/**
 *
 * 
 * @author Abdelilah Jabri
 * @version 0.1
 */
public class Gi5 extends Actor
{
    private boolean searching = false;
    private boolean searched = false; // stores if a search has taken place.
    private nodeMap start;
    private nodeMap finish;
    private List<nodeSearch> unexplored = new LinkedList<nodeSearch>(); // open list
    private List<nodeMap> explored = new LinkedList<nodeMap>(); // closed list
    private double sqrt2 = Math.sqrt(2);
    private boolean i = false;
    private boolean j = false;
    public boolean active = false;
    private long tStart;
    private long tEnd;

    public Gi5() {
        setImage("gi5.png");
        getImage().setTransparency(0);
    }

    public void act() 
    {
        walk();
        flipIt();
        if(searching) {
            findPath();
        } else if(Greenfoot.isKeyDown("space") && !searched) {

            startSearch();
        }
    }

    public void activate()
    {
        active = true;
        getImage().setTransparency(255);
    }

    public void deactivate()
    {
        active = false;
        if(j)
        {
            if(i){
                getImage().mirrorVertically();
                i = false;
            }
            setRotation(0);
            j= false;
        }
    }

    public boolean isAt( int x, int y)
    {
        return getX()==x && getY()==y;
    }

    public void flipIt()
    {
        MyWorld world = (MyWorld) getWorld();
        if(world.finish != null)
        {
            int x = world.finish.getX();
            if(x > getX() && i)
            {
                getImage().mirrorVertically();
                i = false;
                setRotation(0);
            }else if(x < getX() && !i)
            {
                getImage().mirrorVertically();
                i = true;
                setRotation(180);
            }
        }
    }

    public void walk(){ 
        if(active){
            nodeMap current = getNodeAt( getX(), getY());
            MyWorld world = (MyWorld) getWorld();
            Diplome diplome = world.diplome;
            if(Greenfoot.isKeyDown("right")) {
                if(i){
                    getImage().mirrorVertically();
                    i = false;
                }
                setRotation(0);
                if(current.E != null && current.E.traversable && !diplome.isAt( getX()+1, getY()))
                    move(1);
            } 
            if(Greenfoot.isKeyDown("down")) 
            { 
                setRotation(90);
                if(current.S != null && current.S.traversable && !diplome.isAt( getX(), getY()+1))
                    move(1);
                j = true;
            } 
            if(Greenfoot.isKeyDown("left")) 
            { 
                if(!i){
                    getImage().mirrorVertically();
                    i = true;
                }
                setRotation(180);
                if(current.W != null && current.W.traversable && !diplome.isAt( getX()-1, getY()))
                    move(1); 
            } 
            if(Greenfoot.isKeyDown("up")) 
            { 
                setRotation(270);
                if(current.N != null && current.N.traversable && !diplome.isAt( getX(), getY()-1))
                    move(1);
                j = true;
            }

            world.start = getNodeAt( getX(), getY());

        }
    }

    public nodeMap getNodeAt(int x, int y)
    {
        MyWorld world = (MyWorld) getWorld();
        nodeMap node = world.nodes[x][y];
        return node;

    }

    private void toTheTreasure(LinkedList<nodeMap> nodes)
    {
        for(nodeMap node : nodes)
        {
            this.setLocation(node.getX(), node.getY());
            node.getImage().clear();
            Greenfoot.delay(2);
        }
    }

    public void startSearch(nodeMap start, nodeMap finish) {
        this.start = start;
        this.finish = finish;
        tStart = System.currentTimeMillis();
        searching = true;
        unexplored.add(
            new nodeSearch(start, 0, Double.MAX_VALUE, null));

    }

    public void startSearch() {
        MyWorld world = (MyWorld) getWorld();
        if(world.start != null && world.finish != null) {
            if(world.isBlocked(world.start) || world.isBlocked(world.finish))
                finishedPath(null, 0, 0);
            else
                startSearch(world.start, world.finish);
        }
    }

    /**
     * this and expandNodes do the grunt of the actual pathfinding.
     */
    private void findPath() {
        if(unexplored.size()==0) {
            // if there are no more nodes to explre then we couldn't find a path.
            finishedPath(null, 0, 0);
        } else {
            ListIterator<nodeSearch> explorer = unexplored.listIterator();
            int nodePoz = 0;
            double lowestScore = Double.POSITIVE_INFINITY;
            // to start with the best score is inf because it needs to be higher than
            // any possible node in the list.
            nodeSearch bestNode = null;
            // go through each unexplored node searching for the lowest.
            while(explorer.hasNext()) {
                int nextNodePoz = explorer.nextIndex();
                nodeSearch node = explorer.next();
                if(node.getFVal() < lowestScore) {
                    lowestScore = node.getFVal();
                    bestNode = node;
                    nodePoz = nextNodePoz;
                }
            }
            // remove it from the unexplored list and add it to the explored list.
            unexplored.remove(nodePoz);
            explored.add(bestNode.getNode());
            // search all it's neighbours
            List<nodeMap> nodesFound = bestNode.getNode().getTraversableNeighbors();
            expandNodes(bestNode, nodesFound, false);
            //* comment this out for 4-directional only
            nodesFound = bestNode.getNode().getDiagonallyTraversableNeighbors();
            expandNodes(bestNode, nodesFound, true);
            //*/
        }
    }

    private void expandNodes(nodeSearch parent, List<nodeMap> nodesFound, boolean diagonal) {
        for(nodeMap newNode : nodesFound) {
            if(explored.indexOf(newNode) > -1) {
                continue;
            }
            double cost;
            if(!diagonal) {
                cost = parent.getCost() + newNode.cost;
            } else {
                cost = parent.getCost() + newNode.cost*sqrt2;
            }
            boolean nodeAlreadyFound = false;
            ListIterator<nodeSearch> explorer = unexplored.listIterator();
            // go through each node we have already found to see if this
            // is the smae node
            while(explorer.hasNext()) {
                int nextNodePoz = explorer.nextIndex();
                nodeSearch node = explorer.next();
                // if we find a shorter route to the same node.
                if(node.getNode() == newNode) {
                    if(cost < node.getCost()) {
                        // if our route to the node is faster then remove the other node
                        nodeAlreadyFound = false;
                        unexplored.remove(nextNodePoz);
                    } else { // keep the other node
                        nodeAlreadyFound = true;
                    }
                    break;
                }
            }
            if(nodeAlreadyFound) {
                continue;
            }
            // calculate the heuristic score from one of the functions
            //double h = heuristic();
            //double h = heuristic(newNode, finish);
            //double h = heuristicMT(newNode, finish);
            //double h = heuristicD(newNode, finish);
            double h = heuristicDT(newNode, finish);
            //double h = heuristicEuclidean(newNode, finish);
            // calculate the F score
            double FVal = cost + h;
            // create a new nodeSearch object for this newly explored node.
            nodeSearch newNodeSearch = new nodeSearch(newNode, cost, FVal, parent);
            if(newNode == finish) {
                // see if it is the finish node, if so we are done.
                tEnd = System.currentTimeMillis();
                long tDelta = tEnd - tStart;
                double elapsedSeconds = tDelta / 1000.0;
                finishedPath(newNodeSearch,elapsedSeconds, cost);
                searching = false;
            } else {
                unexplored.add(newNodeSearch);
                // otherwise add it to the unexplored list

                // print score, heuristic and FVal onto node:
                GreenfootImage nodeImage = newNode.getImage();
                nodeImage.clear();
                nodeImage.setFont(new Font("Dialog",false, false, 10));
                nodeImage.setColor(greenfoot.Color.WHITE);
                String costString = String.format("%5.1f",cost);
                nodeImage.drawString(costString, 1, 11);
                nodeImage.setColor(greenfoot.Color.GREEN);
                String hString = String.format("%5.1f",h);
                nodeImage.drawString(hString, 18, 11);
                nodeImage.setColor(greenfoot.Color.ORANGE);
                String FValString = String.format("%5.1f",FVal);
                nodeImage.drawString(FValString, 1, 28);
            }
        }
    }

    /**
     * Diagonal heuristic with straight line tiebreaker
     */
    private double heuristicDT(nodeMap a, nodeMap b) {
        double dx=Math.abs(a.getX() - b.getX());
        double dy=Math.abs(a.getY() - b.getY());
        return (dx + dy) + (sqrt2 - 2) * Math.min(dx, dy);
    }

    /**
     * what to do when we have found a path(or we didn't...)
     */
    private void finishedPath(nodeSearch endNode, double elapsedSeconds, double cost) {
        MyWorld world = (MyWorld) getWorld();
        searching = false;
        searched = true;
        world.finished = true;
        world.playing = false;
        if(endNode == null) {
            world.gameOver();
        } else {
            int i = 0;
            LinkedList<nodeMap> nodes = new LinkedList<nodeMap>();
            while(endNode.getParent().getNode() != start) {
                endNode = endNode.getParent();
                nodes.push(endNode.getNode());
                endNode.getNode().setImage("dot.png");
                i++;

            }
            toTheTreasure(nodes);
            world.nodeNum = i;
            world.time = elapsedSeconds;
            world.cost = cost;

        }
    }
}