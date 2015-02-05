date-interval-picker
====================

A nice little date interval picker for Android. It's implemented as a Fragment, so it should be
possible to use in an activity, a dialog or whatever you wish. Supports Android SDK version 16 and
up.

#usage
1. Add the dependency to your build.gradle
    ```
    repositories {
        mavenCentral()
    }

    dependencies {
        compile 'no.hyper:dateintervalpicker:1.0.0@aar' //please note the magic '@aar' at the end
    }
    ```
2. Add the DateIntervalPicker fragment to your layout
    ```
        <fragment
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:name="no.hyper.dateintervalpicker.DateIntervalPicker"
            android:id="@+id/picker_fragment">
        </fragment>
    ```
3. Profit
