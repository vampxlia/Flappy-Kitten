package com.mygdx.game;

import GameLogic.GameHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    private final FlappyKitty game;
    private final OrthographicCamera camera;
    private final Music background_music;
    GameHandler handler;

    public GameScreen(FlappyKitty game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.screenX, game.screenY);

        handler = new GameHandler(game);
        handler.getPlayer().create();
        handler.getObsHandler().spawnObstacle();

        this.background_music = Gdx.audio.newMusic(Gdx.files.internal("mus_ingame.wav"));
    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        renderSprites();
        background_music.setLooping(true);
        background_music.play();
        handler.gameCycle(this);
        game.batch.end();
    }

    private void renderSprites(){
        ScreenUtils.clear(0, 0, 2f, 1);
        camera.update();
        game.shape.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        handler.renderBackground(game.background, game.batch);
        handler.getPlayer().renderSprite(game.batch);
        handler.getObsHandler().renderSprites(game.batch);
        game.font.getData().setScale(1);
        game.glyphLayout.setText(game.font,String.valueOf(handler.getScore()));
        game.font.draw(game.batch, game.glyphLayout, (game.screenX - game.glyphLayout.width)/2, game.screenY);
        game.font.getData().setScale(0.5F);
        game.glyphLayout.setText(game.font,String.valueOf(handler.getHighScore()));
        game.font.draw(game.batch, game.glyphLayout, (game.screenX - game.glyphLayout.width)/2, game.screenY - 30);

        //render hitboxes for debugging
        //handler.getPlayer().renderHitbox(game.shape);
        //handler.getObsHandler().renderHitboxes(game.shape);
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background_music.dispose();
    }
}
