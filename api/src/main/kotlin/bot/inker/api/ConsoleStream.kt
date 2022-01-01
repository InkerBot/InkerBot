package bot.inker.api

import com.eloli.inkcmd.terminal.buffer.TextStream
import java.io.PrintStream

interface ConsoleStream:TextStream {
    val stdout: PrintStream
    val stderr: PrintStream
    val logout: PrintStream
    val logerr: PrintStream
    val standard: PrintStream
    val console: PrintStream
}