# react-native-time-picker

A React Native library providing native time picker components for iOS and Android.

- **iOS**: uses `UIDatePicker` in wheels style
- **Android**: uses `NumberPicker` spinners (hour, minute, AM/PM)
- Supports React Native **New Architecture (Fabric)** only

## Requirements

- React Native 0.73+
- New Architecture enabled

## Installation

```sh
npm install react-native-time-picker
```

### iOS

```sh
cd ios && pod install
```

### Android

No extra steps required.

## Usage

```tsx
import { TimePickerView } from '@roli5005/react-native-time-picker';

export default function App() {
  const [time, setTime] = React.useState(new Date());

  return (
    <TimePickerView
      value={time}
      onValueChange={(date) => setTime(date)}
    />
  );
}
```

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `value` | `Date` | current time | The selected time |
| `onValueChange` | `(date: Date) => void` | — | Called when the user changes the time |
| `minuteInterval` | `number` | `1` | Interval between minute values: `1`, `2`, `3`, `4`, `5`, `6`, `10`, `12`, `15`, `20`, `30` |
| `locale` | `string` | device locale | BCP 47 locale tag (e.g. `"en-US"`, `"de-DE"`). Controls 12/24h format |
| `textColor` | `ColorValue` | system default | Color of the picker text and dividers |
| `fontSize` | `number` | system default | Font size for picker text (Android only) |
| `style` | `ViewStyle` | — | Style applied to the container view |

## Examples

**24-hour format:**
```tsx
<TimePickerView
  value={time}
  onValueChange={setTime}
  locale="de-DE"
/>
```

**5-minute intervals with custom color:**
```tsx
<TimePickerView
  value={time}
  onValueChange={setTime}
  minuteInterval={5}
  textColor="#ffffff"
/>
```

## Contributing

- [Development workflow](CONTRIBUTING.md#development-workflow)
- [Sending a pull request](CONTRIBUTING.md#sending-a-pull-request)
- [Code of conduct](CODE_OF_CONDUCT.md)

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
