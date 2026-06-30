package GameLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {
    private Rectangle topHalf;
    private Rectangle bottomHalf;

    private final int width;
    private final int initX;
    private final int holeSize;
    private final int holeCoordY;
    private Texture topTexture;
    private Texture bottomTexture;

    Obstacle(int width, int initX, int holeSize, int holeCoordY) {
        this.width = width;
        this.initX = initX;
        this.holeSize = holeSize;
        this.holeCoordY = holeCoordY;
    }

    public void create() {
        this.topTexture = new Texture(Gdx.files.internal("city_building_v3_up.png"));
        this.bottomTexture = new Texture(Gdx.files.internal("city_building_v3_down.png"));
        createBottomHalf();
        createTopHalf();
    }

    public float getX() {
        return topHalf.x;
    }

    private void createBottomHalf() {
        bottomHalf = new Rectangle();
        bottomHalf.x = initX;
        bottomHalf.y = 0;
        bottomHalf.width = width;
        bottomHalf.height = holeCoordY;
    }

    private void createTopHalf() {
        topHalf = new Rectangle();
        topHalf.x = initX;
        topHalf.y = holeCoordY + holeSize;
        topHalf.width = width;
        topHalf.height = Gdx.graphics.getHeight() - topHalf.y;
    }

    public void changeCoords(float x) {
        topHalf.x = x;
        bottomHalf.x = x;
    }

    public float getWidth() {
        return topHalf.width;
    }

    public void renderHitbox(ShapeRenderer shape) {
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.GREEN);
        shape.rect(topHalf.x, topHalf.y, topHalf.width, topHalf.height);
        shape.end();

        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.GREEN);
        shape.rect(bottomHalf.x, bottomHalf.y, bottomHalf.width, bottomHalf.height);
        shape.end();
    }

    public void renderSprite(SpriteBatch batch){
        batch.draw(topTexture, topHalf.x, topHalf.y);
        batch.draw(bottomTexture, bottomHalf.x, holeCoordY-80);
    }

    public Rectangle getBottomHitbox() {
        return bottomHalf;
    }

    public Rectangle getTopHitbox() {
        return topHalf;
    }

    public void dispose(){
        this.topTexture.dispose();
        this.bottomTexture.dispose();
    }
}
