package util;

public class Ball {
	private static int count = 0;
	public Vector pos, vel;
	public float radius;
	public boolean hasCollision;
	public int id;
	
	public Ball(float x, float y, float r) {
		pos = new Vector(x, y);
		vel = new Vector(0, 0);
		radius = r;
		hasCollision = true;
		id = ++count;
	}
	
	public boolean vsVector(Vector v) {
		return Math.abs(((v.x - this.pos.x) * (v.x - this.pos.x)) + ((v.y - this.pos.y) * (v.y - this.pos.y))) < this.radius * this.radius;
	}
	
	public boolean vsBall(Ball b) {
		if (b.hasCollision) {
			Vector diff = b.pos.sub(this.pos);
			if (Math.abs((diff.x * diff.x) + (diff.y * diff.y)) <= (this.radius + b.radius) * (this.radius + b.radius)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean vsCone(Vector startPos, double dir, double angle, double distance) {
		if (hasCollision) {
			double dist = startPos.getDist(pos);
			if (dist < distance + radius) {
				double dirToBall = startPos.getAngle(pos);
				if (dirToBall <= dir + (angle * 0.5) && dirToBall >= dir - (angle * 0.5)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean vsCircle(Vector pos, float radius) {
		Vector diff = pos.sub(this.pos);
		if (Math.abs((diff.x * diff.x) + (diff.y * diff.y))<= (this.radius + radius) * (this.radius + radius)) {
			return true;
		}
		return false;
	}
	
	static public boolean vsRay(Vector startPoint, Vector endPoint, Ball targetBall) {
		Vector v1 = endPoint.sub(startPoint);
		Vector v2 = targetBall.pos.sub(startPoint);
		float u = (v2.x * v1.x + v2.y * v1.y) / (v1.y * v1.y + v1.x * v1.x);
		float dist = 0;
		if (u >= 0 && u <= 1) {
			dist = (float)Math.pow(startPoint.x + v1.x * u - targetBall.pos.x, 2) + (float)Math.pow(startPoint.y + v1.y * u - targetBall.pos.y, 2);
		} else {
			dist = u < 0 ?
					(float)Math.pow(startPoint.x - targetBall.pos.x, 2) + (float)Math.pow(startPoint.y - targetBall.pos.y, 2) :
					(float)Math.pow(endPoint.x - targetBall.pos.x,  2) + (float)Math.pow(endPoint.y - targetBall.pos.y,  2);
		}
		return dist < targetBall.radius * targetBall.radius;
	}
	
	static public double vsRayGetDist(Vector startPoint, Vector endPoint, Ball targetBall) {
		Vector v1 = endPoint.sub(startPoint);
		Vector v2 = targetBall.pos.sub(startPoint);
		float u = (v2.x * v1.x + v2.y * v1.y) / (v1.y * v1.y + v1.x * v1.x);
		float dist = 0;
		if (u >= 0 && u <= 1) {
			dist = (float)Math.pow(startPoint.x + v1.x * u - targetBall.pos.x, 2) + (float)Math.pow(startPoint.y + v1.y * u - targetBall.pos.y, 2);
		} else {
			dist = u < 0 ?
					(float)Math.pow(startPoint.x - targetBall.pos.x, 2) + (float)Math.pow(startPoint.y - targetBall.pos.y, 2) :
					(float)Math.pow(endPoint.x - targetBall.pos.x,  2) + (float)Math.pow(endPoint.y - targetBall.pos.y,  2);
		}
		return Math.sqrt(dist);
	}
	
	static public void resolveBallsCollision(Ball b, Ball[] balls) {
		for (Ball otherBall : balls) {
			Vector potentialPosition = b.pos.add(b.vel);
			if (otherBall.id != b.id && otherBall.hasCollision && otherBall.vsCircle(potentialPosition,  b.radius)) {
				float distanceBetween = (float)Math.hypot(otherBall.pos.x - potentialPosition.x, otherBall.pos.y - potentialPosition.y);
				float overlap = distanceBetween - b.radius - otherBall.radius;
				potentialPosition = potentialPosition.sub(potentialPosition.sub(otherBall.pos).mul(overlap).div(distanceBetween));
				b.vel = Vector.fromAngle(b.pos.getAngle(potentialPosition)).normalize().mul(b.pos.getDist(potentialPosition));
			}
		}
	}
	
	static public void resolveBallCollision(Ball b1, Ball b2) {
		Vector potentialPosition = b1.pos.add(b1.vel);
		if (b2.id != b1.id && b2.hasCollision && b2.vsCircle(potentialPosition,  b1.radius)) {
			float distanceBetween = (float)Math.hypot(b2.pos.x - potentialPosition.x, b2.pos.y - potentialPosition.y);
			float overlap = distanceBetween - b1.radius - b2.radius;
			potentialPosition = potentialPosition.sub(potentialPosition.sub(b2.pos).mul(overlap).div(distanceBetween));
			b1.vel = Vector.fromAngle(b1.pos.getAngle(potentialPosition)).normalize().mul(b1.pos.getDist(potentialPosition));
		}
	}
	
	public static void resolveGridCollisions(Ball b, int grid[][]) {
		Vector potentialPosition = b.pos.add(b.vel);
		Vector currentCell = new Vector((float)Math.floor(b.pos.x), (float)Math.floor(b.pos.y));
		Vector targetCell = potentialPosition;
		Vector areaTL = new Vector((float)Math.floor(Math.min(currentCell.x, targetCell.x) - 1), (float)Math.floor(Math.min(currentCell.y, targetCell.y)) - 1);
		Vector areaBR = new Vector((float)Math.floor(Math.max(currentCell.x, targetCell.x) + 1), (float)Math.floor(Math.max(currentCell.y, targetCell.y)) + 1);
		Vector cell = new Vector(0, 0);
		for (cell.y = areaTL.y; cell.y <= areaBR.y; cell.y++) {
			for (cell.x = areaTL.x; cell.x <= areaBR.x; cell.x++) {
				if (cell.x < 0 || cell.y < 0 || cell.x >= grid.length || cell.y >= grid[0].length || grid[(int)cell.x][(int)cell.y] != 0) {
					potentialPosition = b.pos.add(b.vel);
					Vector near = new Vector(
						Math.max(cell.x, Math.min(potentialPosition.x, cell.x + 1))
						,Math.max(cell.y, Math.min(potentialPosition.y, cell.y + 1))
					);
					Vector rayToNear = near.sub(potentialPosition);
					if (rayToNear.x == 0 && rayToNear.y == 0) {
						potentialPosition = potentialPosition.sub(b.vel.normalize().mul(b.radius));
					} else {
						float overlap = b.radius - rayToNear.mag();
						if (!(overlap != overlap) && overlap > 0) {
							potentialPosition = potentialPosition.sub(rayToNear.normalize().mul(overlap));
						}
					}
					b.vel = Vector.fromAngle(b.pos.getAngle(potentialPosition)).normalize().mul(b.pos.getDist(potentialPosition));
				}
			}
		}
	}
}
