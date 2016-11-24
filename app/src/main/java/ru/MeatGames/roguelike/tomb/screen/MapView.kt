package ru.MeatGames.roguelike.tomb.screen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Typeface
import android.view.MotionEvent
import android.view.View
import ru.MeatGames.roguelike.tomb.Game
import ru.MeatGames.roguelike.tomb.Global
import ru.MeatGames.roguelike.tomb.LogClass
import ru.MeatGames.roguelike.tomb.R
import ru.MeatGames.roguelike.tomb.util.ScreenHelper
import ru.MeatGames.roguelike.tomb.util.fillFrame

class MapView(context: Context) : View(context) {

    private val mScreenWidth: Int
    private val mScreenHeight: Int

    val mMaxLogLines = 8
    var mGameEventsLog: Array<LogClass>

    val mBackgroundPaint = Paint()
    val mLightBluePaint = Paint()
    val mDarkBluePaint = Paint()
    val mSemiTransparentBackgroundPaint = Paint()
    var mLightShadowPaint = Paint()
    var mDarkShadowPaint = Paint()
    val mMenuBackgroundPaint = Paint()
    val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var mTileID: Int = 0 // tile id for ProgressBar
    var mProgressBarDuration: Int = 0
    var mProgressBarStartingTime: Long = 0

    var animTime: Long = 0
    var camx: Int = 0
    var camy: Int = 0
    var mIsItemPicked = false
    var mDrawExitDialog = false
    var mDrawLines = true
    var mDrawProgressBar = false
    var mDrawDeathScreen = false
    var mDrawWinScreen = false

    var mActionCount: Int = 0
    var mx: Int = 0
    var my: Int = 0

    private var x1: Int = 0
    private var y1: Int = 0

    private val black: Paint
    private val hud: Paint
    private val circle = false

    init {
        isFocusable = true
        isFocusableInTouchMode = true

        val screenSize = ScreenHelper.getScreenSize((context as Game).windowManager)
        mScreenWidth = screenSize.x
        mScreenHeight = screenSize.y

        mGameEventsLog = Array(mMaxLogLines, { LogClass() })

        mBackgroundPaint.color = resources.getColor(R.color.mainBackground)
        mLightBluePaint.color = resources.getColor(R.color.lightBlue)
        mDarkBluePaint.color = resources.getColor(R.color.darkBlue)
        mSemiTransparentBackgroundPaint.color = resources.getColor(R.color.semiTransparentBackground)
        mLightShadowPaint.color = resources.getColor(R.color.lightShadow)
        mDarkShadowPaint.color = resources.getColor(R.color.darkShadow)
        mMenuBackgroundPaint.color = resources.getColor(R.color.menuBackground)

        mTextPaint.color = resources.getColor(R.color.white)
        mTextPaint.style = Style.FILL
        mTextPaint.textSize = 16f
        mTextPaint.textScaleX = 1f
        mTextPaint.textAlign = Paint.Align.LEFT
        mTextPaint.typeface = Typeface.createFromAsset(Global.game!!.assets, "fonts/Bulgaria_Glorious_Cyr.ttf")

        hud = Paint()
        hud.color = resources.getColor(R.color.hud)
        black = Paint()
        black.color = resources.getColor(R.color.black)
    }

