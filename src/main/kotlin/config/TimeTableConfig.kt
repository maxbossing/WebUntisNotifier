package config

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

typealias TimeTableConfig = @Serializable Map<LocalTime, Int>