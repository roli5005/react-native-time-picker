package com.timepicker

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.format.DateFormat
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.events.Event
import java.util.Calendar
import java.util.Locale

// Fabric event
class OnChangeEvent(surfaceId: Int, viewId: Int, private val timestamp: Double)
  : Event<OnChangeEvent>(surfaceId, viewId) {
  override fun getEventName() = "topTimeChange"
  override fun getEventData(): WritableMap = Arguments.createMap().apply {
    putDouble("timestamp", timestamp)
  }
}

class TimePickerView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

  private val reactContext = context as ReactContext

  private val hourPicker = NumberPicker(context)
  private val minutePicker = NumberPicker(context)
  private val amPmPicker = NumberPicker(context)
  private val separator = TextView(context).apply { text = ":" }

  private var is24Hour = DateFormat.is24HourFormat(context)
  private var minuteInterval = 1
  private var textColor: Int? = null
  // Guard against emitting during programmatic setValue
  private var isSettingValue = false

  init {
    orientation = HORIZONTAL
    gravity = Gravity.CENTER

    hourPicker.wrapSelectorWheel = true
    hourPicker.setSelectionDividerHeight(0)
    minutePicker.wrapSelectorWheel = true
    minutePicker.setSelectionDividerHeight(0)
    amPmPicker.wrapSelectorWheel = false
    amPmPicker.setSelectionDividerHeight(0)
    amPmPicker.minValue = 0
    amPmPicker.maxValue = 1
    amPmPicker.displayedValues = arrayOf("AM", "PM")

    refreshHourRange()
    refreshMinuteValues()
    rebuildLayout()
    attachListeners()
  }

  fun setMinuteInterval(interval: Int) {
    if (interval == minuteInterval) return
    minuteInterval = interval
    refreshMinuteValues()
  }

  fun setLocale(localeString: String) {
    val locale = if (localeString.isEmpty()) Locale.getDefault()
                 else Locale.forLanguageTag(localeString.replace('_', '-'))
    val new24Hour = is24HourLocale(locale)
    if (new24Hour == is24Hour) return
    is24Hour = new24Hour
    refreshHourRange()
    rebuildLayout()
    attachListeners()
  }

  fun setValue(timestampMs: Double) {
    isSettingValue = true
    val cal = Calendar.getInstance().apply { timeInMillis = timestampMs.toLong() }
    val hour = cal.get(Calendar.HOUR_OF_DAY)
    val minute = cal.get(Calendar.MINUTE)

    if (is24Hour) {
      hourPicker.value = hour
    } else {
      val h12 = if (hour % 12 == 0) 12 else hour % 12
      hourPicker.value = h12
      amPmPicker.value = if (hour < 12) 0 else 1
    }
    // Snap minute to nearest interval slot
    val slot = (minute / minuteInterval).coerceIn(0, minutePicker.maxValue)
    minutePicker.value = slot
    isSettingValue = false
  }

  private fun emitChange() {
    if (isSettingValue) return
    val cal = Calendar.getInstance()
    val minuteValue = minutePicker.value * minuteInterval
    if (is24Hour) {
      cal.set(Calendar.HOUR_OF_DAY, hourPicker.value)
    } else {
      val isPm = amPmPicker.value == 1
      val h = hourPicker.value % 12 + if (isPm) 12 else 0
      cal.set(Calendar.HOUR_OF_DAY, h)
    }
    cal.set(Calendar.MINUTE, minuteValue)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)

    val dispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, id) ?: return
    val surfaceId = UIManagerHelper.getSurfaceId(this)
    dispatcher.dispatchEvent(OnChangeEvent(surfaceId, id, cal.timeInMillis.toDouble()))
  }

  private fun attachListeners() {
    val onScrollIdle = NumberPicker.OnScrollListener { _, scrollState ->
      if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) emitChange()
    }
    hourPicker.setOnScrollListener(onScrollIdle)
    minutePicker.setOnScrollListener(onScrollIdle)
    amPmPicker.setOnScrollListener(onScrollIdle)
  }

  private fun refreshHourRange() {
    if (is24Hour) { hourPicker.minValue = 0; hourPicker.maxValue = 23 }
    else          { hourPicker.minValue = 1; hourPicker.maxValue = 12 }
  }

  private fun refreshMinuteValues() {
    val values = (0 until 60 step minuteInterval)
      .map { String.format("%02d", it) }.toTypedArray()
    minutePicker.minValue = 0
    minutePicker.maxValue = values.size - 1
    minutePicker.displayedValues = values
  }

  fun setTextColor(color: Int) {
    textColor = color
    listOf(hourPicker, minutePicker, amPmPicker).forEach {
      applyTextColor(it)
    }
  }

  fun setFontSize(sp: Float) {
    val px = sp * resources.displayMetrics.scaledDensity
    listOf(hourPicker, minutePicker, amPmPicker).forEach { it.setTextSize(px) }
  }

  private fun applyTextColor(picker: NumberPicker) {
    val color = textColor ?: return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      picker.textColor = color
    } else {
      try {
        val field = NumberPicker::class.java.getDeclaredField("mSelectorWheelPaint")
        field.isAccessible = true
        (field.get(picker) as? Paint)?.color = color
        picker.invalidate()
      } catch (_: Exception) {}
    }
  }

  private fun rebuildLayout() {
    removeAllViews()
    addView(hourPicker)
    addView(separator)
    addView(minutePicker)
    if (!is24Hour) addView(amPmPicker)
    textColor?.let { c ->
      listOf(hourPicker, minutePicker, amPmPicker).forEach {
        applyTextColor(it)
      }
    }
  }

  private fun is24HourLocale(locale: Locale): Boolean {
    val pattern = DateFormat.getBestDateTimePattern(locale, "jm")
    return !pattern.contains("a")
  }
}
