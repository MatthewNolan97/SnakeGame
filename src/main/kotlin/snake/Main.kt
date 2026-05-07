package ca.qc.cegep.g30.snake

import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Stage
import javafx.util.Duration
import kotlin.system.exitProcess

class Main : Application() {
    private val appPadding = 50.0
    private val size = 40
    private val gridSize = 450.0
    private val cellSize = gridSize / size
    val game = GameState()
    private val cellRects = Array(size) {
        Array(size) {
            Rectangle(cellSize, cellSize)
        }
    }

    private var speed = Duration.millis(100.0)
    private val timeline: Timeline by lazy {
        if (game.getTheScore() > 10) speed = Duration.millis(50.0)
        if (game.getTheScore() > 20) speed = Duration.millis(25.0)
        if (game.getTheScore() > 30) speed = Duration.millis(10.0)
        Timeline(KeyFrame(speed, {
            try {
                updateGrid()
                score.text = "Score: ${game.getTheScore()}"
            } catch (e: IndexOutOfBoundsException) {
                gameOver()
            }
        })).apply {
            cycleCount = Timeline.INDEFINITE
        }
    }
    var isRunning = false
    val leadersButton = Button("Leaderboards")
    private var leaders = Label("")
    val startQuitButton = Button("Start")
    var score = Label("Score: ${game.getTheScore()}")
    lateinit var primaryStage: Stage
    lateinit var player: Player
    private val tempPlayer = Player("temp", 0, 0)
    private val playerList = tempPlayer.loadPlayers()
    private val scoreboard = Scoreboard()
    private val l = scoreboard.getTop5FromFile()
    var isToggled = false

    private fun findPlayer(name: String): Player {
        for (player in playerList) {
            if (player.username == name) return player
        }
        return tempPlayer
    }

    private fun playerInList(name: String): Boolean {
        for (i in playerList) {
            if (i.username == name) return true
        }
        return false
    }

    override fun start(beginning: Stage) {
        scoreboard.getLeaders(tempPlayer.getTop5())
        scoreboard.sort()
        scoreboard.populateTop5()
        primaryStage = beginning
        val welcomeMessage = Label("Welcome to the Snake Game")
        val requestMessage = Label(
            "Please enter your desired " +
                    "username if you're new, or select your user name from the list if you're " +
                    "a returning player"
        )
        val newPlayerField = TextField()
        val returningPlayerBox = ComboBox<String>().apply {
            for (player in playerList) {
                items.add(player.username)
            }
        }
        if (newPlayerField.text.isNotEmpty()) returningPlayerBox.isDisable = true
        val continueButton = Button("Done").apply {
            setOnAction {
                if (newPlayerField.text.isNotEmpty()) {
                    val alert = Alert(Alert.AlertType.INFORMATION)
                    alert.title = "Error"
                    alert.headerText = "Error"
                    alert.contentText = "That username has already been taken, please try a different name"
                    if (playerInList(newPlayerField.text.trim())) {
                        alert.showAndWait()

                    } else {
                        val uname = newPlayerField.text.trim()
                        player = Player(uname, 0, 0)
                        createGame(primaryStage)
                    }

                } else if (returningPlayerBox.value != null) {
                    player = findPlayer(returningPlayerBox.value)
                    createGame(primaryStage)
                } else {
                    val alert = Alert(Alert.AlertType.INFORMATION)
                    alert.title = "Error"
                    alert.headerText = "Error"
                    alert.contentText = "You must either enter a username or select one from the list"
                    alert.showAndWait()
                }
            }
        }
        val intro = VBox().apply {
            children.addAll(welcomeMessage, requestMessage, newPlayerField, returningPlayerBox, continueButton)
            spacing = 10.0
            padding = Insets(appPadding)
        }
        beginning.title = "Welcome"
        beginning.scene = Scene(intro)
        beginning.show()
    }