    fun initProgressBar(tileID: Int, duration: Int) {
        mProgressBarDuration = duration
        mProgressBarStartingTime = Math.abs(System.currentTimeMillis()) / 10
        mTileID = tileID
        mDrawProgressBar = true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.fillFrame(mScreenWidth, mScreenHeight, mBackgroundPaint)
        if (!mDrawDeathScreen && !mDrawWinScreen) {
            animTime = Math.abs(System.currentTimeMillis()) / 600 % 2
            drawMap(canvas)
            if (Global.hero!!.side)
                canvas.drawBitmap(Game.getHeroImg((animTime).toInt()), Global.hero!!.x.toFloat(), Global.hero!!.y.toFloat(), null)
            else
                canvas.drawBitmap(Game.getHeroImg((animTime + 2).toInt()), Global.hero!!.x.toFloat(), Global.hero!!.y.toFloat(), null)
            drawHUD(canvas)
            if (Global.game!!.lines) drawLines(canvas)
            if (circle) drawActions(canvas, mActionCount)
            if (mDrawExitDialog) drawExit(canvas)

            val currentXP = Global.hero!!.getStat(20).toFloat() / Global.hero!!.getStat(21)
            canvas.drawRect(4f, 789f, Math.round(472 * currentXP).toFloat(), 796f, mDarkBluePaint)

            if (mDrawProgressBar) drawProgressBar(canvas)
        } else {
            drawFinal(canvas)
        }
        if (mDrawLines) drawLog(canvas)
        postInvalidate()
    }

    private fun drawMap(canvas: Canvas) {
        mTextPaint.textAlign = Paint.Align.LEFT
        for (x in camx..camx + 9 - 1) {
            val cx = x - camx
            for (y in camy..camy + 9 - 1) {
                val cy = y - camy
                if (x > -1 && y > -1 && x < Global.game!!.mw && y < Global.game!!.mh && Global.map!![x][y].see) {
                    canvas.drawBitmap(Global.map!![x][y].floorImg, (Global.game!!.step * cx + 24).toFloat(), (Global.game!!.step * cy + 184).toFloat(), null)
                    canvas.drawBitmap(Global.map!![x][y].objectImg, (Global.game!!.step * cx + 24).toFloat(), (Global.game!!.step * cy + 184).toFloat(), null)
                    if (Global.map!![x][y].hasItem())
                        canvas.drawBitmap(Global.map!![x][y].itemImg, (Global.game!!.step * cx + 24).toFloat(), (Global.game!!.step * cy + 184).toFloat(), null)
                    if (Global.map!![x][y].hasMob())
                        canvas.drawBitmap(Global.map!![x][y].mob.getImg((animTime).toInt()), (Global.game!!.step * cx + 24).toFloat(), (Global.game!!.step * cy + 184).toFloat(), null)
                    if ((cx - 4 == 0 || cy - 4 == 0) && Math.abs(cx - 4) + Math.abs(cy - 4) == 3 || cx - 4 != 0 && cy - 4 != 0 && Math.abs(cx - 4) + Math.abs(cy - 4) == 4)
                        canvas.drawRect((Global.game!!.step * cx + 24).toFloat(), (Global.game!!.step * cy + 184).toFloat(), (Global.game!!.step * (cx + 1) + 24).toFloat(), (Global.game!!.step * (cy + 1) + 184).toFloat(), mLightShadowPaint)
                    if ((Math.abs(cx - 4) == 0 || Math.abs(cy - 4) == 0) && Math.abs(cx - 4) + Math.abs(cy - 4) == 4 || (Math.abs(cx - 4) != 0 || Math.abs(cy - 4) != 0) && Math.abs(cx - 4) + Math.abs(cy - 4) == 5 || Math.abs(cx - 4) == Math.abs(cy - 4) && Math.abs(cx - 4) == 3)
                        canvas.drawRect((Global.game!!.step * cx + 24).toFloat(), (Global.game!!.step * cy + 184).toFloat(), (Global.game!!.step * (cx + 1) + 24).toFloat(), (Global.game!!.step * (cy + 1) + 184).toFloat(), mDarkShadowPaint)
                }
            }
        }
        mTextPaint.textAlign = Paint.Align.CENTER
    }

    private fun drawHUD(canvas: Canvas) {
        mTextPaint.textAlign = Paint.Align.CENTER
        canvas.drawBitmap(Global.game!!.b, 43f, 745f, null)
        canvas.drawText("HP  " + Global.hero!!.getStat(5) + " / " + Global.hero!!.getStat(6), 240f, 755f, mTextPaint)
        canvas.drawText("MP  " + Global.hero!!.getStat(7) + " / " + Global.hero!!.getStat(8), 240f, 778f, mTextPaint)
        canvas.drawBitmap(Global.game!!.j, 404f, 743f, null)
        mTextPaint.textAlign = Paint.Align.LEFT
    }

