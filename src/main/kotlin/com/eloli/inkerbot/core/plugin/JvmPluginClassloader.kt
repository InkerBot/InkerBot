package com.eloli.inkerbot.core.plugin

import com.eloli.inkerbot.core.util.StaticEntryUtil
import java.net.URL
import java.net.URLClassLoader

class JvmPluginClassloader(url: URL, parent: ClassLoader) : URLClassLoader(arrayOf(url), parent) {

    private val depends: MutableCollection<ClassLoader> = java.util.ArrayList()

    override fun loadClass(name: String, resolve: Boolean): Class<*> {
        synchronized(getClassLoadingLock(name)) {
            var c = super.findLoadedClass(name)
            if (c == null) {
                c = rewriteStaticEntry(name)
            }
            if (c == null) {
                try {
                    c = parent.loadClass(name)
                } catch (ignore: ClassNotFoundException) {
                    //
                }
            }
            if (c == null) {
                try {
                    c = findClass(name)
                } catch (ignore: ClassNotFoundException) {
                    //
                }
            }
            if (c == null) {
                c = tryDepends(name)
            }
            if (c == null) {
                throw ClassNotFoundException(name)
            }
            if (resolve) {
                resolveClass(c)
            }
            return c
        }
    }

    private fun rewriteStaticEntry(name: String): Class<*>? {
        if (StaticEntryUtil.isStaticEntry(name)) {
            val bytes: ByteArray = StaticEntryUtil.getEntryBuffer(name)
            return defineClass(name, bytes, 0, bytes.size)
        }
        return null
    }

    private fun tryDepends(name: String): Class<*>? {
        for (depend in depends) {
            try {
                return depend.loadClass(name)
            } catch (ignore: ClassNotFoundException) {
                //
            }
        }
        return null
    }

    override fun getResource(name: String?): URL? {
        var u = super.findResource(name)
        if (u != null) {
            return u
        }
        for (depend in depends) {
            u = depend.getResource(name)
            if (u != null) {
                return u
            }
        }
        return parent.getResource(name)
    }

    fun addDepend(depend: ClassLoader) {
        depends.add(depend)
    }

    public override fun addURL(url: URL?) {
        super.addURL(url)
    }
}