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

    public MessageSystem(PlayScreen screen) {
        super(Family.all(MessageComponent.class, BodyComponent.class).get(), 2);
        this.screen = screen;
        mcm = ComponentMapper.getFor(MessageComponent.class);
        bcm = ComponentMapper.getFor(BodyComponent.class);

        font = PlayScreen.getGame().assets.font;

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font.setUseIntegerPositions(false);
        font.getData().setScale(RenderSystem.PIXELS_TO_METERS / 2);

        width = new GlyphLayout(font, "A").width;
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

                font.getData().setScale(RenderSystem.PIXELS_TO_METERS / 2);
                font.setColor(1, 1, 1, mc.timer);

                switch (mc.message) {
                    case Movement:
                        font.draw(screen.getBatch(), "Press", position.x - width * 23/2f, position.y + 0.75f);
                        font.draw(screen.getBatch(), " A D keys to move.", (position.x - width * 23/2f + width * 6), position.y + 0.75f);
                        break;

                    case Jump:
                        font.draw(screen.getBatch(), "Press", position.x - width * 14f, position.y + 0.75f);
                        font.draw(screen.getBatch(), " SpaceBar key to jump.", (position.x - width * 14f + width * 6), position.y + 0.75f);
                        break;
                    case DoubleJump:
                        font.draw(screen.getBatch(), "Press", position.x - width * 17.5f, position.y + 0.75f);
                        font.draw(screen.getBatch(), " SpaceBar key 2 times to jump.", (position.x - width * 17.5f + width * 6), position.y + 0.75f);
                        break;

                    case Attack:
                        font.draw(screen.getBatch(), "Press", position.x - width * 11f, position.y + 0.75f);
                        font.draw(screen.getBatch(), " K key to attack.", (position.x - width * 11f + width * 6), position.y + 0.75f);
                        break;

                    case Wall:
                        font.draw(screen.getBatch(), "Walk through the wall.", position.x - width * 11f, position.y + 0.75f);
                        break;
                }
                screen.getBatch().end();

            }
        }
    }

}
