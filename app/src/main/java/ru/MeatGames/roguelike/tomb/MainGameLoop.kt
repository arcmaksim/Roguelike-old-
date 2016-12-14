package ru.MeatGames.roguelike.tomb

class MainGameLoop : Runnable {

    @Volatile private var isRunning = true

    public fun terminate() {
        isRunning = false
    }

    override fun run() {
        while (isRunning) {
            if (--Global.hero!!.init == 0) {
                Global.game.newTurnCount()
                if (--Global.hero!!.cregen == 0) {
                    Global.hero!!.cregen = Global.hero!!.regen
                    if (Global.hero!!.getStat(5) != Global.hero!!.getStat(6)) {
                        Global.hero!!.modifyStat(5, 1, 1)
                    }
                }
                Global.game.move = true
                Global.game.turn = true
                Global.game.tap = true
                while (Global.game.tap) {}
            }
            if (Global.game.firstMob != null)
                while (Global.game.firstMob.turnCount <= Global.game.turnCount) {
                    val temp = Global.game.firstMob
                    Global.game.firstMob = Global.game.firstMob.next
                    if (Math.abs(temp.x - Global.hero!!.mx) < 5 && Math.abs(temp.y - Global.hero!!.my) < 5) {
                        Global.game.mobTurn(temp)
                    }
                    Global.game.addInQueue(temp)
                }
            Global.game.turnCount++
        }
    }
}