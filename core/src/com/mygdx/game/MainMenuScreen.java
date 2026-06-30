package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
    private final FlappyKitty game;
    private final OrthographicCamera camera;
    private final Stage stage;

    public MainMenuScreen(FlappyKitty game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.screenX, game.screenY);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        ImageButton playButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("play_button.png")))));
        playButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("play_button.png"))));
        playButton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("play_button_pressed.png"))));
        playButton.setSize(350,350);
        playButton.setPosition((float) Gdx.graphics.getWidth()/2-175, 150);
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

        game.batch.begin();
        game.batch.draw(game.background, 0, 0);
        game.font.getData().setScale(1);
        game.glyphLayout.setText(game.font,"WELCOME TO");
        game.font.draw(game.batch, game.glyphLayout, (game.screenX - game.glyphLayout.width)/2, game.screenY-10);
        game.font.getData().setScale(2);
        game.glyphLayout.setText(game.font,"FLAPPY KITTEN");
        game.font.draw(game.batch, game.glyphLayout, (game.screenX - game.glyphLayout.width)/2, game.screenY-10);
        game.batch.end();

        stage.act();
        stage.draw();

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
        stage.dispose();
    }
}
