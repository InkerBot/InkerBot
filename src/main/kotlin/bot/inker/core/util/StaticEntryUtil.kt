package bot.inker.core.util

import bot.inker.api.plugin.PluginMeta
import com.google.inject.Injector
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class StaticEntryUtil private constructor() {
  companion object {
    const val INKERBOT_ENTRY_CLASS = "bot.inker.api.InkerBot"
    fun applyInjector(classLoader: ClassLoader, injector: Injector) {
      try {
        val clazz = classLoader.loadClass(INKERBOT_ENTRY_CLASS)
        val injectorField = clazz.getDeclaredField("realInjector")
        injectorField.isAccessible = true
        injectorField[null] = injector
      } catch (e: RuntimeException) {
        throw e
      } catch (e: Exception) {
        throw RuntimeException(e)
      }
    }

    fun isStaticEntry(name: String): Boolean {
      return name.startsWith(INKERBOT_ENTRY_CLASS)
    }

    fun getEntryBuffer(name: String): ByteArray {
      val internalName = name.replace('.', '/') + ".class"
      val inputStream: InputStream = bot.inker.api.InkerBot.frame.classLoader.getResourceAsStream(internalName)
        ?: throw ClassNotFoundException(name)

      try {
        try {
          ByteArrayOutputStream().use { outputStream ->
            val buffer = ByteArray(4096)
            var bytesRead = -1
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
              outputStream.write(buffer, 0, bytesRead)
            }
            inputStream.close()
            return outputStream.toByteArray()
          }
        } finally {
          inputStream.close()
        }
      } catch (ex: IOException) {
        throw ClassNotFoundException("Cannot load resource for class [$name]", ex)
      }
    }
  }

  init {
    PluginMeta.builder {

    }.build()
    throw IllegalCallerException("Static class shouldn't be instance.")
  }
}