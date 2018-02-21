DynamicView
===========

一个支持动态解析XML布局的库，通过这个库，可以动态渲染类似下面的布局
```xml
<VBox>
    <TitleView title="Hello world" logo="{logo}" />
    <BodyView />
    <FooterView />
</VBox>
```

## 组件说明


### 布局节点

布局的节点并不能直接使用Android Framework或者其他第三方的控件，必须要重新实现节点对应的控件类。
这么做的原因是考虑到性能问题，复杂的布局解析比较耗时，会影响到APP的用户体验

### Simple Binding

```kotlin
verticalLayout {
    editText {
        bind { text("name", mode = TwoWay) }
    }
    button {
        bind { click("hello") }
    }
}
class SimpleViewModel() : ViewModel() {
    @delegate:Property
    var name: String by Delegates.property("Jason")

    // all the parameter for Command is optional, first parameter pass by event Observable, second parameter is lambda (Boolean) -> Unit
    @Command
    val hello(canExecute: (Boolean) -> Unit) {
        toast("Hello, ${name}!")
    }
}
```

### Multiple Binding

```kotlin
//login button enabled only when name and password not empty
class ArrayToBooleanConverter : MultipleConverter<Boolean> {
    override fun convert(params: Array<Any>): Boolean {
        params.forEach {
            if(it.toString().isEmpty()) return false
        }
        return true
    }
}
verticalLayout {
    editText {
        bind { text("name", mode = TwoWay) }
    }
    editText {
            bind { text("password", mode = TwoWay) }
        }
    button {
        bind { enabled("name", "password", mode = OneWay, converter = ArrayToBooleanConverter()) }
        bind { click("login") }
    }
}
class LoginViewModel() : ViewModel() {
    @delegate:Property
    var name: String by Delegates.property("xxx@xxxx.co")

    @delegate:Property
    var password: String by Delegates.property("xxxxxx")

    @Command
    val login() {
        //login processing
    }
}
```

### DependencyProperty and ExtractProperty
```kotlin
// @DependencyProperty will generate binding for nameAndSymbol depends on stock, stock changes then nameAndSymbol changes
// @ExtractProperty will generate binding for stock properties, for example code below, Property name and price will generated. If hasPrefix = true, then Property stock.name stock.price will generated.
class StockViewModel() : ViewModel() {
    @delegate:ExtractProperty(
        "name", "price",
        hasPrefix = false
    )
    var stock: Stock? by Delegates.property()

    @delegate:DependencyProperty("stock")
    var nameAndSymbol: String by Delegates.property { stock?.name + stock?.symbol }
}
```

### Wait/Until

```kotlin
//wait/until just like OneTime binding, but it need apply action, for example below, it wait for market from model, then decide how to display
relativeLayout {
    wait { until("market", converter = viewOfMarket) { inflate(it, this@verticalLayout) }  }
}
```

## Extend Binding Property

Event

```kotlin
    fun View.click(path: String) : PropertyBinding = commandBinding(path, clicks(), enabled())
```

Property

```kotlin
    fun View.enabled(vararg paths: String, mode: OneWay = BindingMode.OneWay, converter: OneWayConverter<Boolean> = EmptyOneWayConverter()) : PropertyBinding = oneWayPropertyBinding(paths, enabled(), false, converter)

    //this implements four binding mode for TextView, if just need OneWay mode, remove last three lines, some for other mode
    fun TextView.text(vararg paths: String, mode: OneWay = BindingMode.OneWay, converter: OneWayConverter<out CharSequence> = EmptyOneWayConverter()) : PropertyBinding = oneWayPropertyBinding(paths, text(), false, converter)
    fun TextView.text(vararg paths: String, mode: OneTime, converter: OneWayConverter<out CharSequence> = EmptyOneWayConverter()) : PropertyBinding = oneWayPropertyBinding(paths, text(), true, converter)
    fun TextView.text(path: String, mode: OneWayToSource, converter: OneWayConverter<*> = EmptyOneWayConverter<String>()) : PropertyBinding = oneWayPropertyBinding(path, textChanges2(), converter)
    fun TextView.text(path: String, mode: TwoWay, converter: TwoWayConverter<String, *> = EmptyTwoWayConverter<String, String>()) : PropertyBinding = twoWayPropertyBinding(path, textChanges2(), text(), converter)
```

## Using with Gradle
```gradle

dependencies {
    compile 'com.benny.library:kbinding:0.2.3'
    kapt 'com.benny.library:kbinding-compiler:0.2.3'

    // for common bindings, View, TextView, and ...
    compile 'com.benny.library:kbinding-common:0.2.3'
    // for recyclerview bindings
    compile 'com.benny.library:kbinding-adapterview:0.2.3'
    // for support v4 bindings
    compile 'com.benny.library:kbinding-support-v4:0.2.3'
}
```

## Contribute

Now is just the beginning of KBinding, so everyone interested in this library, just fork it and pull requests to me.
Let's make it a little better.

## Discussion

### QQ Group: 516157585