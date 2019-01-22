
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.*;
/**
 * 
 * @author Abdelilah Jabri
 * @version 0.1
 */
public class Message extends Actor
{
    /**
     * Act - do whatever the Message wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private int width, height;
    public Message(MyWorld world)
    {
        width = (int)Math.round(world.getWidth()*72);
        height = (int)Math.round(world.getHeight()*72);
        showInstructions(world);

    }

    public void act() 
    {
        // Add your action code here.
    }

    public void showInstructions(MyWorld world)
    {
        setImage("instructions.png");
        getImage().scale(width, height);
        world.playing = false;

    }
    public void showErrors(MyWorld world, boolean gi5Active, boolean diplomeActive)
    {
        if(!gi5Active)
        setImage("gi5Stuck.png");
        else if(!diplomeActive)
        setImage("diplomeStuck.png");
        else
        setImage("noPath.png");
    }

    public void showResults(int nodeNumb, double execTime, double cost)
    {
        GreenfootImage backImage = new GreenfootImage("results.png");
        backImage.scale(width, height);
        backImage.setFont(new Font("Bradley Hand ITC",true, false, 36));
        backImage.setColor(greenfoot.Color.BLACK);
        String costString = String.format("%5.1f",cost);
        String time = String.format("%5.1f",execTime);
        backImage.drawString(time+" s", 480, 210);
        backImage.drawString(costString, 260, 398);
        backImage.drawString(nodeNumb+" points", 400, 302);
        setImage(backImage);
    }
}
