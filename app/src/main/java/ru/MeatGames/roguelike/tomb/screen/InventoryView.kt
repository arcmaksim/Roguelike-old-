package ru.MeatGames.roguelike.tomb.screen

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import ru.MeatGames.roguelike.tomb.Game
import ru.MeatGames.roguelike.tomb.Global
import ru.MeatGames.roguelike.tomb.InvItem
import ru.MeatGames.roguelike.tomb.R
import ru.MeatGames.roguelike.tomb.model.Item
import ru.MeatGames.roguelike.tomb.util.ScreenHelper
import ru.MeatGames.roguelike.tomb.util.UnitConverter
import ru.MeatGames.roguelike.tomb.util.fillFrame
import ru.MeatGames.roguelike.tomb.view.TextButton

class InventoryView(context: Context) : View(context) {

    private val mScreenWidth: Int
    private val mScreenHeight: Int

    private val mMainBackgroundPaint = Paint()
    private val mGreenBackgroundPaint = Paint()
    private val mMainTextPaint: Paint
    private val mSecondaryTextPaint: Paint

    private var curItem: InvItem? = null
    private var sx: Int = 0 //ACTION_DOWN
    private var sy: Int = 0
    private var lx: Int = 0 //ACTION_UP,ACTION_MOVE
    private var ly: Int = 0
    private val mScrollDeadZone = UnitConverter.convertDpToPixels(6F, context)
    private val mMaxItemsOnScreen: Int
    private var mItemsOnScreen: Int = 0
    private var mCurrentScroll: Int = 0
    private var mMaxScroll: Int = 0
    private var mSavedScroll = 0
    private var scroll = false
    private var scrollPermission = true
    private var tap = false

    // flags for drawing different screens
    private var mDrawItem = false
    private var mDrawGear = false

    // vars for drawing item list
    private val mItemListPadding: Float
    private val mItemPanelsLeftBorder: Float
    private val mItemPanelsRightBorder: Float
    private val mItemPanelsTopBorder: Float
    private val mItemPanelsBottomBorder: Float
    private val mSpaceBetweenItemPanels: Float
    private val mItemPanelHeight: Float
    private val mItemPanelCombinedHeight: Int // ItemPanelHeight + SpaceBetweenItemPanels
    private val mItemListBorder: Float // bottom
    private val mLabelsOffsetX: Float

    // vars for drawing item details
    private val mItemDetailsBitmapY: Float
    private var img: Bitmap? = null

    private var list: InvItem? = null

    // regions for touch input and drawing specific screen parts
    private val mScreenRect: Rect
    private val mItemListRect: Rect
    private val parm: Region
    private val sarm: Region
    private val body: Region

    private val mBackButton: TextButton
    private val mLeftSoftButton: TextButton // needs proper name
    private val mMiddleSoftButton: TextButton

    private var mIsListSorted = false

    private val mFilterPanelBorder: Float
    private val mFilterButtonsWidth: Float
    private val mFilterStates: BooleanArray = BooleanArray(5, { true })
    private val mFilterIcons: List<Bitmap>

