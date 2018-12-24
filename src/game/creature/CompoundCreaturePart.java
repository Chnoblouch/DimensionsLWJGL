package game.creature;

import game.GameObject;

public class CompoundCreaturePart 
extends Creature {
	
	private Creature creature;
	
	public void setCompoundCreature(Creature creature)
	{
		this.creature = creature;
	}
	
	public void setHealth(float h)
	{
		creature.setHealth(h);;
	}
	
	public void setMaxHealth(float mx)
	{
		creature.setMaxHealth(mx);
	}
	
	public float getMaxHealth()
	{
		return creature.getMaxHealth();
	}
	
	public void heal(float h)
	{
		creature.heal(h);
	}
	
	public void damage(float h, GameObject attacker)
	{
		creature.damage(h, attacker);
	}
	
	public void instantDamage(float h)
	{
		creature.instantDamage(h);
	}
	
	public float getHealth()
	{
		return creature.getHealth();
	}
	
	public void respawn()
	{
		creature.respawn();
	}
	
	public boolean isDeath()
	{
		return creature.isDeath();
	}
	
	public void setInvulnerableTime(int time)
	{
		creature.setInvulnerableTime(time);
	}
	
	public int getInvulnerableTime()
	{
		return creature.getInvulnerableTime();
	}
		
	public void knockback(float angle, float speed)
	{
		creature.knockback(angle, speed);
	}
	
	public boolean isKnockback()
	{
		return creature.isKnockback();
	}
	
	public float getProtection() 
	{
		return creature.getProtection();
	}
	
	public void tameDragon(Dragon dragon)
	{
		creature.tameDragon(dragon);
	}
	
	public void rideOnDragon(Dragon dragon)
	{
		creature.rideOnDragon(dragon);
	}
	
	public boolean canLeaveDragon()
	{
		return creature.canLeaveDragon();
	}
	
	public void leaveDragon()
	{
		creature.leaveDragon();
	}
}
