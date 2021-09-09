package com.eloli.inkerbot.core.test.config

import com.eloli.inkerbot.core.setting.InkSetting
import com.eloli.inkerbot.core.test.InjectTest
import org.junit.jupiter.api.Test
import javax.inject.Inject

@InjectTest
class ConfigTest {
    @Inject
    private lateinit var setting: InkSetting
    @Test
    fun readSetting(){
        assert(setting.banner)
    }
}