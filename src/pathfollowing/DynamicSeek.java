/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfollowing;

import pathfollowing.SteeringOutput;

/**
 *
 * @author quanrocks11
 */

//Class used to pass variables from a Path in order to update accelerations.
public class DynamicSeek
{
    private double characterX;
    private double characterZ;
    private double characterOrientation;
    private double targetX;
    private double targetZ;
    private double targetOrientation;
    double maxAcceleration;
    
    //Default Constructor
    public DynamicSeek()
    {
        characterX = 0;
        characterZ = 0;
        characterOrientation = 0;
        targetX = 0;
        targetZ = 0;
        targetOrientation = 0;
        maxAcceleration = 0;
    }
    
    //Utillizes a SteeringOutput to update and return accelerations
    public SteeringOutput output(VectorTest character, VectorTest target, double maxA)
    {
        SteeringOutput result = new SteeringOutput();
        result.setParams(character.getX(),character.getZ(),target.getX(),target.getZ(),maxA);
        return result;
        
    }
}
