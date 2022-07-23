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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.edgarsilva.pixelgame.PixelGame;

public class SettingsScreen implements Screen{

    private PixelGame game;
    private Stage stage;

    private Table table;
    private Skin skin;

    private Label titleLabel;
    private Label volumeMusicLabel;
    private Label volumeSoundLabel;
    private Label musicOnOffLabel;
    private Label soundOnOffLabel;

    private final  Slider volumeMusicSlider;
    private final  Slider soundMusicSlider;
    private final  CheckBox soundEffectsCheckbox;
    private final  CheckBox musicCheckbox;
    private final  TextButton backButton;

    int screen = 0;

    public SettingsScreen(final PixelGame game, int screen) {
        this.game = game;
        this.screen = screen;

        stage = new Stage(new ScreenViewport());

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



        titleLabel = new Label( "Preferences", skin );
        volumeMusicLabel = new Label( "Music Volume", skin );
        volumeSoundLabel = new Label( "Sounds Volume", skin );
        musicOnOffLabel = new Label( "Music", skin );
        soundOnOffLabel = new Label( "Sounds", skin );


        table.add(titleLabel).colspan(2);
        table.row().pad(10,10,10,10);
        table.add(volumeMusicLabel);
        table.add(volumeMusicSlider);
        table.row().pad(10,10,10,10);
        table.add(musicOnOffLabel);
        table.add(musicCheckbox);
        table.row().pad(10,10,10,10);
        table.add(volumeSoundLabel);
        table.add(soundMusicSlider);
        table.row().pad(10,10,10,10);
        table.add(soundOnOffLabel);
        table.add(soundEffectsCheckbox);
        table.row().pad(10,10,10,10);
        table.add(backButton).colspan(2);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        stage.clear();
        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            game.setScreen(PixelGame.MENU_SCREEN);
        }
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
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
