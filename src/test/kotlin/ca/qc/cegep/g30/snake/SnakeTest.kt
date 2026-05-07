package ca.qc.cegep.g30.snake

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class SnakeTest {
    private lateinit var snake: Snake

    @BeforeEach
    fun setUp() {
        snake = Snake()
    }

    @Test
    fun testInitialSnakePosition() {
        val headPosition = snake.getHead()
        assertEquals(Pair(20, 20), headPosition)
    }

    @Test
    fun testSnakeLengthAfterInitialization() {
        assertEquals(3, snake.getBody().count())
    }

    @Test
    fun testInitialSnakeDirection() {
        assertEquals(Direction.UP, snake.getTheSnakeDirection())
    }

    @Test
    fun testMoveSnakeUp() {
        snake.move(Direction.UP)
        val headPosition = snake.getHead()
        assertEquals(Pair(20, 19), headPosition)
    }

    @Test
    fun testMoveSnakeDown() {
        snake.move(Direction.DOWN)
        val headPosition = snake.getHead()
        assertEquals(Pair(20, 21), headPosition)
    }

    @Test
    fun testMoveSnakeLeft() {
        snake.move(Direction.LEFT)
        val headPosition = snake.getHead()
        assertEquals(Pair(19, 20), headPosition)
    }

    @Test
    fun testMoveSnakeRight() {
        snake.move(Direction.RIGHT)
        val headPosition = snake.getHead()
        assertEquals(Pair(21, 20), headPosition)
    }

    @Test
    fun testEats() {
        val initialLength = snake.getBody().count()
        snake.eats()
        assertEquals(initialLength + 1, snake.getBody().count())
    }

    @Test
    fun testChangeDirection() {
        snake.setTheSnakeDirection(Direction.RIGHT)
        assertEquals(Direction.RIGHT, snake.getTheSnakeDirection())
        snake.setTheSnakeDirection(Direction.UP)
        assertEquals(Direction.UP, snake.getTheSnakeDirection())
    }
}