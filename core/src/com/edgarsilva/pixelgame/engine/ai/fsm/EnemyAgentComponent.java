package com.edgarsilva.pixelgame.engine.ai.fsm;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.edgarsilva.pixelgame.engine.ai.pfa.Node;
import com.edgarsilva.pixelgame.engine.ecs.components.BodyComponent;
import com.edgarsilva.pixelgame.engine.utils.managers.LevelManager;
import com.edgarsilva.pixelgame.engine.utils.objects.Steering;
import com.edgarsilva.pixelgame.engine.utils.objects.Updateable;

public class EnemyAgentComponent implements Component, Updateable {

    private Entity entity;
    public Body body;
    public Node node = null;
   // public Node target = new Node();

    public StateMachine<EnemyAgentComponent, EnemyState> stateMachine;

   // private IndexedAStarPathFinder<Node> pathFinder;
   // private GraphPathImp resultPath = new GraphPathImp();

    public EnemyAgentComponent() {
    }

    public EnemyAgentComponent(Entity entity, Steering steering) {
        this.entity   = entity;
        body = entity.getComponent(BodyComponent.class).body;

        stateMachine = new DefaultStateMachine<EnemyAgentComponent, EnemyState>(this, EnemyState.Seeking);
    }


    @Override
    public void update(float deltaTime) {
        node = LevelManager.graph.getNodeByXY((int) body.getPosition().x, (int) body.getPosition().y - LevelManager.tilePixelHeight);
        stateMachine.update();
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
    }

    public void moveLeft(){
        body.setLinearVelocity(MathUtils.lerp(body.getPosition().x ,- 2f, 0.1f), body.getLinearVelocity().y);
    }

    public void moveRight(){
        body.setLinearVelocity(MathUtils.lerp(body.getPosition().x ,2f, 0.1f), body.getLinearVelocity().y);
    }

}
