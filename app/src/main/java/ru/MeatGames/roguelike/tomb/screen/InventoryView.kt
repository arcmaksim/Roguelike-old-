package ru.MeatGames.roguelike.tomb.screen

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import ru.MeatGames.roguelike.tomb.Game
import ru.MeatGames.roguelike.tomb.Global
import ru.MeatGames.roguelike.tomb.R
import ru.MeatGames.roguelike.tomb.model.Item
import ru.MeatGames.roguelike.tomb.util.ScreenHelper
import ru.MeatGames.roguelike.tomb.util.UnitConverter
import ru.MeatGames.roguelike.tomb.util.fillFrame
import ru.MeatGames.roguelike.tomb.view.TextButton
import java.util.*

class InventoryView(context: Context) : View(context) {

    private val mScreenWidth: Int
    private val mScreenHeight: Int

    private val mMainBackgroundPaint = Paint()
    private val mGreenBackgroundPaint = Paint()
    private val mMainTextPaint: Paint
    private val mSecondaryTextPaint: Paint

    private var sx: Int = 0 //ACTION_DOWN
    private var sy: Int = 0
    private var lx: Int = 0 //ACTION_UP,ACTION_MOVE
    private var ly: Int = 0

    private val mMaxItemsOnScreen: Int
    private var mItemsOnScreen: Int = 0

    private val mScrollDeadZone = UnitConverter.convertDpToPixels(6F, context)
    private var mCurrentScroll: Int = 0
    private var mMaxScroll: Int = 0
    private var mSavedScroll = 0
    private var scroll = false
    private var scrollPermission = true
    private var tap = false

    // flags for drawing different screens
    private var mDrawItem = false
    private var mDrawGear = false

    // vars for drawing item mItemList
    private var mItemList: LinkedList<Item?> = LinkedList()
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
    private var mSelectedItem: Item? = null
    private var mSelectedItemBitmap: Bitmap? = null

    // regions for touch input and drawing specific screen parts
    private val mScreenRect: Rect
    private val mItemListRect: Rect
    private val mPrimaryArmRect: Rect
    private val mSecondaryArmRect: Rect
    private val mBodyRect: Rect

    private val mBackButton: TextButton
    private val mLeftSoftButton: TextButton // needs proper name
    private val mMiddleSoftButton: TextButton

    // vars for touch input and drawing filter buttons
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
        mFilterIcons = listOf(Bitmap.createScaledBitmap(assetHelper.getBitmapFromAsset("weapons_icon_outline"), 30, 30, false),
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
        mItemPanelHeight = Global.game.step.toFloat()
        mItemPanelCombinedHeight = (mItemPanelHeight + mSpaceBetweenItemPanels).toInt()

        mLabelsOffsetX = mScreenWidth * 0.104F

        mScreenRect = Rect(0, 0, mScreenWidth, mScreenHeight)
        mItemListRect = Rect(mItemPanelsLeftBorder.toInt(), mItemPanelsTopBorder.toInt(), mItemPanelsRightBorder.toInt(), mItemPanelsBottomBorder.toInt())

        var top = mItemPanelsTopBorder + mSpaceBetweenItemPanels + mItemPanelHeight
        var bottom = top + mItemPanelHeight
        mPrimaryArmRect = Rect(mItemPanelsLeftBorder.toInt(), top.toInt(), mItemPanelsRightBorder.toInt(), bottom.toInt())

        top = mItemPanelsTopBorder + 2 * (mSpaceBetweenItemPanels + mItemPanelHeight)
        bottom = top + mItemPanelHeight
        mSecondaryArmRect = Rect(mItemPanelsLeftBorder.toInt(), top.toInt(), mItemPanelsRightBorder.toInt(), bottom.toInt())

        top = mItemPanelsTopBorder + 4 * (mSpaceBetweenItemPanels + mItemPanelHeight)
        bottom = top + mItemPanelHeight
        mBodyRect = Rect(mItemPanelsLeftBorder.toInt(), top.toInt(), mItemPanelsRightBorder.toInt(), bottom.toInt())

        mItemDetailsBitmapY = mItemPanelsTopBorder // using same height

        mMaxItemsOnScreen = mItemListRect.height() / mItemPanelCombinedHeight
        showItemList()
    }

    private fun setFilters(isWeaponsAllowed: Boolean, isShieldsAllowed: Boolean,
                           isArmorAllowed: Boolean, isGearAllowed: Boolean,
                           isConsumablesAllowed: Boolean) {
        mFilterStates[0] = isWeaponsAllowed
        mFilterStates[1] = isShieldsAllowed
        mFilterStates[2] = isArmorAllowed
        mFilterStates[3] = isGearAllowed
        mFilterStates[4] = isConsumablesAllowed
    }