    private fun drawActions(canvas: Canvas, n: Int) {
        val z = (360 / n).toFloat()
        val r = 190f
        var x: Int
        var y: Int
        canvas.drawRect(0f, 0f, 480f, 800f, mMenuBackgroundPaint)
        for (c in 0..n - 1) {
            x = (r * Math.cos(Math.toRadians((270 + z * c).toDouble()))).toInt()
            y = (r * Math.sin(Math.toRadians((270 + z * c).toDouble()))).toInt()
            canvas.drawRect((240 + x - 36).toFloat(), (400 + y - 36).toFloat(), (240 + x + 36).toFloat(), (400 + y + 36).toFloat(), black)
            canvas.drawRect((240 + x - 30).toFloat(), (400 + y - 30).toFloat(), (240 + x + 30).toFloat(), (400 + y + 30).toFloat(), mLightBluePaint)
        }
    }

    private fun drawLines(canvas: Canvas) {
        for (xq in 1..2) {
            canvas.drawLine((160 * xq).toFloat(), 48f, (160 * xq).toFloat(), 720f, mBackgroundPaint)
            canvas.drawLine(0f, (224 * xq + 48).toFloat(), 480f, (224 * xq + 48).toFloat(), mBackgroundPaint)
        }
        canvas.drawLine(0f, 48f, 480f, 48f, mBackgroundPaint)
        canvas.drawLine(240f, 0f, 240f, 48f, mBackgroundPaint)
        canvas.drawLine(0f, 720f, 480f, 720f, mBackgroundPaint)
        canvas.drawLine(120f, 720f, 120f, 800f, mBackgroundPaint)
        canvas.drawLine(360f, 720f, 360f, 800f, mBackgroundPaint)
    }

    private fun drawExit(canvas: Canvas) {
        canvas.drawRect(0f, 0f, 480f, 800f, mSemiTransparentBackgroundPaint)
        canvas.drawRect(40f, 320f, 440f, 472f, mMenuBackgroundPaint)
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = 24f
        canvas.drawText(context.getString(R.string.exit_game_message), 240f, 370f, mTextPaint)
        mTextPaint.textSize = 16f
        canvas.drawText(context.getString(R.string.yes), 140f, 444f, mTextPaint)
        canvas.drawText(context.getString(R.string.No), 340f, 444f, mTextPaint)
    }

    private fun drawLog(canvas: Canvas) {
        mTextPaint.textSize = 16f
        mTextPaint.textAlign = Paint.Align.LEFT
        for (c in 0..mMaxLogLines - 1)
            canvas.drawText(mGameEventsLog[c].t, 5f, (20 + 21 * c).toFloat(), mTextPaint)
    }

    private fun drawProgressBar(canvas: Canvas) {
        if (Math.abs(System.currentTimeMillis()) / 10 - mProgressBarStartingTime > mProgressBarDuration) {
            mDrawProgressBar = false
            mDrawLines = true
            afterProgressBar(mTileID)
        }
        canvas.drawRect(168f, (Global.hero!!.y - 35).toFloat(), 312f, (Global.hero!!.y - 11).toFloat(), mTextPaint)
        canvas.drawRect(168f, (Global.hero!!.y - 35).toFloat(), 168 + (Math.abs(System.currentTimeMillis()) / 10 - mProgressBarStartingTime) * (144f / mProgressBarDuration), (Global.hero!!.y - 11).toFloat(), mLightBluePaint)
        mTextPaint.textAlign = Paint.Align.CENTER
        canvas.drawText(context.getString(R.string.searching_label), 240f, (Global.hero!!.y - 16).toFloat(), mTextPaint)
    }

