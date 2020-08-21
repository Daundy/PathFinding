/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfollowing;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.*;
import java.io.*;
import java.lang.Math.*;
/**
 *
 * @author quanrocks11
 */
//Class for holding the accelerations after a Dynamic Seek calculation
public class SteeringOutput
{
    private double linearX;
    private double linearZ;
    private double angular;
    
    //Default Constructor
    public SteeringOutput()
    {
        linearX = 0;
        linearZ = 0;
        angular = 0;
    }
    
    //Updates the linear and angular accelerations given the character's accelerations and the target's accelerations as well as the max acceleration of the character
    public void setParams(double cx, double cz, double tx, double tz, double ma)
    {
        linearX = tx - cx;
        linearZ = tz - cz;

        normalize(linearX,linearZ);

        linearX *= ma;
        linearZ *= ma;

        angular = 0;
        
    }
    
    //Returns linearX
    public double getLinearX()
    {
        return linearX;
    }
    
    //Returns linearZ
    public double getLinearZ()
    {
        return linearZ;
    }
    
    //Returns Angular
    public double getAngular()
    {
        return angular;
    }
    
    //Normalizes two points as long as they do not make a zero vector
    public void normalize(double x, double z)
    {
        double length = length(x,z);
        
        if(length != 0)
        {
            linearX = x / length;
            linearZ = z / length;
        }
        else
        {
            linearX = 0;
            linearZ = 0;
        }
    }
    
    //Find the length of two points
    //Returns the result as a double
    public double length(double x, double z)
    {
        return (Math.sqrt(Math.pow(x,2) + Math.pow(z,2)));
    }
}

