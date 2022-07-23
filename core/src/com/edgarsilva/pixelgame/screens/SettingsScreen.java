package com.edgarsilva.pixelgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.edgarsilva.pixelgame.PixelGame;

public class SettingsScreen implements Screen{

    private PixelGame game;
    private Stage stage;

    private Table table;
    private Skin skin;

    private Label titleLabel;
    private Label audioLabel;
    private Label videoLabel;
    private Label volumeMusicLabel;
    private Label volumeSoundLabel;
    private Label musicOnOffLabel;
    private Label soundOnOffLabel;

    private final  Slider volumeMusicSlider;
    private final  Slider soundMusicSlider;
    private final  CheckBox soundEffectsCheckbox;
    private final  CheckBox musicCheckbox;
    private final  TextButton backButton;

    private int screen;

    public SettingsScreen(final PixelGame game, int screen) {
        this.game = game;
        this.screen = screen;

        stage = new Stage(new FitViewport(PixelGame.WIDTH, PixelGame.HEIGHT));

        table = new Table();

        table.setFillParent(true);
        table.setDebug(PixelGame.DEBUG);

        skin = game.assets.getSkin();

        volumeMusicSlider = new Slider( 0f, 1f, 0.1f,false, skin );
        volumeMusicSlider.setValue(game.getPreferences().sound.getMusicVolume() );
        volumeMusicSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().sound.setMusicVolume(volumeMusicSlider.getValue() );
                return false;
            }
        });

        soundMusicSlider = new Slider( 0f, 1f, 0.1f,false, skin );
        soundMusicSlider.setValue(game.getPreferences().sound.getSoundVolume());
        soundMusicSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().sound.setSoundVolume(soundMusicSlider.getValue());
                game.sound.update();
                return false;
            }
        });

        musicCheckbox = new CheckBox(null, skin);
        musicCheckbox.setChecked(game.getPreferences().sound.isMusicEnabled() );
        musicCheckbox.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = musicCheckbox.isChecked();
                game.getPreferences().sound.setMusicEnabled( enabled );
                return false;
            }
        });

        soundEffectsCheckbox = new CheckBox(null, skin);
        soundEffectsCheckbox.setChecked(game.getPreferences().sound.isSoundEffectsEnabled());
        soundEffectsCheckbox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = soundEffectsCheckbox.isChecked();
                game.getPreferences().sound.setSoundEffectsEnabled( enabled );
                return false;
            }
        });

        backButton = new TextButton("Back", skin); // the extra argument here "small" is used to set the button to the smaller version instead of the big default version
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeScreen();
                game.sound.update();
            }
        });



        titleLabel = new Label("Preferences", skin);
        audioLabel = new Label("Audio", skin);
        videoLabel = new Label("Video", skin);
        volumeMusicLabel = new Label("Music Volume", skin);
        volumeSoundLabel = new Label("Sounds Volume", skin);
        musicOnOffLabel = new Label("Music", skin);
        soundOnOffLabel = new Label("Sounds", skin);
        backButton.scaleBy(4);

        skin.getFont("BitPotionExt").getData().setScale(2);
        Table left = new Table();
        left.setDebug(PixelGame.DEBUG);
        left.add(audioLabel).colspan(12);
        left.row();
        left.row().pad(10,10,10,10);
        left.add(volumeMusicLabel);
        left.add(volumeMusicSlider);
        left.row().pad(10,10,10,10);
        left.add(musicOnOffLabel);
        left.add(musicCheckbox);
        left.row().pad(10,10,10,10);
        left.add(volumeSoundLabel);
        left.add(soundMusicSlider);
        left.row().pad(10,10,10,10);
        left.add(soundOnOffLabel);
        left.add(soundEffectsCheckbox);

        Table right = new Table();
        right.setDebug(PixelGame.DEBUG);
        right.add(videoLabel).expandX();
        right.row().pad(10,10,10,10);
        right.add(new Label("Light Effects", skin));
        right.add(new CheckBox(null, skin)).expandX();
        right.row().pad(10,10,10,10);
        right.add(new Label("Game", skin)).expandX();
        right.row().pad(10,10,10,10);
        right.add(new TextButton("Reset Game", skin)).width(150);
        right.row().pad(10,10,10,10);
        right.add(new TextButton("Credits", skin)).width(150);

        table.center().center();
        table.add(titleLabel).colspan(24);
        table.row().padTop(30);
        table.add(left).colspan(6);
        table.add(right).colspan(24);
        table.row().pad(10,10,10,10);
        table.add(backButton).colspan(24);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        stage.clear();
        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
            changeScreen();

        Gdx.gl.glClearColor(24 / 255f, 32 / 255f, 61 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,true);
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

    public void setPreviousScreen(int previousScreen) {
        this.screen = previousScreen;

    }
    private void changeScreen(){
        game.setScreen(screen);
    }
}
