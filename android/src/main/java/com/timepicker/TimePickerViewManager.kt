package com.timepicker

import android.graphics.Color
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewManagerDelegate
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.viewmanagers.TimePickerViewManagerInterface
import com.facebook.react.viewmanagers.TimePickerViewManagerDelegate

@ReactModule(name = TimePickerViewManager.NAME)
class TimePickerViewManager : SimpleViewManager<TimePickerView>(),
  TimePickerViewManagerInterface<TimePickerView> {
  private val mDelegate: ViewManagerDelegate<TimePickerView>

  init {
    mDelegate = TimePickerViewManagerDelegate(this)
  }

  override fun getDelegate(): ViewManagerDelegate<TimePickerView>? {
    return mDelegate
  }

  override fun getName(): String {
    return NAME
  }

  public override fun createViewInstance(context: ThemedReactContext): TimePickerView {
    return TimePickerView(context)
  }

  @ReactProp(name = "color")
  override fun setColor(view: TimePickerView?, color: Int?) {
    view?.setBackgroundColor(color ?: Color.TRANSPARENT)
  }

  companion object {
    const val NAME = "TimePickerView"
  }
}
