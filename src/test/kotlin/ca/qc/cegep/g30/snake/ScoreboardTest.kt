package ca.qc.cegep.g30.snake

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.io.File

class ScoreboardTest {

    private lateinit var scoreboard: Scoreboard
    private val testFilePath = "src/main/GameFiles/Scoreboard.txt"

    @BeforeEach
    fun setUp() {
        scoreboard = Scoreboard()
        File(testFilePath).writeText("")
    }

    @Test
    fun testGetLeaders() {
        val players = arrayListOf(Player("Alice", 0, 100), Player("Bob", 0, 200))
        val leaders = scoreboard.getLeaders(players)
        assertEquals(2, leaders.size)
        assertEquals("Alice", leaders[0].username)
        assertEquals("Bob", leaders[1].username)
    }

    @Test
    fun testSort() {
        val players = arrayListOf(Player("Charlie", 0, 150), Player("Alice", 0, 100), Player("Bob", 0, 200))
        scoreboard.getLeaders(players)
        scoreboard.sort()
        assertEquals("Alice", scoreboard.top5[0].username)
        assertEquals("Bob", scoreboard.top5[1].username)
        assertEquals("Charlie", scoreboard.top5[2].username)
    }

    @Test
    fun testPopulateTop5() {
        val players = arrayListOf(Player("Alice", 0, 100), Player("Bob", 0, 200))
        scoreboard.getLeaders(players)
        scoreboard.populateTop5()
        val expectedContent = "Alice\n100\n===\nBob\n200\n===\n"
        assertEquals(expectedContent.trim(), File(testFilePath).readText().trim())
    }

    @Test
    fun testGetTop5FromFile() {
        val players = arrayListOf(Player("Alice", 0, 100), Player("Bob", 0, 200))
        scoreboard.getLeaders(players)
        scoreboard.populateTop5()
        val leaderboardString = scoreboard.getTop5FromFile()
        assertTrue(leaderboardString.contains("1) Alice scored 100"))
        assertTrue(leaderboardString.contains("2) Bob scored 200"))
    }

    @Test
    fun testGetTop5FromFileEmpty() {
        val leaderboardString = scoreboard.getTop5FromFile()
        assertEquals("", leaderboardString.trim())
    }
}