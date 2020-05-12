package me.neon.redcash.utils;

public class TestHandler implements ITest {
	
	ITest i;
	
	@Override
	public int get() {
		return this.i.get();
	}
}
