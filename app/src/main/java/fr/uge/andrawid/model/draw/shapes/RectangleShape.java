package fr.uge.andrawid.model.draw.shapes;

import android.graphics.Canvas;

import java.util.Objects;

import fr.uge.andrawid.model.draw.model.AbstractDrawableShape;
import fr.uge.andrawid.model.draw.model.ShapeKind;
import fr.uge.andrawid.model.draw.model.ShapeProperties;

public class RectangleShape extends AbstractDrawableShape {

    public RectangleShape(float[] coordinates) {
        super(coordinates);
        if (Objects.requireNonNull(coordinates).length != 4)
            throw new IllegalArgumentException("Coordinates length of a square must be 4!");
    }

    @Override
    public void drawShape(ShapeProperties properties, Canvas canvas) {
        Objects.requireNonNull(properties);
        Objects.requireNonNull(canvas);

        canvas.drawRect( properties.getOriginX() + coordinates[0], properties.getOriginY() + coordinates[1],properties.getOriginX() + coordinates[2], properties.getOriginY() + coordinates[3], properties.getPaint());
    }

    @Override
    protected ShapeKind getShapeKind() {
        return ShapeKind.RECTANGLE;
    }
}
