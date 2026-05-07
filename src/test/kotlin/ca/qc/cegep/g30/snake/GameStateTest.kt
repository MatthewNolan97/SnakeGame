package ca.qc.cegep.g30.snake

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

class GameStateTest {

    private lateinit var gameState: GameState

    @BeforeEach
    fun setUp() {
        gameState = GameState()
    }

    @Test
    fun testGenerateApple() {
        val applePosition = gameState.generateApple()
        assertEquals(GameState.Cell.EMPTY, gameState.grid[applePosition.first][applePosition.second])
    }

    @Test
    fun testUpdateGridState() {
        gameState.snake.setTheSnakeDirection(Direction.RIGHT)
        gameState.updateGameState()

        val snakeBody = gameState.snake.getBody().toList()
        assertEquals(GameState.Cell.SNAKE, gameState.grid[snakeBody[0].first][snakeBody[0].second])
        assertEquals(GameState.Cell.APPLE, gameState.grid[gameState.applePosition.first][gameState.applePosition.second])

        for (x in 0 until gameState.gridSize) {
            for (y in 0 until gameState.gridSize) {
                if (gameState.grid[x][y] != GameState.Cell.SNAKE && gameState.grid[x][y] != GameState.Cell.APPLE) {
                    assertEquals(GameState.Cell.EMPTY, gameState.grid[x][y])
                }
            }
        }
    }

    @Test
    fun testUpdateGameStateWithApple() {
        gameState.applePosition = Pair(20, 19)
        gameState.snake.setTheSnakeDirection(Direction.UP)
        gameState.updateGameState()

        assertEquals(1, gameState.getTheScore())
        assertNotEquals(Pair(20, 19), gameState.applePosition)
    }

    @Test
    fun testRestartGridState() {
        gameState.applePosition = Pair(2, 2)
        gameState.snake.eats()
        gameState.restartGridState()

        assertEquals(0, gameState.getTheScore())
        assertNotEquals(Pair(2, 2), gameState.applePosition)
        assertTrue(gameState.snake.getBody().count() == 3)
    }
}