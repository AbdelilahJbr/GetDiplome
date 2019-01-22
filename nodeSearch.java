
/**
 *
 * @author Abdelilah Jabri
 * @version 0.1
 */
public class nodeSearch  
{
    // the node on the grid that we represent
    private nodeMap node;
    // the parent node
    private nodeSearch parent;
    // the cost to get here so far
    private double currCost;
    // the estimated cost to the finish
    private double FVal;
    
    public nodeSearch(nodeMap nodemap, double cost, double f, nodeSearch par) {
        node = nodemap;
        currCost = cost;
        FVal = f;
        parent = par;
    }
    
    public nodeMap getNode() {
        return node;
    }
    public nodeSearch getParent() {
        return parent;
    }
    public double getCost() {
        return currCost;
    }
    public double getFVal() {
        return FVal;
    }
}