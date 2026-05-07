package ca.qc.cegep.g30.snake

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.io.File

class PlayerTest {

    private lateinit var player: Player
    private val testFilePath = "src/main/GameFiles/Players.txt"

    @BeforeEach
    fun setUp() {
        player = Player("TestUser", 0, 0)
        File(testFilePath).writeText("")
    }

    @Test
    fun testSaveStatsNewPlayer() {
        player.saveStats("TestUser", 1, 10)
        val content = File(testFilePath).readText()
        assertTrue(content.contains("Username: TestUser"))
        assertTrue(content.contains("Games Played: 1"))
        assertTrue(content.contains("PR: 10"))
    }

    @Test
    fun testSaveStatsExistingPlayer() {
        player.saveStats("TestUser", 1, 10)
        player.saveStats("TestUser", 2, 20)
        val content = File(testFilePath).readText()
        assertTrue(content.contains("Username: TestUser"))
        assertTrue(content.contains("Games Played: 2"))
        assertTrue(content.contains("PR: 20"))
    }

    @Test
    fun testIncrementGP() {
        player.incrementGP()
        assertEquals(1, player.getTheGP())
    }

    @Test
    fun testGetThePR() {
        assertEquals(0, player.getThePR())
    }

    @Test
    fun testSetThePR() {
        player.setThePR(50)
        assertEquals(50, player.getThePR())
    }

    @Test
    fun testLoadPlayers() {
        player.saveStats("PlayerOne", 3, 30)
        player.saveStats("PlayerTwo", 2, 20)
        val players = player.loadPlayers()
        assertEquals(2, players.size)
        assertEquals("PlayerOne", players[0].username)
        assertEquals(3, players[0].getTheGP())
        assertEquals(30, players[0].getThePR())
    }

    @Test
    fun testGetTop5() {
        player.saveStats("PlayerA", 1, 50)
        player.saveStats("PlayerB", 1, 70)
        player.saveStats("PlayerC", 1, 60)
        player.saveStats("PlayerD", 1, 40)
        player.saveStats("PlayerE", 1, 80)
        player.saveStats("PlayerF", 1, 30)
        val top5 = player.getTop5()
        assertEquals(5, top5.size)
        assertEquals("PlayerE", top5[0].username)
        assertEquals("PlayerB", top5[1].username)
        assertEquals("PlayerC", top5[2].username)
        assertEquals("PlayerA", top5[3].username)
        assertEquals("PlayerD", top5[4].username)
    }
}