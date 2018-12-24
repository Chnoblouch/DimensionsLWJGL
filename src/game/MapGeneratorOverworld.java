package game;

import java.util.ArrayList;
import java.util.Random;

import game.environment.GrassBlock;
import game.environment.GrassGround;
import game.levels.Level;
import utils.Block;

public class MapGeneratorOverworld {
	
	public static Map generate()
	{
		Map map = new Map();
		
		for(float x = 0; x < Level.CENTER * 2; x += Block.SIZE)
		{
			for(float y = 0; y < Level.CENTER * 2; y += Block.SIZE)
			{
				GrassGround ground = new GrassGround();
				ground.setPosition(x, y);
				map.addObject(ground);
			}
		}
		
		for(float x = 0; x < Level.CENTER * 2; x += (10 + new Random().nextInt(11)) * Block.SIZE)
		{
			for(float y = 0; y < Level.CENTER * 2; y += (10 + new Random().nextInt(11)) * Block.SIZE)
			{
				generateHill(map, x, y);
			}
		}
		
		return map;
	}

	private static void generateHill(Map map, float x, float y)
	{		
		int height = 2 + new Random().nextInt(4);
		int length = 7 + new Random().nextInt(4);
		int depth = 3 + new Random().nextInt(3);
		
		generateHillWall(map, x, y, height, length);
		generateHillTop(map, x, y, height, length, depth);
	}
	
	private static void generateHillWall(Map map, float x, float y, float height, float length)
	{
		ArrayList<BlockSample> wallSamples = new ArrayList<>();
		
		for(int bx = 0; bx <= length; bx++)
		{
			int shift = -1 + new Random().nextInt(2);
			if(x == 0 || x == length || x == 1 || x == length - 1) shift = 0;
			shift = 0;
			
			float partHeight = height;
			if(bx == 0 || bx == length) partHeight = height + 1;
			
			for(int by = 0; by <= partHeight; by++)
			{				
				BlockSample sample = new BlockSample();
				sample.x = x - length * Block.SIZE / 2 + bx * Block.SIZE;
				sample.y = y - by * Block.SIZE + shift * Block.SIZE;
				sample.data = 1;	
				
				wallSamples.add(sample);
				
				if(bx == 0) sample.left = true;
				if(bx == length) sample.right = true;
				if(by == 0) sample.bottom = true;
				if(by == partHeight) sample.top = true;
			}
		}
		
		for(int i = 0; i < wallSamples.size(); i++)
		{
			BlockSample s = wallSamples.get(i);
			
			if(s.right)
			{
				if(s.bottom) s.data = 15;
				else if(s.top) s.data = 20;
				else s.data = 19;
			} else if(s.left)
			{
				if(s.bottom) s.data = 9;
				else if(s.top) s.data = 14;
				else s.data = 13;
			} else if(s.bottom)
			{
				s.data = 0;
			} else if(s.top)
			{
				if(s.right) s.data = 17;
				else if(s.left) s.data = 11;
				else s.data = 2;
			}
			
			GrassBlock block = new GrassBlock();
			block.setPosition(s.x, s.y);
			block.readData(s.data);
			map.addObject(block);
		}
	}
	
	private static void generateHillTop(Map map, float x, float y, float height, float length, float depth)
	{
		for(int bx = 1; bx < length; bx++)
		{
			GrassBlock block = new GrassBlock();
			block.setPosition(x - length * Block.SIZE / 2 + bx * Block.SIZE, y - height * Block.SIZE - depth * Block.SIZE);
			block.readData(21);
			map.addObject(block);
		}
		
		GrassBlock leftCorner = new GrassBlock();
		leftCorner.setPosition(x - length * Block.SIZE / 2, y - height * Block.SIZE - depth * Block.SIZE);
		leftCorner.readData(24);
		map.addObject(leftCorner);
		
		GrassBlock rightCorner = new GrassBlock();
		rightCorner.setPosition(x - length * Block.SIZE / 2 + length * Block.SIZE, y - height * Block.SIZE - depth * Block.SIZE);
		rightCorner.readData(25);
		map.addObject(rightCorner);
		
		for(int by = 1; by < depth; by++)
		{
			GrassBlock left = new GrassBlock();
			left.setPosition(x - length * Block.SIZE / 2, y - height * Block.SIZE - by * Block.SIZE);
			left.readData(by == 1 ? 14 : 22);
			map.addObject(left);
			
			GrassBlock right = new GrassBlock();
			right.setPosition(x - length * Block.SIZE / 2 + length * Block.SIZE, y - height * Block.SIZE - by * Block.SIZE);
			right.readData(by == 1 ? 20 : 23);
			map.addObject(right);
		}
	}
}

class BlockSample
{
	public float x, y;
	public int data;
	public boolean right, left, top, bottom;
}
