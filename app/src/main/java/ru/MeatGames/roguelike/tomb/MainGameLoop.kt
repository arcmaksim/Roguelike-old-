package ru.MeatGames.roguelike.tomb

class MainGameLoop : Runnable {

    @Volatile private var isRunning = true

    fun terminate() {
        isRunning = false
    }

    override fun run() {
        while (isRunning) {
            if (--Global.hero!!.init == 0) {
                if (--Global.hero!!.cregen == 0) {
                    Global.hero!!.cregen = Global.hero!!.regen
                    if (Global.hero!!.getStat(5) != Global.hero!!.getStat(6)) {
                        Global.hero!!.modifyStat(5, 1, 1)
                    }
                    if (Global.hero!!.isFullyHealed()) {
                        Global.hero!!.finishResting()
                    }
                }
                Global.game.updateHeroTurnCount(Global.hero!!.mIsResting)
                Global.game.mIsPlayerTurn = true
                Global.game.mIsPlayerMoved = false
                Global.game.mAcceptPlayerInput = true
                while (Global.game.mIsPlayerTurn) {
                    if (Global.hero!!.mIsResting) {
                        Thread.sleep(100)
                        if (Global.hero!!.mIsResting) {
                            Global.game.skipTurn()
                        } else {
                            Global.hero!!.interruptResting()
                        }
                    }
                }
            }
            Global.game.firstMob?.let {
                while (Global.game.firstMob.turnCount <= Global.game.turnCount) {
                    val temp = Global.game.firstMob
                    Global.game.firstMob = Global.game.firstMob.next
                    if (Math.abs(temp.x - Global.hero!!.mx) < 5 && Math.abs(temp.y - Global.hero!!.my) < 5) {
                        Global.game.mobTurn(temp)
                    }
                    Global.game.addInQueue(temp)
                }
            }
            Global.game.turnCount++
        }
    }
}