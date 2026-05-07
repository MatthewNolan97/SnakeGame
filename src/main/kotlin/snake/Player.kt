package ca.qc.cegep.g30.snake

import java.io.File

class Player(val username: String, gp: Int, hs: Int) {
    private val playerFile = File("src/main/GameFiles/Players.txt")
    private var playerList: ArrayList<Player> = ArrayList()
    private var gamesPlayed = gp
    var pr = hs

    fun saveStats(uname: String, games: Int, record: Int) {
        if (!playerInList(uname)) {
            val saveLine = "Username: $uname\n Games Played: $games\n PR: $record \n===\n"
            playerFile.appendText(saveLine)
        } else {
            overWritePlayerScore(uname, games, record)
        }
    }

    fun incrementGP() {
        gamesPlayed++
    }

    fun getTheGP(): Int = gamesPlayed
    fun getThePR(): Int = pr
    fun setThePR(rec: Int) {
        pr = rec
    }

    private fun overWritePlayerScore(name: String, gp: Int, rec: Int) {
        val seperatedPlayers = playerFile.readText().trim().split("===")
        var newOutput = ""
        for (seperatedPlayer in seperatedPlayers) {
            val lines = seperatedPlayer.trim().lines()
            if (lines.isEmpty()) continue
            if (lines.size < 3) continue
            val username = lines[0].substringAfter(":").trim()
            if (name == username) newOutput += "Username: $name\n Games Played: $gp\n PR: $rec\n===\n"
            else newOutput += seperatedPlayer.trim() + "\n===\n"
        }
        playerFile.writeText(newOutput)
    }

    fun loadPlayers(): ArrayList<Player> {
        playerList.clear()
        val separatedPlayers = playerFile.readText().trim().split("===")
        for (separatedPlayer in separatedPlayers) {
            val lines = separatedPlayer.trim().lines()
            if (lines.isEmpty() || lines.size < 3) continue
            val username = lines[0].trim().substringAfter(":").trim()
            val gamesPlayed = lines[1].trim().substringAfter(":").trim().toInt()
            val highScore = lines[2].trim().substringAfter(":").trim().toInt()
            val currentPlayer = Player(username, gamesPlayed, highScore)
            playerList.add(currentPlayer)
        }
        return playerList
    }

    fun getTop5(): ArrayList<Player> {
        val top5List = ArrayList<Player>()
        var bottomScore = 0
        for (player in playerList) {
            if (top5List.size < 5) top5List.add(player)
            else if (top5List.size == 5) {
                top5List.sortWith(compareBy<Player> { it.pr }.reversed())
                bottomScore = top5List.get(4).getThePR()
                if (player.getThePR() > bottomScore) top5List.set(4, player)
            }
        }
        top5List.sortWith(compareBy<Player> { it.pr }.reversed())
        return top5List
    }

    private fun playerInList(name: String): Boolean {
        val list = loadPlayers()
        for (player in list) {
            if (player.username == name) return true
        }
        return false
    }
}