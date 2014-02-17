package sdp.group2.world;

import sdp.group2.geometry.Rect;

public class RectangularObjectAdapter extends StaticObjectAdapter {

	private double width;
	private double height;

	@Override
	public Rect getBoundingRect() {
		return new Rect(
			position.x - width / 2,
			position.y - height / 2,
			position.x + width / 2,
			position.y + height / 2
		);
	}

	public RectangularObjectAdapter(Rect boundingBox) {
		super(boundingBox);
	}
}
