package com.kingbright.snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class FruitsManager {
	private int[] colors = new int[] { Color.RED, Color.BLUE, Color.CYAN,
			Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.WHITE };
	private List<Fruit> mList;
	private int max;

	private Random r;
	private int column;
	private int row;

	public FruitsManager(int column, int row) {
		this.column = column;
		this.row = row;
		mList = new ArrayList<Fruit>();
		r = new Random();
		max = 6;
	}

	public void setMax(int max) {
		this.max = max;
	}

	private int getColor() {
		return colors[r.nextInt(colors.length)];
	}

	private int getColorNot(int color) {
		int c = getColor();
		while (c == color) {
			c = getColor();
		}
		return c;
	}

	public Fruit onEatten(Snake snake) {
		Fruit fruit = null;
		for (Fruit f : mList) {
			if (snake.getHead().x() == f.x && snake.getHead().y() == f.y) {
				fruit = f;
				break;
			}
		}
		if (fruit != null) {
			snake.gain(fruit.color);
			mList.remove(fruit);
		}
		return fruit;
	}

	public void generateFruits(Snake snake) {
		while (mList.size() < max) {
			Node node = snake.getHead();
			int x = r.nextInt(column);
			int y = r.nextInt(row);
			while (node != null) {
				if (!isValid(x, y, node.x(), node.y())) {
					break;
				}
				node = node.next;
			}
			mList.add(new Fruit(x, y, getColorNot(snake.getTail().getColor())));
		}
	}

	private boolean isValid(int x1, int y1, int x2, int y2) {
		return !(x1 == x2 && y1 == y2);
	}

	public void onDraw(Canvas c) {
		for (Fruit f : mList) {
			f.onDraw(c);
			Log.e("position", "x:" + f.x + ",y:" + f.y);
		}
	}

	class Fruit {
		int r = 8;
		int x;
		int y;
		int color;
		Paint paint = new Paint();

		public Fruit(int x, int y, int color) {
			this.x = x;
			this.y = y;
			this.color = color;
			paint.setColor(color);
		}

		public void onDraw(Canvas canvas) {
			canvas.drawCircle((x + 1) * Snake.WIDTH - Snake.WIDTH / 2, (y + 1)
					* Snake.WIDTH - Snake.WIDTH / 2, r, paint);
		}
	}
}
