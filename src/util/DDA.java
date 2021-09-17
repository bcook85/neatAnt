package util;

public class DDA {
	public static Vector getCollisionVector(Vector origin, Vector vel, double maxDistance, int[][] grid) {
		Vector rayNorm = vel.normalize();
		Vector rayUnitStepSize = new Vector(
			(float)Math.sqrt(1 + ((rayNorm.y / rayNorm.x) * (rayNorm.x / rayNorm.x)))
			,(float)Math.sqrt(1 + ((rayNorm.x / rayNorm.y) * (rayNorm.x / rayNorm.y)))
		);
		Vector mapCheck = new Vector(
			(float)Math.floor(origin.x)
			,(float)Math.floor(origin.y)
		);
		Vector rayLength = new Vector(0.0f, 0.0f);
		Vector step = new Vector(0.0f, 0.0f);
		// Starting condition check, prime ray length
		if (rayNorm.x < 0) {
			step.x = -1;
			rayLength.x = (origin.x - mapCheck.x) * rayUnitStepSize.x;
		} else {
			step.x = 1;
			rayLength.x = (mapCheck.x + 1 - origin.x) * rayUnitStepSize.x;
		}
		if (rayNorm.y < 0) {
			step.y = -1;
			rayLength.y = (origin.y - mapCheck.y) * rayUnitStepSize.y;
		} else {
			step.y = 1;
			rayLength.y = (mapCheck.y + 1 - origin.y) * rayUnitStepSize.y;
		}
		boolean tileFound = false;
		double dist = 0.0;
		while (!tileFound && dist < maxDistance) {
			if (rayLength.x < rayLength.y) {
				mapCheck.x += step.x;
				dist = rayLength.x;
				rayLength.x += rayUnitStepSize.x;
			} else {
				mapCheck.y += step.y;
				dist = rayLength.y;
				rayLength.y += rayUnitStepSize.y;
			}
			if (mapCheck.x < 0 || mapCheck.y < 0
				|| mapCheck.x >= grid.length || mapCheck.y >= grid[(int)mapCheck.x].length
				|| grid[(int)mapCheck.x][(int)mapCheck.y] != 0) {
				tileFound = true;
			}
		}
		if (tileFound && dist < maxDistance) {
			return origin.add(rayNorm.mul((float)dist));
		}
		return origin.add(rayNorm.mul((float)maxDistance));
	}

	public static double getCollisionDistance(Vector origin, Vector vel, double maxDistance, int[][] grid) {
		Vector rayNorm = vel.normalize();
		Vector rayUnitStepSize = new Vector(
			(float)Math.sqrt(1 + ((rayNorm.y / rayNorm.x) * (rayNorm.x / rayNorm.x)))
			,(float)Math.sqrt(1 + ((rayNorm.x / rayNorm.y) * (rayNorm.x / rayNorm.y)))
		);
		Vector mapCheck = new Vector(
			(float)Math.floor(origin.x)
			,(float)Math.floor(origin.y)
		);
		Vector rayLength = new Vector(0.0f, 0.0f);
		Vector step = new Vector(0.0f, 0.0f);
		// Starting condition check, prime ray length
		if (rayNorm.x < 0) {
			step.x = -1;
			rayLength.x = (origin.x - mapCheck.x) * rayUnitStepSize.x;
		} else {
			step.x = 1;
			rayLength.x = (mapCheck.x + 1 - origin.x) * rayUnitStepSize.x;
		}
		if (rayNorm.y < 0) {
			step.y = -1;
			rayLength.y = (origin.y - mapCheck.y) * rayUnitStepSize.y;
		} else {
			step.y = 1;
			rayLength.y = (mapCheck.y + 1 - origin.y) * rayUnitStepSize.y;
		}
		boolean tileFound = false;
		double dist = 0.0;
		while (!tileFound && dist < maxDistance) {
			if (rayLength.x < rayLength.y) {
				mapCheck.x += step.x;
				dist = rayLength.x;
				rayLength.x += rayUnitStepSize.x;
			} else {
				mapCheck.y += step.y;
				dist = rayLength.y;
				rayLength.y += rayUnitStepSize.y;
			}
			if (mapCheck.x < 0 || mapCheck.y < 0
				|| mapCheck.x >= grid.length || mapCheck.y >= grid[(int)mapCheck.x].length
				|| grid[(int)mapCheck.x][(int)mapCheck.y] != 0) {
				tileFound = true;
			}
		}
		if (tileFound && dist < maxDistance) {
			return dist;
		}
		return maxDistance;
	}
}