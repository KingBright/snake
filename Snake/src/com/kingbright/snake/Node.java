
package com.kingbright.snake;

import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;

public class Node implements Parcelable {
    Node pre;
    Node next;
    int x, y;
    int dir = -1;
    int nextDir = -1;

    RectF rect;

    int lock;

    Paint paint = new Paint();

    public Node(int pX, int pY, int color) {
        create(pX, pY);
        paint.setColor(color);
    }

    public Node(int pX, int pY) {
        create(pX, pY);
    }

    public void create(int pX, int pY) {
        x = pX;
        y = pY;
        rect = new RectF(x * Snake.WIDTH + Snake.OFFSET, y * Snake.WIDTH + Snake.OFFSET,
                (x + 1) * Snake.WIDTH - Snake.OFFSET,
                (y + 1) * Snake.WIDTH - Snake.OFFSET);
    }

    public Node(Parcel source) {
        create(source.readInt(), source.readInt());

        dir = source.readInt();
        nextDir = source.readInt();
        paint.setColor(source.readInt());

    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void updateDirection() {
        if (nextDir != -1) {
            dir = nextDir;
            nextDir = -1;
        }
        if (pre != null && pre.dir != dir) {
            nextDir = pre.dir;
        }
    }

    public int getColor() {
        return paint.getColor();
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(x);
        dest.writeInt(y);
        dest.writeInt(dir);
        dest.writeInt(nextDir);
        dest.writeInt(paint.getColor());
    }

    public static final Parcelable.Creator<Node> CREATOR = new Parcelable.Creator<Node>() {
        @Override
        public Node createFromParcel(Parcel source) {
            return new Node(source);
        }

        @Override
        public Node[] newArray(int size) {
            return new Node[size];
        }
    };

}
