import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-app-usage' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const AppUsage = NativeModules.AppUsage
  ? NativeModules.AppUsage
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: number, b: number): Promise<number> {
  return AppUsage.multiply(a, b);
}
export function subtract(a: number, b: number): Promise<number> {
  return AppUsage.subtract(a, b);
}
export function getUsageLast24Hr(callBack:any) {
  return AppUsage.getUsageLast24Hr(callBack);
}
export function checkPackagePermission(): Promise<boolean> {
  return AppUsage.checkPackagePermission();
}
export function requestUsagePermission(): void {
  return AppUsage.requestUsagePermission();
}

export default {multiply,subtract,checkPackagePermission,getUsageLast24Hr,requestUsagePermission}
