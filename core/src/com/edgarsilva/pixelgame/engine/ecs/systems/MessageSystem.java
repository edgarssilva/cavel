package com.edgarsilva.pixelgame.engine.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.MessageComponent;
import com.edgarsilva.pixelgame.screens.PlayScreen;


public class MessageSystem extends IteratingSystem {

    private ComponentMapper<MessageComponent> mcm;
    private ComponentMapper<BodyComponent>    bcm;

    public BitmapFont font;
    public PlayScreen screen;
    private float width;
    private float timer = 0f;

    public MessageSystem(PlayScreen screen) {
        super(Family.all(MessageComponent.class, BodyComponent.class).get(), 2);
        this.screen = screen;
        mcm = ComponentMapper.getFor(MessageComponent.class);
        bcm = ComponentMapper.getFor(BodyComponent.class);

        font = PlayScreen.getGame().assets.getSkin().getFont("BitPotionExt");

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font.setUseIntegerPositions(false);
        font.getData().setScale(RenderSystem.PIXELS_TO_METERS / 2);

        width = new GlyphLayout(font, "A").width;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        timer += deltaTime;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MessageComponent mc = mcm.get(entity);
        BodyComponent   bc = bcm.get(entity);

        if (mc != null) {
            if (mc.showMessage) {
                mc.timer = 1f;
            }else if (mc.timer > 0){
                mc.timer -= deltaTime * 2.5f;
            }

            if (mc.timer > 0){
                Vector2 position = bc.body.getPosition().cpy();
                screen.getBatch().setProjectionMatrix(screen.getCameraManager().getCamera().combined);
                screen.getBatch().begin();
                screen.getBatch().enableBlending();
                screen.getBatch().setColor(1, 1, 1, mc.timer);
                font.getData().setScale(RenderSystem.PIXELS_TO_METERS / 2);
                font.setColor(1, 1, 1, mc.timer);

                float length = 0;
                switch (mc.message) {
                    case Movement:
                        length = (width * 25 + RenderSystem.PixelsToMeters(16)) / 2f;

                        font.draw(screen.getBatch(), "Press", position.x - length, position.y + 0.75f);
                        screen.getBatch().draw(
                                PlayScreen.getGame().assets.keyboard
                                        .get("a")
                                        .getKeyFrame(timer, true),
                                position.x - length + width * 7,
                                position.y + 0.6f,
                                RenderSystem.PixelsToMeters(8),
                                RenderSystem.PixelsToMeters(8)
                        );
                        font.draw(screen.getBatch(), " and ", position.x - length + width * 7 + RenderSystem.PixelsToMeters(8), position.y + 0.75f);
                        screen.getBatch().draw(
                                PlayScreen.getGame().assets.keyboard
                                        .get("d")
                                        .getKeyFrame(timer, true),
                                position.x - length + width * 13 + RenderSystem.PixelsToMeters(8),
                                position.y + 0.6f,
                                RenderSystem.PixelsToMeters(8),
                                RenderSystem.PixelsToMeters(8)
                        );
                        screen.getBatch().setColor(1, 1, 1, 1);
                        font.draw(screen.getBatch(), " keys to move.", (position.x - length + width * 13 + RenderSystem.PixelsToMeters(16)), position.y + 0.75f);
                        break;

                    case Jump:
                        length = (width * 20 +  RenderSystem.PixelsToMeters(65/2f))/2f;

                        font.draw(screen.getBatch(), "Press", position.x - length, position.y + 0.75f);
                        screen.getBatch().draw(
                                PlayScreen.getGame().assets.keyboard
                                        .get("space")
                                        .getKeyFrame(timer, true),
                                position.x - length + width * 7f ,
                                position.y + 0.6f,
                                RenderSystem.PixelsToMeters(65/2f),
                                RenderSystem.PixelsToMeters(8)
                        );
                        font.draw(screen.getBatch(), " key to jump.", (position.x - length + width * 7 + RenderSystem.PixelsToMeters(65/2f)), position.y + 0.75f);

                        break;
                    case DoubleJump:
                        length = (width * 24 +  RenderSystem.PixelsToMeters(65/2f))/2f;
                        font.draw(screen.getBatch(), "Press", position.x - length, position.y + 0.75f);
                        screen.getBatch().setColor(1, 1, 1, mc.timer);
                        screen.getBatch().draw(
                                PlayScreen.getGame().assets.keyboard
                                        .get("space")
                                        .getKeyFrame(timer, true),
                                position.x - length + width * 7f ,
                                position.y + 0.6f,
                                RenderSystem.PixelsToMeters(65/2f),
                                RenderSystem.PixelsToMeters(8)
                        );
                        font.draw(screen.getBatch(), " key twice to jump.", (position.x - length + width * 7 +  RenderSystem.PixelsToMeters(65/2f)), position.y + 0.75f);
                        break;

                    case Attack:
                        length = (width * 20f + RenderSystem.PixelsToMeters(8)) / 2f;
                        font.draw(screen.getBatch(), "Press", position.x - length, position.y + 0.75f);
                        screen.getBatch().draw(
                                PlayScreen.getGame().assets.keyboard
                                        .get("k")
                                        .getKeyFrame(timer, true),
                                position.x - length + width * 7,
                                position.y + 0.6f,
                                RenderSystem.PixelsToMeters(8),
                                RenderSystem.PixelsToMeters(8)
                        );
                        font.draw(screen.getBatch(), " key to attack.", (position.x - length + width * 7 + RenderSystem.PixelsToMeters(8)), position.y + 0.75f);
                        break;

                    case Wall:
                        font.draw(screen.getBatch(), "Walk through the wall.", position.x - width * 11f, position.y + 0.75f);
                        break;
                }
                screen.getBatch().setColor(1, 1, 1, 1);
                screen.getBatch().end();

            }
        }
    }

}
