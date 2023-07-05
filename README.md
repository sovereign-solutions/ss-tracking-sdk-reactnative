# ss-tracking-sdk-reactnative

ss tracking sdk for react native

## Installation

```sh
npm install ss-tracking-sdk-reactnative
```

IOS:

- In your info.plist add these keys and descriptions: NSLocationWhenInUseUsageDescription, NSLocationAlwaysAndWhenInUseUsageDescription, NSLocationAlwaysUsageDescription, NSMotionUsageDescription

Android:

- Open AndroidManifest.xml and add this under Application tag:
  ```xml
  <service android:name="com.sovereign.trackingsdk.TrackingService"
       android:foregroundServiceType="location"/>
  <service android:name="com.sovereign.trackingsdk.ARIntentService"/>
  ```
- add some permissions:
  ```xml
   <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
   <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
   <uses-permission android:name="android.permission.INTERNET" />
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
   <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
   <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
   <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
   <uses-permission android:name="com.google.android.gms.permission.ACTIVITY_RECOGNITION" />
   <uses-permission android:name="android.permission.ACTIVITY_RECOGNITION" />
  ```

````
Authen:

    ApiUrl = HOST + "/api/app-base/vdms-tracking/push" // https://testing.skedulomatic.com/api/app-base/vdms-tracking/push
    LoginUrl = AUTHURL + "/oauth/token"                // https://accounts.skedulomatic.com/oauth/token

    use the LoginUrl with your username and password to get the `access token`, `refresh token`
    `curl --location 'https://accounts.skedulomatic.com/oauth/token' \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --data-urlencode 'grant_type=password' \
    --data-urlencode 'username=<username>' \
    --data-urlencode 'password=<password>'
    `

## Usage

```js
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
// init

initTracking(
      'https://testing.skedulomatic.com',
      '/api/app-base/vdms-tracking/push',
      'bearer EPiAx6m-.....',
      'thanh13',
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
// start tracking
    const startTrackingHandler = () => {
        console.log('startTracking');
        startTracking();
    };
// stop tracking
    const stopTrackingHandler = () => {
        console.log('reactnative stopTracking');
        stopTracking();
    };
````

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
