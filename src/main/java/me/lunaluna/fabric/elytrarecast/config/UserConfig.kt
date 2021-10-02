package me.lunaluna.fabric.elytrarecast.config

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import fi.dy.masa.malilib.config.ConfigUtils
import fi.dy.masa.malilib.config.IConfigBase
import fi.dy.masa.malilib.config.IConfigHandler
import fi.dy.masa.malilib.config.options.ConfigBoolean
import fi.dy.masa.malilib.config.options.ConfigHotkey
import fi.dy.masa.malilib.config.options.ConfigInteger
import fi.dy.masa.malilib.util.FileUtils
import fi.dy.masa.malilib.util.JsonUtils
import me.lunaluna.fabric.elytrarecast.Reference
import java.io.File

object UserConfig : IConfigHandler {

    private const val CONFIG_FILE_NAME = "${Reference.MOD_ID}.json"

    object Recasting {
        val ENABLED = ConfigBoolean("enabled", true, "Is the recast functionality enabled?")
        val COOLDOWN = ConfigInteger("cooldown", 4, 1, 20, "The amount of milliseconds until the next relaunch attempt")

        val OPTIONS = listOf<IConfigBase>(
            ENABLED,
            COOLDOWN
        )
    }

    object Jumping {
        val ENABLED = ConfigBoolean("enabled", true, "Is the jump cooldown reset functionality enabled?")
        val COOLDOWN = ConfigInteger("cooldown", 2, 1, 20, "The amount of ticks until the next auto jump")

        val OPTIONS = listOf<IConfigBase>(
            ENABLED,
            COOLDOWN
        )
    }

    object Hotkeys {
        val OPEN_CONFIG = ConfigHotkey("Open Config", "E,C", "Hotkey to open the config")
        val TOGGLE_RECAST = ConfigHotkey("Toggle Recast", "", "Hotkey to toggle the automatic recasting")
        val TOGGLE_JUMP = ConfigHotkey("Toggle Jump", "", "Hotkey to toggle the jump cooldown reset")

        val OPTIONS = listOf<IConfigBase>(
            OPEN_CONFIG,
            TOGGLE_RECAST,
            TOGGLE_JUMP
        )
    }

    override fun load() {
        val configFile = File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME)
        if (configFile.exists() && configFile.isFile && configFile.canRead()) {
            val element: JsonElement? = JsonUtils.parseJsonFile(configFile)
            if (element != null && element.isJsonObject) {
                val root = element.asJsonObject
                ConfigUtils.readConfigBase(root, "Generic", Recasting.OPTIONS)
                ConfigUtils.readConfigBase(root, "Hotkeys", Hotkeys.OPTIONS)
            }
        }
    }

    override fun save() {
        val dir: File = FileUtils.getConfigDirectory()
        if (dir.exists() && dir.isDirectory || dir.mkdirs()) {
            val root = JsonObject()
            ConfigUtils.writeConfigBase(root, "Generic", Recasting.OPTIONS)
            ConfigUtils.writeConfigBase(root, "Hotkeys", Hotkeys.OPTIONS)
            JsonUtils.writeJsonToFile(root, File(dir, CONFIG_FILE_NAME))
        }
    }
}