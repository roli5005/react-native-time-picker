import React, { useEffect } from 'react';
import { View, StyleSheet } from 'react-native';
import { TimePickerView } from 'react-native-time-picker';

export default function App() {
  const [time, setTime] = React.useState(new Date());

  useEffect(() => {
    console.log('TIME CHANGED', time.toTimeString());
  }, [time]);

  return (
    <View style={styles.container}>
      <TimePickerView
        minuteInterval={5}
        value={time}
        textColor={'white'}
        style={styles.picker}
        locale="en-US"
        fontSize={25}
        onValueChange={setTime}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  picker: {
    width: 200,
    height: 150,
    backgroundColor: '#212533',
    borderRadius: 10,
    overflow: 'hidden',
  },
});
