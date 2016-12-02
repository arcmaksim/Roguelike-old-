package ru.MeatGames.roguelike.tomb.screen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import ru.MeatGames.roguelike.tomb.Game
import ru.MeatGames.roguelike.tomb.Global
import ru.MeatGames.roguelike.tomb.LogClass
import ru.MeatGames.roguelike.tomb.R
import ru.MeatGames.roguelike.tomb.util.ScreenHelper
import ru.MeatGames.roguelike.tomb.util.fillFrame

class MapView(mContext: Context) : View(mContext) {

    val mScreenWidth: Int
    val mScreenHeight: Int

    val mMaxLogLines = 8
    var mGameEventsLog: Array<LogClass>

    val mBackgroundPaint = Paint()
    val mLightBluePaint = Paint()
    val mDarkBluePaint = Paint()
    val mSemiTransparentBackgroundPaint = Paint()
    var mLightShadowPaint = Paint()
    var mDarkShadowPaint = Paint()
    val mMenuBackgroundPaint = Paint()
    val mTextPaint: Paint

    var mTileID: Int = 0 // tile id for ProgressBar
    var mProgressBarDuration: Int = 0
    var mProgressBarStartingTime: Long = 0

    var animTime: Long = 0
    var camx: Int = 0
    var camy: Int = 0
    var mDrawExitDialog = false
    var mDrawLog = true
    var mDrawProgressBar = false
    var mDrawDeathScreen = false
    var mDrawWinScreen = false
    val mDrawActionsMenu = false

    var mIsItemPicked = false // currently not used

    var mActionCount: Int = 0

    // hero'single move directions
    var mx: Int = 0
    var my: Int = 0

    val black: Paint

    val mTempTileSize = Global.game!!.step