    private fun isAllowed(item: Item): Boolean =
            mFilterStates[0] && item.isWeapon
                    || mFilterStates[1] && item.isShield
                    || mFilterStates[2] && item.isArmor
                    || mFilterStates[3] && item.isGear
                    || mFilterStates[4] && item.isConsumable

    // populating inventory mItemList according to selected flags
    fun populateItemList() {
        mItemList = LinkedList()

        Global.hero!!.mInventory?.let {
            it
                    .filter { isAllowed(it) }
                    .forEach { mItemList.add(it) }
        }

        mItemsOnScreen = mItemList.size
        mMaxScroll = if (mItemsOnScreen > mMaxItemsOnScreen) -(mItemsOnScreen - mMaxItemsOnScreen) * mItemPanelCombinedHeight else 0
    }

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

        if (mItemList.size > 0) {
            val u = -(mCurrentScroll + mSavedScroll) / mItemPanelCombinedHeight
            val offset = -(mCurrentScroll + mSavedScroll) % mItemPanelCombinedHeight
            var q = 0
            mItemList.forEach {
                if (q >= u) {
                    val top = mItemPanelsTopBorder + (q - u) * (mSpaceBetweenItemPanels + mItemPanelHeight) - offset
                    val bottom = top + mItemPanelHeight
                    val itemPanelBackground = if (!it!!.isConsumable && Global.hero!!.isEquiped(it)) {
                        mGreenBackgroundPaint
                    } else {
                        mMainBackgroundPaint
                    }
                    canvas.drawRect(mItemPanelsLeftBorder, top, mItemPanelsLeftBorder + Global.game.step, bottom, itemPanelBackground)
                    canvas.drawRect(mItemPanelsLeftBorder + Global.game.step - 1, top, mItemPanelsRightBorder, bottom, itemPanelBackground)
                    canvas.drawBitmap(it.image, mItemPanelsLeftBorder, top, null)
                    canvas.drawText(it.mTitle, 115f, top + mItemPanelHeight / 8 * 5, mSecondaryTextPaint)
                }
                q++
            }
        } else {
            canvas.drawText(context.getString(R.string.inventory_is_empty_label), mScreenWidth * 0.5F, mScreenHeight * 0.125F, mMainTextPaint)
        }

        mLeftSoftButton.mLabel = context.getString(R.string.gear_label)
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

