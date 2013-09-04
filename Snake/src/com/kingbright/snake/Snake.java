package com.kingbright.snake;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Canvas;
import android.os.Bundle;

public class Snake {
	public static final String SNAKE = "snake";
	public static final String COLUMN = "column";
	public static final String ROW = "row";

	public static final int OFFSET = 1;
	public static final int WIDTH = 20;

	// Directions
	public static final int LEFT = 0;
	public static final int RIGHT = 3;
	public static final int UP = 1;
	public static final int DOWN = 2;

	private Node mHead;
	private Node mTail;

	private final int mColumn;
	private final int mRow;

	public Snake(int x, int y) {
		mColumn = x;
		mRow = y;
		mHead = mTail = new Node(mColumn / 2, mRow / 2);
		setRandomDir();
	}

	public Snake(int x, int y, int color) {
		mColumn = x;
		mRow = y;
		mHead = mTail = new Node(mColumn / 2, mRow / 2);
		mHead.setColor(color);
		setRandomDir();
	}

	public Snake(Bundle save) {
		mColumn = save.getInt(COLUMN);
		mRow = save.getInt(ROW);
		ArrayList<Node> list = save.getParcelableArrayList(SNAKE);
		for (Node node : list) {
			if (mHead == null) {
				mHead = mTail = node;
			} else {
				mTail.next = node;
				node.pre = mTail;
				mTail = node;
			}
		}
	}

	public void setRandomDir() {
		Random r = new Random();
		int dir = r.nextInt(4);
		while (dir + mHead.dir == 3) {
			dir = r.nextInt(4);
		}
		mHead.dir = mHead.nextDir = dir;
	}

	public void setDir(int dir) {
		if (dir + mHead.dir == 3) {
			return;
		}
		mHead.dir = mHead.nextDir = dir;
	}

	public Node gain() {
		int x = mTail.x;
		int y = mTail.y;
		switch (mTail.dir) {
		case LEFT:
			x++;
			break;
		case RIGHT:
			x--;
			break;
		case UP:
			y++;
			break;
		case DOWN:
			y--;
			break;
		}

		Node node = new Node(x, y);
		mTail.next = node;
		node.pre = mTail;
		node.dir = mTail.dir;
		mTail = node;
		return node;
	}

	public Node getHead() {
		return mHead;
	}

	public Node getTail() {
		return mTail;
	}

	public void gain(int color) {
		Node node = gain();
		node.setColor(color);
	}

	public void onDraw(Canvas canvas) {
		canvas.drawText("(" + mHead.x + "," + mHead.y + ")", 0, 20, mHead.paint);
		Node node = mHead;
		while (node != null) {
			canvas.drawRoundRect(node.rect, 2, 2, node.paint);
			node = node.next;
		}
	}

	public void move() {
		synchronized (this) {
			Node node = mHead;
			while (node != null) {
				// direction change
				node.updateDirection();

				// position change
				switch (node.dir) {
				case LEFT:
					node.x--;
					if (node.x < 0) {
						node.x = mColumn + node.x;
					}
					node.rect.left -= WIDTH;
					node.rect.right -= WIDTH;
					if (node.rect.left < 0) {
						node.rect.left += WIDTH * mColumn;
						node.rect.right += WIDTH * mColumn;
					}
					break;
				case RIGHT:
					node.x++;
					if (node.x >= mColumn) {
						node.x = node.x - mColumn;
					}
					node.rect.left += WIDTH;
					node.rect.right += WIDTH;
					if (node.rect.right > WIDTH * mColumn) {
						node.rect.left -= WIDTH * mColumn;
						node.rect.right -= WIDTH * mColumn;
					}
					break;
				case UP:
					node.y--;
					if (node.y < 0) {
						node.y = mRow + node.y;
					}
					node.rect.top -= WIDTH;
					node.rect.bottom -= WIDTH;
					if (node.rect.top < 0) {
						node.rect.top += WIDTH * mRow;
						node.rect.bottom += WIDTH * mRow;
					}
					break;
				case DOWN:
					node.y++;
					if (node.y >= mRow) {
						node.y = node.y - mRow;
					}
					node.rect.top += WIDTH;
					node.rect.bottom += WIDTH;
					if (node.rect.bottom > WIDTH * mRow) {
						node.rect.top -= WIDTH * mRow;
						node.rect.bottom -= WIDTH * mRow;
					}
					break;
				}

				node = node.next;
			}
		}
	}

	public void save(Bundle save) {
		save.putInt(COLUMN, mColumn);
		save.putInt(ROW, mRow);
		ArrayList<Node> list = new ArrayList<Node>();
		Node node = mHead;
		while (node != null) {
			list.add(node);
			node = node.next;
		}
		save.putParcelableArrayList(Snake.SNAKE, list);
	}

}
