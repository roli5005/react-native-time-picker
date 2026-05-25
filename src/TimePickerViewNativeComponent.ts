import {
  codegenNativeComponent,
  type ColorValue,
  type ViewProps,
} from 'react-native';
import type {
  Int32,
  Double,
  Float,
  DirectEventHandler,
} from 'react-native/Libraries/Types/CodegenTypes';

interface OnChangeEvent {
  timestamp: Double;
}

interface NativeProps extends ViewProps {
  value?: Double;
  minuteInterval?: Int32;
  locale?: string;
  textColor?: ColorValue;
  fontSize?: Float;
  onTimeChange?: DirectEventHandler<OnChangeEvent>;
}

export default codegenNativeComponent<NativeProps>('TimePickerView');
