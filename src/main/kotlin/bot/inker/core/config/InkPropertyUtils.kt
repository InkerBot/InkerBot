package bot.inker.core.config

import org.yaml.snakeyaml.introspector.BeanAccess
import org.yaml.snakeyaml.introspector.FieldProperty
import org.yaml.snakeyaml.introspector.Property
import org.yaml.snakeyaml.introspector.PropertyUtils
import java.lang.reflect.Modifier

class InkPropertyUtils : PropertyUtils() {
  override fun createPropertySet(type: Class<out Any>, bAccess: BeanAccess): Set<Property> {
    val props: MutableCollection<Property> = LinkedHashSet()
    val nameSet: MutableSet<String> = HashSet()
    var c: Class<*>? = type
    while (c != null) {
      for (field in c.declaredFields) {
        val modifiers = field.modifiers
        if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)
          && !nameSet.contains(field.name)
        ) {
          nameSet.add(field.name)
          props.add(FieldProperty(field))
        }
      }
      c = c.superclass
    }
    val properties: MutableSet<Property> = LinkedHashSet()
    for (property in props) {
      if (property.isReadable && (isAllowReadOnlyProperties || property.isWritable)) {
        properties.add(property)
      }
    }
    return properties
  }

  init {
    setBeanAccess(BeanAccess.FIELD)
  }
}