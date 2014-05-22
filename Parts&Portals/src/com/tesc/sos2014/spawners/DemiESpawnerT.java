package com.tesc.sos2014.spawners;

import org.andengine.engine.camera.Camera;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class DemiESpawnerT extends DemiESpawner
{
	public int time = 0;




	public DemiESpawnerT(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
		{
			super(pX, pY, vbo, camera, physicsWorld);
			
		}
	
	public void setTimer(int t)
	{
		this.time = t;
		
	}

}
