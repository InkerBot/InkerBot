package com.eloli.inkerbot.core.config

import com.eloli.inkerbot.api.config.Comment
import com.eloli.inkerbot.api.config.Comments
import com.eloli.inkerbot.api.config.KotlinComment
import org.yaml.snakeyaml.DumperOptions.FlowStyle
import org.yaml.snakeyaml.comments.CommentLine
import org.yaml.snakeyaml.comments.CommentType
import org.yaml.snakeyaml.introspector.Property
import org.yaml.snakeyaml.nodes.*
import org.yaml.snakeyaml.representer.Representer
import java.util.*
import kotlin.collections.ArrayList

class InkYamlRepresenter : Representer() {
    override fun representJavaBean(propertySet: Set<Property>, javaBean: Any): MappingNode {
        val properties: List<Property> = ArrayList(propertySet)
        val value: MutableList<NodeTuple> = ArrayList(properties.size)
        val node = MappingNode(Tag.MAP, value, FlowStyle.AUTO)
        if (0 < properties.size) {
            node.blockComments = getComments(properties[0])
        }
        representedObjects[javaBean] = node
        val bestStyle = FlowStyle.BLOCK
        for (i in properties.indices) {
            val property = properties[i]
            val memberValue = property[javaBean]
            val customPropertyTag = if (memberValue == null) null else classTags[memberValue.javaClass]
            val tuple = representJavaBeanProperty(
                javaBean, property, memberValue,
                customPropertyTag
            ) ?: continue
            if (i + 1 < properties.size) {
                tuple.valueNode.endComments = getComments(properties[i + 1])
            }
            value.add(tuple)
        }
        if (defaultFlowStyle != FlowStyle.AUTO) {
            node.flowStyle = defaultFlowStyle
        } else {
            node.flowStyle = bestStyle
        }
        return node
    }

    protected fun getComments(property: Property?): List<CommentLine> {
        if (property == null) {
            return emptyList()
        }
        val commentValues = ArrayList<CommentLine>()

        val comments: Comments? = property.getAnnotation(Comments::class.java)
        if (comments == null) {
            val comment: Comment? = property.getAnnotation(Comment::class.java)
            if (comment != null) {
                commentValues.add(CommentLine(null, null, " " + comment.value, CommentType.BLOCK))
            }
        } else {
            for (comment in comments.value) {
                commentValues.add(CommentLine(null, null, " " + comment.value, CommentType.BLOCK))
            }
        }

        val kotlinComment: KotlinComment? = property.getAnnotation(KotlinComment::class.java)
        if (kotlinComment != null) {
            for (comment in kotlinComment.value) {
                commentValues.add(CommentLine(null, null, " $comment", CommentType.BLOCK))
            }
        }

        return commentValues
    }

    override fun checkGlobalTag(property: Property, node: Node, obj: Any) {
        // Skip primitive arrays.
        if (obj.javaClass.isArray && obj.javaClass.componentType.isPrimitive) {
            return
        }
        val arguments = property.actualTypeArguments
        if (arguments != null) {
            if (node.nodeId == NodeId.sequence) {
                // apply map tag where class is the same
                val t = arguments[0]
                val snode = node as SequenceNode
                var memberList: Iterable<Any?> = Collections.EMPTY_LIST
                if (obj.javaClass.isArray) {
                    memberList = Arrays.asList(*obj as Array<Any?>)
                } else if (obj is Iterable<*>) {
                    // list
                    memberList = obj
                }
                val iter = memberList.iterator()
                if (iter.hasNext()) {
                    for (childNode in snode.value) {
                        val member = iter.next()
                        if (member != null) {
                            resetNodeTag(t, childNode)
                        }
                    }
                }
            } else if (obj is Set<*>) {
                val t = arguments[0]
                val mnode = node as MappingNode
                val iter: Iterator<NodeTuple> = mnode.value.iterator()
                for (member in obj) {
                    val tuple = iter.next()
                    val keyNode = tuple.keyNode
                    resetNodeTag(t, keyNode)
                }
            } else if (obj is Map<*, *>) { // NodeId.mapping ends-up here
                val keyType = arguments[0]
                val valueType = arguments[1]
                val mnode = node as MappingNode
                for (tuple in mnode.value) {
                    resetNodeTag(keyType, tuple.keyNode)
                    resetNodeTag(valueType, tuple.valueNode)
                }
            } else {
                // the type for collection entries cannot be
                // detected
            }
        }
    }

    private fun resetNodeTag(type: Class<out Any>, node: Node) {
        val tag = node.tag
        if (tag.matches(type)) {
            if (Enum::class.java.isAssignableFrom(type)) {
                node.tag = Tag.STR
            } else {
                node.tag = Tag.MAP
            }
        }
    }

    init {
        propertyUtils = InkPropertyUtils()
    }
}