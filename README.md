# JaHA - Just another Housekeeping App

### Preparations
Please add a secrets.xml-File under app/src/main/res/values with the following content
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="google_maps_api_key">place_your_google_maps_api_key</string>
</resources>
```

To use the fake_keystore please create a keystore.properties-File in the root-folder with the following content:
```
keyAlias=MyAndroidKeystore
keyPassword=MyAndroidKeystore
storeFile=your/path/to/the/fake_keystore.jks
storePassword=fake_keystore
```