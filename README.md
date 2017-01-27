# Buttons
A library with a large amount of themable styled buttons.

For testing and experimentation purposes, a sample apk can be downloaded [here](https://github.com/TheAndroidMaster/Buttons/releases).

## Usage

### Adding A Button In XML
To add a button to a layout file in XML, simply add a button like normal, except replace the tag with `james.buttons.Button` like below.

``` xml
<james.buttons.Button
    android:id="@+id/button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="I\`M SO FANCY, YOU ALREADY KNOW" />
```

### Changing The Background Color
When a `Button` is created, the default background color is solid black. To change this, add an `app:backgroundColor` attribute like below.

``` xml
<james.buttons.Button
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="I\'M IN THE FAST LANE FROM L.A. TO TOKYO"
    app:backgroundColor="#FAFAFA"/>
```


You can also change the color programatically using the `Button.setBackgroundColor(int)` method like below.

``` java
Button button = (Button) findViewById(R.id.button);
button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
```

By default, the button will automatically assign a text color that contrasts the current background when a color is set. To disable that, simply call `Button.setBackgroundColor(int, boolean)` like below.

``` java
Button button = (Button) findViewById(R.id.button);
button.setTextColor(R.color.colorPrimary);
button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent), false);
```

### Changing The Text Color
The text color can be changed by using the `Button.setTextColor(int)` method. It is not currently possible to set a custom text color in XML.

### Changing The Button Style
To change the button type use the `app:backgroundType` attribute like below. There are currently four different styles: `solid`, `outline`, `round`, and `roundOutline`.

``` xml
<james.buttons.Button
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="I LIKE TRAINS"
    app:backgroundStyle="outline"/>
```

The style can also be changed programatically by using the `Button.setBackgroundType(Button.Type)` or `Button.setBackgroundType(Button.Type, boolean)` method like below.

``` java
Button button = (Button) findViewById(R.id.button);
button.setBackgroundType(Button.Type.OUTLINE);
```

### Enabling/Disabling Ripples
Ripple animations will be visible by default (with the exception of outlined buttons - see issue [#6](https://github.com/TheAndroidMaster/Buttons/issues/6)). To disable them, use the `Button.setRippleEnabled(boolean)` method. The ripple color is determined automatically based on the current text color and button style.
