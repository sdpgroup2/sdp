package group2.sdp.pc.world;

import group2.sdp.pc.geom.Rect;

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
