/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfollowing;

import java.io.FileWriter;

/**
 *
 * @author quanrocks11
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.*;
import java.io.*;
import pathfollowing.SteeringOutput;
import pathfollowing.Path;
/**
 *
 * @author quanrocks11
 */
//Main class. Holds variables for character which are sent to the text files
public class PathFollowing
{
    //Some variables aren't used here but are being kept for future use. Namely targetRadius, slowRadius, timeToTarget, and maxPrediction
    private int id;
    private double positionX;
    private double positionZ;
    private double velocityX;
    private double velocityZ;
    private double linearX;
    private double linearZ;
    private double orientation;
    private double rotation;
    private double angular;
    private double maxSpeed;
    private double maxAcceleration;

    private double targetRadius;
    private double slowRadius;
    private double timeToTarget;
    private double maxPrediction;
    private double offset;

    
    public PathFollowing()
    {
        id = 71;
        positionX = -90;
        positionZ = 60;
        velocityX  = 0;
        velocityZ = 0;
        linearX = 0;
        linearZ = 0;
        orientation = 0;
        rotation = 0;
        angular = 0;
        maxSpeed = 1.5;
        maxAcceleration = 1;
        targetRadius = 0;
        slowRadius = 0;
        timeToTarget = 0;
        maxPrediction = 0;
        offset = 0.05;
    }
    
    public PathFollowing(int idNumber, double px, double pz, double ms, double ma)
    {
        id = idNumber;
        positionX = px;
        positionZ = pz;
        velocityX  = 0;
        velocityZ = 0;
        linearX = 0;
        linearZ = 0;
        orientation = 0;
        rotation = 0;
        angular = 0;
        maxSpeed = ms;
        maxAcceleration = ma;
        targetRadius = 0;
        slowRadius = 0;
        timeToTarget = 0;
        maxPrediction = 0;
        offset = 0.05;
    }
    
    //Sets positionX to passed variable
    public void setPositionX(double px)
    {
        positionX = px;
    }
    
    //Sets positionZ to passed variable
    public void setPositionZ(double pz)
    {
        positionZ = pz;
    }
    
    //Sets orientation to passed variable
    public void setOrientation(double r)
    {
        orientation = r;
    }
    
    //Sets velocityX to passed variable
    public void setVelocityX(double vx)
    {
        velocityX = vx;
    }
    
    //Sets velocityZ to passed variable
    public void setVelocityZ(double vz)
    {
        velocityZ = vz;
    }
    
    //Sets rotation to passed variable
    public void setRotation(double r)
    {
        rotation = r;
    }
    
    //Returns positionX
    public double getPositionX()
    {
        return positionX;
    }

    //Returns positionZ
    public double getPositionZ()
    {
        return positionZ;
    }
    
    //Returns offset
    public double getOffset()
    {
        return offset;
    }
    
    //Returns maxAcceleration
    public double getMaxAcceleration()
    {
        return maxAcceleration;
    }
    
    //Returns length of an X and Z coordinate.
    public static double length(double x, double z)
    {
        return (Math.sqrt(x*x+z*z));
    }
    
    //Function for finding the targetPosition which will be sent to DynamicSeek.
    //Returns a SteeringOutput
    public static SteeringOutput followPath(PathFollowing character, Path path)
    {
        VectorTest charPosition = new VectorTest(character.getPositionX(),character.getPositionZ());
        //System.out.println("Character's position is currently (" + charPosition.getX() + "," + charPosition.getZ() + ")");
        double currentParam = path.getParam(path,charPosition);
        double targetParam = currentParam + character.getOffset();
        //System.out.println("Entering target position at" + targetParam + " given current at " + currentParam + "and offset at " + character.getOffset());
        VectorTest targetPosition = path.getPosition(path, targetParam);
        DynamicSeek runner = new DynamicSeek();
        //System.out.println("We are sending a character from position (" + charPosition.getX() + "," + charPosition.getZ() + ") to (" + targetPosition.getX() + "," + targetPosition.getZ() + ")");
        return runner.output(charPosition,targetPosition,character.getMaxAcceleration());
    }
    