    init {
        isFocusable = true
        isFocusableInTouchMode = true

        val screenSize = ScreenHelper.getScreenSize((mContext as Game).windowManager)
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

        mTextPaint = ScreenHelper.getDefaultTextPaint(mContext)
        mTextPaint.textAlign = Paint.Align.LEFT

        black = Paint()
        black.color = resources.getColor(R.color.black)

        Global.hero!!.x = (mScreenWidth - mTempTileSize) / 2
        Global.hero!!.y = (mScreenHeight - mTempTileSize) / 2
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
            if (mDrawActionsMenu) drawActionsMenu(canvas, mActionCount)
            if (mDrawExitDialog) drawExitDialog(canvas)

            val currentXP = Global.hero!!.getStat(20).toFloat() / Global.hero!!.getStat(21)
            canvas.drawRect(4F, mScreenHeight - 11F, Math.round(mScreenWidth * 0.99F * currentXP).toFloat(), mScreenHeight - 4F, mDarkBluePaint)

            if (mDrawProgressBar) drawProgressBar(canvas)
        } else {
            drawFinalScreen(canvas)
        }
        if (mDrawLog) drawLog(canvas)
        postInvalidate()
    }

    private fun drawMap(canvas: Canvas) {
        mTextPaint.textAlign = Paint.Align.LEFT

        val maxDrawnMapWidth = mTempTileSize * 9
        val maxDrawnMapHeight = maxDrawnMapWidth
        val offsetX = (mScreenWidth - maxDrawnMapWidth) / 2
        val offsetY = (mScreenHeight - maxDrawnMapHeight) / 2

        for (x in camx..camx + 9 - 1) {
            val cx = x - camx

            for (y in camy..camy + 9 - 1) {
                val cy = y - camy

                if (x > -1 && y > -1 && x < Global.game!!.mw && y < Global.game!!.mh && Global.map!![x][y].see) {
                    val currentPixelXtoDraw = (Global.game!!.step * cx + offsetX).toFloat()
                    val currentPixelYtoDraw = (Global.game!!.step * cy + offsetY).toFloat()
                    val leftDrawBorder = (Global.game!!.step * (cx + 1) + offsetX).toFloat()
                    val rightDrawBorder = (Global.game!!.step * (cy + 1) + offsetY).toFloat()

                    canvas.drawBitmap(Global.map!![x][y].floorImg, currentPixelXtoDraw, currentPixelYtoDraw, null)
                    canvas.drawBitmap(Global.map!![x][y].objectImg, currentPixelXtoDraw, currentPixelYtoDraw, null)

                    if (Global.map!![x][y].hasItem())
                        canvas.drawBitmap(Global.map!![x][y].itemImg, currentPixelXtoDraw, currentPixelYtoDraw, null)

                    if (Global.map!![x][y].hasMob())
                        canvas.drawBitmap(Global.map!![x][y].mob.getImg((animTime).toInt()), currentPixelXtoDraw, currentPixelYtoDraw, null)

                    if ((cx - 4 == 0 || cy - 4 == 0) && Math.abs(cx - 4) + Math.abs(cy - 4) == 3 || cx - 4 != 0 && cy - 4 != 0 && Math.abs(cx - 4) + Math.abs(cy - 4) == 4)
                        canvas.drawRect(currentPixelXtoDraw, currentPixelYtoDraw, leftDrawBorder, rightDrawBorder, mLightShadowPaint)

                    if ((Math.abs(cx - 4) == 0 || Math.abs(cy - 4) == 0) && Math.abs(cx - 4) + Math.abs(cy - 4) == 4 || (Math.abs(cx - 4) != 0 || Math.abs(cy - 4) != 0) && Math.abs(cx - 4) + Math.abs(cy - 4) == 5 || Math.abs(cx - 4) == Math.abs(cy - 4) && Math.abs(cx - 4) == 3)
                        canvas.drawRect(currentPixelXtoDraw, currentPixelYtoDraw, leftDrawBorder, rightDrawBorder, mDarkShadowPaint)
                }
            }
        }
        mTextPaint.textAlign = Paint.Align.CENTER
    }

    private fun drawHUD(canvas: Canvas) {
        mTextPaint.textAlign = Paint.Align.CENTER

        canvas.drawBitmap(Global.game!!.mInventoryIcon,
                (mScreenWidth / 8 - Global.game!!.mInventoryIcon.width / 2).toFloat(),
                mScreenHeight * 0.9F + (mScreenHeight * 0.1F - Global.game!!.mInventoryIcon.height) / 2,
                null)
        canvas.drawText("HP  " + Global.hero!!.getStat(5) + " / " + Global.hero!!.getStat(6),
                mScreenWidth * 0.5F,
                mScreenHeight * 0.94F,
                mTextPaint)
        canvas.drawText("MP  " + Global.hero!!.getStat(7) + " / " + Global.hero!!.getStat(8),
                mScreenWidth * 0.5F,
                mScreenHeight * 0.965F,
                mTextPaint)
        canvas.drawBitmap(Global.game!!.mSkipTurnIcon,
                (mScreenWidth / 8 * 7 - Global.game!!.mSkipTurnIcon.width / 2).toFloat(),
                mScreenHeight * 0.9F + (mScreenHeight * 0.1F - Global.game!!.mSkipTurnIcon.height) / 2,
                null)
        mTextPaint.textAlign = Paint.Align.LEFT
    }

    // currently not used
    private fun drawActionsMenu(canvas: Canvas, n: Int) {
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
        val temp = (mScreenHeight - (mScreenHeight + mScreenWidth) * 0.1F) / 3
        for (i in 1..2) {
            canvas.drawLine((mScreenWidth / 3 * i).toFloat(),
                    mScreenWidth * 0.1F,
                    (mScreenWidth / 3 * i).toFloat(),
                    mScreenHeight * 0.9F,
                    mBackgroundPaint)
            canvas.drawLine(0F,
                    temp * i + 48,
                    mScreenWidth.toFloat(),
                    temp * i + 48,
                    mBackgroundPaint)
        }
        canvas.drawLine(0F, mScreenWidth * 0.1F, mScreenWidth.toFloat(), mScreenWidth * 0.1F, mBackgroundPaint) // apparently it needs to be exactly width, not height
        canvas.drawLine(mScreenWidth * 0.5F, 0F, mScreenWidth * 0.5F, mScreenWidth * 0.1F, mBackgroundPaint)
        canvas.drawLine(0F, mScreenHeight * 0.9F, mScreenWidth.toFloat(), mScreenHeight * 0.9F, mBackgroundPaint)
        canvas.drawLine(mScreenWidth * 0.25F, mScreenHeight * 0.9F, mScreenWidth * 0.25F, mScreenHeight.toFloat(), mBackgroundPaint)
        canvas.drawLine(mScreenWidth * 0.75F, mScreenHeight * 0.9F, mScreenWidth * 0.75F, mScreenHeight.toFloat(), mBackgroundPaint)
    }

    private fun drawExitDialog(canvas: Canvas) {
        canvas.fillFrame(mScreenWidth, mScreenHeight, mSemiTransparentBackgroundPaint)
        canvas.drawRect(mScreenWidth * 0.05F, mScreenHeight * 0.4F, mScreenWidth * 0.95F, mScreenHeight * 0.59F, mMenuBackgroundPaint)
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = 24f
        canvas.drawText(context.getString(R.string.exit_game_message), mScreenWidth * 0.5F, mScreenHeight * 0.46F, mTextPaint)
        mTextPaint.textSize = 16f
        canvas.drawText(context.getString(R.string.yes), mScreenWidth * 0.29F, mScreenHeight * 0.555F, mTextPaint)
        canvas.drawText(context.getString(R.string.No), mScreenWidth * 0.71F, mScreenHeight * 0.555F, mTextPaint)
    }

    private fun drawLog(canvas: Canvas) {
        mTextPaint.textSize = 16f
        mTextPaint.textAlign = Paint.Align.LEFT
        for (c in 0..mMaxLogLines - 1)
            canvas.drawText(mGameEventsLog[c].t, 5f, (20 + 21 * c).toFloat(), mTextPaint)
    }

    // needs to be directly above the hero
    // so without autoscale of assets leave same height as before
    private fun drawProgressBar(canvas: Canvas) {
        if (Math.abs(System.currentTimeMillis()) / 10 - mProgressBarStartingTime > mProgressBarDuration) {
            mDrawProgressBar = false
            mDrawLog = true
            afterProgressBar(mTileID)
        }
        val offsetX = mScreenWidth * 0.5F - mTempTileSize * 1.5F
        canvas.drawRect(offsetX,
                (Global.hero!!.y - 35).toFloat(),
                offsetX + mTempTileSize * 3,
                (Global.hero!!.y - 11).toFloat(),
                mTextPaint)
        canvas.drawRect(offsetX,
                (Global.hero!!.y - 35).toFloat(),
                offsetX + (Math.abs(System.currentTimeMillis()) / 10 - mProgressBarStartingTime) * (mTempTileSize * 3F / mProgressBarDuration),
                (Global.hero!!.y - 11).toFloat(),
                mLightBluePaint)
        mTextPaint.textAlign = Paint.Align.CENTER
        canvas.drawText(context.getString(R.string.searching_label), mScreenWidth * 0.5F, (Global.hero!!.y - 16).toFloat(), mTextPaint)
    }

    private fun drawFinalScreen(canvas: Canvas) {
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = 24f
        if (mDrawDeathScreen) {
            canvas.drawText(context.getString(R.string.death_from_label), mScreenWidth * 0.5F, mScreenHeight * 0.4F, mTextPaint)
            canvas.drawBitmap(Global.game!!.lastAttack, (mScreenWidth - Global.game!!.lastAttack.width) * 0.5F, mScreenHeight * 0.425F, null)
        }
        if (mDrawWinScreen) {
            canvas.drawText(context.getString(R.string.victory_label),  mScreenWidth * 0.5F, mScreenHeight * 0.4F, mTextPaint)
            canvas.drawText(context.getString(R.string.king_was_slain_label),  mScreenWidth * 0.5F, mScreenHeight * 0.45F, mTextPaint)
        }
        mTextPaint.textSize = 16f
        canvas.drawText(context.getString(R.string.to_menu_label),  mScreenWidth * 0.5F, mScreenHeight * 0.95F, mTextPaint)
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

    fun calculateLineOfSight(x: Int, y: Int) {
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

    private fun onTouchMain(touchX: Int, touchY: Int) {
        clearLog()

        if (touchY < mScreenWidth * 0.1F && touchX > mScreenWidth * 0.5F)
            Global.game!!.changeScreen(3)

        if (touchY < mScreenWidth * 0.1F && touchX < mScreenWidth * 0.5F)
            Global.game!!.lines = !Global.game!!.lines

        val temp = (mScreenHeight - (mScreenHeight + mScreenWidth) * 0.1F) / 3
        if (touchY > mScreenWidth * 0.1F && touchY < mScreenHeight * 0.9F) {

            val x = touchX / (mScreenWidth / 3)
            val y = ((touchY - mScreenWidth * 0.1F) / temp).toInt()

            when (x + y * 3) {
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
        }

        if (touchY > mScreenHeight * 0.9F) {

            if (touchX < mScreenWidth * 0.25F)
                Global.game!!.changeScreen(1)

            if (touchX > mScreenWidth * 0.25F && touchX < mScreenWidth * 0.75F)
                Global.game!!.changeScreen(2)

            if (touchX > mScreenWidth * 0.75F) {
                Global.game!!.move(0, 0)
                Global.mapview!!.addLine(context.getString(R.string.turn_passed_message))
            }
        }
    }

    private fun onTouchExitDialog(touchX: Int, touchY: Int) {
        if (touchY > mScreenHeight * 0.48F && touchY < mScreenHeight * 0.59F && touchX > mScreenWidth * 0.05F && touchX < mScreenWidth * 0.95F)
            if (touchX < mScreenWidth * 0.5F)
                Global.game!!.exitGame()
            else
                mDrawExitDialog = false
    }

    private fun onTouchFinal(touchX: Int, touchY: Int) {
        if (touchY > mScreenHeight * 0.9F) {
            mDrawWinScreen = false
            mDrawDeathScreen = mDrawWinScreen
            Global.game!!.newGame()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP ->
                if (Global.game!!.tap) {
                    val touchX = event.x.toInt()
                    val touchY = event.y.toInt()
                    if (!mDrawProgressBar && !mDrawDeathScreen && !mDrawWinScreen) {
                        if (/*!mIsItemPicked && */!mDrawExitDialog) {
                            onTouchMain(touchX, touchY)
                        } else {
                            /*if (mIsItemPicked)
                                if (touchX > 45 && touchX < 435 && touchY > 448 && touchY < 520) {
                                    if (touchX < mScreenWidth * 0.5F) Global.game!!.pickupItem()
                                    mIsItemPicked = false
                                }*/
                            if (mDrawExitDialog) onTouchExitDialog(touchX, touchY)
                        }
                    }
                    if (mDrawDeathScreen || mDrawWinScreen) onTouchFinal(touchX, touchY)
                }
        }
        return true
    }

}