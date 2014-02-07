
/** @author Jaroslaw Hirniak */

package group2.sdp.ai;

import java.util.concurrent.ConcurrentHashMap;

public class World
{

    private static World world = null;
    private ConcurrentHashMap<String, Float> ally, enemy;
    // add main plane and zones variables here

    private World()
    {
        ally = new ConcurrentHashMap<String, Float>();
        enemy = new ConcurrentHashMap<String, Float>();
    }
    
    public World getSingleton()
    {
        if (world == null)
        { world = new World(); }
        
        return world;
    }
}