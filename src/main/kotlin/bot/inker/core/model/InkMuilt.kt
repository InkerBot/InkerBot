package bot.inker.core.model

import bot.inker.api.model.message.MessageComponent
import bot.inker.api.model.message.MuiltComponent
import bot.inker.api.model.message.PlainTextComponent
import javax.inject.Singleton

class InkMuilt private constructor(override val subs: List<MessageComponent>) : MuiltComponent {

  @Singleton
  class Factory : MuiltComponent.Factory {
    override fun of(subs: List<MessageComponent>): MuiltComponent {
      return InkMuilt(ArrayList(subs))
    }
  }

  class Builder : MuiltComponent.Builder {
    val subs: MutableList<MessageComponent>

    constructor() {
      this.subs = ArrayList()
    }

    constructor(subs: List<MessageComponent>) {
      this.subs = ArrayList(subs)
    }

    override fun plus(component: MessageComponent): MuiltComponent.Builder {
      return add(component)
    }

    override fun plusAssign(component: MessageComponent) {
      add(component)
    }

    override fun add(component: MessageComponent): MuiltComponent.Builder {
      when(component){
        is InkPlainText -> {
          if(component.context.isEmpty()){
            return this
          }
        }
        is InkMuilt -> {
          for (sub in component.subs) {
            add(sub)
          }
          return this
        }
      }
      when(val lastComponent = subs.lastOrNull()){
        is InkPlainText -> {
          if(component is InkPlainText){
            lastComponent.context += component.context
            return this
          }
        }
      }
      subs.add(component)
      return this
    }

    override fun build(): MuiltComponent {
      return MuiltComponent.factory().of(subs)
    }
  }

  override fun toString(): String {
    val builder = StringBuilder()
    for (sub in subs) {
      builder.append(sub.toString())
    }
    return builder.toString()
  }
}