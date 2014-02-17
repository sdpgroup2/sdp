package sdp.group2.world;

import sdp.group2.geometry.Rect;

public class CircularObjectAdapter extends StaticObjectAdapter {

	private final double radius = 0;

	@Override
	public Rect getBoundingRect() {
		return new Rect(position.x - radius, position.y - radius, position.x + radius, position.y+radius);
	}

	public CircularObjectAdapter(Rect boundingBox) {
		super(boundingBox);
	}
}
