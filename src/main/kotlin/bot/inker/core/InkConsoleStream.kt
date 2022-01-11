package bot.inker.core

import bot.inker.api.ConsoleStream
import bot.inker.api.InkerBot
import com.eloli.inkcmd.terminal.buffer.LinePerThreadBufferingOutputStream
import com.eloli.inkcmd.terminal.buffer.TextStream
import org.apache.logging.log4j.core.util.CloseShieldOutputStream
import org.jline.utils.Log
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.OutputStream
import java.io.PrintStream
import javax.inject.Singleton

@Singleton
class InkConsoleStream private constructor(
    stdout:PrintStream,
    stderr:PrintStream,
    output:TextStream,
    private val overrideableTextStream:OverrideableTextStream
): LinePerThreadBufferingOutputStream(overrideableTextStream),TextStream,ConsoleStream{
    override val stdout:PrintStream = stdout
    override val stderr:PrintStream = stderr
    val proxiedStdout by lazy {
        OutputStreamProxy(arrayOf(stdout,logout,this),stdout)
    }
    val proxiedStderr by lazy {
        OutputStreamProxy(arrayOf(stderr,logerr,this),stderr)
    }
    var output:TextStream get() = overrideableTextStream.output
        set(value) { overrideableTextStream.output = value }
    override lateinit var logout:PrintStream private set
    override lateinit var logerr:PrintStream private set
    val logConsole:Logger by lazy { LoggerFactory.getLogger("console") }
    override val console:PrintStream = LinePerThreadBufferingOutputStream {
        logConsole.debug(it.substring(0,it.length-1))
        text("> $it")
    }
    override val standard: PrintStream = this

    fun initLogger(stdoutLogger:Logger, stderrLogger:Logger){
        logout = TextStreamPrintStream{
            stdoutLogger.info("{}",it.substring(0,it.length-1))
        }
        logerr = TextStreamPrintStream{
            stderrLogger.warn("{}",it.substring(0,it.length-1))
        }
    }

    constructor(stdout:PrintStream,
                stderr:PrintStream,
                output:TextStream) : this(stdout,stderr,output,OverrideableTextStream(output))

    override fun equals(other: Any?): Boolean {
        if(other === logout || other === stdout){
            return true
        }
        return super.equals(other)
    }

    override fun text(text: String) {
        overrideableTextStream.text(text)
    }

    class OverrideableTextStream(var output: TextStream):TextStream{
        override fun text(text: String) {
            output.text(text)
        }
    }

    class OutputStreamProxy(
        private val alwaysEqualsObjects: Array<OutputStream>,
        proxied: OutputStream
    ): CloseShieldOutputStream(proxied) {
        override fun equals(other: Any?): Boolean {
            if(this === other){
                return true
            }
            for (alwaysEqualsObject in alwaysEqualsObjects) {
                if(alwaysEqualsObject === other){
                    return true
                }
            }
            return false
        }
    }

    open class TextStreamPrintStream(
        val textStream: TextStream
    ): LinePerThreadBufferingOutputStream({
        if(it.isNotEmpty()){
            textStream.text(it)
        }
    })
}