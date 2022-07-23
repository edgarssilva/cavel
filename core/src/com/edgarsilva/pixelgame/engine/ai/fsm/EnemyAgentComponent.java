package com.edgarsilva.pixelgame.engine.ai.fsm;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.edgarsilva.pixelgame.engine.ai.pfa.GraphPathImp;
import com.edgarsilva.pixelgame.engine.ai.pfa.Node;
import com.edgarsilva.pixelgame.engine.utils.objects.Steering;
import com.edgarsilva.pixelgame.engine.utils.objects.Updateable;

public class EnemyAgentComponent implements Component, Updateable {

    private Entity entity;
    private Steering steering;

    public StateMachine<EnemyAgentComponent, EnemyState> stateMachine;

    private IndexedAStarPathFinder<Node> pathFinder;
    private GraphPathImp resultPath = new GraphPathImp();

    public EnemyAgentComponent() {
    }

    public EnemyAgentComponent(Entity entity, Steering steering) {
        this.entity   = entity;
       /* this.steering = steering;

        stateMachine = new DefaultStateMachine<EnemyAgentComponent, EnemyState>(this, EnemyState.Seeking);
        pathFinder   = new IndexedAStarPathFinder<Node>(LevelManager.graph, false);

        int startX = (int) (steering.getPosition().x * RenderSystem.PPM) - LevelManager.tilePixelWidth;
        int startY = (int) (steering.getPosition().y * RenderSystem.PPM) - LevelManager.tilePixelHeight;


        Node startNode = LevelManager.graph.getNodeByXY(startX, startY);
        Node endNode ;

        if (startNode.type == Node.Type.LEFT)
            endNode = LevelManager.graph.getClosestNode(startX, startY, Node.Type.RIGHT);
        else
            endNode = LevelManager.graph.getClosestNode(startX, startY, Node.Type.LEFT);

        if (endNode == null) return;

        pathFinder.searchNodePath(startNode, endNode, new HeuristicImp(), resultPath);


        steering.setTarget(new GroundSteering(LevelManager.graph.getPositionByNode(endNode)));*/
    }


    @Override
    public void update(float deltaTime) {
        stateMachine.update();
        //steering.update(deltaTime);
        // System.out.println(steering.getPosition());

        //if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
/*

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
        //steering.setSteeringBehavior(Arrive.class);
        steering.setTarget(new GroundSteering(LevelManager.graph.getPositionByNode(endNode)));
        // }


        if (PixelGame.DEBUG) PathfindingDebugger.drawPath(resultPath);*/
    }



}
