import ru.otus.otuskotlin.marketplace.biz.MkplProfileProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings

data class MkplAppSettings(
    val appUrls: List<String> = emptyList(),
    val corSettings: MkplCorSettings = MkplCorSettings(),
    val processor: MkplProfileProcessor = MkplProfileProcessor(settings = corSettings),
)