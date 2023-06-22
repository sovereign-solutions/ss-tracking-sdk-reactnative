import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import {
  initTracking,
  isTrackingSdk,
  startTracking,
  stopTracking,
  setAuthenInfo,
} from 'ss-tracking-sdk-reactnative';

export default function App() {
  React.useEffect(() => {
    initTracking(
      'https://testing.skedulomatic.com',
      '/api/app-base/vdms-tracking/push',
      'bearer EPiAx6m-.....',
      'thanh13',
      1,
      'Tracking',
      'Tracking',
      true
    );
    setAuthenInfo('https://testing.skedulomatic.com','AEkdwq-....', '0');
  }, []);

  const startTrackingHandler = () => {
    console.log('reactnative startTracking');
    startTracking();
  };
  const stopTrackingHandler = () => {
    console.log('reactnative stopTracking');
    stopTracking();
  };

  const getIsTrackingHandler = async () => {
    const isTracking = await isTrackingSdk();
    console.log('isTrackingSdk', isTracking);
  };

  return (
    <View style={styles.container}>
      <Button title="Start tracking" onPress={() => startTrackingHandler()} />
      <Button title="Pause tracking" onPress={() => stopTrackingHandler()} />
      <Button title="Get is tracking" onPress={() => getIsTrackingHandler()} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
