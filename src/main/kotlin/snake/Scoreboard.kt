package ca.qc.cegep.g30.snake

import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class Scoreboard {
    var top5: LinkedList<Player> = LinkedList()
    private val scoreboardFile = File("src/main/GameFiles/Scoreboard.txt")

    fun getLeaders(list: ArrayList<Player>): LinkedList<Player> {
        for (player in list) top5.add(player)
        return top5
    }

    fun sort() {
        top5.sortWith(compareBy { it.username })
    }

    fun populateTop5() {
        var top5String = ""
        for (player in top5) {
            top5String += "${player.username}\n${player.pr}\n===\n"
        }
        scoreboardFile.writeText(top5String)
    }

    fun getTop5FromFile(): String {
        var count = 0
        var leaderBoardString = ""
        val seperatedPlayers = scoreboardFile.readText().trim().split("===")
        for (seperatedPlayer in seperatedPlayers) {
            count++
            val lines = seperatedPlayer.trim().lines()
            if (lines.isEmpty()) continue
            if (lines.size < 2) continue
            val username = lines[0].trim()
            val highScore = lines[1].trim()
            leaderBoardString += "$count) $username scored $highScore\n"
        }
        return leaderBoardString
    }


}