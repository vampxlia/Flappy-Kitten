package com.mygdx.game;

import GameLogic.GameHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameOverScreen implements Screen {
    private final FlappyKitty game;
    private final OrthographicCamera camera;
    private final int score;
    private final int highScore;
    private final int backgroundCoord;
    private final GameHandler handler;
    private final Stage stage;
    public GameOverScreen(FlappyKitty game, int score, int highScore, int backgroundCoord, GameHandler handler) {
        this.game = game;
        this.score = score;
        this.highScore = highScore;
        this.backgroundCoord = backgroundCoord;
        this.handler = handler;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.screenX, game.screenY);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        ImageButton playButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("play_again_btn.png")))));
        playButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("play_again_btn.png"))));
        playButton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("play_again_btn_pressed.png"))));
        playButton.setSize(700,350);
        playButton.setPosition((float) Gdx.graphics.getWidth()/2-350, 120);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.btnSound.play();
                return true;
            }
        });
        stage.addActor(playButton);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        handler.postGame();

        game.batch.begin();
        game.batch.draw(game.background,backgroundCoord,0);
        handler.getObsHandler().renderSprites(game.batch);
        handler.getPlayer().renderSprite(game.batch);
        game.font.getData().setScale(2);
        game.batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        game.shape.begin(ShapeRenderer.ShapeType.Filled);
        game.shape.setColor(new Color(0, 0, 0, 0.5f));
        game.shape.rect(0, 0, game.screenX, game.screenY);
        game.shape.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();
        game.glyphLayout.setText(game.font,"GAME OVER!");
        game.font.draw(game.batch, game.glyphLayout, (game.screenX - game.glyphLayout.width)/2, game.screenY+10);
        if (score <= highScore) {
            game.font.getData().setScale(0.7f);
            game.glyphLayout.setText(game.font, "SCORE:");
            game.font.draw(game.batch, game.glyphLayout, (game.screenX - game.glyphLayout.width) / 3, game.screenY - 40);
            game.glyphLayout.setText(game.font, "BEST:");
            game.font.draw(game.batch, game.glyphLayout, ((game.screenX - game.glyphLayout.width) / 3)*2, game.screenY - 40);
            game.font.getData().setScale(1f);
            game.glyphLayout.setText(game.font, String.valueOf(score));
            game.font.draw(game.batch, game.glyphLayout, (game.screenX - game.glyphLayout.width) / 3, game.screenY - 50);
            game.glyphLayout.setText(game.font, String.valueOf(highScore));
            game.font.draw(game.batch, game.glyphLayout, ((game.screenX - game.glyphLayout.width) / 3)*2, game.screenY - 50);
        }
        else{
            game.font.getData().setScale(0.7f);
            game.glyphLayout.setText(game.font, "NEW HIGH SCORE!");
            game.font.draw(game.batch, game.glyphLayout, (game.screenX - game.glyphLayout.width) / 2, game.screenY - 40);
            game.font.getData().setScale(1f);
            game.glyphLayout.setText(game.font, String.valueOf(score));
            game.font.draw(game.batch, game.glyphLayout, (game.screenX - game.glyphLayout.width) / 2, game.screenY - 50);
        }
        game.batch.end();

        stage.act();
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
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
        handler.dispose();
        stage.dispose();
    }
}
