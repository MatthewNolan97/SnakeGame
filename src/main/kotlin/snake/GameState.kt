package ca.qc.cegep.g30.snake

import kotlin.random.Random

class GameState {

    val gridSize = 40
    var grid: Array<Array<Cell>> = Array(gridSize) { Array(gridSize) { Cell.EMPTY } }
    var snake: Snake = Snake()
    private var score: Int = 0
    var applePosition: Pair<Int, Int> = generateApple()


    fun generateApple(): Pair<Int, Int> {

        var applePosition: Pair<Int, Int>

        do {
            applePosition = Pair(Random.nextInt(0, gridSize), Random.nextInt(0, gridSize))
        } while (grid[applePosition.first][applePosition.second] != Cell.EMPTY)
        return applePosition
    }

    fun updateGameState() {
        snake.move(snake.getTheSnakeDirection())

        if (snake.getHead() == applePosition) {
            snake.eats()
            applePosition = generateApple()
            if (score < 10) score++
            if (score in 10..19) score+=10
            if (score >=20 ) score +=15

        }
        val pos: Pair<Int, Int> = snake.getHead()
        if (Cell.SNAKE == grid[pos.first][pos.second]) {
            throw Exception()
        }
        updateGridState()
    }

    fun updateGridState(): Array<Array<Cell>> {
        for (x in 0 until gridSize) {
            for (y in 0 until gridSize) {
                grid[x][y] = Cell.EMPTY
            }
        }

        for (position in snake.getBody()) {
            val (x, y) = position
            if (x in 0 until gridSize && y in 0 until gridSize) {
                grid[x][y] = Cell.SNAKE
            } else throw IndexOutOfBoundsException()
        }
        val (x, y) = applePosition
        grid[x][y] = Cell.APPLE
        return grid
    }

    fun restartGridState() {
        snake = Snake()
        score = 0
        applePosition = generateApple()
        updateGridState()
    }

    fun getTheScore(): Int = score
    enum class Cell {
        EMPTY, SNAKE, APPLE
    }
}