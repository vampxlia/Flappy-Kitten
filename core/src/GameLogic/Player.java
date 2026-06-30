package GameLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    private Rectangle player;
    private final String texturePath;
    private Texture texture;
    private Sound jumpSound;
    private final int initX;
    private final int initY;
    private final int width;
    private final int height;
    private float angle;
    public Player(String texturePath, int initX, int initY, int width, int height) {
        this.texturePath = texturePath;
        this.initX = initX;
        this.initY = initY;
        this.width = width;
        this.height = height;

        this.angle = 0;
        this.jumpSound = Gdx.audio.newSound(Gdx.files.internal("sfx_jump.wav"));

        new PlayerMovement(this);
    }

    public float getX() {
        return player.x;
    }

    public float getY() {
        return player.y;
    }

    public int getHeight() {
        return this.height;
    }

    public void create() {
        this.texture = new Texture(Gdx.files.internal(texturePath));
        player = new Rectangle();
        changeCoords(initX, initY);
        player.width = width;
        player.height = height;
    }

    public void renderHitbox(ShapeRenderer shape) {
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.RED);
        shape.rect(player.x, player.y, player.width, player.height);
        shape.end();
    }

    public void playJumpSound(){
        this.jumpSound.play(0.5f);
    }

    public void renderSprite(SpriteBatch batch){
        //playerSprite.setTexture(texture);
        //playerSprite.setPosition(player.x, player.y);
        //playerSprite.setRotation(angle);

        //playerSprite.draw(batch);
        //batch.draw(texture, player.x, player.y);

        batch.draw(texture, player.x, player.y,
         (float) width /2, (float) height /2, width, height,
        1,1, angle, 0, 0, width, height, false, false);
    }

    public void changeCoords(float x, float y) {
        player.x = x;
        player.y = y;
    }

    public void changeAngle(float angle){
        this.angle = angle;
    }

    public Rectangle getHitbox() {
        return player;
    }
    public void dispose(){
        this.texture.dispose();
        this.jumpSound.dispose();
    }

    public static class PlayerMovement {
        private static Player player = null;

        PlayerMovement(Player player) {
            PlayerMovement.player = player;
        }

        public static float changeVelocity(float minVelocity, float currentVelocity) {
            if (currentVelocity > minVelocity) currentVelocity -= 1;
            if (player.angle > -80 && currentVelocity==minVelocity) player.angle -= 4;
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && currentVelocity < 10) {
                player.playJumpSound();
                currentVelocity += 9;
                player.angle = 15;
            }
            return currentVelocity;
        }

        public static void movePlayer(float velocity) {
            player.changeCoords(player.getX(), player.getY() + velocity);
        }

        public static void warp(int screenY) {
            if (player.getY() > screenY) player.changeCoords(player.getX(), -player.getHeight());
            if (player.getY() < -player.getHeight()) player.changeCoords(player.getX(), screenY);
        }
    }
}