    fun createGame(gameStage: Stage) {
        val gridPane = createGrid()
        score = Label("Score: ${game.getTheScore()}")
        leaders.apply {
            minHeight = 100.0
        }
        val root = VBox().apply {
            children.addAll(startQuitButton, score, leadersButton, leaders)
            children.add(gridPane)
            spacing = 10.0
            padding = Insets(appPadding)
        }
        startQuitButton.apply {
            setOnAction {
                if (isRunning) {
                    stopGame()
                    text = "Start"
                } else {
                    startGame()
                    root.requestFocus()
                    text = "Quit"
                }
            }
        }
        leadersButton.apply {
            setOnAction {
                if (!isToggled) {
                    leaders.text = l
                    isToggled = true
                } else {
                    leaders.text = ""
                    isToggled = false
                }
            }
        }
        gameStage.title = "SNAKE"
        gameStage.scene = Scene(root).apply {
            setOnKeyPressed { event ->
                when (event.code) {
                    KeyCode.UP -> {
                        if (game.snake.snakeDirection != Direction.DOWN) game.snake.setTheSnakeDirection(Direction.UP)
                    }

                    KeyCode.DOWN -> {
                        if (game.snake.snakeDirection != Direction.UP) game.snake.setTheSnakeDirection(Direction.DOWN)
                    }

                    KeyCode.LEFT -> {
                        if (game.snake.snakeDirection != Direction.RIGHT) game.snake.setTheSnakeDirection(Direction.LEFT)
                    }

                    KeyCode.RIGHT -> {
                        if (game.snake.snakeDirection != Direction.LEFT) game.snake.setTheSnakeDirection(Direction.RIGHT)
                    }

                    KeyCode.ESCAPE -> {

                    }

                    else -> {
                        //do nothing
                    }
                }
            }
        }
        gameStage.show()
    }

    private fun createGrid(): GridPane {
        val gridPane = GridPane()

        for (x in 0 until size) {
            for (y in 0 until size) {
                val rect = cellRects[x][y]
                rect.fill = Color.WHITE
                rect.stroke = Color.BLACK
                rect.strokeWidth = 1.0
                gridPane.add(rect, x, y)
            }
        }
        return gridPane
    }

    fun startGame() {
        isRunning = true
        initializeGrid()
        timeline.play()

    }

    fun stopGame() {
        player.incrementGP()
        if (game.getTheScore() > player.getThePR()) {
            player.saveStats(player.username, player.getTheGP(), game.getTheScore())
            player.setThePR(game.getTheScore())
        } else player.saveStats(player.username, player.getTheGP(), player.getThePR())
        gameOverScene(primaryStage)
        isRunning = false
        timeline.stop()
        startQuitButton.text = "Start"


    }

    private fun initializeGrid() {
        val grid = game.updateGridState()
        for (x in grid.indices) {
            for (y in grid.indices) {
                if (grid[x][y] == GameState.Cell.APPLE) cellRects[x][y].fill = Color.GREEN
                else if (grid[x][y] == GameState.Cell.SNAKE) cellRects[x][y].fill = Color.BLACK
                else cellRects[x][y].fill = Color.WHITE
            }
        }
    }

    fun updateGrid() {
        try {
            game.updateGameState()
        } catch (e: Exception) {
            gameOver()
        }
        for (x in 0 until size) {
            for (y in 0 until size) {
                if (game.grid[x][y] == GameState.Cell.APPLE) cellRects[x][y].fill = Color.GREEN
                else if (game.grid[x][y] == GameState.Cell.SNAKE) cellRects[x][y].fill = Color.BLACK
                else cellRects[x][y].fill = Color.WHITE
            }
        }
    }

    fun gameOver() {
        stopGame()
    }


    private fun gameOverScene(end: Stage) {
        val endMessage = Label("Game Over. You scored: ${game.getTheScore()}")
        val playAgain = Button("Play again").apply {
            setOnAction { restartGame() }
        }
        val quit = Button("Quit").apply {
            setOnAction { exitProcess(0) }
        }
        val ls = Label("Top5\n------\n$l")
        val ending = VBox().apply {
            children.addAll(endMessage, playAgain, quit, ls)
            spacing = 10.0
            padding = Insets(appPadding)
        }
        end.title = "Game Over"
        end.scene = Scene(ending, 300.0, 300.0)
        end.show()
    }

    private fun restartGame() {
        game.restartGridState()
        createGame(primaryStage)
    }
}

fun main() {
    Application.launch(Main::class.java)
}