    init {
        isFocusable = true
        isFocusableInTouchMode = true

        val screenSize = ScreenHelper.getScreenSize((context as Game).windowManager)
        mScreenWidth = screenSize.x
        mScreenHeight = screenSize.y

        mMainBackgroundPaint.color = resources.getColor(R.color.mainBackground)
        mGreenBackgroundPaint.color = resources.getColor(R.color.framegrn)

        mMainTextPaint = ScreenHelper.getDefaultTextPaint(context)
        mMainTextPaint.textSize = 24f

        mSecondaryTextPaint = ScreenHelper.getDefaultTextPaint(context)
        mSecondaryTextPaint.textAlign = Paint.Align.LEFT

        mItemListPadding = UnitConverter.convertDpToPixels(16F, context)
        mItemListBorder = mScreenHeight * 0.9F

        mFilterPanelBorder = mScreenHeight * 0.075F
        mFilterButtonsWidth = mScreenWidth * 0.2F

        val assetHelper = Global.mAssetHelper
        mFilterIcons = listOf(Bitmap.createScaledBitmap(assetHelper!!.getBitmapFromAsset("weapons_icon_outline"), 30, 30, false),
                Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("weapons_icon_filling"), 30, 30, false),
                Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("shield_icon_outline"), 32, 36, false),
                Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("shield_icon_filling"), 32, 36, false),
                Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("armor_icon_outline"), 38, 34, false),
                Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("armor_icon_filling"), 38, 34, false),
                Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("gear_icon_outline"), 32, 28, false),
                Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("gear_icon_filling"), 32, 28, false),
                Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("consumables_icon_outline"), 24, 34, false),
                Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("consumables_icon_filling"), 24, 34, false))

        mLeftSoftButton = TextButton(context, resources.getString(R.string.gear_label))
        mLeftSoftButton.mTextPaint.textAlign = Paint.Align.LEFT
        mLeftSoftButton.mDimensions = Rect(0,
                (mScreenHeight * 0.9F).toInt(),
                mScreenWidth / 3,
                mScreenHeight)

        mMiddleSoftButton = TextButton(context, resources.getString(R.string.drop_item_label))
        mMiddleSoftButton.mDimensions = Rect(mScreenWidth / 3,
                (mScreenHeight * 0.9F).toInt(),
                mScreenWidth / 3 * 2,
                mScreenHeight)
        mMiddleSoftButton.mIsEnabled = false

        mBackButton = TextButton(context, resources.getString(R.string.back_label))
        mBackButton.mTextPaint.textAlign = Paint.Align.RIGHT
        mBackButton.mDimensions = Rect(mScreenWidth / 3 * 2,
                (mScreenHeight * 0.9F).toInt(),
                mScreenWidth,
                mScreenHeight)

        mItemPanelsLeftBorder = mItemListPadding
        mItemPanelsRightBorder = mScreenWidth - mItemListPadding
        mItemPanelsBottomBorder = mItemListBorder - mItemListPadding
        mItemPanelsTopBorder = mFilterPanelBorder + mItemListPadding
        mSpaceBetweenItemPanels = UnitConverter.convertDpToPixels(2F, context)
        // TODO: change to appropriate value
        mItemPanelHeight = Global.game!!.step.toFloat()
        mItemPanelCombinedHeight = (mItemPanelHeight + mSpaceBetweenItemPanels).toInt()

        mLabelsOffsetX = mScreenWidth * 0.104F

        mScreenRect = Rect(0, 0, mScreenWidth, mScreenHeight)
        mItemListRect = Rect(mItemPanelsLeftBorder.toInt(), mItemPanelsTopBorder.toInt(), mItemPanelsRightBorder.toInt(), mItemPanelsBottomBorder.toInt())

        var top = mItemPanelsTopBorder + mSpaceBetweenItemPanels + mItemPanelHeight
        var bottom = top + mItemPanelHeight
        parm = Region(mItemPanelsLeftBorder.toInt(), top.toInt(), mItemPanelsRightBorder.toInt(), bottom.toInt())

        top = mItemPanelsTopBorder + 2 * (mSpaceBetweenItemPanels + mItemPanelHeight)
        bottom = top + mItemPanelHeight
        sarm = Region(mItemPanelsLeftBorder.toInt(), top.toInt(), mItemPanelsRightBorder.toInt(), bottom.toInt())

        top = mItemPanelsTopBorder + 4 * (mSpaceBetweenItemPanels + mItemPanelHeight)
        bottom = top + mItemPanelHeight
        body = Region(mItemPanelsLeftBorder.toInt(), top.toInt(), mItemPanelsRightBorder.toInt(), bottom.toInt())

        mItemDetailsBitmapY = mItemPanelsTopBorder // using same height

        mMaxItemsOnScreen = mItemListRect.height() / mItemPanelCombinedHeight/*if (mItemListRect.height() % mItemPanelCombinedHeight != 0) {
            mItemListRect.height() / mItemPanelCombinedHeight + 1
        } else {
            mItemListRect.height() / mItemPanelCombinedHeight
        }*/
        fillList()
    }

    // populating inventory list according to selected flags
    fun fillList(weapon: Boolean, shield: Boolean, armor: Boolean, gear: Boolean, consumable: Boolean) {
        list = null
        var temp: InvItem? = null
        mCurrentScroll = 0
        mItemsOnScreen = mCurrentScroll
        if (Global.hero!!.inv != null) {
            var cur: InvItem? = Global.hero!!.inv
            while (cur != null) {
                if (isAllowed(cur.item, weapon, shield, armor, gear, consumable)) {
                    if (list == null) {
                        list = cur
                        temp = list
                    } else {
                        temp!!.nextList = cur
                        temp = temp.nextList
                    }
                    mItemsOnScreen++
                }
                cur = cur.next
            }
        }
        if (temp != null)
            temp.nextList = null
        mMaxScroll = if (mItemsOnScreen > mMaxItemsOnScreen) -(mItemsOnScreen - mMaxItemsOnScreen) * mItemPanelCombinedHeight else 0
    }

    private fun isAllowed(item: Item, weapon: Boolean, shield: Boolean, armor: Boolean, gear: Boolean, consumable: Boolean): Boolean =
            weapon && item.isWeapon || shield && item.isShield || armor && item.isArmor || gear && item.isGear || consumable && item.isConsumable

    // populating inventory list according to selected flags
    fun fillList() {
        list = null
        var temp: InvItem? = null
        mCurrentScroll = 0
        mItemsOnScreen = mCurrentScroll
        if (Global.hero!!.inv != null) {
            var cur: InvItem? = Global.hero!!.inv
            while (cur != null) {
                if (isAllowed(cur.item)) {
                    if (list == null) {
                        list = cur
                        temp = list
                    } else {
                        temp!!.nextList = cur
                        temp = temp.nextList
                    }
                    mItemsOnScreen++
                }
                cur = cur.next
            }
        }
        if (temp != null)
            temp.nextList = null
        mMaxScroll = if (mItemsOnScreen > mMaxItemsOnScreen) -(mItemsOnScreen - mMaxItemsOnScreen) * mItemPanelCombinedHeight else 0
    }

    private fun isAllowed(item: Item): Boolean =
            mFilterStates[0] && item.isWeapon
                    || mFilterStates[1] && item.isShield
                    || mFilterStates[2] && item.isArmor
                    || mFilterStates[3] && item.isGear
                    || mFilterStates[4] && item.isConsumable

    private fun drawFlags(canvas: Canvas) {
        for (i in 0..4) {
            val iconBitmap = if (!mFilterStates[i]) mFilterIcons[i * 2] else mFilterIcons[i * 2 + 1]
            canvas.drawBitmap(iconBitmap,
                    mFilterButtonsWidth * i + (mFilterButtonsWidth - iconBitmap.width) / 2,
                    (mFilterPanelBorder - iconBitmap.height) / 2,
                    null)
        }
    }

    internal fun drawList(canvas: Canvas) {
        canvas.clipRect(mItemListRect, Region.Op.REPLACE)
        var cur = list
        if (cur != null) {
            val u = -(mCurrentScroll + mSavedScroll) / mItemPanelCombinedHeight
            val offset = -(mCurrentScroll + mSavedScroll) % mItemPanelCombinedHeight
            var q = 0
            while (cur != null) {
                if (q >= u) {
                    val top = mItemPanelsTopBorder + (q - u) * (mSpaceBetweenItemPanels + mItemPanelHeight) - offset
                    val bottom = top + mItemPanelHeight
                    if (!cur.item.isConsumable && Global.hero!!.isEquiped(cur)) {
                        canvas.drawRect(mItemPanelsLeftBorder,
                                top,
                                mItemPanelsLeftBorder + Global.game!!.step,
                                bottom,
                                mGreenBackgroundPaint)
                        canvas.drawRect(mItemPanelsLeftBorder + Global.game!!.step - 1,
                                top,
                                mItemPanelsRightBorder,
                                bottom,
                                mGreenBackgroundPaint)
                    } else {
                        canvas.drawRect(mItemPanelsLeftBorder,
                                top,
                                mItemPanelsLeftBorder + Global.game!!.step,
                                bottom,
                                mMainBackgroundPaint)
                        canvas.drawRect(mItemPanelsLeftBorder + Global.game!!.step - 1,
                                top,
                                mItemPanelsRightBorder,
                                bottom,
                                mMainBackgroundPaint)
                    }
                    canvas.drawBitmap(cur.item.image, mItemPanelsLeftBorder, top, null)
                    canvas.drawText(cur.item.n, 115f, top + mItemPanelHeight / 8 * 5, mSecondaryTextPaint)
                    if (q == u + mMaxItemsOnScreen) break
                }
                q++
                cur = cur.nextList
            }
        } else {
            canvas.drawText(context.getString(R.string.inventory_is_empty_label), mScreenWidth * 0.5F, mScreenHeight * 0.125F, mMainTextPaint)
        }
        if (!mIsListSorted) {
            mLeftSoftButton.mLabel = context.getString(R.string.gear_label)
        }
        canvas.clipRect(mScreenRect, Region.Op.REPLACE)
    }

    internal fun drawGear(canvas: Canvas) {
        val textVerticalPadding = UnitConverter.convertDpToPixels(4F, context)
        val textHorizontalPadding = UnitConverter.convertDpToPixels(10F, context)
        canvas.drawText(context.getString(R.string.weapon_lebel),
                mItemPanelsLeftBorder + textHorizontalPadding,
                mItemPanelsTopBorder + mItemPanelHeight * 0.5F + textVerticalPadding,
                mSecondaryTextPaint)
        for (x in 0..1) {
            val top = mItemPanelsTopBorder + (x + 1) * (mSpaceBetweenItemPanels + mItemPanelHeight)
            val bottom = top + mItemPanelHeight
            if (Global.hero!!.equipmentList[x] != null) {
                canvas.drawRect(mItemPanelsLeftBorder, top, mItemPanelsLeftBorder + Global.game!!.step, bottom, mMainBackgroundPaint)
                canvas.drawRect(mItemPanelsLeftBorder + Global.game!!.step - 1, top, mItemPanelsRightBorder, bottom, mMainBackgroundPaint)
                canvas.drawBitmap(Global.hero!!.equipmentList[x].item.image, mItemPanelsLeftBorder, top, null)
                canvas.drawText(Global.hero!!.equipmentList[x].item.n, 115f, top + mItemPanelHeight * 0.5F + textVerticalPadding, mSecondaryTextPaint)
            } else {
                mSecondaryTextPaint.textAlign = Paint.Align.CENTER
                canvas.drawRect(mItemPanelsLeftBorder, top, mItemPanelsRightBorder, top, mMainBackgroundPaint)
                canvas.drawText(context.getString(R.string.empty_label), mScreenWidth * 0.5F, mItemPanelHeight * 0.5F + textVerticalPadding, mSecondaryTextPaint)
                mSecondaryTextPaint.textAlign = Paint.Align.LEFT
            }
        }
        var top = mItemPanelsTopBorder + 3 * (mSpaceBetweenItemPanels + mItemPanelHeight)
        canvas.drawText(context.getString(R.string.armor_label), mItemPanelsLeftBorder + textHorizontalPadding, top + mItemPanelHeight * 0.5F + textVerticalPadding, mSecondaryTextPaint)
        top = mItemPanelsTopBorder + 4 * (mSpaceBetweenItemPanels + mItemPanelHeight)
        val bottom = top + mItemPanelHeight
        if (Global.hero!!.equipmentList[2] != null) {
            canvas.drawRect(mItemPanelsLeftBorder, top, mItemPanelsLeftBorder + Global.game!!.step, bottom, mMainBackgroundPaint)
            canvas.drawRect(mItemPanelsLeftBorder + Global.game!!.step - 1, top, mItemPanelsRightBorder, bottom, mMainBackgroundPaint)
            canvas.drawBitmap(Global.hero!!.equipmentList[2].item.image, mItemPanelsLeftBorder, top, null)
            canvas.drawText(Global.hero!!.equipmentList[2].item.n, 115f, top + mItemPanelHeight * 0.5F + textVerticalPadding, mSecondaryTextPaint)
        } else {
            mSecondaryTextPaint.textAlign = Paint.Align.CENTER
            canvas.drawRect(mItemPanelsLeftBorder, top, mItemPanelsRightBorder, bottom, mMainBackgroundPaint)
            canvas.drawText(context.getString(R.string.empty_label), mScreenWidth * 0.5F, top + mItemPanelHeight * 0.5F + textVerticalPadding, mSecondaryTextPaint)
            mSecondaryTextPaint.textAlign = Paint.Align.LEFT
        }
        mLeftSoftButton.mLabel = context.getString(R.string.inventory_label)
    }

    private fun drawItem(canvas: Canvas) {
        val temp = (mScreenWidth - img!!.width) * 0.5F
        val temp1 = UnitConverter.convertDpToPixels(16F, context) + mItemDetailsBitmapY + img!!.height
        val textHeightAdjustment = UnitConverter.convertDpToPixels(4F, context)
        canvas.drawBitmap(img!!, temp, mItemDetailsBitmapY, null)
        mSecondaryTextPaint.textAlign = Paint.Align.CENTER
        canvas.drawText(context.getString(R.string.empty_stat_label), temp * 0.6F, mItemDetailsBitmapY + img!!.height / 2 + textHeightAdjustment, mSecondaryTextPaint)
        canvas.drawText(context.getString(R.string.empty_stat_label), mScreenWidth - temp * 0.6F, mItemDetailsBitmapY + img!!.height / 2 + textHeightAdjustment, mSecondaryTextPaint)
        canvas.drawText(curItem!!.item.n, mScreenWidth * 0.5F, temp1, mMainTextPaint)
        when (curItem!!.item.type) {
            1 -> {
                if (curItem!!.item.property) {
                    canvas.drawText(context.getString(R.string.onehanded_weapon_label), mScreenWidth * 0.5F, temp1 + textHeightAdjustment * 4, mSecondaryTextPaint)
                } else {
                    canvas.drawText(context.getString(R.string.twohanded_weapon_label), mScreenWidth * 0.5F, temp1 + textHeightAdjustment * 4, mSecondaryTextPaint)
                }
                canvas.drawText("Атака +" + curItem!!.item.val1, mScreenWidth * 0.5F, temp1 + textHeightAdjustment * 7, mSecondaryTextPaint)
                canvas.drawText("Урон " + curItem!!.item.val2 + " - " + curItem!!.item.val3, mScreenWidth * 0.5F, temp1 + textHeightAdjustment * 10, mSecondaryTextPaint)
            }
            2 -> {
                canvas.drawText("Защита " + curItem!!.item.val1, mScreenWidth * 0.5F, temp1 + textHeightAdjustment * 4, mSecondaryTextPaint)
                canvas.drawText("Броня " + curItem!!.item.val2, mScreenWidth * 0.5F, temp1 + textHeightAdjustment * 7, mSecondaryTextPaint)
            }
            3 -> {
                canvas.drawText("Защита " + curItem!!.item.val1, mScreenWidth * 0.5F, temp1 + textHeightAdjustment * 4, mSecondaryTextPaint)
                canvas.drawText("Броня " + curItem!!.item.val2, mScreenWidth * 0.5F, temp1 + textHeightAdjustment * 7, mSecondaryTextPaint)
            }
            5 -> canvas.drawText(Global.stats!![curItem!!.item.val1].title + " +" + curItem!!.item.val2, mScreenWidth * 0.5F, temp1 + textHeightAdjustment * 4, mSecondaryTextPaint)
        }
        mSecondaryTextPaint.textAlign = Paint.Align.LEFT
        if (mDrawGear) {
            mLeftSoftButton.mLabel = if (mIsListSorted) {
                context.getString(R.string.equip_item_label)
            } else {
                context.getString(R.string.take_off_item_label)
            }
        } else {
            if (curItem!!.item.isConsumable) {
                mLeftSoftButton.mLabel = context.getString(R.string.use_label)
            } else {
                if (Global.hero!!.equipmentList[curItem!!.item.type - 1] == null) {
                    mLeftSoftButton.mLabel = context.getString(R.string.equip_item_label)
                } else {
                    if (curItem === Global.hero!!.equipmentList[curItem!!.item.type - 1]) {
                        mLeftSoftButton.mLabel = context.getString(R.string.take_off_item_label)
                    } else {
                        mLeftSoftButton.mLabel = context.getString(R.string.change_equipped_item_label)
                    }
                }
            }
            mSecondaryTextPaint.textAlign = Paint.Align.CENTER
            mSecondaryTextPaint.textAlign = Paint.Align.LEFT
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.fillFrame(mScreenWidth, mScreenHeight, mMainBackgroundPaint)
        if (mDrawItem) {
            drawItem(canvas)
        } else if (mDrawGear && !mIsListSorted) {
            drawGear(canvas)
        } else {
            if (!mIsListSorted) drawFlags(canvas)
            drawList(canvas)
        }
        mLeftSoftButton.draw(canvas)
        mMiddleSoftButton.draw(canvas)
        mBackButton.draw(canvas)
        mSecondaryTextPaint.textAlign = Paint.Align.LEFT
        postInvalidate()
    }

    private fun findItem(id: Int): InvItem? {
        var c = 0
        var cur = list
        while (cur != null) {
            if (c++ == id)
                return cur
            cur = cur.nextList
        }
        return null
    }

    private fun onTouchGear(sx: Int, sy: Int) {
        if (parm.contains(sx, sy)) {
            if (Global.hero!!.equipmentList[0] == null) {
                fillList(true, false, false, false, false)
                scrollPermission = true
                mIsListSorted = scrollPermission
            } else {
                curItem = Global.hero!!.equipmentList[0]
                img = Bitmap.createScaledBitmap(curItem!!.item.image, 168, 168, false)
                mDrawItem = true
                scrollPermission = false
            }
            mMiddleSoftButton.mIsEnabled = true
        }
        if (sarm.contains(sx, sy)) {
            if (Global.hero!!.equipmentList[1] == null) {
                fillList(false, true, false, false, false)
                scrollPermission = true
                mIsListSorted = scrollPermission
            } else {
                curItem = Global.hero!!.equipmentList[1]
                img = Bitmap.createScaledBitmap(curItem!!.item.image, 168, 168, false)
                mDrawItem = true
                scrollPermission = false
            }
            mMiddleSoftButton.mIsEnabled = true
        }
        if (body.contains(sx, sy)) {
            if (Global.hero!!.equipmentList[2] == null) {
                fillList(false, false, true, false, false)
                scrollPermission = true
                mIsListSorted = scrollPermission
            } else {
                curItem = Global.hero!!.equipmentList[2]
                img = Bitmap.createScaledBitmap(curItem!!.item.image, 168, 168, false)
                mDrawItem = true
                scrollPermission = false
            }
            mMiddleSoftButton.mIsEnabled = true
        }
        if (mLeftSoftButton.isPressed(sx, sy)) {
            mDrawGear = false
            mCurrentScroll = 0
            scrollPermission = true
        }
        if (mBackButton.isPressed(sx, sy)) {
            Global.game!!.changeScreen(0)
        }
    }

    fun onTouchItem(sx: Int, sy: Int) {
        if (mDrawGear) {
            if (mBackButton.isPressed(sx, sy)) {
                mDrawItem = false
                mMiddleSoftButton.mIsEnabled = false
            }
            if (mLeftSoftButton.isPressed(sx, sy)) {
                if (mIsListSorted) {
                    Global.hero!!.equipItem(curItem)
                } else {
                    Global.hero!!.takeOffItem(curItem)
                }
                mIsListSorted = false
                mDrawItem = mIsListSorted
                scrollPermission = mDrawItem
                Global.game!!.changeScreen(0)
            }
        } else {
            if (mLeftSoftButton.isPressed(sx, sy)) {
                if (curItem!!.item.isConsumable) {
                    Global.hero!!.modifyStat(curItem!!.item.val1, curItem!!.item.val2, 1)
                    Global.mapview!!.addLine(curItem!!.item.n + " использован" + curItem!!.item.n1)
                    Global.hero!!.deleteItem(curItem)
                } else {
                    if (Global.hero!!.equipmentList[curItem!!.item.type - 1] == null) {
                        Global.hero!!.equipItem(curItem)
                    } else {
                        if (curItem === Global.hero!!.equipmentList[curItem!!.item.type - 1]) {
                            Global.hero!!.takeOffItem(curItem)
                        } else {
                            Global.hero!!.takeOffItem(curItem!!.item.type - 1)
                            Global.hero!!.equipItem(curItem)
                        }

                    }
                }
                mDrawItem = false
                scrollPermission = true
                Global.game!!.changeScreen(0)
            }
            if (mMiddleSoftButton.isPressed(sx, sy)) {
                Global.hero!!.dropItem(curItem)
                Game.v.vibrate(30)
                fillList()
                mDrawItem = false
                scrollPermission = true
                Global.game!!.changeScreen(0)
            }
            if (mBackButton.isPressed(sx, sy)) {
                mDrawItem = false
                scrollPermission = true
                mMiddleSoftButton.mIsEnabled = false
            }
        }
    }

    fun onTouchInv(sx: Int, sy: Int) {
        var id = 0
        if (mIsListSorted) {
            if (mBackButton.isPressed(sx, sy)) {
                scrollPermission = false
                fillList()
                mIsListSorted = false
            }
        } else {
            if (sy < mFilterPanelBorder) { // filter buttons panel
                mFilterStates[sx / mFilterButtonsWidth.toInt()] = !mFilterStates[sx / (mScreenWidth / 5)]
                fillList()
                mCurrentScroll = 0
                mSavedScroll = mCurrentScroll
            }
            if (mBackButton.isPressed(sx, sy)) {
                Global.game!!.changeScreen(0)
            }
            if (mLeftSoftButton.isPressed(sx, sy)) {
                mDrawGear = true
                mCurrentScroll = 0
            }
        }
        if (mItemListRect.contains(sx, sy)) {
            if (mMaxScroll == 0) {
                id = (sy - mItemPanelsTopBorder.toInt()) / mItemPanelCombinedHeight
                if (id < mItemsOnScreen) {
                    curItem = findItem(id)
                    img = Bitmap.createScaledBitmap(curItem!!.item.image, 168, 168, false)
                    scrollPermission = false
                    mDrawItem = true
                    mMiddleSoftButton.mIsEnabled = true
                }
            } else {
                //id = (sy - 72 + -mSavedScroll % mItemPanelCombinedHeight) / mItemPanelCombinedHeight + -mSavedScroll / mItemPanelCombinedHeight
                id = (sy - mItemPanelsTopBorder.toInt() + -mSavedScroll % mItemPanelCombinedHeight - mSavedScroll) / mItemPanelCombinedHeight
                curItem = findItem(id)
                img = Bitmap.createScaledBitmap(curItem!!.item.image, 168, 168, false)
                scrollPermission = false
                mDrawItem = true
                mMiddleSoftButton.mIsEnabled = true
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                sx = event.x.toInt()
                lx = sx
                sy = event.y.toInt()
                ly = sy
                tap = true
            }
            MotionEvent.ACTION_MOVE -> {
                lx = event.x.toInt()
                ly = event.y.toInt()
                if (Math.abs(lx - sx) > mScrollDeadZone || Math.abs(ly - sy) > mScrollDeadZone) {
                    if (tap)
                        tap = false
                    if (scrollPermission && !scroll) {
                        scroll = true
                        mCurrentScroll = 0
                        sx = lx
                        sy = ly
                    }
                }
                if (scroll) {
                    if (mMaxScroll != 0) {
                        if (ly - sy + mSavedScroll < mMaxScroll) {
                            mCurrentScroll = mMaxScroll - mSavedScroll
                        } else if (ly - sy + mSavedScroll > 0) {
                            mCurrentScroll = -mSavedScroll
                        } else {
                            mCurrentScroll = ly - sy
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!scroll) {
                    if (tap)
                        if (mDrawItem) {
                            onTouchItem(sx, sy)
                        } else if (mDrawGear && !mIsListSorted) {
                            onTouchGear(sx, sy)
                        } else
                            onTouchInv(sx, sy)
                } else {
                    mSavedScroll += mCurrentScroll
                    mCurrentScroll = 0
                }
                tap = false
                scroll = tap
            }
        }
        return true
    }

}