    //Function for updating the character's variables. Uses a SteeringOutput class which contains the accelerations, the character itself, and the time (deltaTime stamp)
    public static void update(SteeringOutput accelerations, PathFollowing character, double time)
    {
        //Update Positions and Orientation
        character.positionX += character.velocityX * time;
        character.positionZ += character.velocityZ * time;
        character.orientation += character.rotation * time;
        
        //Update accelerations and then velocities, then rotation. Done in this order since we start at time stamp 0.5 instead of 0
        character.linearX = accelerations.getLinearX();
        character.linearZ = accelerations.getLinearZ();
        character.velocityX += character.linearX * time;
        character.velocityZ += character.linearZ * time;
        character.rotation += accelerations.getAngular() * time;

        //Check for velocity above maxSpeed and clip
        if(length(character.velocityX,character.velocityZ) > character.maxSpeed)
        {
            
            if(length(character.velocityX,character.velocityZ) != 0)
            {
                //Clipping using a lengthDivisor variable so that we have a nice static variable that doesn't change mid operation.
                double lengthDivisor = length(character.velocityX,character.velocityZ);
                character.velocityX /= lengthDivisor;
                character.velocityZ /= lengthDivisor;
               
                character.velocityX *= character.maxSpeed;
                character.velocityZ *= character.maxSpeed;
            }
            else
            {
                character.velocityX = 0;
                character.velocityZ = 0;
            }
        }
    }
    
    //Function for printing character's values to a text file. Name is given through main function call.
    public static void printToFile (PathFollowing character, double time, String name, boolean newFile)
    {
        String fileOutput = time + "," + character.id + "," + character.positionX + "," + character.positionZ + "," + character.velocityX + "," + character.velocityZ + "," + character.linearX + "," + character.linearZ + "," + character.orientation + "," + 8 + "\n";
        String fileName = name;
        try {
            FileWriter writer = new FileWriter(fileName, newFile);
            writer.write(fileOutput);
            writer.flush();
        } catch (Exception e) {}
    }
    
    public static void main(String[] args)
    {
        //Newfile booleans to dictate whether or not to erase text file on run. Used to clean text files on every fresh run but still allows program to append data to file.
        boolean newfile1 = false;
        boolean newfile2 = false;
        
        //Create characters for the program. Namely characters with id 71, 72, 81, and 82 respectively
        PathFollowing first = new PathFollowing(71,-90,60,1.5,1);
        PathFollowing second = new PathFollowing(72,75,-10,2,1);
        PathFollowing third = new PathFollowing(81,-90,60,3,1);
        PathFollowing fourth = new PathFollowing(82,75,-10,3,1);
        
        //Create the two paths
        Path one = new Path(-80,-40,40,80,40,70,10,40);
        Path two = new Path(70,25,45,-55,-35,-80,-25,-20,-80,-80,-20,-25);
        
        double deltaTime = 0.5;
        
        //Give the name to the writeout files
        String name1 = "test70.txt";
        String name2 = "test80.txt";
        
        //Print the initial values to the text files
        printToFile(first,0,name1,newfile1);
        newfile1 = true;
        printToFile(second,0,name1,newfile1);
        printToFile(third,0,name2,newfile2);
        newfile2 = true;
        printToFile(fourth,0,name2,newfile2);
        
        //Loop through the program for 100 seconds
        for(double time = 0.5; time <= 100; time += deltaTime)
        {
            //Find the target for the first character (71) and update, then push to text file
            SteeringOutput data = new SteeringOutput();
            data = followPath(first,one);
            update(data,first,deltaTime);
            printToFile(first,time,name1,newfile1);
            
            //Find the target for the second character (72) and update, then push to text file
            data = followPath(second,two);
            update(data,second,deltaTime);
            printToFile(second,time,name1,newfile1);
            
            //Find the target for the third character (81) and update, then push to text file
            data = followPath(third,one);
            update(data,third,deltaTime);
            printToFile(third,time,name2,newfile2);
            
            //Find the target for the fourth character (82) and update, then push to text file
            data = followPath(fourth,two);
            update(data,fourth,deltaTime);
            printToFile(fourth,time,name2,newfile2);
        }
        
    }
    
}