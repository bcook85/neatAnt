package util;

public class Vector {
	public float x, y;
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector(Vector v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public Vector add(Vector v) {
		return new Vector(this.x + v.x, this.y + v.y);
	}
	public Vector add(float n) {
		return new Vector(this.x + n, this.y + n);
	}
	
	public Vector sub(Vector v) {
		return new Vector(this.x - v.x, this.y - v.y);
	}
	public Vector sub(float n) {
		return new Vector(this.x - n, this.y - n);
	}
	
	public Vector rot(float angle) {
		return new Vector(
			(float)(this.x * Math.cos(angle)) - (float)(this.y * Math.sin(angle))
			,(float)(this.x * Math.sin(angle)) + (float)(this.y * Math.cos(angle))
		);
	}
	
	public Vector mul(Vector v) {
		return new Vector(this.x * v.x, this.y * v.y);
	}
	public Vector mul(float n) {
		return new Vector(this.x * n, this.y * n);
	}
	
	public Vector div(Vector v) {
		return new Vector(this.x / v.x, this.y / v.y);
	}
	public Vector div(float n) {
		return new Vector(this.x / n, this.y / n);
	}
	
	public Vector normalize() {
		float m = this.mag();
		if (m != 0) {
			return this.div(m);
		}
		return this;
	}
	
	public float mag() {
		return (float)Math.hypot(this.x,  this.y);
	}
	public float mag2() {
		return (this.x * this.x) + (this.y * this.y); 
	}
	
	public float getDist(Vector v) {
		return (float)Math.hypot(v.x - this.x,  v.y - this.y);
	}
	
	public float getAngle(Vector v) {
		return (float)Math.atan2(v.y - this.y, v.x - this.x);
	}
	
	public static Vector fromAngle(float angle) {
		return new Vector((float)Math.cos(angle), (float)Math.sin(angle));
	}
}
