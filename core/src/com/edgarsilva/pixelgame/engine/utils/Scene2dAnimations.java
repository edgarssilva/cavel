package com.edgarsilva.pixelgame.engine.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Scene2dAnimations {
public final static int
        FADEOUT = 1,
        BOUNCE = 2,
        ELASTICIN = 3,
        ELASTICOUT = 4,
        CIRCLE = 5,
        CIRCLEIN = 6,
        CIRCLEOUT = 7,
        SWING = 8,
        SWINGIN = 9,
        SWINGOUT = 10,
        ROTATEBY = 11;

    final float fadeoutSpeed = .0001f, fadeinSpeed = 3f,
            bounceOutamountX = 0, bounceOutamountY = -100,
            bounceOutDuration = 1f;
    /**
     * boolean value to check that animation is stopped or not
     */
    static boolean screenSelector = false;
    /**
     * Specific region on which animation have to perform
     */
    Image imageRegion;

    public final float elasticInAmountX = 0;

    public final float elasticInAmountY = 100;

    public final float elasticInDuration = 1f;

    public final float elasticOutAmountX = 0;

    public final float elasticOutAmountY = 200;

    public final float elasticOutDuration = 1f;

    public final float circleAmountX = 50;

    public final float circleAmountY = 100;

    public final float circleDuration = 1f;

    public final float circleInAmountX = 50;

    public final float circleInAmountY = 100;

    public final float circleInDuration = 3f;

    public final float swingAmountX = 0;

    public final float swingAmountY = 100;

    public final float swingDuration = 1f;

    public final float swingInAmountX = 0;

    public final float swingInAmountY = 100;

    public final float swingInDuration = 1f;

    public final float swingOutAmountX = 0;

    public final float swingOutAmountY = 100;

    public final float swingOutDuration = 1f;

    public final float rotationAmount = 100;

    public final float rotationDuration = 2f;


    public Scene2dAnimations(Screen tweenSplash, TextureRegion region,
                             Stage stage, int animationNo)

    {

        imageRegion = new Image(region);
        imageRegion.setWidth(Gdx.graphics.getWidth());
        imageRegion.setHeight(Gdx.graphics.getHeight());
        switch (animationNo) {
            case 1:
                imageRegion.addAction(Actions.sequence(
                        Actions.fadeOut(fadeoutSpeed),Actions.fadeIn(fadeinSpeed),
                        Actions.run(onSplashFinishedRunnable)));
                stage.addActor(imageRegion);
                break;
            case 2:
                imageRegion.addAction(Actions.sequence(Actions.moveBy(
                        bounceOutamountX, bounceOutamountY, bounceOutDuration,
                        Interpolation.bounceOut), Actions
                        .run(onSplashFinishedRunnable)));
                stage.addActor(imageRegion);
                break;
            case 3:
                imageRegion.addAction(Actions.sequence(
                        Actions.moveTo(elasticInAmountX, elasticInAmountY, elasticInDuration, Interpolation.elasticIn),
                        Actions.run(onSplashFinishedRunnable)));
                stage.addActor(imageRegion);
            case 4:
                imageRegion.addAction(Actions.sequence(
                        Actions.moveBy(elasticOutAmountX, elasticOutAmountY, elasticOutDuration, Interpolation.elasticOut),
                        Actions.run(onSplashFinishedRunnable)));
                stage.addActor(imageRegion);
                break;
            case 5:
                imageRegion.addAction(Actions.sequence(
                        Actions.moveTo(circleAmountX, circleAmountY, circleDuration, Interpolation.circle),
                        Actions.fadeOut(fadeoutSpeed), Actions.fadeIn(fadeinSpeed),

                        Actions.run(onSplashFinishedRunnable)));
                stage.addActor(imageRegion);

                break;
            case 6:
                imageRegion.addAction(Actions.sequence(Actions.moveBy(0,100, 0.7f, Interpolation.circleIn),
                        Actions.run(onSplashFinishedRunnable)));
                stage.addActor(imageRegion);

                break;

            case 7:
                imageRegion.addAction(Actions.sequence(
                        Actions.fadeIn(fadeinSpeed, Interpolation.circleOut),
                        Actions.run(onSplashFinishedRunnable)));
                stage.addActor(imageRegion);
                break;
            case 8:
                imageRegion.addAction(Actions.sequence(
                        Actions.moveTo(swingAmountX, swingAmountY, swingDuration, Interpolation.swing),
                        Actions.run(onSplashFinishedRunnable)));
                stage.addActor(imageRegion);
                break;
            case 9:
                imageRegion.addAction(Actions.sequence(
                        Actions.moveTo(swingInAmountX, swingInAmountY, swingInDuration, Interpolation.swingIn),
                        Actions.moveTo(200, 0, 0.9f, Interpolation.swingOut),
                        Actions.run(onSplashFinishedRunnable)));
                stage.addActor(imageRegion);
                break;

            case 10:
                imageRegion.addAction(Actions.sequence(
                        Actions.moveTo(swingOutAmountX, swingOutAmountY, swingOutDuration, Interpolation.swingOut),
                        Actions.run(onSplashFinishedRunnable)));
                stage.addActor(imageRegion);
                break;
            case 11:

                imageRegion.addAction(Actions.sequence(Actions.rotateBy(rotationAmount, rotationDuration),
                        Actions.run(onSplashFinishedRunnable)));
                stage.addActor(imageRegion);
                break;
            case 13:
//		 imageRegion.addAction(Actions.sequence(Actions.rotateTo(10, .1f, Interpolation.circleIn),
//		 Actions.run(onSplashFinishedRunnable)));
//		 stage.addActor(imageRegion);
                break;
            default:
                imageRegion.addAction(Actions.sequence(
                        Actions.moveTo(elasticInAmountX, elasticInAmountY, elasticInDuration, Interpolation.elasticIn),
                        Actions.run(onSplashFinishedRunnable)));
                stage.addActor(imageRegion);
                break;
        }

    }
    Runnable onSplashFinishedRunnable = new Runnable() {

        @Override
        public void run() {
            screenSelector = true;
        }
    };

}