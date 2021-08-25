# kron

一个基于 `kotlin-multiplatform` 的 cron解析、生成工具。


*暂时不支持对 `星期(day of week)` 的解析，只支持通配符 `*`。*

```kotlin
val cron = resolveCron("1-3 0 0 1 * *")

val executor = cron.executor()
var i = 1
for (instant in executor) {
    println(instant.toLocalDateTime(TimeZone.currentSystemDefault()))
    if (i++ >= 10) {
        break
    }
}
```



