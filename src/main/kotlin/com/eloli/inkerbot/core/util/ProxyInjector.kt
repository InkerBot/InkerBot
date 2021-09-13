package com.eloli.inkerbot.core.util

import com.google.inject.*
import com.google.inject.spi.Element
import com.google.inject.spi.InjectionPoint
import com.google.inject.spi.TypeConverterBinding
import java.lang.IllegalStateException
import java.util.concurrent.CompletableFuture

open class ProxyInjector:Injector {
    private val completableFutureInjector: CompletableFuture<Injector> = CompletableFuture()
    private val injector: Injector
        get() {
            val injector = completableFutureInjector.getNow(null)
            if (injector == null) {
                throw IllegalStateException("Injector isn't available now.")
            }else{
                return injector
            }
        }

    fun registerProxyInjector(injector: Injector){
        if (completableFutureInjector.getNow(null) != null) {
            throw IllegalStateException("Injector have been set.")
        }
        completableFutureInjector.complete(injector)
    }
    
    override fun injectMembers(instance: Any) {
        injector.injectMembers(instance)
    }

    override fun <T : Any> getMembersInjector(typeLiteral: TypeLiteral<T>): MembersInjector<T> {
        return injector.getMembersInjector(typeLiteral)
    }

    override fun <T : Any> getMembersInjector(type: Class<T>): MembersInjector<T> {
        return injector.getMembersInjector(type)
    }

    override fun getBindings(): MutableMap<Key<*>, Binding<*>> {
        return injector.getBindings()
    }

    override fun getAllBindings(): MutableMap<Key<*>, Binding<*>> {
        return injector.getAllBindings()
    }

    override fun <T : Any> getBinding(key: Key<T>): Binding<T> {
        return injector.getBinding(key)
    }

    override fun <T : Any> getBinding(type: Class<T>): Binding<T> {
        return injector.getBinding(type)
    }

    override fun <T : Any> getExistingBinding(key: Key<T>): Binding<T> {
        return injector.getExistingBinding(key)
    }

    override fun <T : Any> findBindingsByType(type: TypeLiteral<T>): MutableList<Binding<T>> {
        return injector.findBindingsByType(type)
    }

    override fun <T : Any> getProvider(key: Key<T>): com.google.inject.Provider<T> {
        return injector.getProvider(key)
    }

    override fun <T : Any> getProvider(type: Class<T>): com.google.inject.Provider<T> {
        return injector.getProvider(type)
    }

    override fun <T : Any> getInstance(key: Key<T>): T {
        return injector.getInstance(key)
    }

    override fun <T : Any> getInstance(type: Class<T>): T {
        return injector.getInstance(type)
    }

    override fun getParent(): Injector {
        return injector.getParent()
    }

    override fun createChildInjector(modules: MutableIterable<Module>): Injector {
        return injector.createChildInjector(modules)
    }

    override fun createChildInjector(vararg modules: Module): Injector {
        val array = ArrayList<Module>()
        for (module in modules) {
            array.add(module)
        }
        return injector.createChildInjector(array)
    }

    override fun getScopeBindings(): MutableMap<Class<out Annotation>, Scope> {
        return injector.getScopeBindings()
    }

    override fun getTypeConverterBindings(): MutableSet<TypeConverterBinding> {
        return injector.getTypeConverterBindings()
    }

    override fun getElements(): MutableList<Element> {
        return injector.getElements()
    }

    override fun getAllMembersInjectorInjectionPoints(): MutableMap<TypeLiteral<*>, MutableList<InjectionPoint>> {
        return injector.getAllMembersInjectorInjectionPoints()
    }

}