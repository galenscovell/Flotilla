package com.zurui.flotilla.environment

import com.badlogic.gdx.ai.steer.Steerable
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.Array
import com.zurui.flotilla.global.Constants
import com.zurui.flotilla.processing.BaseSteerable
import com.zurui.flotilla.processing.pathfinding.AStarGraph

class Tilemap(private val world: World, mapName: String) {
    private val tileMap: TiledMap = TmxMapLoader().load("maps/tilemaps/$mapName.tmx")
    private val tiledMapRenderer = OrthogonalTiledMapRenderer(tileMap, Constants.TILE_SIZE / Constants.PIXEL_PER_METER)

    private val propSteerables: MutableList<Steerable<Vector2>> = mutableListOf()
    private val baseLayers: IntArray = intArrayOf(0, 1)
    private val overlapLayers: IntArray = intArrayOf(2)

    private var aStarGraph: AStarGraph

    init {
        val prop: MapProperties = tileMap.properties
        val mapWidth: Int = prop.get("width", Int::class.java)
        val mapHeight: Int = prop.get("height", Int::class.java)
        val tileWidth: Int = prop.get("tilewidth", Int::class.java)
        val tileHeight: Int = prop.get("tileheight", Int::class.java)

        println("$mapWidth, $mapHeight, $tileWidth, $tileHeight")

        // Find boundary collision objects
        val boundaryObjectLayer: MapLayer = tileMap.layers.get("BoundaryObjects")
        val boundaryObjects: Array<RectangleMapObject> =
                boundaryObjectLayer.objects.getByType(RectangleMapObject::class.java)

        for (rmo: RectangleMapObject in boundaryObjects) {
            val rect: Rectangle = rmo.rectangle
            createCollisionBody(
                rect.x / Constants.PIXEL_PER_METER, rect.y / Constants.PIXEL_PER_METER,
                rect.width / Constants.PIXEL_PER_METER, rect.height / Constants.PIXEL_PER_METER
            )
        }

        // Find prop collision objects
        val propObjectLayer: MapLayer = tileMap.layers.get("PropObjects")
        val propObjects: Array<RectangleMapObject> =
                propObjectLayer.objects.getByType(RectangleMapObject::class.java)

        for (rmo: RectangleMapObject in propObjects) {
            val rect: Rectangle = rmo.rectangle
            val rmoBody: Body = createCollisionBody(
                rect.x / Constants.PIXEL_PER_METER, rect.y / Constants.PIXEL_PER_METER,
                rect.width / Constants.PIXEL_PER_METER, rect.height / Constants.PIXEL_PER_METER
            )

            propSteerables += BaseSteerable(rmoBody, rect.width / Constants.PIXEL_PER_METER)
        }

        // Assemble AStarGraph for pathfinding
        aStarGraph = AStarGraph(world, tileMap, mapWidth, mapHeight)
    }


    /********************
     *       Get       *
     ********************/
    fun getAStarGraph(): AStarGraph {
        return aStarGraph
    }

    fun getPropSteerables(): List<Steerable<Vector2>> {
        return propSteerables.toList()
    }


    /********************
     *      Update     *
     ********************/
    fun updateShader(shaderProgram: ShaderProgram) {
        tiledMapRenderer.batch.shader = shaderProgram
    }

    fun updateCamera(camera: OrthographicCamera) {
        tiledMapRenderer.setView(camera)
    }


    /********************
     *      Render     *
     ********************/
    fun renderBaseLayer() {
        tiledMapRenderer.render(baseLayers)
    }

    fun renderOverlapLayer() {
        tiledMapRenderer.render(overlapLayers)
    }


    /********************
     *    Creation     *
     ********************/
    private fun createCollisionBody(rx: Float, ry: Float, width: Float, height: Float): Body {
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody
        bodyDef.fixedRotation = true
        bodyDef.angularDamping = 1f
        bodyDef.position.set(rx + width / 2, ry + height / 2)

        val body: Body = world.createBody(bodyDef)

        val shape = PolygonShape()
        shape.setAsBox(width / 2, height / 2)

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = 1f
        fixtureDef.friction = 0.1f
        fixtureDef.filter.categoryBits = Constants.WALL_CATEGORY
        fixtureDef.filter.maskBits = Constants.WALL_MASK

        val fixture = body.createFixture(fixtureDef)
        shape.dispose()

        return body
    }
}
