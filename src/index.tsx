/* eslint-disable prettier/prettier */
import { NativeModules, Platform } from 'react-native';
import DeviceInfo from 'react-native-device-info';
const LINKING_ERROR =
  `The package 'ss-tracking-sdk-reactnative' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const SsTrackingSdkReactnative = NativeModules.SsTrackingSdkReactnative
  ? NativeModules.SsTrackingSdkReactnative
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: number, b: number): Promise<number> {
  return SsTrackingSdkReactnative.multiply(a, b);
}

export function initTracking(
  apiUrl: String,
  trackingUri: String,
  token: String,
  driver: String,
  trackingStatus: number,
  androidTitle?: String,
  androiContent?: String,
  useActivityRegconition?: Boolean
): void {
  const trackingUrl = apiUrl + '' + trackingUri;
  if (Platform.OS === 'android') {
    SsTrackingSdkReactnative.init();
    SsTrackingSdkReactnative.setApiUrl(trackingUrl);
    SsTrackingSdkReactnative.setToken('bearer ' + token);
    SsTrackingSdkReactnative.setTrackingDriver(driver);
    SsTrackingSdkReactnative.setTrackingStatus(trackingStatus);
    SsTrackingSdkReactnative.setLocationUpdateFrequency(10000);
    SsTrackingSdkReactnative.setTrackingInterval(10000);
    SsTrackingSdkReactnative.setTitleAndContentTracking(
      androidTitle,
      androiContent
    );
    SsTrackingSdkReactnative.setUseActivityRegconition(useActivityRegconition === true);
  } else {
    SsTrackingSdkReactnative.configData(
      apiUrl,
      trackingUrl,
      token,
      driver,
      trackingStatus.toString()
    );
    SsTrackingSdkReactnative.setDeviceId(DeviceInfo.getUniqueIdSync());
    SsTrackingSdkReactnative.setTrackingStatus(trackingStatus);
    SsTrackingSdkReactnative.setUseMotionSensor(useActivityRegconition === true);
  }
}

export function setAuthenInfo(
  authenURL: string,
  refreshToken: string,
  expiredIn: string
): void {
  SsTrackingSdkReactnative.setAuthenInfo(authenURL, refreshToken, expiredIn);
}

export function setTrackingFrequency(miliseconds: number): void {
  if (Platform.OS === 'android') {
    SsTrackingSdkReactnative.setTrackingInterval(miliseconds);
  } else {
    SsTrackingSdkReactnative.setTrackingFrequency(miliseconds);
  }
}

// export function setCurrentActivityAndTracking(): void {
//   if (Platform.OS === 'android') {
//     SsTrackingSdkReactnative.setCurrentActivityAndTracking();
//   }
// }

export function startTracking(): void {
  SsTrackingSdkReactnative.startTracking();
}

export function stopTracking(): void {
  SsTrackingSdkReactnative.stopTracking();
}

export function removeTracking(): void {
    SsTrackingSdkReactnative.removeTracking();
}

export function setTitleAndContentTracking(
  newTitle: String,
  newContent: String
): void {
  if (Platform.OS === 'android') {
    SsTrackingSdkReactnative.setTitleAndContentTracking(newTitle, newContent);
  }
}

export function isTrackingSdk(): Promise<boolean> {
  return SsTrackingSdkReactnative.isTrackingSdk();
}

export function logTracking(logType: number): void {
  if (Platform.OS === 'android') {
    SsTrackingSdkReactnative.logTracking(logType);
  }
}

export function getLogDataByType(type: number): Promise<String> {
  if (Platform.OS === 'android') {
    return SsTrackingSdkReactnative.getLogDataByType(type);
  }
  return Promise.resolve('');
}

export function getLogDataByFunctionName(
  functionName: String
): Promise<string> {
  if (Platform.OS === 'android') {
    return SsTrackingSdkReactnative.getLogDataByFunctionName(functionName);
  } else return Promise.resolve('');
}

export function getLogByData(): Promise<string> {
  if (Platform.OS === 'android') {
    return SsTrackingSdkReactnative.getLogByData();
  } else return Promise.resolve('');
}

export function setLogLimitDay(days: number): void {
  if (Platform.OS === 'android') {
    SsTrackingSdkReactnative.setLogLimitDay(days);
  }
}

export function enableLoggingGPS(enabled: boolean): void {
  if (Platform.OS === 'android') {
    SsTrackingSdkReactnative.enableLoggingGPS(enabled);
  }
}

// export function setTrackingDriver(driver: String): void {
//   SsTrackingSdkReactnative.setTrackingDriver(driver);
// }

export function setTrackingStatus(status: number): void {
  SsTrackingSdkReactnative.setTrackingStatus(status);
}

export function setTrackerId(trackerId: String): void {
  if (Platform.OS === 'android') {
    SsTrackingSdkReactnative.setTrackerId(trackerId);
  } else {
    SsTrackingSdkReactnative.setDeviceId(trackerId);
  }
}

export function setEmitEvent(enable: boolean): void {
  if (Platform.OS === 'android') {
  } else {
    if (enable) {
      SsTrackingSdkReactnative.enableEmitter();
    } else {
      SsTrackingSdkReactnative.disableEmitter();
    }
  }
}

export function sendLocation(lat: number, lng: number) {
  if (Platform.OS === 'android') {
    SsTrackingSdkReactnative.sendLocation(lat, lng);
  }
}


export function isIgnoreBatteryOptimization(): boolean {
  if (Platform.OS === 'android') {
    return SsTrackingSdkReactnative.isIgnoreBatteryOptimization();
  }
  return false;
}

export function requestIgnoreBatteryOptimization(): void {
  if (Platform.OS === 'android') {
    SsTrackingSdkReactnative.requestIgnoreBatteryOptimization();
  }
}
