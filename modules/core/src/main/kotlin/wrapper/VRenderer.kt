package wrapper

import external.composition_api.SetupContext
import external.composition_api.Slot
import external.composition_api.createElement
import external.composition_api.invoke
import external.vue.ScopedSlot
import external.vue.VNode
import external.vue.invoke
import kotlinext.js.jsObject

fun vRender(render: VRender.() -> Unit): () -> VNode = {
    VRenderer.of(render).children[0]
}

typealias VRender = VRenderer<*, *, *>

open class VRenderer<P, A, D> : VNodeDataBuilder<P, A, D>() {

    operator fun <T : Any> VComponent<T>.unaryPlus(): VNode {
        val component = component()

        this.propDefs = undefined
        this.setupFunction = undefined
        this.propsBuilder = undefined
        this.domPropsBuilder = undefined

        val vNode = createElement(component, this, children.toTypedArray())
        this@VRenderer.child(vNode)
        return vNode
    }

    fun <P, A, D> h(tag: String, builder: VRenderer<P, A, D>.() -> Unit, props: P): VNode {
        val vNodeData = VRenderer<P, A, D>().apply(builder)

        vNodeData.propsBuilder?.let {
            vNodeData.props = props.apply(it)
            vNodeData.propsBuilder = undefined
        }

        val vNode = createElement(tag, vNodeData, vNodeData.children.toTypedArray())
        child(vNode)
        return vNode
    }

    fun <P : Any> h(tag: P, builder: VRenderer<P, Any, Any>.() -> Unit): VNode {

        val vNodeData = VRenderer<P, Any, Any>().apply(builder)

        vNodeData.propsBuilder?.let {
            vNodeData.props = jsObject(it)
            vNodeData.propsBuilder = undefined
        }

        val vNode = createElement(tag, vNodeData, vNodeData.children)
        child(vNode)
        return vNode
    }

    fun h(tag: String): VNode {
        val vNode = createElement(tag)
        child(vNode)
        return vNode
    }

    fun setSlot(name: String, ctx: SetupContext) =
        ctx.slots?.get(name).unsafeCast<Slot?>()?.invoke()?.let { child(it) }

    fun <Props : Any> setSlot(name: String, ctx: SetupContext, builder: Props.() -> Unit) =
        ctx.slots?.get(name).unsafeCast<ScopedSlot<Props>?>()?.invoke(jsObject(builder))?.let { child(it) }

    fun <Props : Any> setSlot(props: Props, name: String, ctx: SetupContext, builder: Props.() -> Unit) =
        ctx.slots?.get(name).unsafeCast<ScopedSlot<Props>?>()?.invoke(props.apply(builder))?.let { child(it) }

    companion object {
        fun of(block: VRender.() -> Unit) =
            VRenderer<Unit, Unit, Unit>().apply(block)
    }
}
