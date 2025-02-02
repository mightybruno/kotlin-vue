package components.side_menu

import cssdsl.CssRuleSet
import cssdsl.hover
import external.font_awsome.FaIcon
import kotlinext.js.Object
import kotlinx.css.Align
import kotlinx.css.Color
import kotlinx.css.Cursor
import kotlinx.css.LinearDimension
import kotlinx.css.a
import kotlinx.css.alignItems
import kotlinx.css.backgroundColor
import kotlinx.css.color
import kotlinx.css.cursor
import kotlinx.css.flex
import kotlinx.css.padding
import kotlinx.css.properties.TextDecoration
import kotlinx.css.textDecoration
import kotlinx.css.width
import wrapper.VComponent
import wrapper.VComponentBuilder
import wrapper.div
import wrapper.routerLink
import wrapper.vRender
import kotlin.js.Json

class SideMenuItemProps {

    var icon: Any? = null

    fun icon(name: String) {
        icon = name
    }

    fun icon(obj: Json) {
        icon = obj
    }

    fun icon(array: Array<String>) {
        icon = array
    }

    var title: String = ""

    var to: String? = null
}

class SideMenuItem(builder: VComponentBuilder<SideMenuItemProps>) :
    VComponent<SideMenuItemProps>(builder = builder, renderProps = SideMenuItemProps()) {

    init {
        css { +stylesSideMenuItem }

        propData {
            "icon" {
                type = arrayOf(String::class.js, Object::class.js, Array<String>::class.js)
                default = "icons"
            }
            "title"{
                type = String::class.js
                required = true
            }
            "to"{
                type = String::class.js
                default = "home"
            }
        }

        setup { p, _ ->

            vRender {
                div {
                    `class` = "side-menu-item"
                    routerLink {
                        div {
                            `class` = "icon fa-fw"

                            h(FaIcon) {
                                props {
                                    p.icon?.let { icon = it }
                                }
                            }
                        }

                        props {
                            to {
                                name = p.to
                            }
                        }
                        +p.title
                    }
                }
            }
        }
    }
}

private val stylesSideMenuItem: CssRuleSet = {
    ".side-menu-item"{
        width = LinearDimension("100%")
        alignItems = Align.center
        flex(0.0, 1.0)
        backgroundColor = Color("#F4F4F5")
    }

    ".icon" {
        color = Color("#1D457C")
        padding = "0.35rem"
    }

    a {
        color = Color("#414141")
        textDecoration = TextDecoration.none
    }

    hover(".side-menu-item") {
        backgroundColor = Color("#D7D7D7")
        cursor = Cursor.pointer
    }

}
