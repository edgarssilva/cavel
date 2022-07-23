package com.edgarsilva.pixelgame.engine.ai.fsm;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.edgarsilva.pixelgame.engine.ai.pfa.Node;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.EnemyCollisionComponent;
import com.edgarsilva.pixelgame.engine.ecs.components.StatsComponent;
import com.edgarsilva.pixelgame.engine.utils.managers.EntityManager;
import com.edgarsilva.pixelgame.engine.utils.objects.Updateable;

public class EnemyAgentComponent implements Component, Updateable {

    public Entity entity;
    public Body body;
    public Node node = null;
    // public Node target = new Node();

    public boolean isTouchingWallLeft = false;
    public boolean isTouchingWallRight = false;
    public boolean hasGroundLeft = false;
    public boolean hasGroundRight = false;

    public Array<Entity> attackableEntities = new Array<Entity>();
    public boolean moveToLeft = false;

    public float timer = 1f;
    public Animation anim;

    public StateMachine<EnemyAgentComponent, EnemyState> stateMachine;

    private EnemyCollisionComponent collisionComp;
    public StatsComponent statsComp;
    private ComponentMapper<StatsComponent> statsCompMap;
    private ComponentMapper<BodyComponent>  bodyCompMap;
    // private IndexedAStarPathFinder<Node> pathFinder;
    // private GraphPathImp resultPath = new GraphPathImp();

    public EnemyAgentComponent() {

    }

    public EnemyAgentComponent(Entity entity) {
        this.entity   =  entity;
        statsCompMap  =  ComponentMapper.getFor(StatsComponent.class);
        bodyCompMap   =  ComponentMapper.getFor(BodyComponent.class);
        statsComp     =  statsCompMap.get(entity);
        body          =  bodyCompMap.get(entity).body;
        collisionComp =  entity.getComponent(EnemyCollisionComponent.class);
        stateMachine  =  new DefaultStateMachine<EnemyAgentComponent, EnemyState>(this, EnemyState.Seeking);
        MessageManager.getInstance().addListener(stateMachine, 0);
    }


    @Override
    public void update(float deltaTime) {
        // if (PlayScreen.gameOver) stateMachine.changeState(EnemyState.IDLE);
        stateMachine.update();

        hasGroundLeft       =  collisionComp.numGroundLeft  >  0;
        hasGroundRight      =  collisionComp.numGroundRight >  0;
        isTouchingWallLeft  =  collisionComp.numWallLeft    >  0;
        isTouchingWallRight =  collisionComp.numWallRight   >  0;
    }

    public void moveLeft(){
        body.setLinearVelocity(-1.1f, body.getLinearVelocity().y);
    }

    public void moveRight(){
        body.setLinearVelocity(1.1f, body.getLinearVelocity().y);
    }

    public void hit() {
        //Proteção para não contar o ataque mais que uma vez
        if (stateMachine.isInState(EnemyState.Hit) || stateMachine.isInState(EnemyState.Dying)) {
            if (timer < 0.05f || stateMachine.isInState(EnemyState.Dying)) return;
        }

        statsComp.attack(statsCompMap.get(EntityManager.getPlayer()));
        if (statsComp.health <= 0) {
            stateMachine.changeState(EnemyState.Dying);
        }else{
            stateMachine.changeState(EnemyState.Hit);
        }
    }

    public void addEntityToAttack(Entity entity){
        attackableEntities.add(entity);
    }

    public void removeEntityToAttack(Entity entity){
        attackableEntities.removeValue(entity, false);
    }

    public void attack() {
        PlayerAgent.hit(statsComp);
    }

    public boolean attackable() {
        for (Entity ent: attackableEntities)
            if (ent.equals(EntityManager.getPlayer()))return true;

        return false;
    }
}

/**
 * Path Finding Code
 **/
/*        node = LevelManager.graph.getNodeByXY((int) body.getPosition().x + LevelManager.tilePixelHeight, (int) body.getPosition().y - LevelManager.tilePixelHeight);
        stateMachine.update();

        int index = node.getIndex();

        PlayScreen.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (node.type == Node.Type.GROUND)
            PlayScreen.shapeRenderer.setColor(10/255f, 200/255f, 1/255f, 1);
        else if (node.type == Node.Type.AIR)
            PlayScreen.shapeRenderer.setColor(0/255f, 95/255f, 200/255f, 1);
        else if (node.type == Node.Type.WALL)
            PlayScreen.shapeRenderer.setColor(200/255f, 0/255f, 0/255f, 1);
        else if (node.type == Node.Type.LEFT)
            PlayScreen.shapeRenderer.setColor(255/255f, 250/255f, 7/255f, 1);
        else if (node.type == Node.Type.RIGHT)
            PlayScreen.shapeRenderer.setColor(208/255f, 0/255f, 255/255f, 1);

        PlayScreen.shapeRenderer.rect(
                LevelManager.tilePixelWidth / 2f + (index % LevelManager.lvlTileWidth) * LevelManager.tilePixelWidth,
                LevelManager.tilePixelHeight / 2f + (index / LevelManager.lvlTileWidth) * LevelManager.tilePixelHeight,
                LevelManager.tilePixelWidth,
                LevelManager.tilePixelHeight

        );

        PlayScreen.shapeRenderer.end();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            body.setLinearVelocity(MathUtils.lerp(body.getLinearVelocity().x, -2f, 0.1f), body.getLinearVelocity().y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            body.setLinearVelocity(MathUtils.lerp(body.getLinearVelocity().x, 2f, 0.1f), body.getLinearVelocity().y);
        }


 /*       target = LevelManager.graph.getClosestNode((int) body.getPosition().x, (int) body.getPosition().y - LevelManager.tilePixelHeight, Node.Type.GROUND);


        //if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {


        int startX = (int) (steering.getPosition().x);
        int startY = (int) (steering.getPosition().y - LevelManager.tilePixelHeight);
        System.out.println(startX +":"+ startY);
        Node startNode = LevelManager.graph.getNodeByXY(startX, startY);
        Node endNode ;

        if (startNode.type == Node.Type.LEFT)
            endNode = LevelManager.graph.getClosestNode(startX, startY, Node.Type.RIGHT);
        else
            endNode = LevelManager.graph.getClosestNode(startX, startY, Node.Type.LEFT);

        if (endNode == null){
            startX = (int) (steering.getPosition().x) + LevelManager.tilePixelWidth / 2;
            if (startNode.type == Node.Type.LEFT)
                endNode = LevelManager.graph.getClosestNode(startX, startY, Node.Type.RIGHT);
            else
                endNode = LevelManager.graph.getClosestNode(startX, startY, Node.Type.LEFT);

            if (endNode == null) return;
        }


        resultPath.clear();
        pathFinder.searchNodePath(startNode, endNode, new HeuristicImp(), resultPath);

        // }


        if (PixelGame.DEBUG) PathfindingDebugger.drawPath(resultPath);*/