            Global.hero!!.equipmentList[x]?.let {
                canvas.drawRect(mItemPanelsLeftBorder, top, mItemPanelsLeftBorder + Global.game.step, bottom, mMainBackgroundPaint)
                canvas.drawRect(mItemPanelsLeftBorder + Global.game.step - 1, top, mItemPanelsRightBorder, bottom, mMainBackgroundPaint)
                canvas.drawBitmap(it.image, mItemPanelsLeftBorder, top, null)
                canvas.drawText(it.mTitle, 115f, top + mItemPanelHeight * 0.5F + textVerticalPadding, mSecondaryTextPaint)
            } ?: let {
                mSecondaryTextPaint.textAlign = Paint.Align.CENTER
                canvas.drawRect(mItemPanelsLeftBorder, top, mItemPanelsRightBorder, bottom, mMainBackgroundPaint)
                canvas.drawText(context.getString(R.string.empty_label), mScreenWidth * 0.5F, top + mItemPanelHeight * 0.5F + textVerticalPadding, mSecondaryTextPaint)
                mSecondaryTextPaint.textAlign = Paint.Align.LEFT
            }

        }
        var top = mItemPanelsTopBorder + 3 * (mSpaceBetweenItemPanels + mItemPanelHeight)
        canvas.drawText(context.getString(R.string.armor_label), mItemPanelsLeftBorder + textHorizontalPadding, top + mItemPanelHeight * 0.5F + textVerticalPadding, mSecondaryTextPaint)
        top = mItemPanelsTopBorder + 4 * (mSpaceBetweenItemPanels + mItemPanelHeight)
        val bottom = top + mItemPanelHeight

        Global.hero!!.equipmentList[2]?.let {
            canvas.drawRect(mItemPanelsLeftBorder, top, mItemPanelsLeftBorder + Global.game.step, bottom, mMainBackgroundPaint)
            canvas.drawRect(mItemPanelsLeftBorder + Global.game.step - 1, top, mItemPanelsRightBorder, bottom, mMainBackgroundPaint)
            canvas.drawBitmap(it.image, mItemPanelsLeftBorder, top, null)
            canvas.drawText(it.mTitle, 115f, top + mItemPanelHeight * 0.5F + textVerticalPadding, mSecondaryTextPaint)
        } ?: let {
            mSecondaryTextPaint.textAlign = Paint.Align.CENTER
            canvas.drawRect(mItemPanelsLeftBorder, top, mItemPanelsRightBorder, bottom, mMainBackgroundPaint)
            canvas.drawText(context.getString(R.string.empty_label), mScreenWidth * 0.5F, top + mItemPanelHeight * 0.5F + textVerticalPadding, mSecondaryTextPaint)
            mSecondaryTextPaint.textAlign = Paint.Align.LEFT
        }

        mLeftSoftButton.mLabel = context.getString(R.string.inventory_label)
    }

    private fun drawItem(canvas: Canvas) {
        val temp = (mScreenWidth - mSelectedItemBitmap!!.width) * 0.5F
        val temp1 = UnitConverter.convertDpToPixels(16F, context) + mItemDetailsBitmapY + mSelectedItemBitmap!!.height
        val textHeightAdjustment = UnitConverter.convertDpToPixels(4F, context)
        canvas.drawBitmap(mSelectedItemBitmap!!, temp, mItemDetailsBitmapY, null)
        mSecondaryTextPaint.textAlign = Paint.Align.CENTER
        canvas.drawText(context.getString(R.string.empty_stat_label), temp * 0.6F, mItemDetailsBitmapY + mSelectedItemBitmap!!.height / 2 + textHeightAdjustment, mSecondaryTextPaint)
        canvas.drawText(context.getString(R.string.empty_stat_label), mScreenWidth - temp * 0.6F, mItemDetailsBitmapY + mSelectedItemBitmap!!.height / 2 + textHeightAdjustment, mSecondaryTextPaint)
        canvas.drawText(mSelectedItem!!.mTitle, mScreenWidth * 0.5F, temp1, mMainTextPaint)
        when (mSelectedItem!!.mType) {
            1 -> {
                if (!mSelectedItem!!.mProperty) {
                    canvas.drawText(context.getString(R.string.onehanded_weapon_label), mScreenWidth * 0.5F, temp1 + textHeightAdjustment * 4, mSecondaryTextPaint)
                } else {
                    canvas.drawText(context.getString(R.string.twohanded_weapon_label), mScreenWidth * 0.5F, temp1 + textHeightAdjustment * 4, mSecondaryTextPaint)
                }
                canvas.drawText("Атака +${mSelectedItem!!.mValue1}", mScreenWidth * 0.5F, temp1 + textHeightAdjustment * 7, mSecondaryTextPaint)
                canvas.drawText("Урон ${mSelectedItem!!.mValue2} - ${mSelectedItem!!.mValue3}", mScreenWidth * 0.5F, temp1 + textHeightAdjustment * 10, mSecondaryTextPaint)
            }
            2, 3 -> {
                canvas.drawText("Защита ${mSelectedItem!!.mValue1}", mScreenWidth * 0.5F, temp1 + textHeightAdjustment * 4, mSecondaryTextPaint)
                canvas.drawText("Броня ${mSelectedItem!!.mValue2}", mScreenWidth * 0.5F, temp1 + textHeightAdjustment * 7, mSecondaryTextPaint)
            }
            5 -> canvas.drawText("${Global.stats[mSelectedItem!!.mValue1].mTitle} +${mSelectedItem!!.mValue2}", mScreenWidth * 0.5F, temp1 + textHeightAdjustment * 4, mSecondaryTextPaint)
        }
        if (mSelectedItem!!.isConsumable) {
            mLeftSoftButton.mLabel = context.getString(R.string.use_label)
        } else {

            Global.hero!!.equipmentList[mSelectedItem!!.mType - 1]?.let {
                mLeftSoftButton.mLabel = if (mSelectedItem == it) {
                    context.getString(R.string.take_off_item_label)
                } else {
                    context.getString(R.string.change_equipped_item_label)
                }
            } ?: let {
                mLeftSoftButton.mLabel = context.getString(R.string.equip_item_label)
            }

        }
        mSecondaryTextPaint.textAlign = Paint.Align.LEFT
    }

    override fun onDraw(canvas: Canvas) {
        canvas.fillFrame(mScreenWidth, mScreenHeight, mMainBackgroundPaint)
        if (mDrawItem) {
            drawItem(canvas)
        } else if (mDrawGear) {
            drawGear(canvas)
        } else {
            drawFlags(canvas)
            drawList(canvas)
        }
        mLeftSoftButton.draw(canvas)
        mMiddleSoftButton.draw(canvas)
        mBackButton.draw(canvas)
        mSecondaryTextPaint.textAlign = Paint.Align.LEFT
        postInvalidate()
    }

    private fun showItemList(isWeaponsAllowed: Boolean = mFilterStates[0],
                             isShieldsAllowed: Boolean = mFilterStates[1],
                             isArmorAllowed: Boolean = mFilterStates[2],
                             isGearAllowed: Boolean = mFilterStates[3],
                             isConsumablesAllowed: Boolean = mFilterStates[4]) {
        mDrawGear = false
        mDrawItem = false
        scrollPermission = true

        mCurrentScroll = 0
        mSavedScroll = 0

        mMiddleSoftButton.mIsEnabled = false

        setFilters(isWeaponsAllowed, isShieldsAllowed, isArmorAllowed, isGearAllowed, isConsumablesAllowed)
        populateItemList()
    }

    private fun showGear() {
        mDrawGear = true
        mDrawItem = false
        scrollPermission = false

        mMiddleSoftButton.mIsEnabled = false
    }

    private fun showSelectedItem(selectedItem: Item) {
        mSelectedItem = selectedItem
        mSelectedItemBitmap = Bitmap.createScaledBitmap(selectedItem.image, 168, 168, false)
        mDrawItem = true
        scrollPermission = false
        mMiddleSoftButton.mIsEnabled = true
    }

    private fun findItem(id: Int): Item? {
        return if (mItemList.size > id) {
            mItemList[id]
        } else {
            null
        }
    }

    private fun onTouchGear(sx: Int, sy: Int) {
        if (mPrimaryArmRect.contains(sx, sy)) {
            Global.hero!!.equipmentList[0]?.let {
                showSelectedItem(it)
            } ?: let {
                showItemList(true, false, false, false, false)
            }
        }

        if (mSecondaryArmRect.contains(sx, sy)) {
            Global.hero!!.equipmentList[1]?.let {
                showSelectedItem(it)
            } ?: let {
                showItemList(false, true, false, false, false)
            }
        }

        if (mBodyRect.contains(sx, sy)) {
            Global.hero!!.equipmentList[2]?.let {
                showSelectedItem(it)
            } ?: let {
                showItemList(false, false, true, false, false)
            }
        }

        if (mLeftSoftButton.isPressed(sx, sy)) {
            showItemList()
        }

        if (mBackButton.isPressed(sx, sy)) {
            Global.game.changeScreen(0)
        }
    }

    fun onTouchItem(sx: Int, sy: Int) {
        if (mLeftSoftButton.isPressed(sx, sy)) {
            if (mSelectedItem!!.isConsumable) {
                Global.hero!!.modifyStat(mSelectedItem!!.mValue1, mSelectedItem!!.mValue2, 1)
                Global.mapview.addLine("${mSelectedItem!!.mTitle} использован${mSelectedItem!!.mTitleEnding}")
                Global.hero!!.deleteItem(mSelectedItem)
            } else {
                Global.hero!!.equipmentList[mSelectedItem!!.mType - 1]?.let {
                    if (mSelectedItem == it) {
                        Global.hero!!.takeOffItem(it)
                    } else {
                        Global.hero!!.takeOffItem(it.mType - 1)
                        Global.hero!!.equipItem(mSelectedItem)
                    }
                } ?: let {
                    Global.hero!!.equipItem(mSelectedItem)
                }
            }
            Global.game.changeScreen(0)
        }

        if (mMiddleSoftButton.isPressed(sx, sy)) {
            Global.hero!!.dropItem(mSelectedItem)
            Game.v.vibrate(30)
            Global.game.changeScreen(0)
        }

        if (mBackButton.isPressed(sx, sy)) {
            if (mDrawGear) {
                showGear()
            } else {
                showItemList()
            }
        }
    }

    fun onTouchInv(sx: Int, sy: Int) {
        if (sy < mFilterPanelBorder) { // filter buttons panel
            mFilterStates[sx / mFilterButtonsWidth.toInt()] = !mFilterStates[sx / (mScreenWidth / 5)]
            showItemList()
        }

        if (mLeftSoftButton.isPressed(sx, sy)) {
            showGear()
        }

        if (mBackButton.isPressed(sx, sy)) {
            Global.game.changeScreen(0)
        }

        if (mItemListRect.contains(sx, sy)) {
            val possibleItem = (sy - mItemPanelsTopBorder.toInt() + -mSavedScroll % mItemPanelCombinedHeight - mSavedScroll) / mItemPanelCombinedHeight
            findItem(possibleItem)?.let {
                showSelectedItem(it)
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
                        } else if (mDrawGear) {
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