    private fun drawFinal(canvas: Canvas) {
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = 24f
        if (mDrawDeathScreen) {
            canvas.drawText(context.getString(R.string.death_from_label), 240f, 320f, mTextPaint)
            canvas.drawBitmap(Global.game!!.lastAttack, 204f, 340f, null)
        }
        if (mDrawWinScreen) {
            canvas.drawText(context.getString(R.string.victory_label), 240f, 320f, mTextPaint)
            canvas.drawText(context.getString(R.string.king_was_slain_label), 240f, 360f, mTextPaint)
        }
        mTextPaint.textSize = 16f
        canvas.drawText(context.getString(R.string.to_menu_label), 240f, 765f, mTextPaint)
    }

    private fun afterProgressBar(result: Int) {
        when (result) {
            33 -> {
                Global.game!!.fillArea(Global.hero!!.mx + mx, Global.hero!!.my + my, 1, 1, Game.getFloor(Global.hero!!.mx + mx, Global.hero!!.my + my), 35)
                Global.mapview!!.addLine(context.getString(R.string.search_chest_message))
                Global.game!!.createItem(Global.hero!!.mx + mx, Global.hero!!.my + my)
            }
            36 -> {
                Global.game!!.fillArea(Global.hero!!.mx + mx, Global.hero!!.my + my, 1, 1, Game.getFloor(Global.hero!!.mx + mx, Global.hero!!.my + my), 37)
                addLine(context.getString(R.string.search_bookshelf_message))
                if (Global.game!!.rnd.nextInt(3) != 0) {
                    addLine(context.getString(R.string.experience_earned_message))
                    Global.hero!!.modifyStat(20, Global.game!!.rnd.nextInt(4) + 2, 1)
                } else {
                    addLine(context.getString(R.string.nothing_interesting_message))
                }
            }
        }
    }

    fun clearLog() {
        for (c in 0..mMaxLogLines - 1)
            mGameEventsLog[c].t = ""
    }

    fun addLine(s: String) {
        for (c in 0..mMaxLogLines - 1)
            if (mGameEventsLog[c].t == "") {
                mGameEventsLog[c].t = s
                break
            }
    }

    fun addLine1(s: String) {
        var t = true
        for (c in 0..mMaxLogLines - 1)
            if (mGameEventsLog[c].t == s) {
                t = false
                break
            }
        if (t) addLine(s)
    }

    private fun sign(x: Int): Int {
        return if (x > 0) 1 else if (x < 0) -1 else 0
    }

    fun line(xstart: Int, ystart: Int, xend: Int, yend: Int) {
        var x: Int
        var y: Int
        var dx: Int
        var dy: Int
        val incx: Int
        val incy: Int
        val pdx: Int
        val pdy: Int
        val es: Int
        val el: Int
        var err: Int
        var v = true
        dx = xend - xstart
        dy = yend - ystart
        incx = sign(dx)
        incy = sign(dy)
        if (dx < 0)
            dx = -dx
        if (dy < 0)
            dy = -dy
        if (dx > dy) {
            pdx = incx
            pdy = 0
            es = dy
            el = dx
        } else {
            pdx = 0
            pdy = incy
            es = dx
            el = dy
        }
        x = xstart
        y = ystart
        err = el / 2
        Global.map!![x][y].see = true
        for (t in 0..el - 1) {
            err -= es
            if (err < 0) {
                err += el
                x += incx
                y += incy
            } else {
                x += pdx
                y += pdy
            }
            if (x > -1 && y > -1 && x < Global.game!!.mw && y < Global.game!!.mh) {
                if (!Global.map!![x][y].see)
                    Global.map!![x][y].see = v
                if (v)
                    Global.map!![x][y].dis = true
                if (!Global.map!![x][y].vis)
                    v = false
            }
        }
    }

