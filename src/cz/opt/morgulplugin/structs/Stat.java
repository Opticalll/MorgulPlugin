package cz.opt.morgulplugin.structs;

public class Stat
{
	private int xp;
	private int level;
	
	public Stat(int defXP, int defLevel)
	{
		this.xp = defXP;
		this.level = defLevel;
	}
	
	public int getXP()
	{
		return xp;
	}
	
	public void setXP(int xp)
	{
		this.xp = xp;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}
}
