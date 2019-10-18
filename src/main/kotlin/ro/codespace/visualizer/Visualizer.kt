package ro.codespace.visualizer

import processing.core.PApplet
import processing.core.PConstants
import java.io.File
import java.util.*
import kotlin.random.Random
import kotlin.system.exitProcess

data class Particle(val x: Float, val y: Float, val color: Int, val id: Int)

data class Frame(val particleCount: Int, val particles: List<Particle>)

class Visualizer(val maxX: Float, val maxY: Float, val data: List<Frame>) : PApplet() {

    val colorRandom = Random(12313)
    val colors = mutableMapOf(2 to color(255, 0, 0), 3 to color(0, 0, 255), 4 to color(0, 255, 0))

    override fun settings() {
        size(800, 800)
    }

    val scale = 15 * 60 / maxX

    override fun draw() {
        frameRate(120f)
        background(200)
        ellipseMode(PConstants.CENTER)
        val frame = data[frameCount % data.size]

        noStroke()
        for (particle in frame.particles) {
            fill(colors.getOrPut(particle.color) {
                color(colorRandom.nextInt(255), colorRandom.nextInt(255), colorRandom.nextInt(255))
            })
            ellipse(particle.x * width / maxX, particle.y * height / maxY, scale, scale)
        }
    }
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        exitProcess(1)
    }

    val scanner = Scanner(File(args[0]))

    val maxX = scanner.nextFloat()
    val maxY = scanner.nextFloat()

    val frames = mutableListOf<Frame>()
    while (scanner.hasNext()) {
        val count = scanner.nextInt()
        val particles = (1..count).map {
            val id = scanner.nextInt()
            val color = scanner.nextInt()
            val posX = scanner.nextFloat()
            val posY = scanner.nextFloat()

            Particle(posX, posY, color, id)
        }
        frames.add(Frame(count, particles))
    }

    PApplet.runSketch(arrayOf("Visualizer"), Visualizer(maxX, maxY, frames))
}