    fun los(x: Int, y: Int) {
        val cm = if (camx < 0) 0 else camx
        val cm1 = if (camy < 0) 0 else camy
        for (c in cm..(if (cm + 9 >= Global.game!!.mw) Global.game!!.mw else cm + 9) - 1)
            for (c1 in cm1..(if (cm1 + 9 >= Global.game!!.mw) Global.game!!.mw else cm1 + 9) - 1)
                Global.map!![c][c1].see = false
        for (c in x - 1..x + 2 - 1)
            for (c1 in y - 1..y + 2 - 1)
                Global.map!![c][c1].see = true
        for (c in -1..1) {
            line(x, y, x + c, y - 4)
            line(x, y, x + c, y + 4)
        }
        for (c in -3..3) {
            line(x, y, x + c, y - 3)
            line(x, y, x + c, y + 3)
            line(x, y, x + c, y - 2)
            line(x, y, x + c, y + 2)
        }
        for (c in -4..-1 - 1) {
            line(x, y, x + c, y - 1)
            line(x, y, x + c, y + 1)
            line(x, y, x + Math.abs(c), y - 1)
            line(x, y, x + Math.abs(c), y + 1)
        }
        line(x, y, x - 4, y)
        line(x, y, x + 4, y)
    }

    private fun onTouchMain() {
        clearLog()
        if (y1 < 48 && x1 > 240)
            Global.game!!.changeScreen(3)
        if (y1 < 48 && x1 < 240)
            Global.game!!.lines = !Global.game!!.lines
        if (y1 > 48 && y1 < 720)
            when ((y1 - 48) / 224 * 3 + x1 / 160) {
                0 -> Global.game!!.move(-1, -1)
                1 -> Global.game!!.move(0, -1)
                2 -> Global.game!!.move(1, -1)
                3 -> Global.game!!.move(-1, 0)
                4 -> {
                    if (Global.map!![Global.hero!!.mx][Global.hero!!.my].o == 40) {
                        Game.curLvls++
                        Global.mapg!!.mapGen()
                        Global.game!!.move(0, 0)
                    }
                    if (Global.map!![Global.hero!!.mx][Global.hero!!.my].hasItem()) {
                        val item = Global.map!![Global.hero!!.mx][Global.hero!!.my].item
                        Global.hero!!.addItem(item)
                        addLine(item.n + " подобран" + item.n1)
                        Game.v.vibrate(30)
                        Global.game!!.move(0, 0)
                    }
                }
                5 -> Global.game!!.move(1, 0)
                6 -> Global.game!!.move(-1, 1)
                7 -> Global.game!!.move(0, 1)
                8 -> Global.game!!.move(1, 1)
            }
        if (y1 > 720) {
            if (x1 < 120)
                Global.game!!.changeScreen(1)
            if (x1 > 120 && x1 < 360)
                Global.game!!.changeScreen(2)
            if (x1 > 360) {
                Global.game!!.move(0, 0)
                Global.mapview!!.addLine(context.getString(R.string.turn_passed_message))
            }
        }
    }

    private fun onTouchExit() {
        if (y1 > 385 && y1 < 472 && x1 > 40 && x1 < 440)
            if (x1 < 240)
                Global.game!!.exitGame()
            else
                mDrawExitDialog = false
    }

    private fun onTouchFinal() {
        if (y1 > 720) {
            mDrawWinScreen = false
            mDrawDeathScreen = mDrawWinScreen
            Global.game!!.newGame()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP ->
                if (Global.game!!.tap) {
                    x1 = event.x.toInt()
                    y1 = event.y.toInt()
                    if (!mDrawProgressBar && !mDrawDeathScreen && !mDrawWinScreen) {
                        if (!mIsItemPicked && !mDrawExitDialog) {
                            onTouchMain()
                        } else {
                            if (mIsItemPicked)
                                if (x1 > 45 && x1 < 435 && y1 > 448 && y1 < 520) {
                                    if (x1 < 240) Global.game!!.pickupItem()
                                    mIsItemPicked = false
                                }
                            if (mDrawExitDialog) onTouchExit()
                        }
                    }
                    if (mDrawDeathScreen || mDrawWinScreen) onTouchFinal()
                }
        }
        return true
    }

}