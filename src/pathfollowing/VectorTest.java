/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfollowing;

/**
 *
 * @author quanrocks11
 */

//A makeshift class meant to replicate Vectors with a coordinate point (X,Z). Naming is as such since a Vector class already exists in Java that serves another purpose
public class VectorTest
{
    private double x;
    private double z;
    
    //Default Constructor
    public VectorTest()
    {
        x = 0;
        z = 0;
    }
    
    //Constructor given an x Coordinate and Z coordinate
    public VectorTest(double xCoord, double zCoord)
    {
        x = xCoord;
        z = zCoord;
    }
    
    //Returns X coordinate
    public double getX()
    {
        return x;
    }
    
    //Returns Z coordinate
    public double getZ()
    {
        return z;
    }
    
    //Sets X coordinate to a passed value
    public void setX(double xCoord)
    {
        x = xCoord;
    }
    
    //Sets Z coordinate to a passed value
    public void setZ(double zCoord)
    {
        z = zCoord;
    }
    
}
