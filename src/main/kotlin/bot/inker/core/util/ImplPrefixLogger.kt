package bot.inker.core.util

import org.slf4j.Logger
import org.slf4j.helpers.MarkerIgnoringBase

class ImplPrefixLogger(private val parent: Logger, private val prefix: String) : MarkerIgnoringBase() {
  override fun getName(): String {
    return parent.name
  }

  override fun isTraceEnabled(): Boolean {
    return parent.isTraceEnabled
  }

  override fun trace(msg: String) {
    parent.trace(prefix + msg)
  }

  override fun trace(format: String, arg: Any) {
    parent.trace(prefix + format, arg)
  }

  override fun trace(format: String, arg1: Any, arg2: Any) {
    parent.trace(prefix + format, arg1, arg2)
  }

  override fun trace(format: String, vararg arguments: Any) {
    parent.trace(prefix + format, *arguments)
  }

  override fun trace(msg: String, t: Throwable) {
    parent.trace(prefix + msg, t)
  }

  override fun isDebugEnabled(): Boolean {
    return parent.isDebugEnabled
  }

  override fun debug(msg: String) {
    parent.debug(prefix + msg)
  }

  override fun debug(format: String, arg: Any) {
    parent.debug(prefix + format, arg)
  }

  override fun debug(format: String, arg1: Any, arg2: Any) {
    parent.debug(prefix + format, arg1, arg2)
  }

  override fun debug(format: String, vararg arguments: Any) {
    parent.debug(prefix + format, *arguments)
  }

  override fun debug(msg: String, t: Throwable) {
    parent.debug(prefix + msg, t)
  }

  override fun isInfoEnabled(): Boolean {
    return parent.isInfoEnabled
  }

  override fun info(msg: String) {
    parent.info(prefix + msg)
  }

  override fun info(format: String, arg: Any) {
    parent.info(prefix + format, arg)
  }

  override fun info(format: String, arg1: Any, arg2: Any) {
    parent.info(prefix + format, arg1, arg2)
  }

  override fun info(format: String, vararg arguments: Any) {
    parent.info(prefix + format, *arguments)
  }

  override fun info(msg: String, t: Throwable) {
    parent.info(prefix + msg, t)
  }

  override fun isWarnEnabled(): Boolean {
    return parent.isWarnEnabled
  }

  override fun warn(msg: String) {
    parent.warn(prefix + msg)
  }

  override fun warn(format: String, arg: Any) {
    parent.warn(prefix + format, arg)
  }

  override fun warn(format: String, arg1: Any, arg2: Any) {
    parent.warn(prefix + format, arg1, arg2)
  }

  override fun warn(format: String, vararg arguments: Any) {
    parent.warn(prefix + format, *arguments)
  }

  override fun warn(msg: String, t: Throwable) {
    parent.warn(prefix + msg, t)
  }

  override fun isErrorEnabled(): Boolean {
    return parent.isErrorEnabled
  }

  override fun error(msg: String) {
    parent.error(prefix + msg)
  }

  override fun error(format: String, arg: Any) {
    parent.error(prefix + format, arg)
  }

  override fun error(format: String, arg1: Any, arg2: Any) {
    parent.error(prefix + format, arg1, arg2)
  }

  override fun error(format: String, vararg arguments: Any) {
    parent.error(prefix + format, *arguments)
  }

  override fun error(msg: String, t: Throwable) {
    parent.error(prefix + msg, t)
  }
}