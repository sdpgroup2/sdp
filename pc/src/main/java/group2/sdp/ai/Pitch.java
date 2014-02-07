package group2.sdp.ai;

public class Pitch implements IPitch
{

    private static Pitch instance = null;
    private Ball ball;
    private Team ally, enemy;
    
    private Pitch()
    { }
    
    public IPitch getInstance()
    {
        if (instance == null)
        { instance = new Pitch(); }
        
        return (IPitch) instance;
    }

    @Override
    public void setOrigin(Point p)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setZone(String id, PointSet ps)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateBallPosition(Point p)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateRobotPosition(String id, Point p)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getBallZone()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAllyColor()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getEnemyColor()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
