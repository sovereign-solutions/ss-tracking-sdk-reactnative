import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import {
  initTracking,
  isTrackingSdk,
  startTracking,
  stopTracking,
  setAuthenInfo,
  setTrackerId,
  setTrackingStatus,
} from 'ss-tracking-sdk-reactnative';
import DeviceInfo from 'react-native-device-info';
export default function App() {
  React.useEffect(() => {
    const user = 'thanh13';
    initTracking(
      'https://testing.skedulomatic.com',
      '/api/app-base/vdms-tracking/push',
      'bearer EPiAx6m-.....',
      user,
      2,
      'Tracking',
      'Tracking',
      true
    );
    setAuthenInfo('https://testing.skedulomatic.com','AEkdwq-....', '0');
    //setTrackerId
    setTrackerId(DeviceInfo.getUniqueId() + '@' + user);
    //use this to change device status (1: active, 2: idle)
    //setTrackingStatus(2);
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
