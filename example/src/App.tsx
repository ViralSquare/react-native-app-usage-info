import * as React from 'react';

import { StyleSheet, View, Text, PermissionsAndroid, Linking } from 'react-native';
import { requestUsagePermission,getUsageLast24Hr,checkPackagePermission } from 'react-native-app-usage';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();

  React.useEffect(() => {
    checkPackagePermission().then(res=>{
        console.log('res==>',res);
        if(!res)
        {
          requestUsagePermission()
        }
        else{
          getUsageLast24Hr((data:any)=>{
            console.log('data==>',data);
          })
        }
        
      }).catch(error=>{
        console.log('error==>',error);
      })
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
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
