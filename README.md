# react-native-app-usage

This package will get the app usage stats

## Installation

```sh
yarn add react-native-app-usage
```

## Usage

```js
import { requestUsagePermission,getUsageLast24Hr,checkPackagePermission } from 'react-native-app-usage';

// ...

 checkPackagePermission().then(permissionGranted=>{
        if(!permissionGranted)
        {
          requestUsagePermission() ;// If permission not granted then request for permission
        }
        else{
          getUsageLast24Hr((data)=>{
            let allAppUsage=data; // You can get all app usage data of past 24 Hrs
          })
        }
        
      }).catch(error=>{
        console.log('error==>',error);
      })

```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
