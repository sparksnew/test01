package com.oristartech.cinema.utils;

interface A {
	public abstract void hehe();// 传统抽象方法

	public static void haha()// 静态方法
	{
		System.out.println("haha");
	}

	public default void lala() {
		System.out.println("lala");// 非静态要加关键字default
	}
}

public class StaticBlock implements A {
	
	public static void main(String[] args)// 因为不是抽象的所以可以重写也可以直接使用。
	{
		A.haha();// 不能被Test调用
		new StaticBlock().lala();// 非静态要创建实例使用
	}

	@Override
	public void hehe() {
		
	}

}