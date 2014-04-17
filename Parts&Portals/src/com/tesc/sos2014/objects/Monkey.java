package com.tesc.sos2014.objects;

import org.andengine.engine.camera.Camera;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Monkey extends BaseEnemy {

	public Monkey(float pX, float pY, VertexBufferObjectManager vbo,
			Camera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, vbo, camera, physicsWorld);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDie() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void runRight() {
		// TODO Auto-generated method stub
		super.runRight();
	}

	@Override
	public void animateMe() {
		// TODO Auto-generated method stub
		super.animateMe();
	}

	@Override
	public void runLeft() {
		// TODO Auto-generated method stub
		super.runLeft();
	}

	@Override
	public void jump() {
		// TODO Auto-generated method stub
		super.jump();
	}

	@Override
	public void increaseFootContacts() {
		// TODO Auto-generated method stub
		super.increaseFootContacts();
	}

	@Override
	public void decreaseFootContacts() {
		// TODO Auto-generated method stub
		super.decreaseFootContacts();
	}

	

}
