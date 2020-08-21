/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfollowing;

import pathfollowing.VectorTest;
/**
 *
 * @author quanrocks11
 */

//Class for the Paths the the characters will follow
public class Path extends DynamicSeek
{
    //Contains arrays for the coordinates, the coordinates as points, the distances, and the parameter values
    private double xCoords[];
    private double zCoords[];
    private VectorTest points[];
    private double distances[];
    private double parameters[];
    
    //Default constructor
    public Path()
    {
        xCoords = new double[4];
        zCoords = new double[4];
        points = new VectorTest[4];
        distances = new double[5];
        parameters = new double[5];
    }
    
    //Constructor given 4 pairs of points, 8 points total
    public Path( double x1, double x2, double x3, double x4, double z1, double z2, double z3, double z4)
    {
        xCoords = new double[]{x1,x2,x3,x4};
        zCoords = new double[]{z1,z2,z3,z4};
        
        //Populating the points array and instantiating the distances and paramters based on the number of segments.
        int arraySize = xCoords.length;
        int segments = arraySize - 1;
        int tracker = 0;
        points = new VectorTest[arraySize];
        distances = new double[segments + 1];
        parameters = new double[segments + 1];
        
        for(int i = 0; i < xCoords.length;i++)
        {
            VectorTest data = new VectorTest(xCoords[i],zCoords[i]);
            points[tracker] = data;
            tracker++;
        }
        
        //Use of tracker variable since distances starts at i = 1 instead of i = 0
        tracker = 1;
        distances[0] = 0;
        
        //Loop through and populate distances by using the points of the line segment
        for(int i = 0; i < points.length - 1; i++)
        {
            distances[tracker] = distances[tracker-1] + distance(points[i],points[i+1]);
            tracker++;
        }

        //Loop through and populate the parameters by using the distance values
        for(int i = 0; i < parameters.length;i++)
        {
            parameters[i] = distances[i] / distances[distances.length - 1];
        }
    }
    
    //Constructor given 6 pairs of points, 12 points total.
    public Path( double x1, double x2, double x3, double x4, double x5, double x6, double z1, double z2, double z3, double z4, double z5, double z6)
    {
        xCoords = new double[]{x1,x2,x3,x4,x5,x6};
        zCoords = new double[]{z1,z2,z3,z4,z5,z6};
        
        int arraySize = xCoords.length;
        int segments = arraySize - 1;
        int tracker = 0;
        points = new VectorTest[arraySize];
        distances = new double[segments + 1];
        parameters = new double[segments + 1];
        
        for(int i = 0; i < xCoords.length;i++)
        {
            VectorTest data = new VectorTest(xCoords[i],zCoords[i]);
            points[tracker] = data;
            tracker++;
        }
        
        tracker = 1;
        distances[0] = 0;
        for(int i = 0; i < points.length - 1; i++)
        {
            distances[tracker] = distances[tracker-1] + distance(points[i],points[i+1]);
            tracker++;
        }
        
        for(int i = 0; i < parameters.length;i++)
        {
            parameters[i] = distances[i] / distances[distances.length - 1];
        }
    }
    
    //Function for finding the length of two coordinates.
    //Returns the result as a double
    public double length(double x, double z)
    {
        return Math.sqrt(x * x + z * z);
    }
    
    //Finds the product of two vectors
    //Returns the result as a double
    public double dotProduct(VectorTest one, VectorTest two)
    {
        double result = one.getX() * two.getX() + one.getZ() * two.getZ();
        return result;
    }
    
    //Finds the distance between two vectors
    //Returns the result as a double
    public double distance (VectorTest one, VectorTest two)
    {
        double result = Math.sqrt(Math.pow((two.getX() - one.getX()), 2) + Math.pow((two.getZ()-one.getZ()),2));
        return result;
    }
    
    //Finds the closest point on a line given a position and returns the result as a vector (X,Z)
    public VectorTest closestPointLine(VectorTest query, VectorTest A, VectorTest B)
    {
        VectorTest temp1 = new VectorTest(query.getX()-A.getX(),query.getZ()-A.getZ());
        VectorTest temp2 = new VectorTest(B.getX()-A.getX(),B.getZ()-A.getZ());
        double result = dotProduct(temp1,temp2) / dotProduct(temp2,temp2);
        temp1.setX(result * temp2.getX() + A.getX());
        temp1.setZ(result*temp2.getZ() + A.getZ());
        //System.out.println("Temp 1's X and Z are " + temp1.getX() + " " + temp1.getZ());
        return temp1;
    }
    
    //Finds the closest point on a segment given a position and returns the result as a vector (X,Z)
    public VectorTest closestPointSegment(VectorTest query, VectorTest A, VectorTest B)
    {
        VectorTest temp1 = new VectorTest(query.getX()-A.getX(),query.getZ()-A.getZ());
        VectorTest temp2 = new VectorTest(B.getX()-A.getX(),B.getZ()-A.getZ());
        double result = dotProduct(temp1,temp2) / dotProduct(temp2,temp2);

        if(result <= 0)
        {
            return A;
        }
        else if (result >= 1)
        {
            return B;
        }
        else
        {
            temp1.setX(result * temp2.getX() + A.getX());
            temp1.setZ(result*temp2.getZ() + A.getZ());
            return temp1;
        }
    }
    
    //Finds the position on a path given the path and a parameter.
    //Returns position as a vector
    public VectorTest getPosition(Path path, double param)
    {
        VectorTest position = new VectorTest();
        int marker = 0;
        for(int i = 0;i < path.parameters.length - 1; i++)
        {
            if(param > path.parameters[i])
            {
                marker = i;
            }
        }
        double tempParam = (param - path.parameters[marker]) / (path.parameters[marker+1] - path.parameters[marker]);
        VectorTest A = new VectorTest(path.points[marker].getX(),path.points[marker].getZ());
        VectorTest B = new VectorTest(path.points[marker+1].getX(),path.points[marker+1].getZ());
        position.setX((tempParam * (B.getX() - A.getX()))+A.getX());
        position.setZ((tempParam * (B.getZ() - A.getZ()))+A.getZ());
        return position;   
    }
    
    //Gets a param given the path and a position
    //Returns the paramater as a double
    public double getParam(Path path, VectorTest position)
    {
        double closestDistance = 99999;
        VectorTest closestPoint = new VectorTest();
        int closestSegment = 0;
        for(int i = 0; i < path.points.length - 1; i++)
        {
            VectorTest A = new VectorTest(path.points[i].getX(),path.points[i].getZ());
            VectorTest B = new VectorTest(path.points[i+1].getX(),path.points[i+1].getZ());
            VectorTest checkPoint = closestPointSegment(position,A,B);
            double checkDistance = distance(position,checkPoint);
            if(checkDistance < closestDistance)
            {
                closestPoint.setX(checkPoint.getX());
                closestPoint.setZ(checkPoint.getZ());
                closestDistance = checkDistance;
                closestSegment = i;
            }
        }
        VectorTest A = new VectorTest(path.xCoords[closestSegment],path.zCoords[closestSegment]);
        VectorTest B = new VectorTest(path.xCoords[closestSegment + 1],path.zCoords[closestSegment + 1]);
        double aParam = path.parameters[closestSegment];
        double bParam = path.parameters[closestSegment + 1];

        double result = length(closestPoint.getX() - A.getX(),closestPoint.getZ() - A.getZ()) / length(B.getX() - A.getX(), B.getZ() - A.getZ());
        double finalAnswer = aParam + (result * (bParam - aParam));

        return finalAnswer;
    }
}
