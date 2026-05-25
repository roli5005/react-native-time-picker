import {
  type ColorValue,
  type NativeSyntheticEvent,
  type ViewProps,
} from 'react-native';
import NativeTimePicker from './TimePickerViewNativeComponent';

type Props = ViewProps & {
  value?: Date;
  minuteInterval?: number;
  locale?: string;
  textColor?: ColorValue;
  fontSize?: number;
  onValueChange?: (date: Date) => void;
};

export function TimePickerView({ value, onValueChange, ...rest }: Props) {
  return (
    <NativeTimePicker
      {...rest}
      value={value?.getTime()}
      onTimeChange={(e: NativeSyntheticEvent<{ timestamp: number }>) =>
        onValueChange?.(new Date(e.nativeEvent.timestamp))
      }
    />
  );
}
