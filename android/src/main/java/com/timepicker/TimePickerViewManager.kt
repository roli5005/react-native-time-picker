package com.timepicker

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

  private val mDelegate = TimePickerViewManagerDelegate(this)

  override fun getDelegate(): ViewManagerDelegate<TimePickerView> = mDelegate
  override fun getName(): String = NAME
  override fun createViewInstance(context: ThemedReactContext): TimePickerView = TimePickerView(context)

  @ReactProp(name = "value", defaultDouble = 0.0)
  override fun setValue(view: TimePickerView, value: Double) {
    if (value > 0) view.setValue(value)
  }

  @ReactProp(name = "minuteInterval", defaultInt = 1)
  override fun setMinuteInterval(view: TimePickerView, value: Int) {
    view.setMinuteInterval(value)
  }

  @ReactProp(name = "locale")
  override fun setLocale(view: TimePickerView, value: String?) {
    view.setLocale(value ?: "")
  }

  @ReactProp(name = "textColor", customType = "Color")
  override fun setTextColor(view: TimePickerView, value: Int?) {
    value?.let { view.setTextColor(it) }
  }

  @ReactProp(name = "fontSize", defaultFloat = 0f)
  override fun setFontSize(view: TimePickerView, value: Float) {
    if (value > 0f) view.setFontSize(value)
  }


  companion object {
    const val NAME = "TimePickerView"
  }
}
