import { View, type ColorValue, type ViewProps } from 'react-native';

type Props = ViewProps & {
  value?: Date;
  minuteInterval?: number;
  locale?: string;
  textColor?: ColorValue;
  fontSize?: number;
  onValueChange?: (date: Date) => void;
};

export function TimePickerView({ style, ...rest }: Props) {
  return <View {...rest} style={style} />;
}
